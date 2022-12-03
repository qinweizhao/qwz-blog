FROM java:8
EXPOSE 8090
ADD blog-backend.jar app.jar
RUN bash -c 'touch /app.jar'
ENTRYPOINT ["java", "-jar", "/app.jar"]