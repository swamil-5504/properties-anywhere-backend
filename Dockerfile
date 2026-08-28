FROM eclipse-temurin:25-jdk

WORKDIR /app

COPY . .

RUN chmod +x ./gradlew
RUN ./gradlew clean bootJar -x test

EXPOSE 8080

CMD ["sh", "-c", "java -jar build/libs/*.jar --server.port=${PORT:-8080}"]