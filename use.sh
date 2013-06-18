#!/bin/bash
generated=ant/target/*-jar-with-dependencies.jar
target=/home/dropbox/epub-project/lib/antpub.jar
mvn clean install
mvn package assembly:assembly -DdescriptorId=jar-with-dependencies -pl ant
rm $target
cp $generated $target
