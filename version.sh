#!/bin/sh
if [[ -n $1 ]]; then
    echo "Switching to version $1 ..."
	mvn versions:set -DnewVersion=$1
    mvn versions:commit
	echo "Installing new version modules ..."
    mvn clean install
else
    echo "Specify version number: $0 2.0" 
fi