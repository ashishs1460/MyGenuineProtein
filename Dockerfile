FROM openjdk:17
EXPOSE 8080
ADD target/mygenuineprotein-k8s.jar mygenuineprotein-k8s.jar
ENTRYPOINT ["java", "-jar", "/mygenuineprotein-k8s.jar"]