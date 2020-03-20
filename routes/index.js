const express = require('express');

var router = function (app, db) {
    ////// GET WEBPAGES //////////
    app.get('/', function (req, res) {
        res.render('pages/dashboard');
    });
    
    app.get('/statistics', function (req, res) {
        res.render('pages/statistics');
    });
    
    app.get('/reservations', function (req, res) {
        res.render('pages/reservations');
    });
    
    app.get('/customers', function (req, res) {
        db.query('SELECT * FROM CustomerInfo', function (err, rows) {
            if ( err ) {
                res.render('pages/customers', {
                    customers: null,
                });
            } else {
                res.render('pages/customers', {
                    customers: rows,
                });
            }
        });
    });

    ////// CUSTOMER ROUTES //////

    app.get('/deleteCustomer/:barcode', function (req, res) {
        let id = req.params.barcode;
        let deleteCustomerQuery = 'DELETE FROM CustomerInfo WHERE barcode = "' + id + '"';
        db.query(deleteCustomerQuery, function (err, result) {
            if ( err ) {
                return res.status(500).send(err);
            }
            res.redirect('/customers');
        });
    });

    app.post('/addCustomer', function (req, res) {
        let first = req.body.first;
        let last = req.body.last;
        let email = req.body.email;
        let phone = req.body.phone;
        let pass = req.body.password;
        let barcode = req.body.barcode;
        let license = req.body.license;
        let reg = req.body.reg;
        let cctype = req.body.cctype;
        let ccnum = req.body.ccnum;
        let ccexp = req.body.ccexp;
        let cccvv = req.body.cccvv;
        let points = req.body.points;
        let handicap = req.body.handicap;
        let values = [];
        values.push([first, last, email, phone, pass, barcode, license, reg,
            cctype, ccnum, ccexp, cccvv, points, handicap]);
        let addQuery = "INSERT INTO CustomerInfo (FirstName, LastName, Email, PhoneNum, \
                        Password, Barcode, LicenseNum, RegistrationNum, CreditCardType, \
                        CreditCardNum, ExpDate, CVV, Points, Handicap) VALUES ?;";
        db.query(addQuery, [values], function (err, result) {
            if ( err ) {
                return res.status(500).send(err);
            } else {
                return res.status(200).send({
                    'status': "Success"
                });
            }
        });
    });
};

module.exports = router;