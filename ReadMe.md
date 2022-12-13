## PROJETO FINAL STD

*Requer gradle 7.6
*JAVA 19

(ignore os sudos se vc tem o docker instalado localmente como user)
### Criando a rede docker necessária:
``` docker network create <nome_rede> ```


## Auditor

#### Para buildar o container auditor com specs:
 ``` sudo docker build . -t auditor --build-arg PORT=<port> --build-arg PLAYERS=<number>  --build-arg FLAGS=<number> --build-arg MAP=<number> ```

#### Para executar o container auditor (modo default*):
``` sudo docker run --name <nome_servidor_container> --network <nome_rede> -p 8000:50051 auditor ```

## Jogador

#### Para compilar o container jogador(modo default*):
 ``` sudo docker build . -t jogador --build-arg NOME=<nome_jogador> ```

#### Para compilar o container jogador:
 ``` sudo docker build . -t jogador --build-arg NOME=<nome_jogador> --build-arg SERVER=<nome_servidor_container> --build-arg PORT=<number> ```

* O Modo default para o auditor:
  - porta do servidor : 50051
  - numero de jogadores maximo : 2
  - numero de bandeiras : 1
  - tamanho do mapa : 4

* O Modo default para o jogador:
  - nome do jogador padrao : JogadorX
  - servidor que ira se conectar : localhost:50051



### não consegui fazer a classe monitor


