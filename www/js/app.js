var wWidth, wHeight;

var app = {
	init: function() {
		
		app.updateUI();
	},
	updateUI: function() {

	},
    doLogin: function() {
        var login = $(".username").val();
        var pass = $(".password").val();
        if(!login || !pass || login.length < 1 || pass < 1) {
            app.showConfirmBox({
                title: "Błąd",
                body: "Podaj poprawne dane logowania!",
                hideCancel: true,
                confirmText: "OK"
            });
            return;
        }

        app.doRequest("/login", {
            username: login,
            password: Sha256.hash(pass)
        }, "POST", function(resp) {
            if(resp.type == "success") {
                var redir = "dashboard";
                if(resp.redirect) redir = resp.redirect;

                window.location.href = redir;
            } else {
                app.showConfirmBox({
                    title: "Błąd",
                    body: resp.message,
                    hideCancel: true,
                    confirmText: "OK"
                });
            }
        });
    },
    doRegister: function() {
        var login = $(".username").val();
        var pass = $(".password").val();
        var pass2 = $(".password2").val();
        var email = $(".email").val();

        if(!login || !pass || login.length < 1 || pass < 1) {
            var message = "Podaj poprawne dane do rejestracji!";
            if(pass != pass2) message = "Podane hasła nie są identyczne.";
            if(!email || email.length < 1) message = "Podaj adres e-mail!";

            app.showConfirmBox({
                title: "Błąd",
                body: message,
                hideCancel: true,
                confirmText: "OK"
            });
            return;
        }

        app.doRequest("/register", {
            username: login,
            password: Sha256.hash(pass),
            email: email
        }, "POST", function(resp) {
            if(resp.type == "success") {
                app.showConfirmBox({
                    title: "Sukces!",
                    body: "Twoje konto zostało zarejestrowane!",
                    hideCancel: true,
                    confirmText: "Logowanie",
                    confirmClick: function() {
                        window.location.href = '/login';
                    }
                });
            } else {
                app.showConfirmBox({
                    title: "Błąd",
                    body: resp.message,
                    hideCancel: true,
                    confirmText: "OK"
                });
            }
        });
    },

    showConfirmBox: function(options) {
        var $modal = $('[data-remodal-id=modal_confirm]');
        $modal.find(".modal-title").html(options.title);
        $modal.find(".modal-body").html(options.body);
        if (options.confirmText) {
            $modal.find(".remodal-confirm").html(options.confirmText);
        }
        $modal.find(".remodal-confirm").unbind();
        if (options.confirmClick) {
            $modal.find(".remodal-confirm").click(function() {
                options.confirmClick();
            });
        }
        if (options.cancelText) {
            $modal.find(".remodal-cancel").html(options.cancelText);
        }
        $modal.find(".remodal-cancel").unbind();
        if (options.cancelClick) {
            $modal.find(".remodal-cancel").click(function() {
                options.cancelClick();
            });
        }
        if (options.hideCancel) {
            $modal.find(".remodal-cancel").hide();
        } else {
            $modal.find(".remodal-cancel").show();
        }

        var instance = $modal.remodal();
        instance.open();
        return instance;
    },
    doRequest: function(url, params, type, callback) {

        $.ajax({
            type: type,
            url: url,
            data: params,
            dataType: "json",
            success: function(response) {
                if(callback) callback(response);
            }
        });
    }
};

$(window).resize(function() {
	wWidth = $(window).width();
	wHeight = $(window).height();

	app.updateUI();
});

$(document).ready(function() {
	wWidth = $(window).width();
	wHeight = $(window).height();

    $(".btn-login").click(function() {
        app.doLogin();
    });
    $(".btn-register").click(function() {
        app.doRegister();
    });


	app.init();
});