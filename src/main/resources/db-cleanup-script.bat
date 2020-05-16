@ECHO OFF
ECHO "Start run script"
cd C:\Program Files\MongoDB\Server\4.0\bin
mongo.exe ErpDB < E:\ws\erp-server\src\main\resources\run.sql
ECHO "Successfully done!"
PAUSE 