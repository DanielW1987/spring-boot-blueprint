FROM eclipse-temurin:17-jre

ENV TZ=Europe/Berlin
EXPOSE 8080

RUN apt-get update && apt-get install -y \
  curl \
  && rm -rf /var/lib/apt/lists/*

RUN mkdir /app
WORKDIR /app

RUN useradd --no-log-init --no-create-home --shell /bin/false service_user \
  && chown -cR service_user:service_user /app
USER service_user

ARG SOURCE_JAR_FILE="build/libs/*.jar"
ARG BUILD_DATE
ARG VCS_REF

LABEL org.label-schema.build-date=$BUILD_DATE
LABEL org.label-schema.vcs-ref=$VCS_REF

COPY $SOURCE_JAR_FILE app.jar

CMD ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
