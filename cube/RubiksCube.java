package cube;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;

import search.Searchable;

/**
 * Creates an manages Rubik's Cube data
 * Rubix cubes are represented as 3D arrays
 * @author Ryan Paulitschke
 */
public class RubiksCube implements Searchable {
	public int[][][] cube;
	//Cube size
	public int size;
	
	//Last move taken
	public int x,y,XYZ=4,dir;
	

	/**
	 * Rubix cube constructor
	 * @param cube_size length of the cube in # of cubies
	 */
	public RubiksCube(int cube_size) {
		size = cube_size;
		
	}
	
	/**
	 * Sets the previous move a cube made to get to it's current state
	 * @param x_pos horizontal selection position
	 * @param y_pos vertical selection position
	 * @param axis axis to rotate the cube (X,Y,Z = 0,1,2)
	 * @param direction direction to rotate (CW,CCW = 0,1)
	 */
	public void prevMove(int x_pos, int y_pos, int axis, int direction){
		//Last move
		x = x_pos;
		y = y_pos; 
		XYZ = axis; 
		dir = direction;
	}
	/**
	 * Build RubixCube copies
	 * @param c RubixCube to copy
	 */
	public RubiksCube(RubiksCube c){
		
		this.size = c.size;
		this.cube = new int[c.cube.length][][];
		
		for(int i = 0; i < c.cube.length; i++){
			this.cube[i] = new int[c.cube[i].length][];
			for(int j = 0; j < c.cube[i].length; j++){
				this.cube[i][j] = c.cube[i][j].clone();
			}
		}
	}

	
	/**
	 * Sets a RubixCube to the solved state
	 */
	public void initSolvedState(){
		cube = new int[6][size][size];
		
		for (int j = 0; j < size; j++) {
			for (int k = 0; k < size; k++) {
				cube[0][j][k] = 0; // yellow
				cube[1][j][k] = 1; // orange
				cube[2][j][k] = 2; // red
				cube[3][j][k] = 3; // purple
				cube[4][j][k] = 4; // blue
				cube[5][j][k] = 5; // green
			}
		}
	}

	
	/**
	 * Applies random rotations to a RubixCube
	 * @param rotations # of rotations to apply
	 */
	public void shuffle(int rotations){
		Random num = new Random();
		
		for (int i=0;i<rotations;i++){
			rotate(num.nextInt(size),num.nextInt(size),num.nextInt(3),num.nextInt(2));
		}
	}
	
