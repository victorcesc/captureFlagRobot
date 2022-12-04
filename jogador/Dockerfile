FROM gradle:jdk17-jammy

COPY . /auditor

WORKDIR /auditor

RUN gradle installDist

CMD ["./build/install/jogador/bin/jogador"]