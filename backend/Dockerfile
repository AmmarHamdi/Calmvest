# ============================================================
# Multi-stage Dockerfile for the Calmvest backend
#
# Stage 1 (build):  compiles the Spring Boot application and
#                   produces a layered JAR for efficient caching.
# Stage 2 (runtime): copies only the extracted JAR layers into
#                    a minimal JRE image.
#
# Usage (local build):
#   docker build -t calmvest-backend:local .
#
# Usage in CI (pre-built JAR supplied by Gradle step):
#   The CI pipeline builds the JAR with Gradle first, then runs:
#   docker build -t calmvest-backend:<tag> .
#   The build stage re-uses the Gradle cache via Docker layer caching.
# ============================================================

# ── Stage 1: Build ───────────────────────────────────────────
FROM eclipse-temurin:17-jdk-alpine AS build

WORKDIR /workspace

# Copy Gradle wrapper and dependency spec files first so that
# dependency resolution is cached as a separate layer.
COPY gradlew gradlew.bat ./
COPY gradle/ gradle/
COPY settings.gradle.kts build.gradle.kts ./

# Copy each submodule's build descriptor (preserves cache when
# source code changes but dependencies do not).
COPY domain/build.gradle.kts         domain/build.gradle.kts
COPY application/build.gradle.kts    application/build.gradle.kts
COPY infrastructure/build.gradle.kts infrastructure/build.gradle.kts
COPY api/build.gradle.kts            api/build.gradle.kts
COPY app/build.gradle.kts            app/build.gradle.kts

# Resolve dependencies (cached unless build files change).
RUN ./gradlew dependencies --no-daemon --quiet

# Copy the full source and build the executable JAR.
COPY . .
RUN ./gradlew :app:bootJar --no-daemon -x test

# Extract the layered JAR so that Docker can cache each Spring Boot
# layer (dependencies, snapshot-dependencies, resources, application).
RUN java -Djarmode=layertools \
    -jar app/build/libs/*.jar extract --destination /workspace/extracted

# ── Stage 2: Runtime ─────────────────────────────────────────
FROM eclipse-temurin:17-jre-alpine AS runtime

# Run as a non-root user for security.
RUN addgroup -S calmvest && adduser -S calmvest -G calmvest \
    && apk add --no-cache curl

WORKDIR /app

# Copy layered content from the build stage in dependency-cache order.
# Layers that change less frequently are copied first.
COPY --from=build /workspace/extracted/dependencies/          ./
COPY --from=build /workspace/extracted/spring-boot-loader/    ./
COPY --from=build /workspace/extracted/snapshot-dependencies/ ./
COPY --from=build /workspace/extracted/application/           ./

USER calmvest

EXPOSE 8080

# Health check so container orchestrators (Docker Compose, K8s liveness probe)
# can verify the application started successfully.
HEALTHCHECK --interval=30s --timeout=5s --start-period=60s --retries=3 \
  CMD curl -sf http://localhost:8080/actuator/health | grep -q '"status":"UP"' || exit 1

ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
