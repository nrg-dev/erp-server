mongo localhost/admin;
use ErpDB;
db.createCollection("test")
db.createCollection("randomNumber")

db.randomNumber.insert({ "randomID" : 1,"poinvoicenumber" : 001,"poinvoicecode" : "INVPO","salesinvoicenumber" : 001,"salesinvoicecode":"INVSA"})
db.randomNumber.insert({ "randomID" : 2,"vendorinvoicenumber" : 001,"vendorinvoicecode" : "VEN","customerinvoicenumber" : 001,"customerinvoicecode":"CUST"})
db.randomNumber.insert({ "randomID" : 3,"categoryinvoicenumber" : 01,"categoryinvoicecode" : "CAT","productinvoicenumber" : 01,"productinvoicecode":"PROD"})
db.randomNumber.insert({ "randomID" : 4,"employeeinvoicenumber" : 01,"employeeinvoicecode" : "EMP"})
db.randomNumber.insert({ "randomID" : 5,"discountinvoicenumber" : 01,"discountinvoicecode" : "DIS"})
db.randomNumber.insert({ "randomID" : 6,"poreturninvoicenumber" : 001,"poreturninvoicecode" : "INVPORET","soreturninvoicenumber" : 001,"soreturninvoicecode":"INVSORET"})
db.randomNumber.insert({ "randomID" : 7,"stockreturninvoicenumber" : 001,"stockreturninvoicecode" : "INVRET","stockdamageinvoicenumber" : 001,"stockdamageinvoicecode" : "INVDAM"})
db.randomNumber.insert({ "randomID" : 8,"stockIninvoicenumber" : 001,"stockIninvoicecode" : "INVSTIN","stockOutinvoicenumber" : 001,"stockOutinvoicecode" : "INVSTOUT"})


show collections
db.test.drop()
db.dropDatabase()
db.pOInvoiceDetails.remove( { } )
db.pOInvoiceDetails.find();
mongoexport --db ErpDB --collection test --out E:\home\test.json

sudo mongoexport --db ErpDB --collection test --out /home/ec2-user/test.json

-- Purchase
db.pOInvoice.find();
db.pOInvoice.remove( { } )
db.pOInvoiceDetails.find();
db.pOInvoiceDetails.remove( { } )

-- PurchaseReturn
db.pOReturnDetails.find();
db.pOReturnDetails.remove( { } )

-- Sales
db.sOInvoice.find();
db.sOInvoice.remove( { } )
db.sOInvoiceDetails.find();
db.sOInvoiceDetails.remove( { } )

-- employee
db.employee.drop()
db.employee.find();
db.employee.remove( { } )

-- Customer
db.customer.find();
db.customer.drop()
db.customer.remove( { } )

-- Venodr
db.vendor.find();
db.vendor.drop()
db.vendor.remove( { } )

-- category
db.category.find();
db.category.drop()
db.category.remove( { } )
-- Item or Product
db.item.drop()
db.item.find();
db.item.remove( { } )

-- Discount
db.discount.drop()
db.discount.find();
db.discount.remove( { } )

-- Random Number
db.randomNumber.drop()
db.randomNumber.find();
db.randomNumber.remove( { } )


db.stock.find({"_id" :ObjectId("5e4fb74ab1840225ec539395") });
db.pOInvoiceDetails.find({"paymentStatus" :"Not Paid"});
db.pOInvoiceDetails.find({"itemname" :"Heals-PROD6"});

db.stock.remove({"status" :"StockOut" });

db.randomNumber.remove({"_id" :ObjectId("5e4fbb4987a94ea98989b9f4") });
db.pOInvoiceDetails.update({"_id" :ObjectId("5e4f8e3cb184021bb0c9e7d5") },{$set : {"paymentStatus":'Not Paid'}})


APIServerUrl
============
{ "apiurl": "http://34.214.60.154:8095/erp/" }   


APILocalUrl
===========
{ "apiurl": "http://localhost:8095/erp/" } 



db.login.insertOne({ "invnumber":"INVLO002","username":"alex","password":"alex","status":"Active","userOtp":""});