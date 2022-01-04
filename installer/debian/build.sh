#!/bin/bash

APP_DIR_RT=./data/usr/share/tomcat9-dater
APP_DIR=$APP_DIR_RT/dater
APP_SRC=../../war/

do_check()
{
  if [ ! -d $1 ]; then
    echo "Error, missing directory: $1"
    exit 1
  fi
}

do_clean()
{
  if [ -d $APP_DIR ]; then
    rm -r -f $APP_DIR
    echo "Purged: $APP_DIR"
  fi
}

do_check $APP_SRC
do_check $APP_DIR_RT

do_clean
cp -r $APP_SRC $APP_DIR
fakeroot dpkg-deb -b data/ date-commander.deb
do_clean
echo "Finish."
