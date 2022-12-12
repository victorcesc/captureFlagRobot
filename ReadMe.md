## PROJETO FINAL STD

Requer gradle 7.6
JAVA 19

(ignore os sudos se vc tem o docker instalado localmente como user)
criando a rede docker necessária:
 - docker network create -d bridge <nome_rede>

para executar o container auditor default:
- sudo docker build . -t auditor

para executar o container auditor com specs:
 - sudo docker build . -t auditor --build-arg PORT=<port> --build-arg PLAYERS=<number>  --build-arg FLAGS=<number> --build-arg MAP=<number>

para compilar o container jogador(modo default):
  - sudo docker build . -t jogador --build-arg NOME=<nome_jogador>

para compilar o container jogador:
 - sudo docker build . -t jogador --build-arg NOME=<nome_jogador> --build-arg SERVER=<nome_servidor_container> --build-arg PORT=<number>

para rodar o auditor
 - sudo docker run --network <rede> auditor
para rodar o jogador
 - sudo docker run --network <rede> jogador


# nao consegui usar o docker
# não consegui fazer a classe monitor


