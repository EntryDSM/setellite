FROM eclipse-temurin:17-jre-focal

EXPOSE 8080
ENV TZ=Asia/Seoul

COPY ./satellite-infrastructure/build/libs/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]