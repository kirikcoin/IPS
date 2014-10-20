#!

now=`date +"%Y%m%d-%H%M"`
name=${1:-$now}

mysqldump -h 192.168.10.19 -u ips -ppassword ips >"ips.prod.$name.sql"
