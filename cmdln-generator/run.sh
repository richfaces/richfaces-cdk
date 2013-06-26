#!/bin/sh

while getopts ":p:" opt; do
  case $opt in
    p)
	export PROJECT="$OPTARG"
      ;;
  esac
done

if [ -z "$PROJECT" ]; then
	PROJECT="."
fi

if [ ! -f "$PROJECT/pom.xml" ]; then
	echo "Error: project argument must be valid Maven project (contain pom.xml)"
	exit 2
fi

if [ ! -d "$PROJECT/target/classes" -o ! -d "$PROJECT/target/dependency" ]; then
	mvn -f "$PROJECT/pom.xml" compiler:compile dependency:unpack-dependencies -DexcludeTypes=pom
fi

TARGET=$(dirname $0)"/target"
CLASSPATH="$PROJECT/target/classes:$PROJECT/target/dependency:$TARGET/cdk-cmdline-generator.jar"
echo $CLASSPATH
java -classpath $CLASSPATH org.richfaces.cdk.GenerateMain -t "src/main/templates/**/*.xml" -t "src/main/java/**/*.template.xml" $*
