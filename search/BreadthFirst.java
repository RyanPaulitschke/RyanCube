package search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Breadth First Search
 * 
 * @author Ryan Paulitschke
 */
public class BreadthFirst implements Search {
	
	/**
	 *Search node for BFS
	 */
	public class searchNode {
		Searchable value;
		searchNode parent = null;

		public boolean equals(Object obj) {
			if (obj instanceof searchNode)
				return value.isEqual(((searchNode) obj).value);
			return false;
		}

		public int hashCode() {
			return value.hashCode();
		}

	}
	
	/**
	 * Searches for a path from start state to goal state
	 */
	@Override
	public LinkedList<Searchable> search(Searchable start, Searchable goal) {
		LinkedList<searchNode> openList = new LinkedList<searchNode>();
		HashMap<searchNode, searchNode> closedList = new HashMap<searchNode, searchNode>();

		
		searchNode startNode = new searchNode();
		startNode.value = start;

		openList.addLast(startNode);

		while (!openList.isEmpty()) {
			searchNode current = openList.poll();

			if (current.value.isEqual(goal))
				return buildPath(current);
			else {
				for(Searchable child : current.value.generateChildren()){
					if (!closedList.containsKey(child)){
						searchNode childNode = new searchNode();
						childNode.value = child;
						childNode.parent = current;
						
						closedList.put(childNode, childNode);
						openList.addLast(childNode);
					}
				}
			}

		}

		System.out.println("No path");
		return null;
	}

	/**
	 * Builds a path from starting initial node to end node
	 * @param end the final node in a path
	 * @return the path from the initial node to the end node
	 */
	public LinkedList<Searchable> buildPath(searchNode end) {
		LinkedList<Searchable> path = new LinkedList<>();

		searchNode active = end;
		while (active.parent != null) {
			path.addFirst(active.value);
			active = active.parent;
		}
		
		//add starting node (needed for encoding)
		path.addFirst(active.value);
		
		return path;
	}


}
