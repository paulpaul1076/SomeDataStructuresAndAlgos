import java.util.*;

/**
 * Created by Paul on 1/9/2016.
 */
public class BTree<Key extends Comparable<Key>> {
    private Page<Key> root = new Page<>(true);
    private int size = 0;
    private Key sentinel;

    public BTree(Key sentinel) {
        this.sentinel = sentinel;
        add(sentinel);
    }

    public boolean contains(Key key) {
        return contains(root, key);
    }

    private boolean contains(Page<Key> page, Key key) {
        if (page.isExternal()) return page.contains(key);
        return contains(page.next(key), key);
    }

    public void add(Key key) {
        add(root, key);
        if (root.isFull()) {
            Page<Key> leftHalf = root;
            Page<Key> rightHalf = root.split();
            root = new Page<>(false);
            root.add(leftHalf);
            root.add(rightHalf);
            root.size = leftHalf.size() + rightHalf.size();
        }
    }

    public boolean add(Page<Key> page, Key key) {
        if (page.isExternal()) {
            boolean change = page.add(key);
            if (change) {
                page.size++;
                size++;
            }
            return change;
        }
        Page<Key> next = page.next(key);
        boolean change = add(next, key);
        if (next.isFull()) {
            page.add(next.split());
        }
        if (change) page.size++;
        next.close();
        return change;
    }

    public Key min() {
        assert size() > 0;
        return min(root);
    }

    private Key min(Page<Key> page) {
        if (page.next(sentinel) == null) {
            Iterator<Key> it = page.keys.keySet().iterator();
            it.next();
            return it.next();
        }
        return min(page.next(sentinel));
    }

    public Key max() {
        assert size() > 0;
        return max(root);
    }

    private Key max(Page<Key> page) {
        if (page.next(page.keys.lastKey()) == null) {
            return page.keys.lastKey();
        }
        return max(page.next(page.keys.lastKey()));
    }

    public Key floor(Key key) {
        int cmp = key.compareTo(sentinel);
        assert cmp > 0;
        Key result = floor(root, key);
        if (result.equals(sentinel)) return null;
        return result;
    }

    private Key floor(Page<Key> page, Key key) {
        if (page.contains(key)) return key;
        Key floorKey = page.keys.floorKey(key);
        Page<Key> next = page.next(floorKey);
        if (next == null) return floorKey;
        else return floor(next, key);
    }

    public Key ceiling(Key key) {
        int cmp = key.compareTo(sentinel);
        assert cmp > 0;
        Key result = ceiling(root, key);
        return result;
    }

    private Key ceiling(Page<Key> page, Key key) {
        if (page == null) return null;
        Key prev = null;
        Key ceil = null;
        for (Key xkey : page.keys()) {
            prev = ceil;
            ceil = xkey;
            if (key.compareTo(xkey) <= 0) break;
        }
        int cmp = key.compareTo(ceil);
        if (cmp == 0) {
            return ceil;
        } else if (cmp < 0) {
            Key temp = ceiling(page.next(prev), key);
            if (temp == null) return ceil;
            return temp;
        } else return ceiling(page.next(ceil), key);
    }

    public Key deleteMin() {
        Key min = min();
        delete(min);
        return min;
    }

    public Key deleteMax() {
        Key max = max();
        delete(max); // don't check for null
        return max;
    }

    public Key select(int k) {
        if (!(k < size() && k >= 0)) throw new IllegalArgumentException("Bad arguments");
        assert k < size() && k >= 0; // k in {0, size() - 1}
        return select(root, k + 1, 0);
    }

    private Key select(Page<Key> page, int k, int rightCost) {
        Map.Entry<Key, Page<Key>> prevEntry = null;
        int countLeft = 0;
        for (Map.Entry<Key, Page<Key>> e : page.entries()) {
            if (countLeft + rightCost < k) prevEntry = e;
            else if (countLeft + rightCost == k) return e.getKey();
            else break;
            countLeft += getSize(e.getValue());
        }
        return select(prevEntry.getValue(), k, rightCost + countLeft - getSize(prevEntry.getValue()));
    }

