db.randomNumber.remove( { } )
db.randomNumber.insert({ "randomID" : 1,"number" : 10000,"code" : "E","description"    : "Employee Code"})
db.randomNumber.insert({ "randomID" : 2,"number" : 10000,"code" : "C","description"    : "Customer"})
db.randomNumber.insert({ "randomID" : 3,"number" : 10000,"code" : "V","description"    : "Vendor"})
db.randomNumber.insert({ "randomID" : 4,"number" : 10000,"code" : "P","description"    : "Product"})
db.randomNumber.insert({ "randomID" : 5,"number" : 10000,"code" : "CT","description"   : "Category"})
db.randomNumber.insert({ "randomID" : 6,"number" : 10000,"code" : "PO","description"   : "Purchase Order"})
db.randomNumber.insert({ "randomID" : 7,"number" : 10000,"code" : "SO","description"   : "Sales Order"})
db.randomNumber.insert({ "randomID" : 8,"number" : 10000,"code" : "POR","description"  : "Purchase Return"})
db.randomNumber.insert({ "randomID" : 9,"number" : 10000,"code" : "SOR","description"  : "Sales Return"})
db.randomNumber.insert({ "randomID" : 10,"number": 10000,"code" : "INVP","description" : "Purchase Invoice"})
db.randomNumber.insert({ "randomID" : 11,"number": 10000,"code" : "INVS","description" : "Sales Invoice"})
db.randomNumber.insert({ "randomID" : 12,"number": 10000,"code" : "INVPR","description": "Purchase Invoice return"})
db.randomNumber.insert({ "randomID" : 13,"number": 10000,"code" : "INVSR","description": "Sales Invoice return"})
db.randomNumber.insert({ "randomID" : 14,"number": 10000,"code" : "STD","description"  : "Stock damage"})
db.randomNumber.insert({ "randomID" : 15,"number": 10000,"code" : "D","description"    : "Discount"})
db.randomNumber.insert({ "randomID" : 16,"number": 10000,"code" : "STIN","description" : "Stock In"})
db.randomNumber.insert({ "randomID" : 17,"number": 10000,"code" : "STOT","description" : "Stock Out"})
db.randomNumber.insert({ "randomID" : 18,"number": 10000,"code" : "MEN","description": "Menu"})
db.randomNumber.insert({ "randomID" : 19,"number": 10000,"code" : "TR","description" : "Transaction"})

db.employee.remove( { } )
db.absentList.remove( { } )
db.dailyReport.remove( { } )

db.customer.remove( { } )
db.vendor.remove( { } )

db.purchaseOrder.remove( { } )
db.pOInvoice.remove( { } )
db.pOReturnDetails.remove( { } )

db.item.remove( { } )
db.discount.remove( { } )
db.units.remove( { } )
db.category.remove( { } )

db.sOInvoice.remove( { } )
db.sOInvoiceDetails.remove( { } )
db.sOReturnDetails.remove( { } )
db.salesOrder.remove( { } )

db.stock.remove( { } )

db.menu.remove( { } )

db.randomNumber.remove( {"randomID" :19 } )

db.purchaseOrder.update({"_id" :ObjectId("5e84406bb184021e9421f6d3") },{$set : {"status":'Open'}})


db.menu.insertOne({ "menucode":"MEN10000","menuname":"Dashboard","submenuname":"","langcode":"EN"});
db.menu.insertOne({ "menucode":"MEN10000","menuname":"Dasbor","submenuname":"","langcode":"INDO"});

db.menu.insertOne({ "menucode":"MEN10001","menuname":"Employees","submenuname":"","langcode":"EN"});
db.menu.insertOne({ "menucode":"MEN10001","menuname":"Para karyawan","submenuname":"","langcode":"INDO"});

db.menu.insertOne({ "menucode":"MEN10002","menuname":"Vendors","submenuname":"","langcode":"EN"});
db.menu.insertOne({ "menucode":"MEN10002","menuname":"Vendor","submenuname":"","langcode":"INDO"});

db.menu.insertOne({ "menucode":"MEN10003","menuname":"Purchases","submenuname":"Orders,Invoices,Returns","langcode":"EN"});
db.menu.insertOne({ "menucode":"MEN10003","menuname":"Pembelian","submenuname":"Pesanan,Faktur,Kembali","langcode":"INDO"});

db.menu.insertOne({ "menucode":"MEN10004","menuname":"Product","submenuname":"product,units,category","langcode":"EN"});
db.menu.insertOne({ "menucode":"MEN10004","menuname":"Produk","submenuname":"Produk,unit,kategori","langcode":"INDO"});

db.menu.insertOne({ "menucode":"MEN10005","menuname":"Sales","submenuname":"Orders,Invoices,Customer,Returns","langcode":"EN"});
db.menu.insertOne({ "menucode":"MEN10005","menuname":"Penjualan","submenuname":"Pesanan,Faktur,Pelanggan,Kembali","langcode":"INDO"});

db.menu.insertOne({ "menucode":"MEN10006","menuname":"Stock","submenuname":"","langcode":"EN"});
db.menu.insertOne({ "menucode":"MEN10006","menuname":"persediaan","submenuname":"","langcode":"INDO"});

db.menu.insertOne({ "menucode":"MEN10007","menuname":"Finance","submenuname":"pettycash","langcode":"EN"});
db.menu.insertOne({ "menucode":"MEN10007","menuname":"Keuangan","submenuname":"kas kecil","langcode":"INDO"});

db.menu.insertOne({ "menucode":"MEN10008","menuname":"User Management","submenuname":"","langcode":"EN"});
db.menu.insertOne({ "menucode":"MEN10008","menuname":"manajemen pengguna","submenuname":"","langcode":"INDO"});
