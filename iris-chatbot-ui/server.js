var express = require('express');
var bodyParser = require('body-parser');
var https = require('https');
var http = require('http');
var fs = require('fs');
var localtunnel = require('localtunnel');

// Custom javascripts
var conf = require(__dirname + '/conf.js');

function server(ops) {

    // Create askIris App
    var app = express();
    app.use(bodyParser.json());
    app.use(bodyParser.urlencoded({
        extended: true
    }));
    app.use(express.static(__dirname + conf.static_dir));


    //declare option and create https server.
    var options = {
        port : conf.securePort,
        key : fs.readFileSync(conf.sslKeyPath),
        cert : fs.readFileSync(conf.sslCertPath),
        requestCert : false,
        rejectUnauthorized : false

    };



    https.createServer( options, app)
    .listen(conf.securePort,  conf.hostname, function() {
        console.log('** Starting secure webserver on port ' + conf.securePort);
    });

    http.createServer(app)
    .listen(conf.port, conf.hostname, function() {
        console.log('** Starting webserver on port ' + conf.port);
    });
    
    if(ops.lt) {
        var tunnel = localtunnel(conf.port, {subdomain: 'askiris'}, function(err, tunnel) {
            if (err) {
                console.log(err);
                process.exit();
            }
            console.log("Your bot is available on the web at the following URL: " + tunnel.url + '/facebook/receive');
        });

        tunnel.on('close', function() {
            console.log("Your bot is no longer available on the web at the localtunnnel.me URL.");
            process.exit();
        });
    }


    return app;
}

module.exports = server;
