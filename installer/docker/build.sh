#!/bin/bash

set -e

WAR_FILE="../../DaterWebApp.war"
SUDO_CMD=""

check_file() # Args: dir msg
{
  if [ ! -f "$1" ]; then
    echo "$2"
    echo "Missing: $1"
  fi
}

check_file Dockerfile "Error, change to this directory first"
check_file "$WAR_FILE" "Error, build the web-archive first with: ant war"

. config.sh

cp "$WAR_FILE" .
check_sudo
$SUDO_CMD docker build --tag "tomcat-webapp" .
