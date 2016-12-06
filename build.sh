#!/bin/sh

set -e

WEEK=$1
COMMAND=$2

CP=algs4/algs4.jar:${WEEK}/src
JAVA=$(find ${WEEK}/src -name *.java)
CLASS=$(find ${WEEK}/src -name *.class)

clean() {
  rm ${WEEK}/src/*.class
}

check() {
  checkstyle -c algs4/checkstyle-coursera.xml ${JAVA}
}

build() {
  javac -cp ${CP} ${JAVA}
}

run() {
  RUN=$(cat ${WEEK}/run)

  java -cp ${CP} ${RUN}
}

case ${COMMAND} in
  clean)
    clean
  ;;
  build)
    check
    build
  ;;
  run)
    check
    build
    run
  ;;
  *)
    check
    build
    run
  ;;
esac

