package engtelecom.std;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import engtelecom.std.game.AuditorGrpc;
import engtelecom.std.game.Jogada;
import engtelecom.std.game.Jogada.Coordenadas;
import engtelecom.std.game.Ingresso;

//import engtelecom.std.agenda.Pessoa.TipoTelefone;
import io.grpc.ManagedChannelBuilder;
public class App {
    private static final Logger logger = Logger.getLogger(App.class.getName());
    
    
public static void main(String[] args) throws Exception {
    // Por padrão o gRPC sempre será sobre TLS, como não criamos um certificado digital, forçamos aqui nã
    //o usar TLS
    ShortestPathBetweenCellsBFS teste = new ShortestPathBetweenCellsBFS();
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
    

    //JOGADA ----

    while(true){      
      try{
        Coordenadas coord = Coordenadas.newBuilder().setX(0).setY(1).build();
        var jogada = Jogada.newBuilder().setNome(user)
        .addCoord(coord)
        .build();  
        Thread.sleep(10000);
        auditorBlockingStub.jogar(jogada);
      }catch (InterruptedException e) {
        // recommended because catching InterruptedException clears interrupt flag
        Thread.currentThread().interrupt();
        // you probably want to quit if the thread is interrupted
        return;
      }



    }

    
}



}