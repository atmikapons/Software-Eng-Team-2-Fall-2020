const express = require('express');
const date = require('date-and-time');

var router = function (app, db) {
    ////// GET WEBPAGES //////////
    app.get('/', function (req, res) {
        let now = new Date();
        res.render('pages/dashboard', {
            today: date.format(now, 'MMM DD, YYYY'),
        });
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
    /////// STATISTICS ROUTES /////
    app.get('/byDate', function (req, res) {
        db.query('SELECT Date, COUNT(*) AS `Count` FROM `Records` GROUP BY Date', function (err, rows) {
            if (err) {
                res.render('pages/byDate', {
                    records: null,
                });
            } else {
                res.render('pages/byDate', {
                    records: rows,
                });
            }
        });
    });

    app.get('/byTime', function (req, res) {
        db.query('SELECT HOUR(StartTime) AS `Time`, COUNT(*) AS `Count` FROM `Records` GROUP BY HOUR(StartTime)', function (err, rows) {
            if (err) {
                res.render('pages/byTime', {
                    records: null,
                });
            } else {
                res.render('pages/byTime', {
                    records: rows,
                });
            }
        });
    });
    ////// DASHBOARD ROUTES //////
    app.get('/getParkingSpots', function (req, res) {
        let getParkingQuery = 'SELECT COUNT(SpotNum) AS spots FROM `Parking Spots` WHERE Status="Unoccupied"';
        db.query(getParkingQuery, function (err, rows) {
            if ( err ) {
                return res.status(500).send(err);
            } else {
                JSON.stringify(rows);
                return res.status(200).send({
                    'status' : "Success",
                    'data' : rows,
                });
            }
        });
    });

    app.get('/getNumReservations', function (req, res) {
        let date = new Date();
        let getNumResQuery = 'SELECT Date, COUNT(*) AS num FROM Reservations GROUP BY Date';
        db.query(getNumResQuery, function (err, rows) {
            if ( err ) {
                return res.status(500).send(err);
            } else {
                JSON.stringify(rows);
                return res.status(200).send({
                    'status' : "Success",
                    'data' : rows,
                });
            }     
        });
    });

    app.get('/getRevenue', function (req, res) {
        let date = new Date();
        let yesterday = date.getFullYear() + '-' + (date.getMonth()+1) + '-' + (date.getDate()-1);
        let getRevQuery = 'SELECT SUM(Charge) AS rev FROM Records WHERE Date= "' + yesterday + '"';
        db.query(getRevQuery, function (err, rows) {
            if ( err ) {
                return res.status(500).send(err);
            } else {
                JSON.stringify(rows);
                return res.status(200).send({
                    'status' : "Success",
                    'data' : rows,
                });
            }     
        });
    });

    app.get('/getNumCustomers', function (req, res) {
        let getNumCustomersQuery = 'SELECT COUNT(*) AS num FROM CustomerInfo';
        db.query(getNumCustomersQuery, function (err, rows) {
            if ( err ) {
                return res.status(500).send(err);
            } else {
                JSON.stringify(rows);
                return res.status(200).send({
                    'status' : "Success",
                    'data' : rows,
                });
            }     
        });
    });

    app.get('/getPriceForm', function (req, res) {
        db.query('SELECT * FROM Payment', function (err, rows) {
            if ( err ) {
                return res.status(500).send(err);
            } else {
                JSON.stringify(rows);
                return res.status(200).send({
                    'status' : "Success",
                    'data' : rows,
                });
            }
        });
    });

    app.get('/getPointsForm', function (req, res) {
        db.query('SELECT * FROM Points', function (err, rows) {
            if ( err ) {
                return res.status(500).send(err);
            } else {
                JSON.stringify(rows);
                return res.status(200).send({
                    'status' : "Success",
                    'data' : rows,
                });
            }
        });
    });

    app.post('/editPriceForm', function (req, res) {
        let times = JSON.parse(req.body.times);
        let bases = JSON.parse(req.body.bases);
        let multipliers = JSON.parse(req.body.multipliers);
        let editQuery = "UPDATE Payment SET BasePrice = (case ";
        let index;
        for ( index = 0; index < times.length; index++ ) {
            editQuery+=("when StartTime = " + times[index] + " then " + bases[index] + " ");
        }
        editQuery+="end), Multiplier = (case ";
        for ( index = 0; index < times.length; index++ ) {
            editQuery+=("when StartTime = " + times[index] + " then " + multipliers[index] + " ");
        }
        editQuery+="end) WHERE StartTime in (";
        for ( time in times ) {
            editQuery+=(times[time]+",");
        }
        editQuery=editQuery.slice(0,-1);
        editQuery+=")";

        db.query(editQuery, [times], function (err, result) {
            if ( err ) {
                return res.status(500).send(err);
            } else {
                return res.status(200).send({
                    'status': "Success",
                });
            }
        });
    });

    app.post('/editPointsForm', function (req, res) {
        let p1 = req.body.createRes;
        let p2 = req.body.createRes30x;
        let p3 = req.body.onTime;
        let p4 = req.body.overstay;
        let p5 = req.body.cancels;
        let p6 = req.body.exchangeVIP;

        let editQuery = "UPDATE Points SET Points = (case \
                            when Action = 'CreateRes' then ? \
                            when Action = 'CreateRes30x' then ? \
                            when Action = 'OnTime' then ? \
                            when Action = 'Overstay' then ? \
                            when Action = 'Cancels' then ? \
                            when Action = 'ExchangeVIP' then ? \
                        end) WHERE Action in ('CreateRes', 'CreateRes30x', 'OnTime', \
                        'Overstay', 'Cancels', 'ExchangeVIP')";
        db.query(editQuery, [p1, p2, p3, p4, p5, p6], function (err, result) {
            if ( err ) {
                return res.status(500).send(err);
            } else {
                JSON.stringify(result);
                return res.status(200).send({
                    'status': "Success",
                    'data':result,
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
        let getReservationQuery = 'SELECT Date, StartTime, EndTime, Barcode, AssignedSpot, VIP, Charge FROM Reservations WHERE rID = (?)';
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
        let vip = req.body.vip;
        let charge = req.body.charge;
        let rid = req.body.rid;
        let values = [];
        values.push([date, start, end, barcode, spot, vip, charge, rid]);
        let addQuery = "INSERT INTO Reservations (Date, StartTime, EndTime, Barcode, AssignedSpot, \
                        VIP, Charge, Rid) VALUES ?;";
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
        let vip = req.body.vip;
        let charge = req.body.charge;
        let rid = req.body.rid;
        let editQuery = "UPDATE Reservations SET Date = ?, StartTime = ?, EndTime = ?, \
                         Barcode = ?, AssignedSpot = ?, VIP = ?, Charge = ? WHERE rID = ?";
        db.query(editQuery, [date, start, end, barcode, spot, vip, charge, rid], function (err, result) {
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

    app.post('/getCustomer', function (req, res) {
        let barcode = req.body.barcode;
        let getCustomerQuery = 'SELECT FirstName, LastName, Email, PhoneNum, \
        Password, LicenseNum, RegistrationNum, CreditCardType, \
        CreditCardNum, ExpDate, CVV, Points, Handicapped FROM CustomerInfo \
        WHERE Barcode = (?)';
        db.query(getCustomerQuery, barcode, function (err, rows) {
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

    app.post('/editCustomer', function (req, res) {
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

        let editQuery = "UPDATE CustomerInfo SET FirstName = ?, LastName = ?, Email = ?, \
                         PhoneNum = ?, Password = ?, LicenseNum = ?, RegistrationNum = ?, \
                         CreditCardType = ?, CreditCardNum = ?, ExpDate = ?, CVV = ?, Points = ?, \
                         Handicapped = ? WHERE Barcode = ?";
        db.query(editQuery, [first, last, email, phone, pass, license, reg, cctype, ccnum,
                             ccexp, cccvv, points, handicap, barcode], function (err, result) {
            if ( err ) {
                return res.status(500).send(err);
            } else {
                return res.status(200).send({
                    'status': "Success",
                });
            }
        });
    });

};

module.exports = router;