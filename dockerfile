FROM openjdk:17-oracle 
VOLUME /tmp 
COPY target/StudentManagementSystem-0.0.1-SNAPSHOT.jar StudentManagementSystem.jar 
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","StudentManagementSystem.jar"]