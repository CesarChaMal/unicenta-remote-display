#!/bin/sh

DIRNAME=`dirname $0`
CP=$DIRNAME/remotedisplay.jar

# start uniCenta oPOS
java -cp $CP com.unicenta.configuration.Configuration 
