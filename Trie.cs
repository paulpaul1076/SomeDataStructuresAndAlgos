using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

class Alphabet
{
    private string id_ch;
    private Dictionary<char, int> ch_id = new Dictionary<char, int>();
    public Alphabet(string letters)
    {
        SortedSet<char> dedup = new SortedSet<char>();
        foreach (char ch in letters)
            dedup.Add(ch);
        StringBuilder sb = new StringBuilder(dedup.Count);
        int i = 0;
        foreach (char ch in dedup)
        {
            sb.Append(ch);
            ch_id.Add(ch, i++);
        }
        id_ch = sb.ToString();
    }
    public char CharAt(int i) { return id_ch[i]; }
    public int IndexOf(char ch) { return ch_id[ch]; }
    public IEnumerable<char> Letters { get { foreach (char ch in id_ch) { yield return ch; } } }
    public int R { get { return id_ch.Length; } }
}

class Trie<Value>
{
    class Entry
    {
        public Entry(Value val)
        {
            this.val = val;
        }
        public Value val;
    }
    private class Node
    {
        public Node(int R)
        {
            next = new Node[R];
        }
        public Entry entry;
        public Node[] next;
        public int size;
    }

    private Alphabet alpha;
    private int R;

    public Trie(Alphabet alpha)
    {
        this.alpha = alpha;
        this.R = alpha.R;
        root = new Node(R);
    }

    private Node root;

    public void Put(string key, Value val)
    {
        //if (Contains(key)) return;
        Node x = GetNode(key);

        if (x != null && x.entry != null) { x.entry.val = val; return; }

        x = root;
        for (int i = 0; i < key.Length; i++)
        {
            ++x.size;
            char ch = key[i];
            if (x.next[alpha.IndexOf(ch)] == null) x.next[alpha.IndexOf(ch)] = new Node(R);
            x = x.next[alpha.IndexOf(ch)];
        }
        ++x.size;
        if (x.entry == null)
            x.entry = new Entry(val);
        else x.entry.val = val;
    }
    public bool Get(string key, out Value answer)
    {
        answer = default(Value);
        Node x = GetNode(key);
        if (x == null) return false;
        else if (x.entry != null) { answer = x.entry.val; return true; }
        return false;
    }
    private Node GetNode(string key)
    {
        Node x = root;
        for (int i = 0; i < key.Length; i++)
        {
            char ch = key[i];
            if (x.next[alpha.IndexOf(ch)] == null) return null;
            x = x.next[alpha.IndexOf(ch)];
        }
        return x;
    }
    public bool Contains(string key)
    {
        Node x = GetNode(key);
        return x != null && x.entry != null;
    }

    public string Floor(string key)
    {
        Node lln = null; // last left node
        int lln_ID = -1;
        Node x = root;
        int lastKeyPos = -1, lastLeftPos = -1;
        int i = 0;
        for (; i < key.Length; i++)
        {
            //Console.WriteLine("inside loop, key length is {0}", key.Length);
            // encountered a letter that tells us that this word doesn't exist in our symbol table
            if (x == null) break;
            // save key (it might hang lower than the left node)
            if (x.entry != null) lastKeyPos = i;
            char ch = key[i];
            //find last left node
            for (int j = alpha.IndexOf(ch) - 1; j >= 0; --j) if (x.next[j] != null) { lln = x; lln_ID = j; lastLeftPos = i; break; }
            x = x.next[alpha.IndexOf(ch)];
        }
        //Console.WriteLine("letter : {0}", alpha.CharAt(lln_ID));
        if (i == key.Length && x != null && x.entry != null)
        {
            return key;
        }
        else if (lln == null)
        {
            //Console.WriteLine("hererererere");
            if (lastKeyPos == -1 && root.entry == null)
            {
                //Console.WriteLine("1");
                return null;
            }
            //Console.WriteLine("2, lastKeyPos = {0}", lastKeyPos);
            return key.Substring(0, lastKeyPos);
        }
        // we want the biggest key which is less than the given one
        // in this case left node will be farther to the left
        if (lastLeftPos <= lastKeyPos) return key.Substring(0, lastKeyPos);
        //Console.WriteLine("last case");
        return key.Substring(0, lastLeftPos) + MaxStringFromNode(lln.next[lln_ID], "" + alpha.CharAt(lln_ID));
    }

    private string MaxStringFromNode(Node x, string result)
    {
        //there's a key ending at a leaf
        while (x != null)
        {
            //find right-most node, append character to result
            Node temp = null;
            for (int i = R - 1; i >= 0; --i)
            {
                if (x.next[i] != null)
                {
                    //Console.WriteLine("mhm");
                    result += alpha.CharAt(i);
                    temp = x.next[i];
                    break;
                }
            }
            x = temp;
        }
        return result;
    }

