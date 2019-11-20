export JAVA_TOOL_OPTIONS=-Dfile.encoding=UTF8 
./build.sh test
STATUS=$?
if [ $STATUS -ne 0 ]; then
	exit 1
fi
mkdir -p build
javac -d ./build ./tests/*.java -cp ".:/usr/share/java/junit.jar:./build/:./build/com/:./com/:./tests/"
STATUS=$?
if [ $STATUS -ne 0 ]; then
	exit 1
fi

#exit 0

cp ./data/manifest.txt ./build/manifest.txt 
cd build

java -cp ".:/usr/share/java/junit.jar:./build/:./build/com/:./com/:./tests/" junit.textui.TestRunner com.AgentTest
java -cp ".:/usr/share/java/junit.jar:./build/:./build/com/:./com/:./tests/" junit.textui.TestRunner com.MatrixTest
java -cp ".:/usr/share/java/junit.jar:./build/:./build/com/:./com/:./tests/" junit.textui.TestRunner com.NetworkTest
java -cp ".:/usr/share/java/junit.jar:./build/:./build/com/:./com/:./tests/" junit.textui.TestRunner com.DataHandlerTest
