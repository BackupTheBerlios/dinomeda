#!/bin/bash

CLASSPATH=classes
MAINCLASS=demo.Dinomeda
CONFIG=src/demo/dinomeda.cfg
JAVA=java

$JAVA -cp $CLASSPATH $MAINCLASS --cfg $CONFIG $@

