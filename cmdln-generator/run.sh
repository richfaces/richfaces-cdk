#!/bin/sh

while getopts ":p:" opt; do
  case $opt in
    p)
	export PROJECT="$OPTARG"
      ;;
  esac
done

if [ -z "$PROJECT" ]; then
	echo "Error: No project provided, use -p to provide project home"
	exit 1
fi

if [ ! -f "$PROJECT/pom.xml" ]; then
	echo "Error: project argument must be valid Maven project (contain pom.xml)"
	exit 2
fi

if [ ! -d "$PROJECT/target/classes" -o ! -d "$PROJECT/target/dependency" -o ! -d "$PROJECT/target/dependency-sources" ]; then
	# compile and extract dependencies
	mvn -f "$PROJECT/pom.xml" compiler:compile dependency:unpack-dependencies -DexcludeTypes=pom
	# extract and extract dependency sources
	mvn -f "$PROJECT/pom.xml" dependency:unpack-dependencies -DexcludeTypes=pom -Dclassifier='sources' -Dmdep.failOnMissingClassifierArtifact=false -DoutputDirectory='${project.build.directory}/dependency-sources' -DincludeGroupIds=org.richfaces.ui.common
fi

TARGET=$(dirname $0)"/target"
java -jar $TARGET/cdk-cmdline-generator.jar $*
