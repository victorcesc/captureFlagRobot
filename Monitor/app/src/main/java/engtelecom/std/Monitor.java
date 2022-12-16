package engtelecom.std;

public class Monitor {
    
    

    private String map;
    private int size;
    
    public Monitor(String map, int size) {
        this.map = map;
        this.size = size;
    }

    public String getMap() {
        return map;
    }
    public void setMap(String map) {
        this.map = map;
    }
    public int getSize() {
        return size;
    }
    public void setSize(int size) {
        this.size = size;
    }

    public String monta_mapa(){
        String retorno = "";
        String lines[] = getMap().split(";");
        String cells[] = lines[0].split(",");
        
        for (int i=0; i<size; i++) {
            for (int j=0; j<size; j++) {
                retorno += lines[j] + " ";
                if(j < size - 1) {
                    retorno += "\n"; 
                }
            }
            retorno += cells[i];
            if(i < size){
                retorno += "\n";
                break;
            }            
        }
        return  retorno.substring(0,retorno.length() - 3);
    
    }

}
