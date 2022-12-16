# PROJETO FINAL STD

## Requer:
### gradle 7.6
### JAVA 19
### Docker version 20.10.21, build baeda1f82a

(ignore os sudos se vc tem o docker instalado localmente como user)
### Criando a rede docker necessária:
``` docker network create <nome_rede> ```


## Auditor

### Build Auditor:

- Entre na pasta do projeto Auditor

#### Para buildar o container auditor(modo DEFAULT*):

``` docker build . -t auditor ```

#### Para buildar o container auditor com specs:
 ``` docker build . -t auditor --build-arg PORT=<port> --build-arg PLAYERS=<numero_jogadores>  --build-arg FLAGS=<numero_bandeiras> --build-arg MAP=<tamanho_mapa> ```


### Run Auditor:

#### Para executar o container auditor :
``` docker run --name <nome_servidor_container> --network <nome_rede> auditor ```


- Entre na pasta do projeto Monitor

### Build Monitor:

#### Para buildar o container auditor(modo DEFAULT*):

``` docker build . -t monitor ```

#### Para buildar o container auditor com specs:
 ``` docker build . -t monitor --build-arg IP_SERVER=<ip_auditor> PORT=<port_auditor>```


### Run Monitor:

#### Para executar o container monitor :
``` docker run --name <nome_container_monitor> --network <nome_rede> monitor ```


- Entre na pasta do projeto Jogador


## Jogador

### Build Jogador

#### Para compilar o container jogador(modo DEFAULT*):
 ``` docker build . -t jogador --build-arg NOME=<nome_jogador> ```

#### Para compilar o container jogador:
 ``` docker build . -t jogador --build-arg NOME=<nome_jogador> --build-arg IP_SERVER=<ip_auditor> --build-arg PORT=<port_auditor> ```

### Run Jogador

#### Para excecutar o container jogador:

``` docker run --name <nome_container> --network <nome_rede> jogador ```

* O Modo default para o auditor:
  - Porta do servidor : 50051
  - Numero de jogadores maximo : 2
  - Numero de bandeiras : 1
  - Tamanho do mapa : 4

* O Modo default para o jogador:
  - Nome do jogador padrao : JogadorX
  - Servidor e porta que ira se conectar : localhost:50051

* O Modo default para o monitor:
  - Servidor e porta que ira se conectar : localhost:50051



* NOTE : Para conectar ao auditor utilizando o modo de nomes de container precisa-se criar o jogador em modo custom e adicionar na variavel SERVIDOR o nome do container e não em modo default, como dito acima, o modo default só aceita conexões do localhost. Outra opção é utilizar o docker compose : 

## Docker Compose

``` docker compose up auditor ```
``` docker compose up monitor ```
``` docker compose up jogador ```
``` docker compose up jogador2 ```
* Todos os comandos acima devem ser executados na pasta raiz do projeto

## Requisitos atendidos:

###  Arquivo Readme.md contém explicações em como executar cada processo para permitir iniciar e observar uma partida
###  Arquivo Readme.md contém uma lista dos requisitos atendidos e não atendidos
### Após clonar o repositório, é possível compilar e executar cada projeto de forma individual (auditor,jogador ou monitor) usando o gradle (pode ser com gradle run ou gradle installDist)
### É possível executar somente o contêiner com o processo auditor
### É possível executar 2 contêineres com o processo jogador e esses conseguem conectar corretamente no processo auditor
### É possível executar corretamente o monitor e esse consegue conectar corretamente no processo monitor

## Requisitos não atendidos:

### Processo Auditor não atendeu todos os requisitos - não testado com mais de 2 jogadores e mais de 1 bandeira
### Processo Monitor não é grafico
### Só consegui testar o jogo em modo default.