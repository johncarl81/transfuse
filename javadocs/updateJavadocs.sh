
git clone git@github.com:johncarl81/transfuse.git
git rm -r api
git rm -r main

mkdir api
mkdir main

cd transfuse/transfuse/
mvn clean install
cd ../transfuse-api
mvn clean install
cd ../..;

cp -r transfuse/transfuse/target/apidocs/* main/
cp -r transfuse/transfuse-api/target/apidocs/* api/

git add main
git add api

rm -fr transfuse
