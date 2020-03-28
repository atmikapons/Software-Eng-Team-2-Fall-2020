// automated route testing
const Reservation = require('../models').Reservation;
const User = require('../models').User;

let chai = require('chai');
let chaiHttp = require('chai-http');
let server = require('../server');
let should = chai.should();

chai.use(chaiHttp);

describe('Initializing', () => {
    it('it should check app status', (done) => {
        chai.request(server).get('/').end((err, res) => {
            should.not.exist(err);
            res.should.have.status(200);
            done();
        });
    });
});

describe('Customer Account', () => {
    // describe('/ADD customer', () => {
    //     it('it should ADD a customer account', (done) => {
    //         const customer = {
    //             first: 'Sam',
    //             last: 'Cheng',
    //             email: 'test@email.com',
    //             phone:'555-555-5555',
    //             password: 'password',
    //             barcode: 31415926535,
    //             license: '123ABC',
    //             reg: '1234567',
    //             cctype: 'Visa',
    //             ccnum:'1234576578',
    //             ccexp:'01-24',
    //             cccvv:'123',
    //             points:50,
    //             handicap:0
    //         }
    //         chai.request(server)
    //         .post('/addCustomer')
    //         .send(customer)
    //         .end((err, res) => {
    //             res.should.have.status(200);
    //             done();
    //         });
    //     });
    // });
    describe('/DELETE customer', () => {
        it('it should DELETE a customer given the barcode', (done) => {
            const customer = {
                barcode: 31415926535
            }
            chai.request(server)
            .get('/deleteCustomer/' + customer.barcode)
            .send(customer)
            .end((err, res) => {
                res.should.have.status(200);
                done();
            });
        });
    });
});

describe('Reservation', () => {
    describe('/GET reservation', () => {
        it('it should GET a reservation given the id', (done) => {
            const reservation = {
                rid: 23145
            }
            chai.request(server)
            .post('/getReservation')
            .send(reservation)
            .end((err, res) => {
                res.should.have.status(200);
                done();
            });
        });
    });
    describe('/EDIT reservation', () => {
        it('it should EDIT a reservation given the id', (done) => {
            const reservation = {
                date: '2020-03-21',
                start: '11:00',
                end: '23:00',
                barcode: 2345678,
                spot: -1,
                charge: 160,
                rid: 134576,
            }
            chai.request(server)
            .post('/editReservation')
            .send(reservation)
            .end((err, res) => {
                res.should.have.status(200);
                done();
            });
        });
    });
});