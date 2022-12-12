package engtelecom.std;

public class Cell  {
    int x;
    int y;
    int dist;  	//distance
    Cell prev;  //parent cell in the path
    Cell(int x, int y, int dist, Cell prev) {
        this.x = x;
        this.y = y;
        this.dist = dist;
        this.prev = prev;
    }
    
    @Override
    public String toString(){
        return "(" + x + "," + y + ")";
    }
}