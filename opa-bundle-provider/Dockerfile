FROM adoptopenjdk/openjdk11
VOLUME /tmp
COPY target/opa-bundle-provider-0.0.1.jar bundle-provider.jar
COPY . .
ENTRYPOINT ["java","-jar","/bundle-provider.jar"]


# docker build . -t bundle-provider-app

# docker run -p 9090:8080 bundle-provider-app