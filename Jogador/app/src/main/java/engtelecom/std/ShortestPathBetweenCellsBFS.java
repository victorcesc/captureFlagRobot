package engtelecom.std;
import java.util.LinkedList;

public class ShortestPathBetweenCellsBFS {
    //ALGORITMO ???????

	public static LinkedList<Cell> shortestPath(int[][] matrix, int[] start, int[] end) {
	
	   
		int jx = start[0], jy = start[1];//jogador
		int bx = end[0], by = end[1];//bandeira
		//if start or end value is 0, return
		

		if (matrix[jx][jy] == 0 || matrix[bx][by] == 0) {
            // System.out.println(matrix[sx][sy]);
            // System.out.println(matrix[dx][dy]);
			// System.out.println("There is no path.");
			return null;  
		}

		LinkedList<Cell> cells = new LinkedList<>();
		for (int i = 0; i < matrix.length; i++) {
			if(jy == by){
				Cell c = new Cell(jx, jy);
				cells.add(c);
				break;
			}else{
				if(jx < bx){
					Cell c = new Cell(jx++, jy);
					cells.add(c);
				}else if(jx > bx){
					Cell c = new Cell(jx--, jy);
					cells.add(c);
				}
			}
			
			for (int j = 0; j < matrix[0].length; j++) {
				if(jy == by){
					Cell c2 = new Cell(jx, jy);
					cells.add(c2);
					break;
				}else{
					if(jy < by){
						Cell c2 = new Cell(jx, jy++);
						cells.add(c2);
					}else if(jy > by){
						Cell c2 = new Cell(jx, jy--);
						cells.add(c2);
					}
				}
			}				
		}
		return cells;
	}

	
	//function to update cell visiting status, Time O(1), Space O(1)
	

}