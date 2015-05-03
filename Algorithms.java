import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;


public class Algorithms {
	// can generate N! permutations
	static <T> void shufftArray(T[] a, int from, int to){
		for(int i = from; i < to; ++i){
			for(int j = i; j < to; ++j){
				//find random
				int randomId = getRandomInt(from, to - 1);
				//swap
				T temp = a[randomId];
				a[randomId] = a[j];
				a[j] = temp;
			}
		}
	}
	static int getRandomInt(int from, int to){
		assert from < to;
		int diff = to - from;
		int rand = from + (int)(Math.random() * diff);
		return rand;
	}
	static double getRandomDouble(double from, double to){
		assert from < to;
		double diff = to - from;
		double rand = from + (Math.random() * diff);
		return rand;
	}
	static void JosephusProblem(int size, int M){
		GeneralizedQueueArray<Integer> gq = new GeneralizedQueueArray<Integer>();
		for(int i = size - 1; i >= 0; --i){
    		gq.insert(i);
    	}
    	for(int i = M - 1; !gq.isEmpty(); i = (gq.size() == 0 ? 0 : (i + M - 1) % gq.size())){
    		System.out.println(gq.delete(i));
    	}
	}
	public static void printDirs(String path, String indent){
		File f = new File(path);
		if(!f.isDirectory() && f.isFile()){
			System.out.println(indent + f.getName());
			return;
		}
		if(f.isDirectory()){
			File[] files = f.listFiles();
			System.out.println(indent + f.getAbsolutePath());
			for(File file : files){
				printDirs(file.getAbsolutePath(), indent + "\t");
			}
		}
	}
	static double getDist(double x, double y){
    	return Math.abs(x - y);
    }
    static Pair<Double, Double> closestPair(double[] x){
    	Pair<Double, Double> ret = new Pair<Double, Double>();
    	double minDist = Double.MAX_VALUE;
    	for(int i = 0; i < x.length; ++i){
    		for(int j = 0; j < x.length; ++j){
    			if(i != j){
    				double tempDist = getDist(x[i], x[j]);
    				if(tempDist < minDist){
    					minDist = tempDist;
    					ret.first = x[i]; ret.second = x[j];
    				}
    			}
    		}
    	}
    	//System.out.println("Min dist = " + minDist);
    	return ret;
    }
    static Pair<Double, Double> closestPairNlogN(double[] a){
    	Pair<Double, Double> ret = new Pair<Double, Double>();
    	Arrays.sort(a);
    	double minDist = Double.MAX_VALUE;
    	double tempDist = 0;
    	for(int i = 0; i < a.length - 1; ++i){
    		tempDist = getDist(a[i], a[i + 1]);
    		if(minDist > tempDist) {
    			minDist = tempDist;
    			ret.first = a[i]; ret.second = a[i + 1];
    		}
    	}
    	return ret;
    }
    static Pair<Double, Double> farthestPairNlogN(double[] a){
    	Pair<Double, Double> p = new Pair<Double, Double>();
    	Arrays.sort(a);
    	double maxDist = 0;
    	double tempDist = 0;
    	
    	for(int i = 0; i < a.length - 1; ++i){
    		tempDist = getDist(a[i], a[i + 1]);
    		if(maxDist < tempDist){
    			maxDist = tempDist;
    			p.first = a[i]; p.second = a[i + 1];
    		}
    	}
    	
    	return p;
    }
    static int binarySearch(int[] a, int key){
		int lo = 0, hi = a.length - 1;
		while(hi >= lo){
			 int mid = lo + (hi - lo) / 2;
			 if(a[mid] > key) hi = mid - 1;
			 else if(a[mid] < key) lo = mid + 1;
			 else return mid;
		}
		return -1;
	}
	static int twoSum(int[] a){
		int count = 0;
		for(int i = 0; i < a.length; ++i){
			if(binarySearch(a, a[i]) > i) count++;
		}
		return count;
	}
	static int twoSumFaster(int[] a){
		int count = 0;
		int lo = 0, hi = a.length - 1;
		while(lo != hi){
			if(a[lo] + a[hi] > 0) hi--;
			else if(a[lo] + a[hi] < 0) lo++;
			else{
				count++;
				hi--;
			}
		}
		return count;
	}
	static int threeSum(int[] a){
		int count = 0;
		for(int i = 0; i < a.length; ++i){
			for(int j = i + 1; j < a.length; ++j){
				if(binarySearch(a, (-a[i] - a[j])) > j) count++;
			}
		}
		return count;
	}
	static int fourSum(int[] a){
		int count = 0;
		for(int k = 0; k < a.length; ++k){
			for(int i = k + 1; i < a.length; ++i){
				for(int j = i + 1; j < a.length; ++j){
					if(binarySearch(a, (-a[i] - a[j] - a[k])) > j) count++;
				}
			}
		}
		return count;
	}
	public static void printAll(int[] a) {
        int N = a.length;
        Arrays.sort(a);
        for (int i = 0; i < N; i++) {
            int j = Arrays.binarySearch(a, -a[i]);
            //if (j > i) StdOut.println(a[i] + " " + a[j]);
        }
    } 

