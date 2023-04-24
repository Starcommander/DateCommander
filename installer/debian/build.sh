#!/bin/bash

APP_DIR_RT=./data/usr/share/tomcat9-dater
APP_DIR=$APP_DIR_RT/dater
APP_SRC=../../war/
APP_VERS=X.X-master
CTL_FILE=data/DEBIAN/control

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

get_vers()
{
  if type git ; then
    APP_VERS=1.$(git log | grep -c "^commit [0-9a-f]")-$(git branch --show-current)
  else
    APP_VERS=$(cat $CTL_FILE | grep "^Version: " | cut -d' ' -s -f 2)
  fi
}

set_vers()
{
  sed -i -e "s/^Version:.*/Version: $APP_VERS/g" $CTL_FILE
}

reset_vers()
{
  if type git ; then
    git checkout data/DEBIAN/control
  fi
}

do_check $APP_SRC
do_check $APP_DIR_RT
get_vers

do_clean
cp -r $APP_SRC $APP_DIR
set_vers
mkdir -p target
fakeroot dpkg-deb -b data/ target/date-commander_v${APP_VERS}.deb
reset_vers
do_clean
echo "Finish."
