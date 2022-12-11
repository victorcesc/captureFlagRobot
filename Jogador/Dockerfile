FROM gradle:jdk11-focal

ARG NOME

ENV NOME_JOGADOR=${NOME}

COPY . /Jogador

WORKDIR /Jogador

RUN gradle installDist

CMD .app/build/install/app/bin/app ${NOME_JOGADOR}