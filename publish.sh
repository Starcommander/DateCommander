#!/bin/bash

set -e

SERVER="root@kxn4rm.myvserver.online"
TARGET="/var/lib/tomcat9/webapps/"

scp DaterWebApp.war "$SERVER:/tmp/DaterWebApp.war"
ssh "$SERVER" chown tomcat:tomcat /tmp/DaterWebApp.war
ssh "$SERVER" mv /tmp/DaterWebApp.war "$TARGET"
echo "Successfully published to $TARGET"
