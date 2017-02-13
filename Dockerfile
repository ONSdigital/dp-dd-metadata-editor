FROM onsdigital/java-component

WORKDIR /app/

ADD ./target/dp-dd-metadata-editor-*.jar .

ENTRYPOINT java -Dspring.datasource.driverClassName=$DB_DRIVER -Dspring.datasource.url=$DB_URL -Dspring.datasource.username=$DB_USER -Dspring.datasource.password=$DB_PASSWORD -Dserver.port=$SERVER_PORT -jar ./dp-dd-metadata-editor-*.jar
