package engtelecom.std;
import engtelecom.std.game.AuditorGrpc;
import engtelecom.std.game.Jogada;
import engtelecom.std.game.Resposta;
import engtelecom.std.game.Ingresso;
import engtelecom.std.game.Mapa;


import java.util.Random;
import java.util.Scanner;

import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import io.grpc.stub.annotations.GrpcGenerated;

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
private boolean mapa_montado = false;
private int pos_bandeira[][];
private boolean fim = false;


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
    this.pos_bandeira = new int[max_bandeiras][2];

}

public AuditorImpl(int max_jogadores, int max_bandeiras, int tamanho_mapa) {
    this.max_jogadores = max_jogadores;
    this.max_bandeiras = max_bandeiras;
    this.tamanho_mapa = tamanho_mapa;
    this.jogadas = new HashMap<>();
    this.jogadores = new HashMap<>();
    this.mapa = new int[tamanho_mapa][tamanho_mapa];
    this.pos_bandeira = new int[max_bandeiras][2];
}


@Override
public void entrar(Ingresso request, StreamObserver<Resposta> responseObserver){
    String mensagem = "Jogador " + request.getNome() + " entrou no jogo!!";   
    //verifica se jogador esta no jogo, true = sim, false = nao   
    boolean ingame = this.in_game(request.getNome());
    int id_jogador = 0;
    Random r = new Random();
    int x = r.nextInt(getTamanho_mapa());
    int y = r.nextInt(getTamanho_mapa());
    if(ingame){ 
        mensagem = "O jogador : " + request.getNome() + " ja esta no jogo";    
    }else{
        //jogador ganha um par de coordenadas somente quando é alocado no mapa              
        Jogador newplayer = new Jogador(request.getNome(),x,y);
        if(getNumero_jogadores() < getMax_jogadores()){
            this.jogadores.put(getNumero_jogadores(),newplayer);
            id_jogador = getNumero_jogadores() + 1;
            this.numero_jogadores++;
        }else{
            mensagem = "Numero maximo de jogadores atingido!!!";            
        }        
    }
    logger.info(mensagem);
    System.out.println(this.jogadores.toString());
    Resposta id = Resposta.newBuilder().setId(id_jogador).setX(x).setY(y).build();
    responseObserver.onNext(id);
    responseObserver.onCompleted();
    
    logger.info("Numero de jogadores no momento : " + getNumero_jogadores());
}


