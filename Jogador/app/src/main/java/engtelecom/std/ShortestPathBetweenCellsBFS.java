package engtelecom.std;
import java.util.LinkedList;

public class ShortestPathBetweenCellsBFS {
    
	//BFS, Time O(n^2), Space O(n^2)
	public static LinkedList<Cell> shortestPath(int[][] matrix, int[] start, int[] end) {
		int sx = start[0], sy = start[1];
		int dx = end[0], dy = end[1];	
		//if start or end value is 0, return
		if (matrix[sx][sy] == 0 || matrix[dx][dy] == 0) {
            // System.out.println(matrix[sx][sy]);
            // System.out.println(matrix[dx][dy]);
			// System.out.println("There is no path.");
			return null;  
		}
		//initialize the cells 
	    int m = matrix.length;
	    int n = matrix[0].length;
        System.out.println("m : " + m);	    
        System.out.println("n : " + n);
	    Cell[][] cells = new Cell[m][n];
	    for (int i = 0; i < m; i++) {
	        for (int j = 0; j < n; j++) {
	            if (matrix[i][j] != -1) {
	                cells[i][j] = new Cell(i, j, Integer.MAX_VALUE, null);
                    //System.out.println(cells[i][j]);
	            }
	        }
	    }
        //System.out.println(cells.toString());
	    //breadth first search
	    LinkedList<Cell> queue = new LinkedList<>();       
	    Cell src = cells[sx][sy];
        //System.out.println("src" + src.toString());
	    src.dist = 0;
	    queue.add(src);
	    Cell dest = null;
	    Cell p;
	    while ((p = queue.poll()) != null) {
	    	//find destination 
            //System.out.println(p);
	        if (p.x == dx && p.y == dy) { 
	            dest = p;
                //System.out.println(p);
	            break;
	        }      
	        // moving up
	        visit(cells, queue, p.x - 1, p.y, p);        
	        // moving down
	        visit(cells, queue, p.x + 1, p.y, p);        
	        // moving left
	        visit(cells, queue, p.x, p.y - 1, p);        
	        //moving right
	        visit(cells, queue, p.x, p.y + 1, p);
	    }
	    
	    //compose the path if path exists
	    if (dest == null) {
	    	//System.out.println("dest == null there is no path.");
	        return null;
	    } else {
	        LinkedList<Cell> path = new LinkedList<>();
	        p = dest;
	        do {
	            path.addFirst(p);
	        } while ((p = p.prev) != null);
	        //System.out.println(path);
            return path;
	    }
	}
	
	//function to update cell visiting status, Time O(1), Space O(1)
	private static void visit(Cell[][] cells, LinkedList<Cell> queue, int x, int y, Cell parent) { 
		//out of boundary
	    if (x < 0 || x >= cells.length || y < 0 || y >= cells[0].length || cells[x][y] == null) {
	        return;
	    }    
	    //update distance, and previous node
	    int dist = parent.dist + 1;
	    Cell p = cells[x][y];
	    if (dist < p.dist) {
	        p.dist = dist;
	        p.prev = parent;
	        queue.add(p);
	    }
	}

}