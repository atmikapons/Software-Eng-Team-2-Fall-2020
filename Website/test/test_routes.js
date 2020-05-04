/*written by: Kylie Chow, Samantha Cheng, Nandita Shenoy
tested by: Kylie Chow, Samantha Cheng, Nandita Shenoy
debugged by: Kylie Chow, Samantha Cheng, Nandita Shenoy*/

// automated route testing
let chai = require('chai');
let chaiHttp = require('chai-http');
let server = require('../server'); // server.js
let should = chai.should();

chai.use(chaiHttp);

describe('Initializing...', () => {  // check basic pages
    it('check LOGIN', (done) => {
        chai.request(server).get('/').end((err, res) => {
            should.not.exist(err);
            res.should.have.status(200);
            done();
        });
    });
    it('check DASHBOARD', (done) => {
        chai.request(server).get('/dashboard').end((err, res) => {
            should.not.exist(err);
            res.should.have.status(200);
            done();
        });
    });
    it('check STATISTICS and send DB information', (done) => {
        chai.request(server).get('/statistics').end((err, res) => {
            should.not.exist(err);
            res.should.have.status(200);
            done();
        });
    });
    it('check RESERVATIONS', (done) => {
        chai.request(server).get('/reservations').end((err, res) => {
            should.not.exist(err);
            res.should.have.status(200);
            done();
        });
    });
    it('check CUSTOMERS', (done) => {
        chai.request(server).get('/customers').end((err, res) => {
            should.not.exist(err);
            res.should.have.status(200);
            done();
        });
    });
});

describe('Customer Account', () => { // Check routes specific to Customer page
    it('ADD a customer account', (done) => { // Add an example account
        const customer = {
            first: 'Sam',
            last: 'Cheng',
            email: 'test@email.com',
            phone:'555-555-5555',
            password: 'password',
            barcode: '314159',
            license: '123ABC',
            reg: '1234567',
            cctype: 'Visa',
            ccnum:'1234576578',
            ccexp:'01-24',
            cccvv:'123',
            points:'0',
            handicap:'0'
        }
        chai.request(server)
        .post('/addCustomer')
        .send(customer)
        .end((err, res) => {
            res.should.have.status(200);
            done();
        });
    });
    it('UPDATE a customer with an existing account', (done) => { // Update the same account
        const customer = {
            first: 'Bob',
            last: 'Cheng',
            email: 'bob@email.com',
            phone:'555-555-5555',
            password: 'password',
            barcode: '314159',
            license: '123ABC',
            reg: '1234567',
            cctype: 'Visa',
            ccnum:'1234576578',
            ccexp:'01-24',
            cccvv:'123',
            points:'0',
            handicap:'0'
        }
        chai.request(server)
        .post('/editCustomer')
        .send(customer)
        .end((err, res) => {
            res.should.have.status(200);
            done();
        });
    });
    it('GET an existing customer', (done) => { // Get the same account by barcode
        const customer = {
            barcode: 314159
        }
        chai.request(server)
        .post('/getCustomer')
        .send(customer)
        .end((err, res) => {
            res.should.have.status(200);
            done();
        });
    });
    it('DELETE a customer given the barcode', (done) => { // Delete the test account
        const customer = {
            barcode: 314159
        }
        chai.request(server)
        .get('/deleteCustomer/' + customer.barcode)
        .end((err, res) => {
            res.should.have.status(200);
            done();
        });
    });  
});

