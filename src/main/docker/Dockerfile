FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD tiny-tictactoe-0.1.0.jar tiny-tictactoe-0.1.0.jar
RUN sh -c 'touch /tiny-tictactoe-0.1.0.jar'
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /tiny-tictactoe-0.1.0.jar" ]