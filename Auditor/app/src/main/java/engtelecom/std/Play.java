package engtelecom.std;

public class Play {
    private String nome;    
    private int x;
    private int y;
        
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    @Override
    public String toString() {
        return "Jogada \n[ Nome do jogador = " + nome + " ]\nCoordenadas : \n[ x=" + x + ", y=" + y + " ]";
    }

    
}
