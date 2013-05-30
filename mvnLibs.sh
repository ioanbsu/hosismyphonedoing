mvn install:install-file -Dfile=google-play-services.jar -DgroupId=com.google.android.gms -DartifactId=google-play-services -Dversion=7 -Dpackaging=jar
mvn install:install-file -Dfile=compatibility-v4-13.jar -DgroupId=android.support -DartifactId=compatibility-v4 -Dversion=11 -Dpackaging=jar
mvn install:install-file -Dfile=gwt-gae-channel-2.0.0.jar -DgroupId=com.google.appengine -DartifactId=gwt-gae-channel -Dversion=2.0.0 -Dpackaging=jar
