cd communicationapi
mvn clean install
cd ../webapp
mvn clean install -DskipTests
/Users/ivanbahdanau/IdeaProjects/git/hosismyphonedoing/appengine-java-sdk-1.9.17/bin/appcfg.sh --oauth2 update /Users/ivanbahdanau/IdeaProjects/git/hosismyphonedoing/webapp/target/webapp-1.0-SNAPSHOT