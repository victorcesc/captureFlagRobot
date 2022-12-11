package engtelecom.std;
import engtelecom.std.game.AuditorGrpc;
import engtelecom.std.game.Jogada;
import engtelecom.std.game.Resposta;
import engtelecom.std.game.Ingresso;
import java.util.Random;
import io.grpc.stub.StreamObserver;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
public class AuditorImpl extends AuditorGrpc.AuditorImplBase {
// Serviço de log para registrar as mensagens de depuração, informação, erro, etc.
private static final Logger logger = Logger.getLogger(AuditorImpl.class.getName());

// Banco de dados para armazenar todos os contatos
private Map<Integer, Play> jogadas;
private Map<Integer, Jogador> jogadores;
private int numero_jogadas = 1;
private int numero_jogadores = 1;
private int tamanho_mapa = 3; //16 celulas[ matriz 4x4] matriz[3][3] 0123 é o tamanho default, 4 é o multiplicador da matriz
public AuditorImpl() {
    this.jogadas = new HashMap<>();
    this.jogadores = new HashMap<>();
}

@Override
public void entrar(Ingresso request, StreamObserver<Resposta> responseObserver){
    String mensagem = "Jogador " + request.getNome() + " entrou no jogo!!";   
    //verifica se jogador esta no jogo, true = sim, false = nao   
    boolean ingame = this.in_game(request.getNome());

    if(ingame){ 
        mensagem = "O jogador : " + request.getNome() + " ja esta no jogo";    
    }else{
        //jogador ganha um par de coordenadas no ingresso por sorteio
        Random rand = new Random();
        int x = rand.nextInt(tamanho_mapa);
        int y = rand.nextInt(tamanho_mapa);
        Jogador newplayer = new Jogador(request.getNome(),x,y);
        this.jogadores.put(numero_jogadores,newplayer);
        //mensagem = "Jogador " + request.getNome() + " entrou no jogo!!";
    }
    logger.info(mensagem);
    System.out.println(this.jogadores.toString());
    Resposta resposta = Resposta.newBuilder().setResultado(mensagem).build();
    responseObserver.onNext(resposta);
    responseObserver.onCompleted();
    numero_jogadores++;
}


@Override
public void jogar(Jogada request, StreamObserver<Resposta> responseObserver) {
    String mensagem = "O jogador " + request.getNome() + " nao pertence a esse jogo!!" ;
    
    //VERIFICAR SE O JOGADOR ESTÁ NO JOGO
   
    boolean ingame = this.in_game(request.getNome());

    if(ingame){
        System.out.println("O jogador pertence a esse jogo!");
        System.out.println("Iniciando jogada...");   
        Play jogada = new Play();    
        jogada.setNome(request.getNome());//setando nome do jogador que efetuou a jogada
        jogada.setX(request.getCoord(0).getX());//setando coord da jogada
        jogada.setY(request.getCoord(0).getY());//setando coord da jogada
        this.jogadas.put(numero_jogadas,jogada);
        String mensagem = "A Jogada : X:" + request.getCoord(0).getX()
        + ",Y:" +request.getCoord(0).getY() + 
        " do " + request.getNome() + " foi adicionada com sucesso!!";
        System.out.println(this.jogadas.toString());        
        logger.info(mensagem);
        numero_jogadas++; 
    }

    Resposta resposta = Resposta.newBuilder().setResultado(mensagem).build();
    responseObserver.onNext(resposta);
    responseObserver.onCompleted();
}
// @Override
// public void buscar(Jogada request, StreamObserver<Jogada> responseObserver) {
//     // Jogada resposta = this.jogadas.get(request.getId());
//     // logger.info("Buscar... " + resposta);
//     // responseObserver.onNext(resposta);
//     // responseObserver.onCompleted();
// }


public boolean in_game(String nome){

    if(!jogadores.isEmpty()){
        for(int i=1; i < numero_jogadores ; i++){
            if (this.jogadores.get(i).getNome().equals(nome)){     
                //jogador esta no jogo           
                System.out.println("TA NO JOGO :" + nome);
                return true;                
            }
        }
    }
    return false;

}




}
