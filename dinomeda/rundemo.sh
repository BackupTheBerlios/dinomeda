#!/bin/bash
DIR=$(dirname $0)
CLASSPATH=$DIR/classes
MAINCLASS=demo.Dinomeda
CONFIG=$DIR/src/demo/dinomeda.cfg
JAVA=java

$JAVA -cp $CLASSPATH $MAINCLASS --cfg $CONFIG "$@"

