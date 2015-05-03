
public class Pair<F,S> {
	public F first;
	public S second;
	public Pair(){}
	public Pair(F first, S second){
		this.first = first; this.second = second;
	}
	public String toString(){
		return "<" + first.toString() + ", " + second.toString() + ">";
	}
}
