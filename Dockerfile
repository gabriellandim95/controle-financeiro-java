FROM openjdk:17-jdk-slim

RUN apt-get update && apt-get install -y --no-install-recommends tzdata && \
    ln -fs /usr/share/zoneinfo/America/Sao_Paulo /etc/localtime && \
    dpkg-reconfigure -f noninteractive tzdata && \
    apt-get clean && rm -rf /var/lib/apt/lists/*

ENV TZ=America/SaoPaulo
RUN ln -fs /usr/share/zoneinfo/America/Sao_Paulo /etc/localtime && dpkg-reconfigure -f noninteractive tzdata

WORKDIR /app

COPY target/controlefinanceiro-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-Duser.timezone=America/Sao_Paulo", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005", "-jar", "app.jar"]

EXPOSE 8080