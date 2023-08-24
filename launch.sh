cwd=$(pwd)
dbpath="$cwd/data/db"
logpath="$cwd/data/log/mongo.log"

echo "'creating database...'"
sleep 1

mongod --dbpath "$dbpath" --logpath "$logpath" --fork &&
	echo ""
echo ""
sleep 2

echo "'starting spring boot...'"
mvn spring-boot:run
