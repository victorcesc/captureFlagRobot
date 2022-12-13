FROM gradle:jdk19-focal

ARG PORT
ARG PLAYERS
ARG FLAGS
ARG MAP

ENV PORT_NUMBER=${PORT}
ENV PLAYERS=${PLAYERS}
ENV FLAGS=${FLAGS}
ENV MAP=${MAP}

COPY . /Auditor

WORKDIR /Auditor

RUN gradle installDist

CMD ./app/build/install/app/bin/app ${PORT_NUMBER} ${PLAYERS} ${FLAGS} ${MAP}