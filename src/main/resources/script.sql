mongo localhost/admin;
use ErpDB;
db.createCollection("test")
db.createCollection("randomNumber")

db.randomNumber.insert({ "randomID" : 1,"number" : 10000,"code" : "E","description"   : "Employee Code"})
db.randomNumber.insert({ "randomID" : 2,"number" : 10000,"code" : "C","description"   : "Customer"})
db.randomNumber.insert({ "randomID" : 3,"number" : 10000,"code" : "V","description"   : "Vendor"})
db.randomNumber.insert({ "randomID" : 4,"number" : 10000,"code" : "P","description"   : "Product"})
db.randomNumber.insert({ "randomID" : 5,"number" : 10000,"code" : "CT,"description"   : "Category"})
db.randomNumber.insert({ "randomID" : 6,"number" : 10000,"code" : "PO,"description"   : "Purchase Order"})
db.randomNumber.insert({ "randomID" : 7,"number" : 10000,"code" : "SO,"description"   : "Sales Order"})
db.randomNumber.insert({ "randomID" : 8,"number" : 10000,"code" : "POR,"description"  : "Purchase Return"})
db.randomNumber.insert({ "randomID" : 9,"number" : 10000,"code" : "SOR,"description"  : "Sales Return"})
db.randomNumber.insert({ "randomID" : 10,"number": 10000,"code" : "INVP,"description" : "Purchase Invoice"})
db.randomNumber.insert({ "randomID" : 11,"number": 10000,"code" : "INVS,"description" : "Sales Invoice"})
db.randomNumber.insert({ "randomID" : 12,"number": 10000,"code" : "INVPR,"description": "Purchase Invoice return"})
db.randomNumber.insert({ "randomID" : 13,"number": 10000,"code" : "INVSR,"description": "Sales Invoice return"})
db.randomNumber.insert({ "randomID" : 14,"number": 10000,"code" : "STD,"description"  : "Stock damage""})
db.randomNumber.insert({ "randomID" : 15,"number": 10000,"code" : "D","description"   : "Discount"})
db.randomNumber.insert({ "randomID" : 16,"number": 10000,"code" : "STIN","description": "Stock In"})
db.randomNumber.insert({ "randomID" : 17,"number": 10000,"code" : "STOT","description": "Stock Out"})

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


Login Insert Query
===================
db.login.insertOne({ "invnumber":"INVLO001","username":"admin","password":"admin","status":"Active","userOtp":""});
db.login.insertOne({ "invnumber":"INVLO002","username":"josni","password":"josni","status":"Active","userOtp":""});
db.login.insertOne({ "invnumber":"INVLO003","username":"alex","password":"alex","status":"Active","userOtp":""});
UserRole Insert Query
=====================
db.userRole.insertOne({ "invnumber":"INVUS001","userRole":"HRD","menuItem":"Employment,Vendor & Customer,Category & Product,Purchase,Sales,Finance,Stock,Report","subMenuItem":"","status":"Active"});
db.userRole.insertOne({ "invnumber":"INVUS002","userRole":"SALES MANAGER","menuItem":"Employment,Vendor & Customer,Category & Product,Purchase,Sales,Finance","subMenuItem":"","status":"Active"});
db.userRole.insertOne({ "invnumber":"INVUS003","userRole":"ADMINISTRATOR","menuItem":"Employment,Vendor & Customer,Category & Product,Purchase,Sales,Stock","subMenuItem":"","status":"Active"});
db.userRole.insertOne({ "invnumber":"INVUS004","userRole":"WAREHOUSE CHIEF","menuItem":"Employment,Vendor & Customer,Category & Product,Stock,Report","subMenuItem":"","status":"Active"});