@Override
public void jogar(Jogada request, StreamObserver<Mapa> responseObserver) {
    String mensagem = "O jogador " + request.getNome() + " nao pertence a esse jogo!!" ; 
    Mapa map;   
    if(fim){
        map = Mapa.newBuilder().setInfo("Fim").setTamanho(0).build();
        responseObserver.onNext(map);
        responseObserver.onCompleted();
        return;
    }else{
        if(getNumero_jogadores() != getMax_jogadores()){
            logger.info("Faltam jogadores no jogo!!");
            map = Mapa.newBuilder().setInfo("Faltam jogadores no jogo!!").setTamanho(0).build();
            responseObserver.onNext(map);
            responseObserver.onCompleted();
            return;
        }
        logger.info("Jogada solicitada do <"+ request.getNome() + "> e o jogo esta pronto para começar !!");
        
        if(getNumero_jogadas() == 0 && !mapa_montado){
            logger.info("Primeira jogada do jogo !!");              
            logger.info("Montando mapa ....");
            this.monta_mapa(true);
            mapa_montado = true;
            map = Mapa.newBuilder().setInfo("Mapa montado!").setTamanho(0).build();
            responseObserver.onNext(map);
            responseObserver.onCompleted();
            return;        
        }       
        System.out.println(getMap().toString()); 
        
        logger.info("Processando jogada do :" + request.getNome());
        //verifica se o jogador ta no jogo
        boolean ingame = this.in_game(request.getNome());
        boolean isplayed = this.is_played(request.getNome());
        if(!isplayed){
            logger.info("Primeira vez do " + request.getNome() + " Retornando mapa ...");
            Play jogada = new Play();
            jogada.setNome(request.getNome());//setando nome do jogador que efetuou a jogada
            jogada.setX(request.getCoord(0).getX());//setando coord da jogada
            jogada.setY(request.getCoord(0).getY());
            this.jogadas.put(numero_jogadas,jogada);
            numero_jogadas++;
            //atualizando pos jogador
            this.jogadores.get(request.getId()).setX(request.getCoord(0).getX());
            this.jogadores.get(request.getId()).setY(request.getCoord(0).getY());
            logger.info("Agora jogador " + this.jogadores.get(request.getId()).getNome() + " foi atualizado!\n" 
            + request.getCoord(0).getX() +","+ request.getCoord(0).getY());        
            //atualiza mapa
            map = Mapa.newBuilder().setDados(getMap()).setTamanho(getTamanho_mapa()).build();
            //atualiza mapa
            responseObserver.onNext(map);
            responseObserver.onCompleted();
            return;
            
        }
    
        if(ingame){
            System.out.println("O jogador pertence a esse jogo!");
            System.out.println("Iniciando jogada..."); 
            Play jogada = new Play();    
            jogada.setNome(request.getNome());//setando nome do jogador que efetuou a jogada
            jogada.setX(request.getCoord(0).getX());//setando coord da jogada
            jogada.setY(request.getCoord(0).getY());//setando coord da jogada                    
            this.jogadas.put(numero_jogadas,jogada);
            numero_jogadas++;
            mensagem = "A Jogada : X:" + request.getCoord(0).getX()
            + ",Y:" +request.getCoord(0).getY() + 
            " do " + request.getNome() + " foi adicionada com sucesso!!";
            if(mapa[jogada.getX()][jogada.getY()] == 66){              
              logger.info("Jogador " + request.getNome() + " pegou a bandeira!");
              logger.info("<<<<<<" + request.getNome() + ">>>> <<<<<VENCEU>>>>>");
              logger.info("Fim de jogo");   
              fim = true;           
              map = Mapa.newBuilder().setInfo("O Jogador " + request.getNome() + " Venceu").build(); 
              responseObserver.onNext(map);
              responseObserver.onCompleted();
              return;             
            }
            this.monta_mapa(false);

            map = Mapa.newBuilder().setDados(getMap()).setTamanho(getTamanho_mapa()).build();
            responseObserver.onNext(map);
            responseObserver.onCompleted();
        }
        //jogada valida e bem sucedida
        logger.info(mensagem);
       
    }

}


//verifica se o jogador ja fez uma jogada, se nunca fez, a jogada nao pode ser efetuada
public boolean is_played(String nome){
    if(!jogadas.isEmpty()){
        for(int i=0; i < getNumero_jogadas() ; i++){
            if (this.jogadas.get(i).getNome().equals(nome)){ 
                //jogador ja jogou uma vez    
                return true;                
            }
        }
    }
    return false;

}

//verifica se o jogador está no jogo 
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



public void monta_mapa(boolean first){
    String mapa_str = "";    
    //alocando jogadores
    //reseta mapa
    if(first){
        for(int i = 0; i < getNumero_jogadores(); i++){
            if(mapa[this.jogadores.get(i).getX()][this.jogadores.get(i).getY()] == 0){        
                mapa[this.jogadores.get(i).getX()][this.jogadores.get(i).getY()] = i+1;
            }
        }
        aloca_bandeiras();
    }else{     
        for (int i = 0; i < getTamanho_mapa(); i++) {
            for (int j = 0; j < getTamanho_mapa(); j++) {
                mapa[i][j] = 0;
            }
        }
        for (int i = 0; i < getMax_bandeiras(); i++) {
            mapa[pos_bandeira[i][0]][pos_bandeira[i][1]] = 66;
        }
        //realoca jogadores na posicao atualizada
        for(int i = 0; i < getNumero_jogadores(); i++){             
            mapa[this.jogadores.get(i).getX()][this.jogadores.get(i).getY()] = i+1;       
        }    
    }
   
    //preparando string para enviar
    for(int i = 0; i < getTamanho_mapa();i++){
        for(int j = 0; j < getTamanho_mapa();j++){
            mapa_str = mapa_str + mapa[i][j];
            if(j < getTamanho_mapa() - 1 ){
                mapa_str = mapa_str + ",";
            }
        }
        if (i < getTamanho_mapa() - 1 )        
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
               pos_bandeira[i][0] = x;
               pos_bandeira[i][1] = y;
               numero_bandeiras++;
            }
        }else{
            aloca_bandeiras();
        }
    }
}



}

