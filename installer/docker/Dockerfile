FROM tomcat:9.0-alpine
LABEL maintainer="soundmodul@gmx.at"

ADD DaterWebApp.war /usr/local/tomcat/webapps/

EXPOSE 8080
CMD ["catalina.sh", "run"]