    public string Ceil(string key)
    {
        Node lrn = null; // last right node
        int lrn_ID = -1;
        int lastKeyPos = -1, lastRightPos = -1;

        Node x = root;

        int i = 0;
        for (; i < key.Length; i++)
        {
            if (x == null) break;
            if (x.entry != null) lastKeyPos = i;
            char ch = key[i];
            for (int j = alpha.IndexOf(ch) + 1; j < R; ++j) if (x.next[j] != null) { lrn_ID = j; lastRightPos = i; lrn = x; break; }
            x = x.next[alpha.IndexOf(ch)];
        }

        if (i == key.Length && x != null && x.entry != null) return key;
        else if (lrn == null)
        {
            if (lastKeyPos == -1 && root.entry == null) return null;
            else return "";
        }
        else if (lastRightPos </*=*/ lastKeyPos) { return key.Substring(0, lastKeyPos); }
        //Console.WriteLine("last case");
        /*Console.WriteLine();
        Console.WriteLine("last right pos = " + lastRightPos);
        Console.WriteLine("letter : " + alpha.CharAt(lrn_ID));*/
        // go into the middle if you havent finished a key
        if (x != null) {
            Node downNode = null;
            int ID = -1;
            for (i = 0; i < R; i++)
            {
                if (x.next[i] != null) { downNode = x.next[i]; ID = i; break; }
            }
            return key + MinimumStringFromNode(downNode, "" + alpha.CharAt(ID));
        }
        return key.Substring(0, lastRightPos) + MinimumStringFromNode(lrn.next[lrn_ID], "" + alpha.CharAt(lrn_ID));
    }
    private string MinimumStringFromNode(Node x, string result)
    {
        while (x != null)
        {
            Node temp = null;
            for (int i = 0; i < R; ++i)
            {
                if (x.next[i] != null)
                {
                    //Console.WriteLine("Found {0}", alpha.CharAt(i));
                    temp = x.next[i];
                    result += alpha.CharAt(i);
                    break;
                }
            }
            x = temp;
        }
        return result;
    }
    public void Delete(string key)
    {
        if (!Contains(key)) return;
        Node x = root;
        for (int i = 0; i < key.Length; i++)
        {
            char ch = key[i];
            --x.size;
            int id = alpha.IndexOf(ch);
            if (x.next[id].size == 1) { x.next[id] = null; return; }
            x = x.next[id];
        }
        x.entry = null;
    }
    private int GoDown(string s)
    {
        Node x = root;
        int pos = 0;
        for (int i = 0; i < s.Length + 1; i++)
        {
            if (x == null) break;
            if (x.entry != null) pos = i;
            x = x.next[alpha.IndexOf(s[i])];
        }
        return pos;
    }
    public string LongestPrefixOf(string s)
    {
        int rightBound = GoDown(s);
        if (rightBound == 0 && root.entry == null) return null;
        return s.Substring(0, rightBound);
    }
    private class pair
    {
        public pair(Node node, string str)
        {
            this.node = node;
            this.str = str;
        }
        public string str;
        public Node node;
    }
    public IEnumerable<string> KeysWithPrefix(string prefix)
    {
        Queue<string> Q = new Queue<string>();
        Node x = GetNode(prefix);

        if (x == null) return Q;

        Stack<pair> dfsStack = new Stack<pair>();
        dfsStack.Push(new pair(x, prefix));
        while (dfsStack.Count > 0)
        {
            pair p = dfsStack.Pop();
            x = p.node;
            if (p.node.entry != null) Q.Enqueue(p.str);
            for (int i = R - 1; i >= 0; --i) if (x.next[i] != null) dfsStack.Push(new pair(x.next[i], p.str + alpha.CharAt(i)));
        }
        return Q;
    }
    public IEnumerable<string> KeysThatMatch(string pattern)
    {
        Queue<pair> bfsQ = new Queue<pair>();
        bfsQ.Enqueue(new pair(root, ""));
        for (int i = 0; i < pattern.Length; i++)
        {
            Queue<pair> temp = new Queue<pair>();
            foreach (pair p in bfsQ)
            {
                Node x = p.node;
                char ch = pattern[i];
                if (ch == '.')
                {
                    for (int j = 0; j < R; j++) if (x.next[j] != null) temp.Enqueue(new pair(x.next[j], p.str + alpha.CharAt(j)));
                }
                else
                {
                    if (x.next[alpha.IndexOf(ch)] != null)
                        temp.Enqueue(new pair(x.next[alpha.IndexOf(ch)], p.str + ch));
                }
            }
            bfsQ = temp;
        }

        Queue<string> result = new Queue<string>();
        foreach (pair p in bfsQ)
        {
            if (p.node.entry != null) result.Enqueue(p.str);
        }
        return result;
    }
    public IEnumerable<string> Keys { get { return KeysWithPrefix(""); } }
}

class Program
{
    static Random rnd = new Random(DateTime.Now.Millisecond);
    static string RandomString(int count)
    {
        StringBuilder result = new StringBuilder(count);
        for (int i = 0; i < count; ++i)
        {
            result.Append((char)('a' + rnd.Next(26)));
        }
        return result.ToString();
    }
    static string[] RandomStrings(int sizeBound, int count)
    {
        string[] result = new string[count];

        for (int i = 0; i < count; i++)
        {
            result[i] = RandomString(rnd.Next(sizeBound));
        }

        return result;
    }
    static void Main(string[] args)
    {
        //string[] strings = RandomStrings(20, 20);
        string[] strings = "she sells seashells by the seashore the shells she sells are surely seashells".Split();
        Trie<int> trie = new Trie<int>(new Alphabet("abcdefghijklmnopqrstuvwxyz"));
        /*foreach (string s in strings)
        {
            trie.Put(s, 3);
        }*/
        Console.WriteLine("all keys : ");
        foreach (string s in trie.Keys)
        {
            Console.WriteLine(s);
        }
        trie.Put("", 3);
        Console.WriteLine("\n\nfloor:\n");
        Console.WriteLine(trie.Ceil("seashora")); // try searching floor and ceil in an empty trie
    }
}

