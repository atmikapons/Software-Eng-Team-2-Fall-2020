var express = require('express');
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

    //////////////////////////////

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

};

module.exports = router;