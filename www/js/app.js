var wWidth, wHeight;

var app = {
	init: function() {
		
		app.updateUI();
	},
	updateUI: function() {

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
};

$(window).resize(function() {
	wWidth = $(window).width();
	wHeight = $(window).height();

	app.updateUI();
});

$(document).ready(function() {
	wWidth = $(window).width();
	wHeight = $(window).height();

	app.init();
});