    private int getSize(Page<Key> page) {
        if (page == null) return 1;
        return page.size();
    }

    public int rank(Key key) { // shouldn't matter what arguments you give it
        return rank(root, key, 0).rankNo;
    }

    private class RankPair {
        public RankPair(boolean isIncluded, int rankNo) {
            this.isIncluded = isIncluded;
            this.rankNo = rankNo;
        }

        public boolean isIncluded;
        public int rankNo;
    }

    private RankPair rank(Page<Key> page, Key key, int rightCost) {
        Map.Entry<Key, Page<Key>> prevEntry = null;
        int countLeft = 0;
        Map.Entry<Key, Page<Key>> eqEntry = null;
        for (Map.Entry<Key, Page<Key>> e : page.entries()) {
            int cmp = e.getKey().compareTo(key);
            if (cmp < 0) prevEntry = e;
            else{
                if(cmp == 0) eqEntry=e;
                break;
            }
            countLeft += getSize(e.getValue());
        }
        if (prevEntry.getValue() == null) {
            if (eqEntry != null && eqEntry.getKey().equals(key)) {
                return new RankPair(true, countLeft + rightCost - 1);
            } else {
                return new RankPair(false, countLeft + rightCost - 1);
            }
        }
        return rank(prevEntry.getValue(), key, rightCost + countLeft - getSize(prevEntry.getValue()));
    }

    public int size(Key lo, Key hi) {
        if(size() == 0) return 0;
        RankPair loPair = rank(root, lo, 0);
        int loRank = loPair.rankNo;
        RankPair hiPair = rank(root, hi, 0);
        boolean isRightInclusive = hiPair.isIncluded;
        int hiRank = hiPair.rankNo;
        if(isRightInclusive){
            return hiRank - loRank + 1;
        } else {
            return hiRank - loRank;
        }
    }

    public void delete(Key key) {
        assert size() > 0;
        Pair pair = delete(key, root);
        root = pair.page;
    }

    class Pair {
        public Pair(Page<Key> page, boolean hasDeleted) {
            this.page = page;
            this.hasDeleted = hasDeleted;
        }

        public Page<Key> page;
        public boolean hasDeleted;
    }

    private Pair delete(Key key, Page<Key> page) {
        if (page == null) return new Pair(null, false);
        Map.Entry<Key, Page<Key>> floorEntry = page.keys.floorEntry(key);
        Key floorKey = floorEntry.getKey();
        Page<Key> floorPage = floorEntry.getValue();
        Pair pair = delete(key, floorPage);
        Page<Key> p = pair.page;
        boolean hasDeleted = pair.hasDeleted;
        if (hasDeleted) page.size--;
        if (p != null) {
            page.delete(floorKey);
            floorEntry = page.keys.floorEntry(floorKey);
            if (floorEntry == null) floorEntry = page.keys.firstEntry();
            if (floorEntry == null) return new Pair(p, hasDeleted);
            page.delete(floorEntry.getKey());
            floorPage = floorEntry.getValue();
            floorPage.merge(p);
            if (floorPage.keys.size() >= MAXIMUM_KEY_NUM) {
                Page<Key> x = floorPage;
                Page<Key> y = floorPage.split();
                page.add(x);
                page.add(y);
            } else {
                page.add(floorPage);
            }
        } else {
            hasDeleted = page.delete(key);
            if (hasDeleted) {
                page.size--;
                size--;
            }
        }
        return new Pair(page, hasDeleted);
    }

    public Iterable<Key> keys(Key lo, Key hi) {
        ArrayDeque<Key> keys = new ArrayDeque<>();
        collect(keys, root, lo, hi, null);
        return keys;
    }

