FROM gradle:jdk11-focal

ARG PORT

ENV PORT_NUMBER=${PORT}

COPY . /Auditor

WORKDIR /Auditor

RUN gradle installDist

CMD .app/build/install/app/bin/app ${PORT_NUMBER}