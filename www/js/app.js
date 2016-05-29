var wWidth, wHeight;

function parse_template(elem, vars) {
    elem = (typeof elem == 'string') ? $(elem) : elem;
    var html = elem.html();
    for (var i in vars) {
        var val = vars[i] ? vars[i] : "";
        html = html.replace(new RegExp("{" + i + "}", "gi"), "" + val);
    }
    return html;
}

var app = {
	init: function() {
		
		app.updateUI();
	},
	updateUI: function() {
        var content = $(".center-content");
        var leftcol = $(".left-panel");
        var rightcol = $(".right-panel");
        var topbar = $(".top-header");

        var contentWidth = wWidth;
        if(!$(".page-body").hasClass("columns-full-width")) {
            contentWidth = 1024;
        }
        content.width(contentWidth - leftcol.outerWidth() - rightcol.outerWidth());
        leftcol.height(wHeight - topbar.outerHeight());
        rightcol.height(wHeight - topbar.outerHeight());
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
    },
    lastWindowId: 0,
    showWindow: function(options) {
        var title = options.title ? options.title : "Brak tytułu";
        var body = options.body ? options.body : "";
        var close_label = options.closeLabel ? options.closeLabel : "Anuluj";
        var continue_label = options.continueLabel ? options.continueLabel : "Kontynuuj";

        var cancelFunc = options.cancelFunc ? options.cancelFunc : null;
        var confirmFunc = options.confirmFunc ? options.confirmFunc : null;

        var newWindowId = "window_" + (++app.lastWindowId);
        var window_html = parse_template("#window_template", {
            title: title,
            body: body,
            window_name_id: newWindowId,
            close_label: close_label,
            continue_label: continue_label
        });

        var container = $("body");


        var handle = $(window_html).appendTo(container);
        $(handle).modal({

        }).show();

        var _closeFunc = function(h) {
            $(h).modal('hide')
            setTimeout(function() {
                $(h).next(".modal-backdrop").remove();
                $(h).remove();
            }, 500);
        };

        handle.find(".modal-btn-close").click(function() {
            var t = $(this);
            var tModal = t.parents(".modal");
            _closeFunc(tModal);

            if(cancelFunc != null) cancelFunc();
        });
        handle.find(".modal-btn-continue").click(function() {
            var t = $(this);
            var tModal = t.parents(".modal");
            _closeFunc(tModal);

            if(confirmFunc != null) confirmFunc();
        });

    },
    showCreateEventWindow: function() {
        var body_template = parse_template("#create_event_template", {

        });

        app.showWindow({
            title: "Dodawanie nowego zdarzenia",
            body: body_template
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
    $(".trigger-add-event").click(function() {
        app.showCreateEventWindow();
    });


    $(".screen-trigger").click(function() {
        var btn = $(this);
        var screen = btn.attr("data-screen");
        $(".screen-trigger").removeClass("lm-current");
        btn.addClass("lm-current");
        if(screen) {
            $(".page-screen").hide();
            $(".screen-"+screen).show();
        }
    });


	app.init();
});