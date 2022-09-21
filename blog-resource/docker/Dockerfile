FROM java:8
EXPOSE 8090
ADD blog-backend-3.0.5.jar app.jar
RUN bash -c 'touch /app.jar'
ENTRYPOINT ["java", "-jar", "/app.jar"]