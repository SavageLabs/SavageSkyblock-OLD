FROM maven:3.3.9-jdk-8
WORKDIR /build

RUN mvn --batch-mode compile
RUN mvn --batch-mode package

# Shall use the minecraft server image and deploy the plugin to it