	public void rotate(int x, int y, int XYZ, int dir) {
		int cs = (size-1); //cube index size
		//X
		if (XYZ==0){
	
		switch(dir){
		case 0:
		
		for (int i=0;i<size;i++){
			int temp = cube[1][x][i];
			cube[1][x][i] = cube[2][x][i];
			cube[2][x][i] = cube[3][x][i];
			cube[3][x][i] = cube[0][x][i];
			cube[0][x][i] = temp;
		}
		//sides spin 90 degrees
		if (x==0)
			spin(4,0);
		else if (x==cs)
			spin(5,1);
		break;
		
		case 1:
			for (int i=0;i<size;i++){
				int temp = cube[1][x][i];
				cube[1][x][i] = cube[0][x][i];
				cube[0][x][i] = cube[3][x][i];
				cube[3][x][i] = cube[2][x][i];
				cube[2][x][i] = temp;
			}
			//sides spin 90 degrees
			if (x==0)
				spin(4,1);
			else if (x==cs)
				spin(5,0);
			break;
		}
		}
		
		//Y
		if (XYZ==1){
			
			
		switch(dir){
		case 0:
		
		for (int i=0;i<size;i++){
			int temp = cube[1][i][y];
			cube[1][i][y] = cube[4][i][y];
			cube[4][i][y] = cube[3][cs-i][cs-y];
			cube[3][cs-i][cs-y] = cube[5][i][y];
			cube[5][i][y] = temp;
		}
		//sides spin 90 degrees
		if (y==0)
			spin(2,1);
		else if (y==cs)
			spin(0,0);
		break;
		
		case 1:
			for (int i=0;i<size;i++){
				int temp = cube[1][i][y];
				cube[1][i][y] = cube[5][i][y];
				cube[5][i][y] = cube[3][cs-i][cs-y];
				cube[3][cs-i][cs-y] = cube[4][i][y];
				cube[4][i][y] = temp;
			}
			//sides spin 90 degrees
			if (y==0)
				spin(2,0);
			else if (y==cs)
				spin(0,1);
			break;
		}
		}
		
		//Z
		if (XYZ==2){
			
			
		switch(dir){
		case 0:
		
		for (int i=0;i<size;i++){
			int temp = cube[2][i][y];
			cube[2][i][y] = cube[5][cs-y][i];
			cube[5][cs-y][i] = cube[0][cs-i][cs-y];
			cube[0][cs-i][cs-y] = cube[4][y][cs-i];
			cube[4][y][cs-i] = temp;
		}
		//sides spin 90 degrees
				if (y==0)
					spin(3,0);
				else if (y==cs)
					spin(1,1);
				break;
				
		case 1:
			for (int i=0;i<size;i++){
				int temp = cube[2][i][y];
				cube[2][i][y] = cube[4][y][cs-i];
				cube[4][y][cs-i] = cube[0][cs-i][cs-y];
				cube[0][cs-i][cs-y] = cube[5][cs-y][i];
				cube[5][cs-y][i] = temp;
			}
			//sides spin 90 degrees
					if (y==0)
						spin(3,1);
					else if (y==cs)
						spin(1,0);
			break;
				
		}
		}
	}

	//dir: 0 CW, 1 CCW
	//90 Degree face rotation
	public void spin(int face, int dir) {
		int cs = (size-1); //cube size
		int lower = (int) Math.floor(size*0.5);
		int	upper = (int) Math.ceil(size*0.5);
		
		switch (dir){
		case 0:
			//CW
			for (int i = 0;i<lower;i++){
			  for (int j = 0;j<upper;j++){
				int temp = cube[face][i][j];
				cube[face][i][j] = cube[face][j][cs-i];
				cube[face][j][cs-i] = cube[face][cs-i][cs-j];
				cube[face][cs-i][cs-j] = cube[face][cs-j][i];
				cube[face][cs-j][i] = temp;
			}}
			break;
			
		case 1:
			//CCW
			for (int i = 0;i<lower;i++){
			  for (int j = 0;j<upper;j++){
			    int temp = cube[face][j][cs-i]; 
			    cube[face][j][cs-i] = cube[face][i][j];
			    cube[face][i][j]= cube[face][cs-j][i];
			    cube[face][cs-j][i] = cube[face][cs-i][cs-j];
			    cube[face][cs-i][cs-j] = temp;
			  }}
			break;
		}

				

	}

	/**
	 * Generates all possible children 1 move away from a cube
	 * and returns it as a Searchable LinkedList
	 */
	@Override
	public LinkedList<Searchable> generateChildren() {
		LinkedList<Searchable> children = new LinkedList<Searchable>();
		
		RubiksCube parent = this;
		for (int i=0;i<parent.size;i++){
		//X rotations
		RubiksCube a = new RubiksCube(parent);
		a.rotate(i, 0, 0, 0);
		a.prevMove(i, 0, 0, 0);
		RubiksCube b = new RubiksCube(parent);
		b.rotate(i, 0, 0, 1);
		b.prevMove(i, 0, 0, 1);
		
		//Y rotations
		RubiksCube c = new RubiksCube(parent);
		c.rotate(0, i, 1, 0);
		c.prevMove(0, i, 1, 0);
		RubiksCube d = new RubiksCube(parent);
		d.rotate(0, i, 1, 1);
		d.prevMove(0, i, 1, 1);
		
		//Z rotations
		RubiksCube e = new RubiksCube(parent);
		e.rotate(0, i, 2, 0);
		e.prevMove(0, i, 2, 0);
		RubiksCube f = new RubiksCube(parent);
		f.rotate(0, i, 2, 1);
		f.prevMove(0, i, 2, 1);
		
		//add new children
		children.add(a);
		children.add(b);
		children.add(c);
		children.add(d);
		children.add(e);
		children.add(f);
		}
		
		return children;
		
	}
	