describe('Reservations', () => { // Check routes specific to Reservations page
    before((done) => { // Making a reservation requires an existing barcode, so create a Customer account first
        const customer = {
            first: 'Sam',
            last: 'Cheng',
            email: 'test@email.com',
            phone:'555-555-5555',
            password: 'password',
            barcode: '314159',
            license: '123ABC',
            reg: '1234567',
            cctype: 'Visa',
            ccnum:'1234576578',
            ccexp:'01-24',
            cccvv:'123',
            points:'0',
            handicap:'0'
        }
        chai.request(server)
        .post('/addCustomer')
        .send(customer)
        .end((err, res) => {
            res.should.have.status(200);
            done();
        });
    });
    after((done) => { // Delete the customer account when done testing Reservation routes
        const customer = {
            barcode: 314159
        }
        chai.request(server)
        .get('/deleteCustomer/' + customer.barcode)
        .end((err, res) => {
            res.should.have.status(200);
            done();
        });
    })
    it('ADD a reservation for an existing account', (done) => { // Add a reservation for this customer
        const reservation = {
            date: '2020-04-30',
            start: '10:00',
            end: '12:00',
            barcode: '314159',
            spot: '2',
            vip: '0',
            charge: '25.00',
            rid: '265358979'
        }
        chai.request(server)
        .post('/addReservation')
        .send(reservation)
        .end((err, res) => {
            if (err) return done(err);
            res.should.have.status(200);
            done();
        });
    });
    it('GET a reservation given the reservation id', (done) => { // Get the reservation just made
        const reservation = {
            rid: '265358979'
        }
        chai.request(server)
        .post('/getReservation')
        .send(reservation)
        .end((err, res) => {
            res.should.have.status(200);
            done();
        });
    });
    it('UPDATE a reservation given the reservation id', (done) => { // Update the reservation just made
        const reservation = {
            date: '2020-05-30',
            start: '10:00',
            end: '12:00',
            barcode: '314159',
            spot: '2',
            vip: '0',
            charge: '30.00',
            rid: '265358979'
        }
        chai.request(server)
        .post('/editReservation')
        .send(reservation)
        .end((err, res) => {
            res.should.have.status(200);
            done();
        });
    });
    it('DELETE a reservation given the reservation id', (done) => { // Delete hte reservation just made
        const reservation = {
            rid: '265358979'
        }
        chai.request(server)
        .get('/deleteReservation/' + reservation.rid)
        .end((err, res) => {
            res.should.have.status(200);
            done();
        });
    });
});

describe('Dashboard', () => {  // Check routes specific to Dashboard page
    it('GET number of open parking spots', (done) => { 
        chai.request(server)
        .get('/getParkingSpots')
        .end((err, res) => {
            res.should.have.status(200);
            done();
        });
    });
    it('GET number of reservations by date', (done) => {
        chai.request(server)
        .get('/getNumReservations')
        .end((err, res) => {
            res.should.have.status(200);
            done();
        });
    });
    it("GET yesterday's revenue", (done) => {
        chai.request(server)
        .get('/getRevenue')
        .end((err, res) => {
            res.should.have.status(200);
            done();
        });
    });
    it('GET number of customer accounts', (done) => {
        chai.request(server)
        .get('/getNumCustomers')
        .end((err, res) => {
            res.should.have.status(200);
            done();
        });
    });
    it('GET price scheme', (done) => {
        chai.request(server)
        .get('/getPriceForm')
        .end((err, res) => {
            res.should.have.status(200);
            done();
        });
    });
    let createRes, createRes30x, onTime, overstay, cancels, exchangeVIP;
    it('GET points scheme', (done) => { // Get points and keep track of original points scheme
        chai.request(server)
        .get('/getPointsForm')
        .end((err, res) => {
            res.should.have.status(200);
            let data = JSON.parse(res.text).data;
            createRes = data[0].Points;
            createRes30x = data[1].Points;
            onTime = data[2].Points;
            overstay = data[3].Points;
            cancels = data[4].Points;
            exchangeVIP = data[5].Points;
            done();
        });
    });
    it('UPDATE points scheme', (done) => { // Update point scheme (temporarily for testing)
        const pointScheme = {
            createRes: createRes,
            createRes30x: createRes30x,
            onTime: onTime,
            overstay: overstay,
            cancels: cancels,
            exchangeVIP: -50,
        }
        chai.request(server)
        .post('/editPointsForm')
        .send(pointScheme)
        .end((err, res) => {
            res.should.have.status(200);
            done();
        });
    });
    after((done) => { // Reset point scheme to normal after updating it
        const pointScheme = {
            createRes: createRes,
            createRes30x: createRes30x,
            onTime: onTime,
            overstay: overstay,
            cancels: cancels,
            exchangeVIP: exchangeVIP
        }
        chai.request(server)
        .post('/editPointsForm')
        .send(pointScheme)
        .end((err, res) => {
            res.should.have.status(200);
            done();
        });
    })
});