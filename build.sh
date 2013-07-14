#!/bin/bash

if [ "$POST_BUILD" == "true" ] && [ "$TRAVIS_PULL_REQUEST" == "false" ] then

	git clone -b gh-pages https://${GH_TOKEN}@github.com/johncarl81/transfuse.git site;

	jekyll build -d site;

	cd site;

	touch .nojekyll;
	rm build.sh;

	git commit -a -m "Travis build $TRAVIS_BUILD_NUMBER";
	git push;

	rm -rf site;

fi
