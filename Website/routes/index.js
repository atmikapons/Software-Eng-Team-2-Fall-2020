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
      db.query('SELECT * FROM Reservations', function (err, rows) {
          if ( err ) {
              res.render('pages/reservations', {
                  reservations: null,
              });
          } else {
              res.render('pages/reservations', {
                  reservations: rows,
              });
          }
      });
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

    ////// RESERVATION ROUTES //////

    app.get('/deleteReservation/:rid', function (req, res) {
        let rid = req.params.rid;
        let deleteReservationQuery = 'DELETE FROM Reservations WHERE rID = "' + rid + '"';
        db.query(deleteReservationQuery, function (err, result) {
            if ( err ) {
                return res.status(500).send(err);
            }
            res.redirect('/reservations');
        });
    });

    app.post('/getReservation', function (req, res) {
        let rid = req.body.rid;
        let getReservationQuery = 'SELECT Date, StartTime, EndTime, Barcode, AssignedSpot, Charge FROM Reservations WHERE rID = (?)';
        db.query(getReservationQuery, rid, function (err, rows) {
            if ( err ) {
                return res.status(500).send(err);
            } else if ( rows.length === 0 ) {
                return res.status(404).send({
                    'status' : 'not found'
                });
            } else {
                JSON.stringify(rows);
                return res.status(200).send({
                    'status' : "Success",
                    'data' : rows,
                });
            }
        });
    });

    app.post('/addReservation', function (req, res) {
        let date = req.body.date;
        let start = req.body.start;
        let end = req.body.end;
        let barcode = req.body.barcode;
        let spot = req.body.spot;
        let charge = req.body.charge;
        let rid = req.body.rid;
        let values = [];
        values.push([date, start, end, barcode, spot, charge, rid]);
        let addQuery = "INSERT INTO Reservations (Date, StartTime, EndTime, Barcode, AssignedSpot, \
                        Charge, Rid) VALUES ?;";
        db.query(addQuery, [values], function (err, result) {
            if ( err ) {
                return res.status(500).send(err);
            } else {
                return res.status(200).send({
                    'status': "Success",
                });
            }
        });
    });

    app.post('/editReservation', function (req, res) {
        let date = req.body.date;
        let start = req.body.start;
        let end = req.body.end;
        let barcode = req.body.barcode;
        let spot = req.body.spot;
        let charge = req.body.charge;
        let rid = req.body.rid;
        let editQuery = "UPDATE Reservations SET Date = ?, StartTime = ?, EndTime = ?, \
                         Barcode = ?, AssignedSpot = ?, Charge = ? WHERE rID = ?";
        db.query(editQuery, [date, start, end, barcode, spot, charge, rid], function (err, result) {
            if ( err ) {
                return res.status(500).send(err);
            } else {
                return res.status(200).send({
                    'status': "Success",
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
                        CreditCardNum, ExpDate, CVV, Points, Handicapped) VALUES ?;";
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
