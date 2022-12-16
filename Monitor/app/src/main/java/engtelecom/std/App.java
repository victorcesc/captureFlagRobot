package engtelecom.std;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import java.util.logging.Logger;
import engtelecom.std.game.AuditorGrpc;
import engtelecom.std.game.Jogada;
import engtelecom.std.game.Jogada.Coordenadas;
import engtelecom.std.game.Ingresso;
import engtelecom.std.game.Mapa;
import io.grpc.ManagedChannelBuilder;

public class App {
// Serviço de log para registrar as mensagens de depuração, informação, erro, etc.



private static final Logger logger = Logger.getLogger(App.class.getName());




public static void main(String[] args) throws Exception {

      //String map = "1,0,0;0,0,0;66,0,2";
     
    String port = "50051";
    String server = "localhost:"+port;   
    var channel = ManagedChannelBuilder.forTarget(server).usePlaintext().build();
    var ingresso = Ingresso.newBuilder().setNome("monitor").build();
    var auditorBlockingStub = AuditorGrpc.newBlockingStub(channel);
    
    
    

    while(true){
        try{
            Thread.sleep(1000);
            logger.info("Aguardando MAPA...");
            var resultado = auditorBlockingStub.mapear(ingresso);
            if(resultado.getInfo().equals("Fim")){
                logger.info("Fim de jogo");
                break;
            }
            if(resultado.getInfo().equals("Aguardando jogadores")){
                logger.info("Aguardando jogadores");
            }else{
                if(!resultado.getDados().isEmpty()) {
                    Monitor m = new Monitor(resultado.getDados(),resultado.getTamanho());  
                    System.out.println(m.monta_mapa());
                }                      

            }
        }catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }  
    }
}

}