    private void collect(ArrayDeque<Key> keys, Page<Key> page, Key lo, Key hi, Key doNotInclude) {
        if (page == null) return;
        int cmp = hi.compareTo(page.keys.firstKey());
        if (cmp < 0) return;
        ArrayList<Key> floorCeilRange = new ArrayList<>();
        for (Key xkey : page.keys.keySet()) {
            int cmp2 = xkey.compareTo(hi);
            if (cmp2 <= 0) {
                floorCeilRange.add(xkey);
            } else {
                break;
            }
        }
        for (Key xkey : floorCeilRange) {
            int cmp1 = lo.compareTo(xkey);
            int cmp2 = hi.compareTo(xkey);
            if (cmp1 <= 0 && cmp2 >= 0 && !xkey.equals(doNotInclude)) {
                keys.add(xkey);
            }
            collect(keys, page.next(xkey), lo, hi, xkey);
        }
    }

    public int rootPageSize() {
        return root.size() - 1;
    }

    public int linearSize() {
        return size(min(), max());
    }

    public int size() {
        return size - 1;
    }

    public Iterable<Key> keys() {
        if (size() <= 0) return new ArrayList<>();
        return keys(min(), max());
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static final int MAXIMUM_KEY_NUM = 4;

    private class Page<Key> {
        private boolean bottom;
        private int size = 0;
        private TreeMap<Key, Page<Key>> keys = new TreeMap<>();

        public Page(boolean bottom) {
            this.bottom = bottom;
        }

        public void close() {
            //useless method
        }

        public Iterable<Page<Key>> children() {
            return keys.values();
        }

        public Iterable<Map.Entry<Key, Page<Key>>> entries() {
            return keys.entrySet();
        }

        public boolean add(Key key) {
            if (!contains(key)) {
                keys.put(key, null);
                return true;
            }
            return false;
        }

        public void add(Page<Key> page) {
            Map.Entry<Key, Page<Key>> e = page.keys.firstEntry();
            if (!contains(e.getKey())) {
                keys.put(e.getKey(), page);
            }
        }

        public void merge(Page<Key> other) {
            for (Map.Entry<Key, Page<Key>> e : other.keys.entrySet()) {
                Key key = e.getKey();
                Page<Key> page = e.getValue();
                if (!contains(key)) {
                    if (page == null) {
                        add(key);
                        this.size++;
                    } else {
                        add(page);
                        this.size += page.size();
                    }
                }
            }
        }

        public int size() {
            return size;
        }

        public boolean isExternal() {
            return bottom;
        }

        public boolean contains(Key key) {
            return keys.containsKey(key);
        }

        public boolean delete(Key key) {
            if (contains(key)) {
                Page<Key> page = keys.get(key);
                keys.remove(key);
                if (page == null) {
                    return true;
                } else {
                    return true;
                }
            }
            return false;
        }

        public Page<Key> next(Key key) {
            return keys.floorEntry(key).getValue();
        }

        public boolean isFull() {
            return keys.size() >= MAXIMUM_KEY_NUM;
        }

        public Page<Key> split() {
            int count = 0;
            ArrayList<Map.Entry<Key, Page<Key>>> toMove = new ArrayList<>();
            for (Iterator<Map.Entry<Key, Page<Key>>> it = keys.entrySet().iterator(); it.hasNext(); ++count) {
                if (count >= keys.size() / 2) {
                    toMove.add(it.next());
                } else {
                    it.next();
                }
            }
            Page<Key> resultPage = new Page<>(this.bottom);
            for (Map.Entry<Key, Page<Key>> e : toMove) {
                Key key = e.getKey();
                Page<Key> page = e.getValue();
                {
                    if (page == null) {
                        resultPage.keys.put(key, page);
                        resultPage.size++;
                        this.size--;
                    } else {
                        resultPage.keys.put(key, page);
                        resultPage.size += page.size();
                        this.size -= page.size();
                    }
                }
                {
                    delete(key);
                }
            }
            return resultPage;
        }

        public Iterable<Key> keys() {
            return keys.keySet();
        }
    }
}