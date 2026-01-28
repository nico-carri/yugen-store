# ETAPA 1: Compilación (Build)
FROM maven:3.9.6-eclipse-temurin-17-alpine AS build
WORKDIR /app
COPY . .
# Compilam el proyecto omitiendo los tests para ganar velocidad
RUN mvn clean package -DskipTests

# ETAPA 2: Imagen Final (Runtime)
FROM eclipse-temurin:17-jre-alpine
RUN apk add --no-cache curl bash
WORKDIR /app

# Crear directorio para wallet de Oracle Autonomous DB
RUN mkdir -p /app/wallet && chmod 755 /app/wallet

# Usuario de seguridad
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# COPIAMOS EL JAR DESDE LA ETAPA DE BUILD
COPY --from=build --chown=spring:spring /app/target/*.jar app.jar

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

EXPOSE 8080 
ENTRYPOINT ["java", "-jar", "-Djava.security.egd=file:/dev/./urandom", "app.jar"]