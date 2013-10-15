#!/bin/bash

mvn clean install

echo Installing ANT tool
generated=ant/target/*-jar-with-dependencies.jar
target=~/Dropbox/epub-project/lib/antpub.jar
#target=/home/jirka/.ant/lib/antpub.jar
mvn package assembly:assembly -DdescriptorId=jar-with-dependencies -pl ant
rm $target
cp $generated $target

echo Installing epubtool
generated=cli/target/*-jar-with-dependencies.jar
target=/home/jirka/bin/epubtool.jar
mvn package assembly:assembly -DdescriptorId=jar-with-dependencies -pl cli
rm $target
cp $generated $target
