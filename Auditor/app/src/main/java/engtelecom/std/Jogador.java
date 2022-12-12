package engtelecom.std;

public class Jogador {
    
    private String nome;
    //posição atual do jogador
    private int x;
    private int y;

    public Jogador(String nome,int x, int y) {
        this.nome = nome;
        this.x = x;
        this.y = y;
    }
    public int getX() {
        return x;
    }
    public void setX(int x) {
        this.x = x;
    }
    public int getY() {
        return y;
    }
   
    public void setY(int y) {
        this.y = y;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    @Override
    public String toString() {
        return "Jogador [nome=" + nome + ", x=" + x + ", y=" + y + "]";
    }
}
