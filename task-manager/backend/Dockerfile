FROM eclipse-temurin:17-jdk-jammy

ENV LANGUAGE='en_US:en'

# Copia as dependências e o jar do Quarkus
COPY --chown=185 target/quarkus-app/lib/ /deployments/lib/
COPY --chown=185 target/quarkus-app/*.jar /deployments/
COPY --chown=185 target/quarkus-app/app/ /deployments/app/
COPY --chown=185 target/quarkus-app/quarkus/ /deployments/quarkus/
COPY src/main/resources/publicKey.pem /deployments/publicKey.pem
COPY src/main/resources/publicKey.pem /deployments/


EXPOSE 8080
USER 185
ENV JAVA_OPTS_APPEND="-Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager"
# ENV JAVA_APP_JAR="/deployments/quarkus-run.jar" # Comentado pois a imagem base pode não ter esse script/path
ENV QUARKUS_PROFILE=dev

# ENTRYPOINT [ "/opt/jboss/container/java/run/run-java.sh" ] # Comentado pois a imagem base pode não ter esse script/path

# Comando de entrada padrão para aplicações Quarkus em Java
CMD ["java", "-Dquarkus.http.host=0.0.0.0", "-Djava.util.logging.manager=org.jboss.logmanager.LogManager", "-jar", "/deployments/quarkus-run.jar"]
