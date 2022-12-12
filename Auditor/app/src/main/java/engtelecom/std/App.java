package engtelecom.std;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import java.util.logging.Logger;
public class App {
// Serviço de log para registrar as mensagens de depuração, informação, erro, etc.
private static final Logger logger = Logger.getLogger(App.class.getName());
public static void main(String[] args) throws Exception {
    AuditorImpl auditor;
    Server servidor;
if(args.length < 1 && args.length < 4){
    System.out.println("Jogo iniciado no modo padrao: ");
    System.out.println("Porta do servidor : 50051");
    System.out.println("Jogadores max: 2");
    System.out.println("Tamanho do mapa : 4");
    System.out.println("Numero de bandeiras: 1");
    auditor = new AuditorImpl();
    servidor = ServerBuilder.forPort(50051)
    .addService(auditor)
    .build()
    .start();
}else{
    auditor = new AuditorImpl(Integer.parseInt(args[1]),Integer.parseInt(args[2]),Integer.parseInt(args[3]));
    servidor = ServerBuilder.forPort(Integer.parseInt(args[0]))
        .addService(auditor)
        .build()
        .start();
    logger.info("Jogo iniciado na porta :" + args[0]);
    logger.info("Numero max de jogadores :" + args[1]);
    logger.info("Numero de bandeiras :" + args[2]);
    logger.info("Tamanho mapa :" + args[3]);
}

System.out.println("Aguardando jogadores...");
// Padrão de projeto Builder. Veja mais em https://java-design-patterns.com/patterns/builder/
// Iniciando o servidor com a implementação da AgendaImpl
// Para finalizar o servidor quando a JVM for finalizada
    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
    System.err.println("servidor gRPC sendo desligado pois a JVM está sendo desligada");
    servidor.shutdown();
    System.err.println("Servidor parado com sucesso");
}));
// Para Finalizar o servidor
servidor.awaitTermination();
}
}
