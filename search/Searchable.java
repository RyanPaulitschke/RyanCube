package search;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Interface for searchable objects
 * 
 * @author Ryan Paulitschke
 */
public interface Searchable {

	public int calculateHeuristic();

	public boolean isEqual(Searchable other);

	LinkedList<Searchable> generateChildren();
	
}
