var express = require('express');
var router = express.Router();

router.get('/', function (req, res) {
    //res.send('Hello World');
    res.render('pages/dashboard');
});

router.get('/statistics', function (req, res) {
    res.render('pages/statistics');
});

router.get('/reservations', function (req, res) {
    res.render('pages/reservations');
});

router.get('/customers', function (req, res) {
    res.render('pages/customers', {
        // customers: info.data.filter(function(item) {
        //     return item.role === "";
        // })
    });
});

module.exports = router;