FROM gradle:jdk17-focal

ARG NOME
ARG PORT
ARG IP_SERVER

ENV NOME_JOGADOR=${NOME}
ENV PORT_NUMBER=${PORT}
ENV SERVER=${IP_SERVER}

COPY . /Jogador

WORKDIR /Jogador

RUN gradle installDist

CMD ./app/build/install/app/bin/app ${NOME_JOGADOR} ${SERVER} ${PORT_NUMBER}