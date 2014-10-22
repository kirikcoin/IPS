#!

SQL=${1:?"File name not set"}
DB=${2:?"Target DB name not set"}

MYSQL="mysql -h localhost -u user -ppassword"

$MYSQL -e "CREATE DATABASE $DB;"
$MYSQL $DB <$SQL
