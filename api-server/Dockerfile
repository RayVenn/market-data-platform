# ── Stage 1: build bootJar ────────────────────────────────────────────────────
FROM eclipse-temurin:17-jdk AS builder

WORKDIR /app

COPY gradlew .
COPY gradle/ gradle/
COPY settings.gradle build.gradle ./
COPY api-server/build.gradle api-server/build.gradle
COPY api-server/src api-server/src

RUN ./gradlew :api-server:bootJar --no-daemon

# ── Stage 2: minimal JRE runtime ─────────────────────────────────────────────
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

RUN groupadd --gid 1001 apiuser && \
    useradd --uid 1001 --gid 1001 --no-create-home apiuser

COPY --from=builder /app/api-server/build/libs/api-server.jar app.jar

USER apiuser

EXPOSE 8090

HEALTHCHECK --interval=15s --timeout=5s --start-period=30s --retries=3 \
    CMD curl -f http://localhost:8090/actuator/health || exit 1

CMD ["java", "-jar", "app.jar"]
