package search;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;

/**
 * AStar Search
 * 
 * @author Ryan Paulitschke
 */
public class AStar implements Search{

	/**
	 * Search node for A*
	 */
	public class searchNode{
		Searchable value;
		searchNode parent=null;
		
		int g=0;
		int h;
		
		public int f(){
			return g + h;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (obj instanceof searchNode)
				return value.isEqual(((searchNode) obj).value);
			return false;
		}
		
		@Override
		public int hashCode() {
			return value.hashCode();
		}
	}
	
	
	/**
	 * Builds a path from starting initial node to end node
	 * @param end the final node in a path
	 * @return the path from the initial node to the end node
	 */
	public LinkedList<Searchable> buildPath(searchNode end){
		LinkedList<Searchable> path = new LinkedList<>();
		searchNode active = end;
		
		while (active.parent!=null){
			path.addFirst(active.value);
			active = active.parent;
		}
		//add starting node (needed for encoding)
		path.addFirst(active.value);
		
		return path;
		
	}
	
	/**
	 * Searches for a path from start state to goal state
	 */
	@Override
	public LinkedList<Searchable> search(Searchable start, Searchable goal) {
		PriorityQueue<searchNode> openList = new PriorityQueue<searchNode>(6, (searchNode a, searchNode b)->{
			if(a.f() < b.f())
				return -1;
			else if(a.f() == b.f())
				return 0;
			else
				return 1;
		});
		
		HashMap<searchNode,searchNode> closedList = new HashMap<searchNode,searchNode>();
		
		
		searchNode startnode = new searchNode();
		startnode.value = start;
		startnode.h = start.calculateHeuristic();
		
		openList.add(startnode);
		while (!openList.isEmpty()){
			
			searchNode current = openList.poll();
			
			if(current.value.isEqual(goal)){
				
				return buildPath(current);
			}
			
			closedList.put(current, current);
			for (Searchable child : current.value.generateChildren()){
				boolean add_child = true;
				
				int g = current.g + 1; 
				
				if (closedList.containsKey(child)){
					searchNode match = closedList.get(child);
					add_child=false;
					
					if (g < match.g){
						closedList.remove(match);
						add_child=true;
					}
				}
				if (add_child){
					searchNode thematch = null;
					for (searchNode match : openList){
						if (match.value.isEqual(child)){
							thematch = match; break;
						}
					}
					//If child is in openlist
					if(thematch != null){
						add_child=false;
						
						if (g < thematch.g){
							add_child=true;
							openList.remove(thematch);
						}
					}
				} 
				
				if (add_child){
					searchNode childnode = new searchNode();
					childnode.value = child;
					childnode.parent = current;
					childnode.g = g;
					childnode.h = child.calculateHeuristic();
					openList.add(childnode);
				}
					
				
			}
		}
		
		
		
		
		return null;
	}



	

	
	
	

}
