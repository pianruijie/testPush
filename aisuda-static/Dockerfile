ARG JAVA_BASE_IMAGE=openjdk:11

FROM ${JAVA_BASE_IMAGE}

ENV BUILD_DIR .
ADD $BUILD_DIR/app /app-engine/app
ADD $BUILD_DIR/bin /app-engine/bin
ADD $BUILD_DIR/conf /app-engine/conf
ADD $BUILD_DIR/script /app-engine/script
ADD $BUILD_DIR/webroot /app-engine/webroot
WORKDIR /app-engine/bin

# 独立配置文件替换
RUN javac /app-engine/script/FilterConfig.java

CMD ["sh", "-c", "cd /app-engine/script; java FilterConfig; cd /app-engine/bin; bash start.sh -daemonOff"]

EXPOSE 8899
