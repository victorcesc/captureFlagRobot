FROM gradle:jdk11-focal

COPY . /auditor

WORKDIR /auditor

RUN gradle installDist

CMD ["./build/install/auditor/bin/auditor"]