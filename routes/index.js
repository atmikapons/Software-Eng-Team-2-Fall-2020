var express = require('express');
var router = function (app, db) {
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
};

module.exports = router;