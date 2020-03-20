var express = require('express');
var mysql = require('mysql');
var http = require('http');
var path = require('path');
var app = express();
const bodyParser = require('body-parser');
const {getHomePage} = require('./routes/index');
const {addPlayerPage, addPlayer, deletePlayer, editPlayer, editPlayerPage} = require('./routes/customer');


app.set('view engine', 'ejs');
app.set('views', path.join(__dirname, 'views'));
app.use(express.static(path.join(__dirname, 'public')));
app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json()); // parse form data client
app.use(express.static(path.join(__dirname, 'public'))); // configure express to use public folder
//app.get('/', getHomePage);
//app.get('/add', addPlayerPage);
//app.get('/edit/:id', editPlayerPage);
//app.get('/delete/:barcode', deletePlayer);
//app.post('/add', addPlayer);
//app.post('/edit/:id', editPlayer);

// database connection
var db = mysql.createConnection({
    host: '127.0.0.1', // local host
    user: 'zippypark',
    password: 'goyhVynIiLcJgHpN',
    database: 'zippypark'
});

db.connect( function (err) {
    if ( err ) {
        console.error('error connecting: ' + err.stack);
        return;
    }
    console.log('connected as id ' + db.threadId);
});

var routes = require('./routes/index.js')(app,db);

var httpServer = http.createServer( app );
httpServer.listen(8080); // can change port