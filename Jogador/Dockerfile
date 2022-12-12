FROM gradle:jdk11-focal

ARG NOME
ARG PORT

ENV NOME_JOGADOR=${NOME}
ENV PORT_NUMBER=${PORT}

COPY . /Jogador

WORKDIR /Jogador

RUN gradle installDist

CMD ./app/build/install/app/bin/app ${NOME_JOGADOR} ${PORT_NUMBER}