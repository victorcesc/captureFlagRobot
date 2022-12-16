FROM gradle:jdk19-focal

ARG PORT
ARG IP_SERVER

ENV PORT_NUMBER=${PORT}
ENV SERVER=${IP_SERVER}

COPY . /Monitor

WORKDIR /Monitor

RUN gradle installDist

CMD ./app/build/install/app/bin/app ${SERVER} ${PORT_NUMBER}