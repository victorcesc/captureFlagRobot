FROM gradle:jdk17-jammy

COPY . /auditor

WORKDIR /auditor

RUN ./gradlew installDist


CMD ["./build/install/auditor/bin/auditor"]