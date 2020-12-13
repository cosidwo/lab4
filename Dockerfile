FROM java:8
COPY ./Main.java /
COPY ./mysql-connector-java-8.0.13.jar /
WORKDIR /
RUN javac Main.java
CMD ["java", "-classpath", "mysql-connector-java-8.0.13.jar:.","Main"]