	/**
	 * Calculates the h value of a RubixCube 
	 * for A* search
	 */
	@Override
	public int calculateHeuristic() {
		int h = 0;
	for (int i = 0; i < 6; i++) {
		for (int j = 0; j < size; j++) {
			for (int k = 0; k < size; k++) {
				switch (i){
				case 0:
					if (cube[i][j][k] == 0) // yellow
						break;
					else
						if (cube[i][j][k] == 2)
							h+=2;
						else
							h++;
					break;
				case 1:
					if (cube[i][j][k] == 1) // orange
						break;
					else
						if (cube[i][j][k] == 3)
							h+=2;
						else
							h++;
					break;
				case 2:
					if (cube[i][j][k] == 2) // red
						break;
					else
						if (cube[i][j][k] == 0)
							h+=2;
						else
							h++;
					break;
				case 3:
					if (cube[i][j][k] == 3) // purple
						break;
					else
						if (cube[i][j][k] == 1)
							h+=2;
						else
							h++;
					break;
				case 4:
					if (cube[i][j][k] == 4) // blue
						break;
					else
						if (cube[i][j][k] == 5)
							h+=2;
						else
							h++;
					break;
				case 5:
					if (cube[i][j][k] == 5) // green
						break;
					else
						if (cube[i][j][k] == 4)
							h+=2;
						else
							h++;
					break;
				}
			}
		}
		}
		
		return (int) Math.abs(h/8.0); //8,7.6
	}

	/**
	 * Checks if another Searchable is the same as the calling RubixCube
	 * @param other Searchable/RubixCube to test equality
	 */
	@Override
	public boolean isEqual(Searchable other) {
		return equals(other);
	}
	
	/**
	 * Checks if another Object is the same as the calling RubixCube
	 * @param other Object/RubixCube to test equality
	 */
	@Override
	public boolean equals(Object other){
		if(!(other instanceof RubiksCube))
			return false;
		
		RubiksCube other_c = (RubiksCube)other;
		if (Arrays.deepEquals(cube,other_c.cube))
			return true;
		else
			return false;
	}
	
	/**
	 * Hashcode for hashing a RubixCube
	 * 
	 * Jenkins's one-at-a-time hash
	 * @Author Bob Jenkins
	 * Adapted algorithm for use in Java
	 */
	@Override
	public int hashCode(){
		  int hash = 0;
		  
		  for (int j = 0; j < size; j++) {
				for (int k = 0; k < size; k++) {
					hash += cube[0][j][k]; // yellow
					hash += hash << 10;
				    hash ^= hash >> 6;
					
					hash +=cube[1][j][k]; // orange
					hash += hash << 10;
				    hash ^= hash >> 6;
					
					hash += cube[2][j][k]; // red
					hash += hash << 10;
				    hash ^= hash >> 6;
					
					hash += cube[3][j][k]; // purple
					hash += hash << 10;
				    hash ^= hash >> 6;
					
					hash += cube[4][j][k]; // blue
					hash += hash << 10;
				    hash ^= hash >> 6;
					
					hash += cube[5][j][k]; // green
					hash += hash << 10;
				    hash ^= hash >> 6;
				}
			}
		  
		  hash += hash << 3;
		  hash ^= hash >> 11;
		  hash += hash << 15;
		  return hash;
		
	}
	
	
	/**
	 *Applies the steps found by the searches to solve the cube 
	 * @param steps to get from initial state to goal state
	 */
	public void solve(LinkedList<Searchable> steps){
		
		for (Searchable step : steps){
			if(!(step instanceof RubiksCube))
				break;
			
			RubiksCube cube = (RubiksCube)step;
			if (cube.XYZ!=4){
				rotate(cube.x,cube.y,cube.XYZ,cube.dir);
				
			}
			
		}
		
	}
	
