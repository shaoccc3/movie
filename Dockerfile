# 使用官方的 OpenJDK 作为基础镜像
FROM tomcat:10.0

# 刪除webapps目錄下的預設應用
RUN rm -rf /usr/local/tomcat/webapps/ROOT/*

# 将本地文件系统中的 jar 文件复制到容器中
COPY target/Movie-proj-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

# 指定应用程序启动命令
CMD ["catalina.sh", "run"]

EXPOSE 8080