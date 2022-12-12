package engtelecom.std;
import engtelecom.std.game.AuditorGrpc;
import engtelecom.std.game.Jogada;
import engtelecom.std.game.Resposta;
import engtelecom.std.game.Ingresso;
import engtelecom.std.game.Requisicao;
import engtelecom.std.game.Mapa;


import java.util.Random;
import java.util.Scanner;

import io.grpc.ManagedChannelBuilder;
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
private int max_jogadores;
private int max_bandeiras;
private int tamanho_mapa; //16 celulas[ matriz 4x4] matriz[3][3] 0123 é o tamanho default
private int numero_jogadas;
private int numero_jogadores;
private int numero_bandeiras;
private String map;
private int mapa[][];


public void setMap(String map) {
    this.map = map;
}

public String getMap() {
    return map;
}



public int getMax_jogadores() {
    return max_jogadores;
}

public int getMax_bandeiras() {
    return max_bandeiras;
}

public int getTamanho_mapa() {
    return tamanho_mapa;
}

public int getNumero_jogadas() {
    return numero_jogadas ;//porcausa do index do hashmap??
}

public int getNumero_jogadores() {
    return numero_jogadores;//porcausa do index do hashmap??
}

public AuditorImpl() {
    this.max_jogadores = 2;
    this.max_bandeiras = 1;
    this.tamanho_mapa = 3;
    this.jogadas = new HashMap<>();
    this.jogadores = new HashMap<>();
    this.mapa = new int[tamanho_mapa][tamanho_mapa];
}

public AuditorImpl(int max_jogadores, int max_bandeiras, int tamanho_mapa) {
    this.max_jogadores = max_jogadores;
    this.max_bandeiras = max_bandeiras;
    this.tamanho_mapa = tamanho_mapa;
    this.jogadas = new HashMap<>();
    this.jogadores = new HashMap<>();
    this.mapa = new int[tamanho_mapa][tamanho_mapa];
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
        int x = rand.nextInt(getTamanho_mapa());
        int y = rand.nextInt(getTamanho_mapa());
        Jogador newplayer = new Jogador(request.getNome(),x,y);
        if(getNumero_jogadores() < getMax_jogadores()){
            this.jogadores.put(getNumero_jogadores(),newplayer);
            this.numero_jogadores++;
        }else{
            mensagem = "Numero maximo de jogadores atingido!!!";            
        }        
    }
    logger.info(mensagem);
    System.out.println(this.jogadores.toString());
    Resposta resposta = Resposta.newBuilder().setResultado(mensagem).build();
    responseObserver.onNext(resposta);
    responseObserver.onCompleted();
    
    logger.info("Numero de jogadores no momento : " + getNumero_jogadores());
}


@Override
public void jogar(Jogada request, StreamObserver<Mapa> responseObserver) {
    String mensagem = "O jogador " + request.getNome() + " nao pertence a esse jogo!!" ;    

    //permitir jogada somente apos todos os jogadores conectados
    if(getNumero_jogadores() != getMax_jogadores()){
        logger.info("Faltam jogadores no jogo!!");
        Mapa map = Mapa.newBuilder().setDados("Faltam jogadores no jogo!!").setTamanho(0).build();
        responseObserver.onNext(map);
        responseObserver.onCompleted();
        return;
    }
    logger.info("Jogada solicitada do <"+ request.getNome() + "> e o jogo esta pronto para começar !!");
    logger.info("Montando mapa ....");
    if(getNumero_jogadas() == 0){
        this.monta_mapa(true);
    }else{
        this.monta_mapa(false);
    }
    
    logger.info("Processando jogada do :" + request.getNome());
    //verifica se o jogador ta no jogo
    boolean ingame = this.in_game(request.getNome());

    if(ingame){
        System.out.println("O jogador pertence a esse jogo!");
        System.out.println("Iniciando jogada..."); 
        Play jogada = new Play();    
        jogada.setNome(request.getNome());//setando nome do jogador que efetuou a jogada
        jogada.setX(request.getCoord(0).getX());//setando coord da jogada
        jogada.setY(request.getCoord(0).getY());//setando coord da jogada
        if(this.valida_jogada(request.getNome(),jogada)){
            this.jogadas.put(numero_jogadas,jogada);
            mensagem = "A Jogada : X:" + request.getCoord(0).getX()
            + ",Y:" +request.getCoord(0).getY() + 
            " do " + request.getNome() + " foi adicionada com sucesso!!";
            System.out.println(this.jogadas.toString());
            numero_jogadas++; 
        }else{
            mensagem = "Jogada invalida do " + request.getNome();
        }        
        //VALIDAR JOGADA                
        logger.info(mensagem);
    }
    Mapa map = Mapa.newBuilder().setDados(getMap()).setTamanho(getTamanho_mapa()).build();
    responseObserver.onNext(map);
    responseObserver.onCompleted();
}


