import java.util.ArrayList;
import java.util.Iterator;


public class IteratorTrial {
	private static ArrayList<String> al;
	private static Iterator<String> it;
	
	public static void main(String[] args) {
		al = new ArrayList<>();
		/*
		al.add("kobe");
		al.add("kobe2");
		al.add("kobe3");
		al.add("kobe4");
		*/
		
		it = al.iterator();

		while(it.hasNext()) {
			System.out.println(it.next());
		}
		if (it.hasNext()) { System.out.println("has next"); }
		else { System.out.println("no next"); }
	}
}
