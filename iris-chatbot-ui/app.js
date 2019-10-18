var Botkit = require('botkit');
var commandLineArgs = require('command-line-args');
var localtunnel = require('localtunnel');


var server = require(__dirname + '/server.js');
var conf = require(__dirname + '/conf.js');
var webhook = require(__dirname + '/webhooks.js');

const fetch = require("node-fetch");
const url = "http://localhost:8080/respond?";

const ops = commandLineArgs([
      {name: 'lt', alias: 'l', args: 1, description: 'Use localtunnel.me to make your bot available on the web.',
      type: Boolean, defaultValue: false},
      {name: 'ltsubdomain', alias: 's', args: 1,
      description: 'Custom subdomain for the localtunnel.me URL. This option can only be used together with --lt.',
      type: String, defaultValue: null},
   ]);

var controller = Botkit.facebookbot({
    debug: true,
    log: true,
    access_token: conf.access_token,
    verify_token: conf.verify_token,
    app_secret: conf.app_secret,
    validate_requests: true
});

// Create server 
var app = server(ops);



    
// Receive post data from fb, this will be the messages you receive 
app.post('/facebook/receive', function(req, res) {

    if (req.query && req.query['hub.mode'] == 'subscribe') {
        if (req.query['hub.verify_token'] == controller.config.verify_token) {
            res.send(req.query['hub.challenge']);
        } else {
            res.send('OK');
        }
    }

    // respond to FB that the webhook has been received.
    res.status(200);
    res.send('ok');

    var bot = controller.spawn({});

    // Now, pass the webhook into be processed
    controller.handleWebhookPayload(req, res, bot);


});

// Perform the FB webhook verification handshake with your verify token 
app.get('/facebook/receive', function(req, res) {
    if (req.query['hub.mode'] == 'subscribe') {
        if (req.query['hub.verify_token'] == controller.config.verify_token) {
            res.send(req.query['hub.challenge']);
        } else {
            res.send('OK');
        }
    }else{
        res.send('NOT-OK');
    }

});


// Ping URL 
app.get('/ping', function(req, res) {
            res.send('{"status":"ok"}');
});

// Ping URL
app.post('/alexa', function(req, res) {

    var text = req.body.request.intent.slots.utteranceSlot.value;
    var session = req.body.session.user.userId;
    var timestamp = req.body.request.timestamp;


    var params = {
        message: text,
        sender: session,
        seq: 100,
        timestamp: 1524326401
    };

    var esc = encodeURIComponent;
    var query = Object.keys(params)
                    .map(k => esc(k) + '=' + esc(params[k]))
                    .join('&');

    fetch(url +query).then(response => {
        response.json().then(json => {

            var alexaResp = {
                "version": "string",
                "sessionAttributes": {},
                "response": {
                    "outputSpeech": {
                        "type": "PlainText",
                        "text": json.message,
                        "ssml": "<speak>"+json.message+"</speak>"
                    }
                }
            }

            res.json(alexaResp);
            });
        })
        .catch(error => {

        var alexaResp = {
            "version": "string",
            "sessionAttributes": {},
            "response": {
                "outputSpeech": {
                    "type": "PlainText",
                    "text": "Sorry, My team is having bad day to get this information to you. Please try again in some time.",
                    "ssml": "<speak>Sorry, My team is having bad day to get this information to you. Please try again in some time.</speak>"
                }
            }
        }

        res.json(alexaResp);
    });

});


webhook(controller);
