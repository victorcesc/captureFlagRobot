FROM gradle:jdk17-jammy

COPY . /jogador

WORKDIR /jogador

RUN gradle installDist

CMD ["./build/install/jogador/bin/jogador"]