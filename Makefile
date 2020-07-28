test:
	\
	 ./gradlew clean test

testSelenoidFirefox:
	\
	./gradlew clean -Dbrowser=firefox testSelenoid

testSeleniumFirefox:
	\
	./gradlew clean -Dbrowser=firefox testSelenium
