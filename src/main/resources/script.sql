mongo localhost/admin;
use ErpDB;
db.createCollection("test")
db.createCollection("randomNumber")
db.randomNumber.insert({ "randomID" : 1,"poinvoicenumber" : 001,"poinvoicecode" : "INVPO","salesinvoicenumber" : 001,"salesinvoicecode":"INVSA"})
db.randomNumber.drop()

db.randomNumber.find();
db.randomNumber.remove( { } )


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
