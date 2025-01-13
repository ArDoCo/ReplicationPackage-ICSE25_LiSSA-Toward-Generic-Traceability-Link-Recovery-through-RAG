FROM maven:3-eclipse-temurin-21
WORKDIR /replication
COPY . .
RUN cd LiSSA-RATLR-V1/lissa && mvn -B compile test-compile package && mvn -B dependency:go-offline
RUN cd LiSSA-RATLR-V2/lissa && mvn -B compile test-compile package && mvn -B dependency:go-offline

ENV OPENAI_API_KEY=dummy
ENV OPENAI_ORGANIZATION_ID=dummy

ENTRYPOINT bash -c "cat README.md && bash"
