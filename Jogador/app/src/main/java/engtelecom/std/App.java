package engtelecom.std;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import engtelecom.std.game.AuditorGrpc;
import engtelecom.std.game.Jogador;
import engtelecom.std.game.Jogador.Coordenadas;
//import engtelecom.std.agenda.Pessoa.TipoTelefone;
import io.grpc.ManagedChannelBuilder;
public class App {
    private static final Logger logger = Logger.getLogger(App.class.getName());
    
    
public static void main(String[] args) throws Exception {
    // Por padrão o gRPC sempre será sobre TLS, como não criamos um certificado digital, forçamos aqui nã
    //o usar TLS
    String server = "auditor:50051";
    String user = "JogadorX";
    // Criando uma pessoa usando o padrão de projeto Builder
    if (args.length > 0) {
        if ("--help".equals(args[0])) {
          System.err.println("Usage: [nome [target]]");
          System.err.println("");
          System.err.println("  nome    O nome do jogador pretendido. O padrao é " + user);
          System.err.println("  target  O ip:porta do servidor auditor que será conectado. O padrao é " + server );
          System.exit(1);
        }
        user = args[0];
      }
      if (args.length > 1) {
        server = args[1];
      } 
    var channel = ManagedChannelBuilder.forTarget(server).usePlaintext().build();

    Coordenadas coord = Coordenadas.newBuilder().setX(0).setY(1).build();
    var jogador = Jogador.newBuilder().setNome(user)
    .setId(1)
    .addCoord(coord)
    .build();

    logger.info("Iniciando uma jogada !!");
    var auditorBlockingStub = AuditorGrpc.newBlockingStub(channel);
    auditorBlockingStub.jogar(jogador);
    logger.info("Jogada enviada");
    //logger.info("Buscando por uma pessoa na agenda de contatos");
    //var resultado = agendaBlockingStub.buscar(juca);
    //logger.info("Dados da pessoa retornada pelo servidor: " + resultado);
    logger.info("Finalizando...");
    channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
}



}