FROM imagenarium/openjdk-alpine:17 AS TEMP

RUN apk add dos2unix

ENV APP_HOME=/usr/app
WORKDIR $APP_HOME

COPY . .
RUN dos2unix ./gradlew
RUN ./gradlew --no-daemon bootJar
RUN ls -l /usr/app/build/libs

FROM imagenarium/openjdk-alpine:17
ENV ARTIFACT_NAME=attendance-0.0.1-SNAPSHOT.jar
ENV APP_HOME=/usr/app
WORKDIR $APP_HOME
COPY --from=TEMP $APP_HOME/build/libs/$ARTIFACT_NAME .
EXPOSE 8080
CMD java -jar $ARTIFACT_NAME