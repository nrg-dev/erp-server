mongo localhost/admin;
use ErpDB;
db.createCollection("test")
db.createCollection("RandomNumber")
db.RandomNumber.insert({"poinvoicenumber" : "INVPO001","salesinvoicenumber" : "INVSA001","type" : "invoice"})

db.RandomNumber.find();
db.RandomNumber.remove( { } )


db.test.insert({"name" : "alex"})
db.test.insert({"name" : "alex","address" : "annai nager","education" : "mca"})
show collections
db.test.drop()
db.dropDatabase()
db.purchaseOrder.remove( { } )
db.purchaseOrder.find();
mongoexport --db ErpDB --collection test --out E:\home\test.json

sudo mongoexport --db ErpDB --collection test --out /home/ec2-user/test.json


db.pOInvoiceDetails.find();
db.pOInvoiceDetails.remove( { } )
