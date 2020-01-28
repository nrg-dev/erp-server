mongo localhost/admin;
use ErpDB;
db.createCollection("test")
db.createCollection("randomNumber")

db.randomNumber.insert({ "randomID" : 1,"poinvoicenumber" : 001,"poinvoicecode" : "INVPO","salesinvoicenumber" : 001,"salesinvoicecode":"INVSA"})
db.randomNumber.insert({ "randomID" : 2,"vendorinvoicenumber" : 001,"vendorinvoicecode" : "VEN","customerinvoicenumber" : 001,"customerinvoicecode":"CUST"})
db.randomNumber.insert({ "randomID" : 3,"categoryinvoicenumber" : 01,"categoryinvoicecode" : "CAT","productinvoicenumber" : 01,"productinvoicecode":"PROD"})


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
db.randomNumber.remove({"_id" :ObjectId("5e302b5419cf607a9c899bd3") });
db.pOInvoice.update({"_id" :ObjectId("5e3028feb184020f4c20e256") },{$set : {"vendorname":"Alex Ubalton-VEN2"}});

db.pOInvoiceDetails.update({"_id" :ObjectId("5e3028c3b184020f4c20e24d") },{$set : {"vendorname":"Alex Ubalton-VEN1"}});

db.pOInvoiceDetails.update({"_id" :ObjectId("5e30051bb18402288c3c8dd8") },{$set : {"vendorname":"Alex Ubalton-VEN2"}});



db.pOInvoice.find();
db.pOInvoice.remove( { } )

db.pOInvoiceDetails.find();
db.pOInvoiceDetails.remove( { } )


db.vendor.find();
db.vendor.remove( { } )


db.customer.find();
db.customer.remove( { } )

