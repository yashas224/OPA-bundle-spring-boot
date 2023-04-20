FROM adoptopenjdk/openjdk11
VOLUME /tmp
COPY target/opa-bundle-consumer-0.0.1.jar bundle-consumer.jar
ENTRYPOINT ["java","-jar","/bundle-consumer.jar"]


# docker build . -t bundle-consumer-app