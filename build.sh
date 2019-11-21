if [[ "$OSTYPE" == "darwin"* ]]; then
	docker run -v "$(pwd)":/target/ -it java_doc bash
elif [[ "$OSTYPE" == "linux-gnu" ]]; then
	rm -rf ./build/*
	export JAVA_TOOL_OPTIONS=-Dfile.encoding=UTF8 && javac  *.java -d ./build
	STATUS=$?
	if [ $STATUS -ne 0 ]; then
		exit 1
	fi
	cd build 
	jar -cvfm Agent.jar ../data/manifest.txt ./com/*.class
	cd .. 
	if [ $# -eq 0 ]
	  then
	  	echo "run"
	    java -jar build/Agent.jar
	fi
fi