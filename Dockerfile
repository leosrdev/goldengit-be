FROM openjdk:17-alpine
RUN mkdir -p /opt/goldengit
COPY target/goldengit-1.0.jar /opt/goldengit/
WORKDIR /opt/goldengit
EXPOSE 8080
CMD ["java", "-jar", "goldengit-1.0.jar"]