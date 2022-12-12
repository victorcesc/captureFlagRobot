package engtelecom.std;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import com.google.rpc.context.AttributeContext.Request;

import engtelecom.std.game.AuditorGrpc;
import engtelecom.std.game.Jogada;
import engtelecom.std.game.Jogada.Coordenadas;
import engtelecom.std.game.Ingresso;
import engtelecom.std.game.Mapa;

//import engtelecom.std.agenda.Pessoa.TipoTelefone;
import io.grpc.ManagedChannelBuilder;
public class App {
    private static final Logger logger = Logger.getLogger(App.class.getName());
    
    

//tira o menor caminho do mapa retornado pelo auditor
public LinkedList<Cell> caminho(String dados_mapa){

  //transformar a matrix string em matrix[][]
    // int matrix[][] = {
    //   { 0, 0, 1, 0},
    //   { 0, 0, 0, 0},
    //   { 0, 0, 0, 0},
    //   { 0, 0, 66, 0} };

    //criar mensagem proto onde envia uma string de mapa

  //String dados_mapa = "0,0,1,0;0,0,0,0;0,0,0,0;0,0,66,0";

  String lines[] = dados_mapa.split(";");
  int width = lines.length;
  String cells[] = lines[0].split(",");
  int height = cells.length;
  int output[][] = new int[width][height];  
  for (int i=0; i<width; i++) {
    String cells1[] = lines[i].split(",");
    for(int j=0; j<height; j++) {
        output[i][j] = Integer.parseInt(cells1[j]);
    }
  }
// 66 = bandeira
// 1,2,3,4,5,6 ... = jogadores 
//onde eu to ?? - talvez ja saiba
//onde ta a bandeira ??
int xb=0,xp = 0;
int yb=0,yp = 0;
for ( int i = 0; i < 4; ++i ) {
  for ( int j = 0; j < 4; ++j ) {
      if ( output[i][j] == 66 ) {
          xb = i;
          yb = j;
          //break;
      }
      if (output[i][j] > 0 && output[i][j]!= 66 ){
          xp = i;
          yp = j;
      } 
  }
}
int [] posb = {xb,yb}; 
System.out.println(posb[0] + " " + posb[1]);  
int [] posp = {xp,yp};
System.out.println(posp[0] + " " + posp[1]);

return ShortestPathBetweenCellsBFS.shortestPath(output, posp, posb);



}
public static void main(String[] args) throws Exception {
    // Por padrão o gRPC sempre será sobre TLS, como não criamos um certificado digital, forçamos aqui nã
    //o usar TLS
    String server = "localhost:50051";
    String user = "JogadorX";
    // Criando uma pessoa usando o padrão de projeto Builder
    if (args.length > 0) {
        if ("--help".equals(args[0])) {
          System.err.println("Usage: [nome [target]]");
          System.err.println("");
          System.err.println("  nome    O nome do Jogada pretendido. O padrao é " + user);
          System.err.println("  target  O ip:porta do servidor auditor que será conectado. O padrao é " + server );
          System.exit(1);
        }
        user = args[0];
      }
      if (args.length > 1) {
        server = args[1];
      } 
    var channel = ManagedChannelBuilder.forTarget(server).usePlaintext().build();

    
    
    //ENTRAR NO JOGO ----
    logger.info("Criando ingresso");

    var ingresso = Ingresso.newBuilder().setNome(user).build();
    var auditorBlockingStub = AuditorGrpc.newBlockingStub(channel);
    logger.info("Ingressando no jogo...");
    var resultado = auditorBlockingStub.entrar(ingresso);
    logger.info(resultado.toString());

//  -- JOGANDO
    Coordenadas coord = Coordenadas.newBuilder().setX(0).setY(1).build();
        var jogada = Jogada.newBuilder().setNome(user)
        .addCoord(coord)
        .build();      
    var map = auditorBlockingStub.jogar(jogada);
    logger.info(map.toString());



    
    //JOGADA ----

    // while(true){      
    //   try{
    //     Coordenadas coord = Coordenadas.newBuilder().setX(0).setY(1).build();
    //     var jogada = Jogada.newBuilder().setNome(user)
    //     .addCoord(coord)
    //     .build();  
    //     Thread.sleep(10000);
    //     auditorBlockingStub.jogar(jogada);
    //   }catch (InterruptedException e) {
    //     // recommended because catching InterruptedException clears interrupt flag
    //     Thread.currentThread().interrupt();
    //     // you probably want to quit if the thread is interrupted
    //     return;
    //   }



    //}
    
      

    
    
}



}