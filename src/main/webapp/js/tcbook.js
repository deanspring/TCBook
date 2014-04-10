(function($) {

	var main = $('body > .container > .row[role="main"]');

	$(document).ready(function() {
		$('a[href="#pessoas"]').click(function() {
			main.find('.active').slideUp().removeClass('active');
			main.find('.pessoas').slideDown().addClass('active');
		});
	});

})(window.jQuery);
