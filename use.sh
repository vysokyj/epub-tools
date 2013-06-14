#!/bin/bash
mvn clean package assembly:assembly -DdescriptorId=jar-with-dependencies -pl ant
cp ant/target/*-jar-with-dependencies.jar /home/dropbox/epub-project/lib/antpub.jar
