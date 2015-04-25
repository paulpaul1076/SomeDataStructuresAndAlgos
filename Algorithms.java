
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
	static void JosephusProblem(int size, int M){
		GeneralizedQueue<Integer> gq = new GeneralizedQueue<Integer>();
		for(int i = size - 1; i >= 0; --i){
    		gq.insert(i);
    	}
    	for(int i = M - 1; !gq.isEmpty(); i = (gq.size() == 0 ? 0 : (i + M - 1) % gq.size())){
    		System.out.println(gq.delete(i));
    	}
	}
}