	/**
	 * Shows the steps required to go from initial state to goal state
	 * @param steps to get from initial state to goal state
	 */
	public void showSolution(LinkedList<Searchable> steps){
		
		for (Searchable step : steps){
			if(!(step instanceof RubiksCube))
				break;
			
			RubiksCube cube = (RubiksCube)step;
			if (cube.XYZ!=4){
				int x,y;
				String face, dir="";
				
				if (cube.XYZ==2)
					face="UPPER";
				else
					face="LOWER";
				
				if (face=="LOWER"){
					x=cube.x;
					y=cube.y+size;
					//Rotate which dir?
					if (cube.dir==0 && cube.XYZ==0)
						dir="Rotate: DOWN";
					else if(cube.dir==1 && cube.XYZ==0)
						dir="Rotate: UP";
					else if(cube.dir==0 && cube.XYZ==1)
						dir="Rotate: RIGHT";
					else if(cube.dir==1 && cube.XYZ==1)
						dir="Rotate: LEFT";
				}else{
					x=cube.x;
					y=cube.y;
					//Rotate which dir?
					if (cube.dir==0)
						dir="Rotate: LEFT";
					else if(cube.dir==1)
						dir="Rotate: RIGHT";

				}
				System.out.println("At ["+x+","+y+"] "+dir);
				
			}
			
		}
		
	}
	
	/**
	 * Encodes a RubixCube into all 1s/-1s
	 * @return an encoded rubiks cube
	 */
	public String encode(){
		String e_cube="";
		
	for (int i=0;i<6;i++){
		for (int j = 0; j < size; j++) {
			for (int k = 0; k < size; k++) {
				switch(cube[i][j][k]){
				case 0://yellow
					e_cube=e_cube.concat("-1,-1,-1,-1,-1,1,");
					break;
				case 1://orange
					e_cube=e_cube.concat("-1,-1,-1,-1,1,-1,");
					break;
				case 2://red
					e_cube=e_cube.concat("-1,-1,-1,1,-1,-1,");
					break;
				case 3://purple
					e_cube=e_cube.concat("-1,-1,1,-1,-1,-1,");
					break;
				case 4://blue
					e_cube=e_cube.concat("-1,1,-1,-1,-1,-1,");
					break;
				case 5://green
					e_cube=e_cube.concat("1,-1,-1,-1,-1,-1,");
					break;
				}
				
			}
		}}
		//remove last comma
		return e_cube.substring(0,e_cube.length()-1);
		
	}
	
	/**
	 * Encodes a cube rotation into all 1s/-1s
	 * @return an encoded move
	 */
	public String encodeMove(){
		String e_move="";
		
		switch(XYZ){
		case 0://X
			e_move=e_move.concat("-1,-1,1,");
			break;
		case 1://Y
			e_move=e_move.concat("-1,1,-1,");
			break;
		case 2://Z
			e_move=e_move.concat("1,-1,-1,");
			break;
		}
		
		if (dir==0)//CW else CCW
			e_move=e_move.concat("-1,1,");
		else
			e_move=e_move.concat("1,-1,");
			
		//x position
		for (int i=0; i<size;i++){
		if ((size-1-i-x)==0)
			e_move=e_move.concat("1,");
		else
			e_move=e_move.concat("-1,");
		}
		
		//y position
		for (int i=0; i<size;i++){
		if ((size-1-i-y)==0)
			e_move=e_move.concat("1,");
		else
			e_move=e_move.concat("-1,");
		}
	
		//remove last comma
		return e_move.substring(0,e_move.length()-1);
		
	}
	
}