    // return number of distinct pairs (i, j) such that a[i] + a[j] = 0
    public static int count(int[] a) {
        int N = a.length;
        Arrays.sort(a);
        int cnt = 0;
        for (int i = 0; i < N; i++) {
            int j = Arrays.binarySearch(a, -a[i]);
            if (j > i) cnt++;
        }
        return cnt;
    } 
    static public int countLIN(int[] a){ // a must be sorted
		int left = 0, right = a.length - 1;
		int count = 0;
		while(left < right){
			if(a[left] + a[right] == 0){
				count++;
				left++;
				right--;
			}
			else if(a[left] + a[right] > 0) right--;
			else /*if(a[left] + a[right] < 0)*/ left++;
		}
		return count;
	}
    static int quad3sum(int[] a){
    	Arrays.sort(a);
    	HashSet<ArrayList<Integer>> hs = new HashSet<ArrayList<Integer>>();
    	for(int i = 0; i < a.length - 3; ++i){
    		int start = i + 1;
    		int end = a.length - 1;
    		while(start < end){
    			int A = a[i], B = a[start], C = a[end];
    			if(A + B + C == 0){
    				int[] ar = {A,B,C};
    				Arrays.sort(ar);
    				ArrayList<Integer> arg = new ArrayList<Integer>();
    				for(int k = 0; k < ar.length; ++k){
    					arg.add(ar[k]);
    				}
    				hs.add(arg);
    				start++;
    				end--;
    			}
    			else if(A + B + C > 0){
    				end--;
    			}
    			else {
    				start++;
    			}
    		}
    	}
    	System.out.println("output quad");
    	for(ArrayList<Integer> ar : hs){
    		for(int i : ar){
    			System.out.print(i + " ");
    		}
    		System.out.println();
    	}
    	return hs.size();
    }
    static int n2logn3sum(int[] a){
    	Arrays.sort(a);
    	HashSet<ArrayList<Integer>> hs = new HashSet<ArrayList<Integer>>();
    	for(int i = 0; i < a.length; ++i){
    		for(int j = i + 1; j < a.length; ++j){
    			if(binarySearch(a, -a[i]-a[j]) > j){
    				int[] ar = {-a[i]-a[j],a[i],a[j]};
    				Arrays.sort(ar);
    				ArrayList<Integer> arList = new ArrayList<Integer>();
    				for(int k = 0; k < ar.length; ++k){
    					arList.add(ar[k]);
    				}
    				hs.add(arList);
    			}
    		}
    	}
    	System.out.println("output n2logn");
    	for(List<Integer> ar : hs){
    		for(int i : ar){
    			System.out.print(i + " ");
    		}
    		System.out.println();
    	}
    	return hs.size();
    }
    static Integer localMinimum(int[] a){
		int lo = 0, hi = a.length - 1;
		while(lo <= hi){
			int mid = lo + (hi - lo) / 2;
			if(mid == 0){
				return a[mid];
			}
			else if(mid == a.length - 1){
				return a[mid];
			}
			else if(a[mid] < a[mid + 1] && a[mid] < a[mid - 1]){
				return a[mid];
			}
			else if(a[mid] > a[mid - 1]){
				hi = mid - 1;
			}
			else if(a[mid] > a[mid + 1]){
				lo = mid + 1;
			}
			else if(a[mid] == a[mid + 1] || a[mid] == a[mid - 1]){
				hi = mid - 1;
			}
			//System.out.println(a[mid - 1] + ", " + a[mid] + ", " + a[mid + 1]);
		}
		return null;
	}
}
