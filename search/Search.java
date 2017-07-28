package search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Search Interface
 * 
 * @author Ryan Paulitschke
 */
public interface Search {

	// closed list
	HashMap<Searchable, Integer> closedList = new HashMap<Searchable, Integer>();
	// child list
	ArrayList<Searchable> childList = new ArrayList<Searchable>();

	public LinkedList<Searchable> search(Searchable start, Searchable goal);

}
