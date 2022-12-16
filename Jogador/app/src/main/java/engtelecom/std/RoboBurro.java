package engtelecom.std;
import java.util.LinkedList;

public class RoboBurro {
    //ALGORITMO ???????

	public static LinkedList<Cell> shortestPath(int[][] matrix, int[] start, int[] end) {
	
	    int tamanho = matrix.length;
		int jx = start[0], jy = start[1];//jogador
		int bx = end[0], by = end[1];//bandeira
		//if start or end value is 0, return
		

		if (matrix[jx][jy] == 0 || matrix[bx][by] == 0) {
			return null;  
		}
		//bx = 2
		//jx = 2
		//by = 1
		//jy = 0


		LinkedList<Cell> cells = new LinkedList<>();

		for (int i = 0; i < tamanho; i++) {
			if(jx == bx){
				Cell c = new Cell(jx, jy);
				cells.add(c);
				i = tamanho;
			}else{
				if(jx < bx){
					jx++;
					Cell c = new Cell(jx, jy);
					cells.add(c);
				}else if(jx > bx){
					jx--;
					Cell c = new Cell(jx, jy);
					cells.add(c);
				}
			}
			
			for (int j = 0; j < tamanho; j++) {
				if(jy == by){
					Cell c2 = new Cell(jx, jy);
					cells.add(c2);
					j = tamanho;
				}else{
					if(jy < by){
						jy++;
						Cell c2 = new Cell(jx, jy);
						cells.add(c2);
					}else if(jy > by){
						jy--;
						Cell c2 = new Cell(jx, jy);
						cells.add(c2);
					}
				}
			}				
		}
		return cells;
	}
	

}