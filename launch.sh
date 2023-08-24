echo "'creating database...'"
sleep 1
mongod --dbpath /Users/tvntvn/workspace/projects/java/letsplay/data/db/ --logpath /Users/tvntvn/workspace/projects/java/letsplay/data/logs/mongo.log --fork &&
	echo ""
echo ""
sleep 2
echo "'starting spring boot...'"
mvn spring-boot:run
