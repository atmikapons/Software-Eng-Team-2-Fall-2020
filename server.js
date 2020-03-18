var express = require('express');
var mysql = require('mysql');
var http = require('http');
var path = require('path');
var app = express();

app.set('view engine', 'ejs');
app.set('views', path.join(__dirname, 'views'));
app.use(express.static(path.join(__dirname,'public')));

app.get('/', function (req, res) {
    //res.send('Hello World');
    res.render('pages/dashboard');
});

app.get('/statistics', function (req, res) {
    res.render('pages/statistics');
});

app.get('/reservations', function (req, res) {
    res.render('pages/reservations');
});

app.get('/customers', function (req, res) {
    res.render('pages/customers');
});

var db = mysql.createConnection({
    //host: '128.6.238.10',
    host: '127.0.0.1',
    user: 'zippypark',
    password: 'goyhVynIiLcJgHpN',
    database: 'zippypark'
    //port:
});

// db.connect( function (err) {
//     if ( err ) {
//         console.error('error connecting: ' + err.stack);
//         return;
//     }

//     console.log('connected as id ' + db.threadId);
// })

// var server = app.listen(8081, function () {
//     var host = server.address().address;
//     var port = server.address().port;

//     console.log("Example app listening at https://%s:%s", host, port);
// });

var httpServer = http.createServer( app );
httpServer.listen(8080);