#!/bin/sh

DEST=$1

scp build/libs/Client-Pi-0.1.jar $DEST:~

ssh $DEST killall java
ssh $DEST java -jar Client-Pi-0.1.jar
