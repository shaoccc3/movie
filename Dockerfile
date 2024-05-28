# 使用Tomcat官方映像作為基礎
FROM tomcat:10.0

# 刪除webapps目錄下的預設應用
RUN rm -rf /usr/local/tomcat/webapps/ROOT/*

COPY web.xml /usr/local/tomcat/conf/web.xml
# 將你的WAR文件複製到Tomcat的webapps目錄
COPY target/Movie-proj-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

# 容器啟動時自動運行Tomcat
CMD ["catalina.sh", "run"]
# 開放8080端口供外部訪問
EXPOSE 8080

