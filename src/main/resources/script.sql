mongo localhost/admin;
use ErpDB;
db.createCollection("test")
db.createCollection("randomNumber")

db.randomNumber.insert({ "randomID" : 1,"poinvoicenumber" : 001,"poinvoicecode" : "INVPO","salesinvoicenumber" : 001,"salesinvoicecode":"INVSA"})
db.randomNumber.insert({ "randomID" : 2,"vendorinvoicenumber" : 001,"vendorinvoicecode" : "VEN","customerinvoicenumber" : 001,"customerinvoicecode":"CUST"})
db.randomNumber.insert({ "randomID" : 3,"categoryinvoicenumber" : 01,"categoryinvoicecode" : "CAT","productinvoicenumber" : 01,"productinvoicecode":"PROD"})
db.randomNumber.insert({ "randomID" : 4,"employeeinvoicenumber" : 01,"employeeinvoicecode" : "EMP"})
db.randomNumber.insert({ "randomID" : 5,"discountinvoicenumber" : 01,"discountinvoicecode" : "DIS"})





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






