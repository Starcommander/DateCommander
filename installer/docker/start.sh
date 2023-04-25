#!/bin/bash

set -e

SUDO_CMD=""

. config.sh

check_sudo

echo "######## Open in browser: http://127.0.0.1:8080/DaterWebApp/DaterWebApp.html ##########"
$SUDO_CMD docker run -p=8080:8080 tomcat-webapp
