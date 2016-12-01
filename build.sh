#!/bin/sh

set -e

WEEK=$1

CP=algs4/algs4.jar:${WEEK}/src
JAVA=$(find ${WEEK}/src -name *.java)

checkstyle -c algs4/checkstyle-coursera.xml ${JAVA}

RUN=$(cat ${WEEK}/run)

set -x
javac -cp ${CP} ${JAVA}
java -cp ${CP} ${RUN}
