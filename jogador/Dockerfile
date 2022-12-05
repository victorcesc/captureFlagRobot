FROM gradle:jdk11-focal

COPY . /jogador

WORKDIR /jogador

RUN gradle installDist

CMD ["./build/install/jogador/bin/jogador"]