package engtelecom.std;
import engtelecom.std.game.AuditorGrpc;
import engtelecom.std.game.Jogador;
import engtelecom.std.game.Resposta;
import io.grpc.stub.StreamObserver;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
public class AuditorImpl extends AuditorGrpc.AuditorImplBase {
// Serviço de log para registrar as mensagens de depuração, informação, erro, etc.
private static final Logger logger = Logger.getLogger(AuditorImpl.class.getName());
private int numero_jogadas = 0;
// Banco de dados para armazenar todos os contatos
private Map<Integer, Jogador> auditor;
public AuditorImpl() {
    this.auditor = new HashMap<>();
}

@Override
public void jogar(Jogador request, StreamObserver<Resposta> responseObserver) {
    String mensagem = "Essa jogada do " + request.getNome() + "ja foi processada!!!" ;    
    if (!this.auditor.containsKey(request.getId())) {
        this.auditor.put(numero_jogadas, request);
        mensagem = "Jogada do " + request.getNome() + " foi adicionada com sucesso!!";
        numero_jogadas++;
    }
    logger.info(mensagem);
    Resposta resposta = Resposta.newBuilder().setResultado(mensagem).build();
    responseObserver.onNext(resposta);
    responseObserver.onCompleted();
    // Padrão de projeto Builder. Veja mais em https://java-design-patterns.com/patterns/builder/
    
}
@Override
public void buscar(Jogador request, StreamObserver<Jogador> responseObserver) {
    Jogador resposta = this.auditor.get(request.getId());
    logger.info("Buscar... " + resposta);
    responseObserver.onNext(resposta);
    responseObserver.onCompleted();
}

}
