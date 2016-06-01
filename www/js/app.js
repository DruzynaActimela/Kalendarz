var wWidth, wHeight;

var MOMENT_DATE_FORMAT_DAY = "DD-MM-YYYY";
var MOMENT_DATE_FORMAT_DAY_TIME = MOMENT_DATE_FORMAT_DAY + " hh:mm";

var WINDOW_NO_CLOSE = 123551;
var WINDOW_CLOSE = 123555;

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
		app.displayUserGroups();
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

        var postShowFunc = options.postShow ? options.postShow : null;

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

        setTimeout(function() {
            if(postShowFunc != null) postShowFunc(handle);
        }, 1);

        var _closeFunc = function(h) {
            app.closeWindow(h);
        };

        handle.next(".modal-backdrop").click(function() {
            var t = $(this);
            var tModal = t.prev(".modal");
            
            var funcReturnStatus = WINDOW_CLOSE;
            if(cancelFunc != null) funcReturnStatus = cancelFunc(tModal);
            if(funcReturnStatus === WINDOW_CLOSE) _closeFunc(tModal);
        });

        handle.find(".modal-btn-close").click(function() {
            var t = $(this);
            var tModal = t.parents(".modal");

            var funcReturnStatus = WINDOW_CLOSE;
            if(cancelFunc != null) funcReturnStatus = cancelFunc(tModal);
            if(funcReturnStatus === WINDOW_CLOSE) _closeFunc(tModal);

        });
        handle.find(".modal-btn-continue").click(function() {
            var t = $(this);
            var tModal = t.parents(".modal");

            var funcReturnStatus = WINDOW_CLOSE;
            if(confirmFunc != null) funcReturnStatus = confirmFunc(tModal);
            if(funcReturnStatus === WINDOW_CLOSE) _closeFunc(tModal);
        });

    },
    closeWindow: function(h) {
        if(h) {            
            $(h).modal('hide')
            setTimeout(function() {
                $(h).next(".modal-backdrop").remove();
                $(h).remove();
            }, 1000);
        }
    },
    showCreateEventWindow: function() {
        var body_template = parse_template("#create_event_template", {

        });

        app.showWindow({
            title: "Dodawanie nowego zdarzenia",
            body: body_template,
            continueLabel: "Dodaj zdarzenie",
            postShow: function(handle) {
                
                handle.find(".bind-datepicker").datetimepicker({
                    controlType: 'select',
                    oneLine: true,
                    dateFormat: "dd-mm-yy",
                    timeFormat:  "HH:mm",
                    firstDay: 1
                });

                app.getUserGroups(function(groups) {
                    var sel = handle.find(".new-event-group");
                    sel.empty();
                    sel.append("<option value='-1' data-tokens=\"brak grupy\">Brak grupy</option>");

                    for(var i in groups) {
                        var group = groups[i];
                        var option = "<option data-tokens='"+group.name+"' value='"+group.id+"'>"+group.name+"</option>";
                        sel.append(option);
                    }
                    

                    handle.find("select").selectpicker();
                });

            },
            confirmFunc: function(handle) {
                var name = handle.find(".new-event-name").val();
                var group = handle.find(".new-event-group").selectpicker('val');
                var date_start = handle.find(".new-event-date-start").val();
                var date_end = handle.find(".new-event-date-end").val();

                var is_wholeday = (handle.find(".new-event-whole-day:checked").length > 0) ? 1 : 0;
                var is_public = (handle.find(".new-event-public:checked").length > 0) ? 1 : 0;

                if(!name || name.trim().length < 1                     
                    || !date_start || date_start.trim().length < 1
                    || !date_end || date_end.trim().length < 1
                    ) {
                    app.showConfirmBox({
                        title: "Błąd",
                        body: "Nie podano poprawnych danych.",
                        hideCancel: true,
                        confirmText: "OK"
                    });
                    return WINDOW_NO_CLOSE;
                }
                var minute_diff = moment(date_end, MOMENT_DATE_FORMAT_DAY_TIME).diff(moment(date_start, MOMENT_DATE_FORMAT_DAY_TIME), "minutes");
                if(minute_diff <= 0) {
                    app.showConfirmBox({
                        title: "Błąd",
                        body: "Data zakończenia zdarzenia nie może być szybciej niż data rozpoczęcia.",
                        hideCancel: true,
                        confirmText: "OK"
                    });
                    return WINDOW_NO_CLOSE;
                }
                app.doCreateEventRequest(name, group, date_start, date_end, is_wholeday, is_public, function() {
                    app.closeWindow(handle);
                });
            }

        });
    },
    showCreateGroupWindow: function() {
        var body_template = parse_template("#create_group_template", {

        });

        app.showWindow({
            title: "Dodawanie grupy zdarzeń",
            body: body_template,
            continueLabel: "Dodaj grupę",
            postShow: function(handle) {
                handle.find('#cp2').colorpicker();
            },
            confirmFunc: function(handle) {
                var name = handle.find(".new-group-name").val();
                var color = handle.find(".new-group-color").val();
                var is_public = (handle.find(".new-group-public:checked").length > 0) ? 1 : 0;

                if(!name || name.trim().length < 1 || !color || color.trim().length < 1) {
                    app.showConfirmBox({
                        title: "Błąd",
                        body: "Nie podano poprawnych danych.",
                        hideCancel: true,
                        confirmText: "OK"
                    });
                    return WINDOW_NO_CLOSE;
                }

                app.doCreateGroupRequest(name, color, is_public, function() {
                    app.closeWindow(handle);
                    app.displayUserGroups();
                });
            }
        });
    },
    doCreateGroupRequest: function(name, color, is_public, onSuccess, onFailure) {

        app.doRequest("/api/groups/create", {
            name: name,
            color: color,
            "public": is_public
        }, "POST", function(resp) {
            var is_success = (resp.type == "success");
            app.showConfirmBox({
                title: (is_success ? "Sukces!" : "Błąd"),
                body: resp.message,
                hideCancel: true,
                confirmText: "OK",
                confirmClick: function() {
                    if(is_success) {
                        if(onSuccess) onSuccess(resp);
                    } else {
                        if(onFailure) onFailure(resp);
                    }
                }
            });
        });
    },
    doCreateEventRequest: function(name, group, date_start, date_end, is_wholeday, is_public, onSuccess, onFailure) {
        app.doRequest("/api/events/create", {
            name: name,
            group: group,
            date_start: date_start,
            date_end: date_end,
            wholeday: is_wholeday,
            "public": is_public
        }, "POST", function(resp) {
            var is_success = (resp.type == "success");
            app.showConfirmBox({
                title: (is_success ? "Sukces!" : "Błąd"),
                body: resp.message,
                hideCancel: true,
                confirmText: "OK",
                confirmClick: function() {
                    if(is_success) {
                        if(onSuccess) onSuccess(resp);
                    } else {
                        if(onFailure) onFailure(resp);
                    }
                }
            });
        });
    },
    getUserGroups: function(onResponse) {
        app.doRequest("/api/groups", {}, "POST", function(resp) {
            if(onResponse) onResponse(resp);
        });
    },
    displayUserGroups: function() {
        app.getUserGroups(function(resp) {
            var gcontainer = $(".user-groups-list");
            gcontainer.empty();
            if(resp && resp.length > 0) {
                for(var i in resp) {
                    var group = resp[i];
                    var html = parse_template("#user_group_legend_template", {
                        id: group.id,
                        color: group.color,
                        name: group.name
                    });
                    var elem = $(html).appendTo(gcontainer);
                }
            } else {
                gcontainer.html("<div style='padding:10px;text-align:center;margin-bottom:10px;'>Brak dodanych grup.</div>");
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
    $(".trigger-add-event").click(function() {
        app.showCreateEventWindow();
    });

    $(".trigger-add-group").click(function() {
        app.showCreateGroupWindow();
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