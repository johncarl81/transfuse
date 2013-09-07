#!/bin/bash

if [ "$POST_BUILD" == "true" ] && [ "$TRAVIS_PULL_REQUEST" == "false" ]; then

	#Set git user
	git config --global user.email "johncarl81@gmail.com"
	git config --global user.name "Travis"
	git config --global push.default simple

	echo -e "Cloning gh-pages\n"
	git clone -b gh-pages https://${GH_TOKEN}@github.com/johncarl81/transfuse.git site

	echo -e "Building Jekyll site\n"
	jekyll build -d site

	cd site

	touch .nojekyll
	rm build.sh


	echo -e "Committing site\n"
	git commit -a -m "Travis build $TRAVIS_BUILD_NUMBER"
	git push origin

        cd ..
	rm -rf site

fi

