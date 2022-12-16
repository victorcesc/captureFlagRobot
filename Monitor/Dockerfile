FROM gradle:jdk19-focal

ARG NOME
ARG PORT
ARG IP_SERVER

ENV NOME_JOGADOR=${NOME}
ENV PORT_NUMBER=${PORT}
ENV SERVER=${IP_SERVER}

COPY . /Monitor

WORKDIR /Monitor

RUN gradle installDist

CMD ./app/build/install/app/bin/app ${SERVER} ${PORT_NUMBER}