// logger.info("Montando mapa ....");
//     this.monta_mapa();



public boolean in_game(String nome){

    if(!jogadores.isEmpty()){
        for(int i=0; i < getNumero_jogadores() ; i++){
            if (this.jogadores.get(i).getNome().equals(nome)){     
                //jogador ja esta no jogo           
                System.out.println("TA NO JOGO :" + nome);
                return true;                
            }
        }
    }
    return false;

}


//TESTAR DEPOIS DE VALIDAR MAPA
public boolean valida_jogada(String nome_jogador, Play jogada){
    String msgx = "", msgy ="";
    jogada.getY();
    boolean validaX = false, validaY = false;
    
    if(jogada.getX() < 0 || jogada.getY() < 0){
        return false;
    }

    for(int i=0; i < getNumero_jogadores() ; i++){
        if (this.jogadores.get(i).getNome().equals(nome_jogador)){     
            //jogador esta no jogo           
            if(this.jogadores.get(i).getX() ==  jogada.getX() || this.jogadores.get(i).getY() ==  jogada.getY()){
                System.out.println("Nao se moveu");
            }else{
                if(this.jogadores.get(i).getX() > 0){
                    if((jogada.getX() == this.jogadores.get(i).getX() + 1) || (jogada.getX() == this.jogadores.get(i).getX() - 1)){
                        validaX = true;
                        msgx = "jogada correta : \n jogada x :"+ jogada.getX() + "\n pos atual x :"+this.jogadores.get(i).getX();
                    }else{
                        msgx ="jogada incorreta : \n jogada x :"+ jogada.getX() + "\n pos atual x :"+this.jogadores.get(i).getX();
                        validaX = false;
                    }                    
                }else{//getx = 0
                    if(jogada.getX() ==  this.jogadores.get(i).getX() + 1){
                        validaX = true;
                        msgx ="jogada correta : \n jogada x :"+ jogada.getX() + "\n pos atual x :"+this.jogadores.get(i).getX();
                    }else{
                        msgx = "jogada incorreta : \n jogada x :"+ jogada.getX() + "\n pos atual x :"+this.jogadores.get(i).getX();
                        validaX = false;
                    }
                }
                //para y
                if(this.jogadores.get(i).getX() > 0){
                    if((jogada.getY() == this.jogadores.get(i).getY() + 1) || (jogada.getY() == this.jogadores.get(i).getY() - 1)){
                        validaX = true;
                        msgy = "jogada correta : \n jogada y :"+ jogada.getY() + "\n pos atual y :"+this.jogadores.get(i).getY();
                    }else{
                        msgy = "jogada incorreta : \n jogada y :"+ jogada.getY() + "\n pos atual y :"+this.jogadores.get(i).getY();
                        validaX = false;
                    }                    
                }else{//getx = 0
                    if(jogada.getY() ==  this.jogadores.get(i).getY() + 1){
                        validaX = true;
                        msgy = "jogada correta : \n jogada y :"+ jogada.getY() + "\n pos atual y :"+this.jogadores.get(i).getY();
                    }else{
                        msgy = "jogada incorreta : \n jogada x :"+ jogada.getY() + "\n pos atual x :"+this.jogadores.get(i).getY();
                        validaX = false;
                    }
                }
            }                                                         
        }
    }
    if(validaX && validaY){
        System.out.println("validou a jogada!!");
        System.out.println(msgx);
        System.out.println(msgy);
        return true;
    }else{
        System.out.println(msgx);
        System.out.println(msgy);
        return false;
    }

}

public void monta_mapa(boolean first){
    String mapa_str = "";    
    //alocando jogadores
    for(int i=0; i < getNumero_jogadores() ; i++){
        mapa[this.jogadores.get(i).getX()][this.jogadores.get(i).getY()] = i+1;
    }
    //alocando bandeira
    if(first)
        aloca_bandeiras();
    //preparando string para enviar
    for(int i = 0; i < getTamanho_mapa();i++){
        for(int j = 0; j < getTamanho_mapa();j++){
            mapa_str = mapa_str + mapa[i][j];
            if(j < getTamanho_mapa() - 1 ){
                mapa_str = mapa_str + ",";
            }
        }        
        mapa_str = mapa_str + ";";
    }
    setMap(mapa_str);
    System.out.println(mapa_str);
}

public void aloca_bandeiras(){
    Random rand = new Random();
    for(int i=0;i < getMax_bandeiras();i++){
        int x = rand.nextInt(getTamanho_mapa());
        int y = rand.nextInt(getTamanho_mapa());        
        if(mapa[x][y] == 0){
           if(numero_bandeiras == getMax_bandeiras()){
                return;
            }else{
               mapa[x][y] = 66;
               numero_bandeiras++;
           }
        }else{
            aloca_bandeiras();
        }
    }
}



}

