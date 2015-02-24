#!/bin/sh

DEST=$1

scp build/libs/Client-Pi-Debug-0.1.jar $DEST:~

ssh $DEST killall java
ssh -Y $DEST java -jar Client-Pi-Debug-0.1.jar
