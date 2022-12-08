FROM gradle:jdk11-focal

ARG PORT

ENV PORT_NUMBER=${PORT}

COPY . /Jogador

WORKDIR /Jogador

RUN gradle installDist

CMD .app/build/install/app/bin/app ${PORT_NUMBER}