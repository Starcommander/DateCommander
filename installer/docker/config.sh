#!/bin/bash

check_sudo()
{
  if [ -w /var/run/docker.sock ]; then
    SUDO_CMD="" # Already root, or docker-socket writeable
  elif type sudo ; then
    SUDO_CMD="sudo" # sudo command existing
  else
    SUDO_CMD="su root" # Switch user to root
  fi
}

