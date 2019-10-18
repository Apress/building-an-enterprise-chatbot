const fetch = require("node-fetch");
const url = "http://localhost:8080/respond?";

function webhooks(controller){

    //This is the Initial message seen by user which come to Iris for First Time
    controller.api.messenger_profile.greeting('Hi, my name is IRIS. I am continuously training to become a virtual assistant');

    controller.hears(['.*'], 'message_received,facebook_postback', function(bot, message) {

        var params = {
            message: message.text,
            sender: message.sender.id,
            seq: message.seq,
            timestamp: message. timestamp
        };

        var esc = encodeURIComponent;
        var query = Object.keys(params)
        .map(k => esc(k) + '=' + esc(params[k]))
        .join('&');



        fetch(url +query)
        .then(response => {
            response.json().then(json => {
               bot.reply(message, json.message);
            });
        })
        .catch(error => {
            bot.reply(message, "");
        });
    });

}

module.exports = webhooks;
