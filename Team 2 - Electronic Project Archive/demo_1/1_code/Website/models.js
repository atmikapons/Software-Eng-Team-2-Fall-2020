const {sequelize, type} = require('sequelize');

let models = (sequelize, type) => {
    let User = sequelize.define('User', {
        FirstName: type.STRING,
        LastName: type.STRING,
        Email: type.STRING,
        PhoneNum: type.STRING,
        Password: type.STRING,
        Barcode: { 
            type: type.INTEGER,
            primaryKey: true,
            autoIncrement: true
        },
        LicenseNum: type.STRING,
        RegistrationNum: type.STRING,
        CreditCardType: type.STRING, 
        CreditCardNum: type.STRING,
        ExpDate: type.STRING,
        CVV: type.STRING,
        Points: type.INTEGER,
        Handicap: {
            type: type.INTEGER,
            default: true,
        }
    }, {
        tableName: 'CustomerInfo'
    });
    let Reservation = sequelize.define('Reservation', {
        Date: type.DATE,
        StartTime: type.TIME,
        EndTime: type.TIME,
        Barcode: type.INTEGER,
        AssignedSpot: type.INTEGER,
        Charge: type.DECIMAL,
        rID: {
            type: type.INTEGER,
            autoIncrement: true
        }
    }, {
        tableName: 'Reservations'
    });
}

module.exports = models;