var conf  = function() {

    var conf = {
        securePort: 443,
        port: 80,
        sslKeyPath: '/etc/pki/tls/private/localhost.key',
        sslCertPath: '/etc/pki/tls/certs/localhost.crt',
        static_dir:'',
        hostname: null,
        access_token: process.env.page_token,
        verify_token: process.env.verify_token,
        app_secret: process.env.app_secret
    };

        /*
        * Validate the configurations and set the options.
        */
    console.log(conf);
    return conf;
}


module.exports = conf();
