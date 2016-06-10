var wWidth, wHeight;

var MOMENT_DATE_FORMAT_DAY = "DD-MM-YYYY";
var MOMENT_DATE_FORMAT_TIME = "HH:mm";
var MOMENT_DATE_FORMAT_DAY_TIME = MOMENT_DATE_FORMAT_DAY + " " + MOMENT_DATE_FORMAT_TIME;

var FULLCALENDAR_INTERNAL_DATE_FORMAT = "YYYY-MM-DD";

var WINDOW_NO_CLOSE = 123551;
var WINDOW_CLOSE = 123555;

var DEFAULT_GROUP_COLOR = "gray";

var LAST_RIGHTCLICKED_DATE, LAST_RIGHTCLICKED_EVENT;

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
    screenShowEvents: [],
    locationArgs: [],
    rebuildArgs: function() {
        var args = "";
        for(var key in app.locationArgs) {
            var val = app.locationArgs[key];
            if(args.length > 0) args += "|";
            args += key + ":" + encodeURIComponent(val);

        }
        location.hash = args;
    },
    setArg: function(key, val) {
        app.locationArgs[key] = val;

        app.rebuildArgs();
    },
    getArg: function(key) {
        return app.locationArgs[key];
    },
    hasArg: function(a) {
        return app.locationArgs[a] != undefined;
    },
    parseArgs: function() {
        if(location.hash.length > 1) {
            var strArgs = location.hash.substring(1).split("|");
            for(var i in strArgs) {
                var keyval = strArgs[i].split(":");
                if(keyval.length == 2 && keyval[0] && keyval[1]) {
                    app.locationArgs[keyval[0]] = keyval[1];
                }
            }
        }

    },
    registerScreenShowEvent: function(screenName, func) {
        var screenNames = [screenName];
        if(screenName.indexOf(",") > -1) {
            screenNames = screenName.split(",");

        }
        for(var i in screenNames) {
            var name = screenNames[i].trim();
            if(app.screenShowEvents[name] == undefined) {
                app.screenShowEvents[name] = [];
            }
            app.screenShowEvents[name].push(func);   
        }
    },
    executeScreenShowEvents: function(screenName) {
        if(app.screenShowEvents[screenName] != undefined) {
            for(var i in app.screenShowEvents[screenName]) {
                var func = app.screenShowEvents[screenName][i];
                if(typeof func == 'function') {
                    func($(".screen-" + screenName));
                }
            }
        }
    },
    userEventGroups: [],
    getEventGroup: function(gId) {
        for(var i in app.userEventGroups) {
            var gr = app.userEventGroups[i];
            if(gr.id == gId) return gr;
        }
        return null;
    },

	init: function() {
        app.parseArgs();
        app.getUserGroups(function() {
            app.displayUserGroups();
            app.updateUI();

            app.registerScreenShowEvent("export-ical,export-csv", function(screen) {
                screen.find(".bind-datepicker").datetimepicker({
                    controlType: 'select',
                    oneLine: true,
                    dateFormat: "dd-mm-yy",
                    timeFormat:  "HH:mm",
                    firstDay: 1
                });
            });
            app.registerScreenShowEvent("calendar", function(screen) {
                if(calendarRef) {
                    calendarRef.fullCalendar("refetchEvents");
                }
            });
            
            app.registerScreenShowEvent("import-uz", function(screen) {
                app.getDepartments(function(depts) {
                    var selectElem = screen.find(".select-wydzial");
                    selectElem.empty();
                    if(depts.length > 0) {
                        selectElem.append("<option value='-1'>-- wybierz wydział --</option>");

                        for(var i in depts) {
                            var dept = depts[i];
                            selectElem.append('<optgroup label="'+dept.nazwa+'">');
                            for(var j in dept.kierunki) {
                                var division = dept.kierunki[j];
                                selectElem.append("<option value='"+division.id+"'>"+division.nazwa+"</option>");        
                            }
                            selectElem.append('</optgroup>');
                        }
                    } else {
                        selectElem.append("<option value='-1'>Brak wydziałów!</option>");
                    }
                });
                screen.find(".select-wydzial").unbind().change(function() {
                    var t = $(this);

                    var selectedDivision = t.val();
                    console.log("select", selectedDivision);

                    app.getGroups(selectedDivision, function(groups) {
                        var selectElem = screen.find(".select-grupa");
                        selectElem.empty();
                        for(var j in groups) {
                            var group = groups[j];
                            selectElem.append("<option value='"+group.id+"'>"+group.nazwa+"</option>");        
                        }

                    });
                });

                screen.find(".bind-datepicker").datepicker({
                    controlType: 'select',
                    oneLine: true,
                    dateFormat: "dd-mm-yy",
                });
            });
            


            setTimeout(function() {
                initialize_calendar();
                app.updateUI();


                if(app.hasArg("screen")) {
                    app.setCurrentScreen(app.getArg("screen"));
                }
            }, 1);
        });

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
        content.height(wHeight - topbar.outerHeight());
        leftcol.height(wHeight - topbar.outerHeight());
        rightcol.height(wHeight - topbar.outerHeight());

        $('#calendar').fullCalendar('option', 'height', content.height());

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

    getDepartments: function(onResponse) {
        app.doRequest("/api/uz/departments", {}, "POST", function(resp) {
            if(onResponse) onResponse(resp);
        });
    },

    getGroups: function(divisionId, onResponse) {
        app.doRequest("/api/uz/groups", {
            divId: divisionId
        }, "POST", function(resp) {
            if(onResponse) onResponse(resp);
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
    showCreateEventWindow: function(fillDate, is_edit, data) {

        var _fillDate_start = (fillDate ? moment(fillDate, FULLCALENDAR_INTERNAL_DATE_FORMAT).format(MOMENT_DATE_FORMAT_DAY_TIME) : "");
        var _fillDate_end = (fillDate ? moment(fillDate, FULLCALENDAR_INTERNAL_DATE_FORMAT).add(30, 'minutes').format(MOMENT_DATE_FORMAT_DAY_TIME) : "");


        if(is_edit) {
            _fillDate_start = moment.unix(data.start/1000).format(MOMENT_DATE_FORMAT_DAY_TIME);
            _fillDate_end = moment.unix(data.end/1000).format(MOMENT_DATE_FORMAT_DAY_TIME);
        }


        var template_vars = {
            fillDate_start: _fillDate_start,
            fillDate_end: _fillDate_end
        };

        var data_keys = ["title"];

        for(var i in data_keys) {
            var key = data_keys[i];
            if(data && data[key]) {

                var val = data[key];
                if(!val || val.trim().length < 1) val = "";
                template_vars[key] = val;
            } else {
                template_vars[key] = "";
            }
        }

        var body_template = parse_template("#create_event_template", template_vars);


        var title = is_edit ? "Edycja zdarzenia" : "Dodawanie nowego zdarzenia";
        var button_text = is_edit ? "Zapisz zdarzenie" : "Dodaj zdarzenie";

        app.showWindow({
            title: title,
            body: body_template,
            continueLabel: button_text,
            postShow: function(handle) {

                
                handle.find(".bind-datepicker").datetimepicker({
                    controlType: 'select',
                    oneLine: true,
                    dateFormat: "dd-mm-yy",
                    timeFormat:  "HH:mm",
                    firstDay: 1
                });

                handle.find(".new-event-date-start").change(function() {
                    var t = $(this);
                    var d = t.val();
                    if(d && d.length > 0) {
                        var dObj = moment(d, MOMENT_DATE_FORMAT_DAY_TIME);
                        var endBox = t.parents(".modal").find(".new-event-date-end");
                        if(dObj && endBox.val() == "") {
                            endBox.val(dObj.add(30, "minutes").format(MOMENT_DATE_FORMAT_DAY_TIME));
                        }
                    }
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


                    if(is_edit) {
                        handle.find(".new-event-group").selectpicker('val', data.parent_group_id);
                        handle.find(".new-event-whole-day").prop("checked", (data.allday == true));
                        handle.find(".new-event-public").prop("checked", (data.is_public == true));
                    }
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
                if(is_edit) {
                    app.doSaveEventRequest(data._id, name, group, date_start, date_end, is_wholeday, is_public, function() {
                        app.closeWindow(handle);
                        $("#calendar").fullCalendar( 'refetchEvents' );
                    });
                } else {                    
                    app.doCreateEventRequest(name, group, date_start, date_end, is_wholeday, is_public, function() {
                        app.closeWindow(handle);
                        $("#calendar").fullCalendar( 'refetchEvents' );
                    });
                }
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
    
    doSaveEventRequest: function(eventId, name, group, date_start, date_end, is_wholeday, is_public, onSuccess, onFailure) {
        app.doRequest("/api/events/saveEvent", {
            id: eventId,
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
            app.userEventGroups = resp;
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


    },
    eventChanged: function(event, delta, revertFunc) {
        console.log("app.eventChanged", event, delta);
        var _endDate = event.end ? event.end.format(MOMENT_DATE_FORMAT_DAY_TIME) : "";
        app.doRequest("/api/events/change", {
            "eventId": event.appEventData._id,
            'new_start': event.start.format(MOMENT_DATE_FORMAT_DAY_TIME),
            'new_end': _endDate
        }, "POST", function(resp) {
            console.log(resp);
        });
    },
    setCurrentScreen: function(screen) {
        $(".screen-trigger").removeClass("lm-current");
        $(".screen-trigger[data-screen='"+screen+"']").addClass("lm-current");
        if(screen) {
            $(".page-screen").hide();
            var screenObj = $(".screen-"+screen);
            if(screenObj.hasClass("group-fill-trigger")) {
                var cont = $(screenObj.attr("data-group-fill-container"));
                console.log(cont);

                if(cont) {
                    cont.empty();
                    if(app.userEventGroups.length > 0) {

                        for(var i in app.userEventGroups) {
                            var gr = app.userEventGroups[i];
                            var html = parse_template("#user_groups_export_template", {
                                id: gr.id,
                                color: gr.color,
                                name: gr.name
                            });
                            var elem = $(html).appendTo(cont);
                            if(screenObj.attr("data-no-check") == "1") {
                                elem.find("input[type='checkbox']").prop("checked", false);
                            }
                            if(screenObj.attr("data-one-checkbox-only") == "1") {
                                elem.find("input[type='checkbox']").change(function() {
                                    var t = $(this);
                                    var parent = t.parents(".page-screen");
                                    parent.find("input[type='checkbox']").prop("checked", false);

                                    t.prop("checked", true);
                                });
                            }

                        }
                    } else {
                        cont.html("Nie posiadasz żadnych grup.");
                    }
                }
            }

            screenObj.show();

            app.executeScreenShowEvents(screen);
        }
        app.setArg("screen", screen);


    },
    openContextMenu: function(menuClass, x, y) {
        app.hideContextMenus();

        $(".cmenu-" + menuClass).css({
            left: x + "px",
            top: y + "px"
        }).show();
    },
    hideContextMenus: function() {
        $(".context-menu").hide();
    },
    iCal_doExportRequest: function() {
        var scope = $(".wizard-ical");
        var groupIds = "";
        scope.find(".egs-holder").each(function() {
            var t = $(this);
            if(t.find(".export-group-id").is(":checked")) {
                if(groupIds.length > 0) groupIds += ",";
                groupIds += t.attr("data-group-id");
            }
        });

        var start = scope.find(".export-date-start").val();
        var end = scope.find(".export-date-end").val();

        app._doExportRequest("ical", groupIds, start, end, function(resp) {
            if(parseInt(resp.affected_events) > 0) {                
                app.showConfirmBox({
                    title: "Potwierdź",
                    body: "Znaleziono <b>"+resp.affected_events+"</b> eventów pasujących do podanych kryteriów.<br>Czy chcesz je wyeksportować?",                
                    confirmText: "Tak",
                    confirmClick: function() {
                        app._doExportRequest("ical", groupIds, start, end, function(resp) {
                            console.log('_doExportRequest', resp);
                            if(resp.download_link) {
                                window.location.href = resp.download_link;
                            }
                        }, 1);
                    }
                });
            } else {
                app.showConfirmBox({
                    title: "Błąd",
                    body: "Nie znaleziono eventów dla podanych kryteriów.",                
                    hideCancel: true,
                    confirmText: "OK",
                });
            }
        });
    },
    CSV_doExportRequest: function() {
        var scope = $(".wizard-csv");
        var groupIds = "";
        scope.find(".egs-holder").each(function() {
            var t = $(this);
            if(t.find(".export-group-id").is(":checked")) {
                if(groupIds.length > 0) groupIds += ",";
                groupIds += t.attr("data-group-id");
            }
        });

        var start = scope.find(".export-date-start").val();
        var end = scope.find(".export-date-end").val();

        app._doExportRequest("csv", groupIds, start, end, function(resp) {
            if(parseInt(resp.affected_events) > 0) {                
                app.showConfirmBox({
                    title: "Potwierdź",
                    body: "Znaleziono <b>"+resp.affected_events+"</b> eventów pasujących do podanych kryteriów.<br>Czy chcesz je wyeksportować?",                
                    confirmText: "Tak",
                    confirmClick: function() {
                        app._doExportRequest("csv", groupIds, start, end, function(resp) {
                            console.log('_doExportRequest', resp);
                            if(resp.download_link) {
                                window.location.href = resp.download_link;
                            }
                        }, 1);
                    }
                });
            } else {
                app.showConfirmBox({
                    title: "Błąd",
                    body: "Nie znaleziono eventów dla podanych kryteriów.",                
                    hideCancel: true,
                    confirmText: "OK",
                });
            }
        });
    },


    _doExportRequest: function(exportType, groupIds, start, end, onResponse, confirmation) {
        var _confirmation = (confirmation == 1) ? "yes" : "";
        app.doRequest("/api/events/export", {
            exportType: exportType,
            groupIds: groupIds,
            start: start,
            end: end,
            confirm: _confirmation
        }, "POST", function(resp) {
            if(onResponse) onResponse(resp);
        });
    },


    UZ_doImportRequest: function(confirmation) {
        var scope = $(".wizard-uz-import");
        var groupId = (scope.find(".export-group-id:checked").length > 0) ? scope.find(".export-group-id:checked").first().attr("data-group-id") : -1;

        var uzGroupId = scope.find(".select-grupa").val();


        var _confirmation = (confirmation == 1) ? "yes" : "";
        app.doRequest("/api/uz/import", {
            eventGroupId: groupId,
            uzGroupId: uzGroupId,
            confirm: _confirmation
        }, "POST", function(resp) {
            if(!confirmation) {

                if(parseInt(resp.affected_events) > 0) {                
                    app.showConfirmBox({
                        title: "Potwierdź",
                        body: "Znaleziono <b>"+resp.affected_events+"</b> eventów.<br>Czy chcesz je zaimportować?",
                        confirmText: "Tak",
                        confirmClick: function() {
                            app.UZ_doImportRequest(1);
                        }
                    });
                } else {
                    var msg = resp.message;
                    if(!msg) msg = "Nie znaleziono zdarzeń."; 
                    app.showConfirmBox({
                        title: "Błąd",
                        body: msg,
                        hideCancel: true,
                        confirmText: "OK",
                    });
                }
            } else {
                app.showConfirmBox({
                    title: "Wiadomość",
                    body: resp.message,
                    hideCancel: true,
                    confirmText: "OK",
                });
            }
        });
    },  
    iCal_doImportRequest: function() {
        var scope = $(".wizard-ical-import");
        var groupId = (scope.find(".export-group-id:checked").length > 0) ? scope.find(".export-group-id:checked").first().attr("data-group-id") : -1;

        var fileObj = scope.find("#ical-import-file")[0];

        app._doImportRequest(fileObj, "ical", groupId, function(resp) {
            if(parseInt(resp.affected_events) > 0) {                
                app.showConfirmBox({
                    title: "Potwierdź",
                    body: "Znaleziono <b>"+resp.affected_events+"</b> eventów.<br>Czy chcesz je zaimportować?",
                    confirmText: "Tak",
                    confirmClick: function() {
                        app._doImportRequest(fileObj, "ical", groupId, function(resp) {
                            console.log('iCal_doImportRequest', resp);
                                app.showConfirmBox({
                                    title: "Wiadomość",
                                    body: resp.message,
                                    hideCancel: true,
                                    confirmText: "OK",
                                });
                        }, 1);
                    }
                });
            } else {
                app.showConfirmBox({
                    title: "Błąd",
                    body: "Nie znaleziono zdarzeń w podanym pliku.",                
                    hideCancel: true,
                    confirmText: "OK",
                });
            }
        });
    },

    CSV_doImportRequest: function() {
        var scope = $(".wizard-csv-import");
        var groupId = (scope.find(".export-group-id:checked").length > 0) ? scope.find(".export-group-id:checked").first().attr("data-group-id") : -1;

        var fileObj = scope.find("#csv-import-file")[0];

        app._doImportRequest(fileObj, "csv", groupId, function(resp) {
            if(parseInt(resp.affected_events) > 0) {                
                app.showConfirmBox({
                    title: "Potwierdź",
                    body: "Znaleziono <b>"+resp.affected_events+"</b> eventów.<br>Czy chcesz je zaimportować?",
                    confirmText: "Tak",
                    confirmClick: function() {
                        app._doImportRequest(fileObj, "csv", groupId, function(resp) {
                            console.log('CSV_doImportRequest', resp);
                                app.showConfirmBox({
                                    title: "Wiadomość",
                                    body: resp.message,
                                    hideCancel: true,
                                    confirmText: "OK",
                                });
                        }, 1);
                    }
                });
            } else {
                app.showConfirmBox({
                    title: "Błąd",
                    body: "Nie znaleziono zdarzeń w podanym pliku.",                
                    hideCancel: true,
                    confirmText: "OK",
                });
            }
        });
    },

    _doImportRequest: function(fileObj, importType, groupId, onResponse, confirmation) {
        
        var formData = new FormData();
        //var progressBar = parent.find(".db-progress-bar");
        if(fileObj) formData.append("import_file", fileObj.files[0]);
        formData.append("groupId", groupId);
        formData.append("importType", importType);

        if(confirmation == 1) {
            formData.append("confirm", "yes");
        }

        var endpoint = "/api/events/import";

        if(importType == "uz") endpoint = "/api/uz/import";

        $.ajax({
            url: endpoint,
            xhr: function() {

                var xhr = new XMLHttpRequest();
                xhr.upload.addEventListener("progress", function(evt) {
                    var loaded = (evt.loaded / evt.total).toFixed(2) * 100;
                    //progressBar.css("width", loaded + "%").show();
                }, false);

                return xhr;
            },
            type: 'post',
            processData: false,
            contentType: false,
            data: formData,
            success: function(data) {
                console.log(data);

                setTimeout(function() {
                    //parent.find(".db-progress-bar").fadeOut();
                }, 5);

                if (onResponse) onResponse(data);
            }
        });


    },

    editEvent: function(id) {
        var events = $('#calendar').fullCalendar('clientEvents');
        if(events) {
            for(var i in events) {
                if(events[i].appEventData && events[i].appEventData._id == id) {
                    app.showCreateEventWindow(false, true, events[i].appEventData);
                    break;
                }
            }
        }
    },
    deleteEvent: function(id, onResponse) {
        app.showConfirmBox({
            title: "Potwierdź",
            body: "Czy na pewno chcesz usunąć to zdarzenie?",                
            confirmText: "Tak",
            confirmClick: function() {

                app._doDeleteEvent(id, function(resp) {
                    var type = (resp.type == "success") ? "Sukces" : "Błąd";

                    app.showConfirmBox({
                        title: type,
                        body: resp.message,
                        confirmText: "Tak"
                    });

                    if(resp.type == "success") {
                        if(calendarRef) {
                            calendarRef.fullCalendar("refetchEvents");
                        }
                    }
                });
            }
        });
    },
    _doDeleteEvent: function(id, onResponse) {
        app.doRequest("/api/events/delete", {
            id: id
        }, "POST", function(resp) {
            if(onResponse) onResponse(resp);
        })
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
    
    $(".btn-export-ical").click(function() {
        app.iCal_doExportRequest();
    });
    $(".btn-export-csv").click(function() {
        app.CSV_doExportRequest();
    });
    $(".btn-import-csv").click(function() {
        app.CSV_doImportRequest();
    });

    $(".btn-import-ical").click(function() {
        app.iCal_doImportRequest();
    });
    $(".btn-import-uz").click(function() {
        app.UZ_doImportRequest();
    });




    $(".menu-action-create-event").click(function() {

        app.showCreateEventWindow(LAST_RIGHTCLICKED_DATE);
    });
    $(".menu-action-edit").click(function() {
        if(LAST_RIGHTCLICKED_EVENT) {
            var id = LAST_RIGHTCLICKED_EVENT.attr("data-event-id");
            app.editEvent(id);
        }
    });
    $(".menu-action-delete").click(function() {
        if(LAST_RIGHTCLICKED_EVENT) {
            var id = LAST_RIGHTCLICKED_EVENT.attr("data-event-id");
            app.deleteEvent(id);
        }
    });



    $("body").on("click", function(e) {
        app.hideContextMenus();
    });
    $("body").on("contextmenu", ".fc-day-grid-event", function(e) {
        console.log(e);

        LAST_RIGHTCLICKED_EVENT = $(this);

        app.openContextMenu("event", e.pageX, e.pageY);

        if(!e.ctrlKey) return false;
    });
    
    $("body").on("contextmenu", ".fc-day", function(e) {
        console.log(e);


        LAST_RIGHTCLICKED_DATE = $(this).attr("data-date");

        app.openContextMenu("cell", e.pageX, e.pageY);

        if(!e.ctrlKey) return false;
    });
    

    $("body").on("contextmenu", ".fc-content-skeleton tbody tr > td", function(e) {
        console.log(e);
        var index = $(this).index();
        console.log(index);

        var parent = $(this).parents(".fc-content-skeleton");

        var td = parent.find("thead tr td").eq(index);
        console.log(td.attr("data-date"));

        LAST_RIGHTCLICKED_DATE = td.attr("data-date");

        app.openContextMenu("cell", e.pageX, e.pageY);

        if(!e.ctrlKey) return false;
    });





    $(".screen-trigger").click(function() {
        var btn = $(this);
        var screen = btn.attr("data-screen");
        app.setCurrentScreen(screen);
    });

	app.init();
});