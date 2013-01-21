#!/bin/bash
#Grab transfuse
git clone git@github.com:johncarl81/transfuse.git

# Remove old javadocs
git rm -r api
git rm -r main

# Grab transfuse
git clone git@github.com:johncarl81/transfuse.git

# Build selected modules
cd transfuse/transfuse/
mvn clean javadoc:javadoc
cd ../transfuse-api
mvn clean javadoc:javadoc
cd ../..;

# Add structure folders
mkdir api
mkdir main

# Move javadocs into place
cp -r transfuse/transfuse/target/apidocs/* main/
cp -r transfuse/transfuse-api/target/apidocs/* api/

# Re-add javadocs
git add main
git add api

# Cleanup
rm -fr transfuse
