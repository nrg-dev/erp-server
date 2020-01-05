mongo localhost/admin;
use ErpDB;
db.createCollection("test")
db.test.insert({"name" : "alex"})
db.test.insert({"name" : "alex","address" : "annai nager","education" : "mca"})
show collections
db.test.drop()
db.dropDatabase()
db.purchaseOrder.remove( { } )

mongoexport --db ErpDB --collection test --out E:\home\test.json

sudo mongoexport --db ErpDB --collection test --out /home/ec2-user/test.json
