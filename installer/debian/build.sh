#!/bin/bash

APP_DIR_RT=./data/usr/share/tomcat8-dater
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
do_check ./target

do_clean
cp -r $APP_SRC $APP_DIR
dpkg-deb -b data/ target/date-commander.deb
do_clean
echo "Finish."
