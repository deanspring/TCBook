(function($) {

	var mainContainer = $('body > .container');

	$(document).ready(function() {
		$('.navbar a, [role="main"] .jumbotron a').click(function () {
			if ($(this).data('toggle') || $(this).attr('href').substring(1) === mainContainer.find('.active').attr('id'))
				return;

			mainContainer.find('.active').slideUp().removeClass('active');
			if ($(this).attr('href') == '#main')
				mainContainer.find('#main').slideDown().addClass('active');
		});

		$('a[href="#pessoas"]').click(function() {
			people.load();
		});

		$('a[href="#estatisticas"]').click(function() {
			statistics.load();
		});
	});

	$.loadTemplate = function(url, selector, context, callback) {
		$.ajax({
			type : 'GET',
			url : url,
			data : 'text',
			success : function(template, status, jqXHR) {
				$(selector).html(doT.template(jqXHR.responseText)(context));

				if (typeof callback === 'function')
					callback(context);
			}
		});
	};

	people = {};

	people.load = function() {
		$.getJSON('s/people/', function(people) {
			$.loadTemplate('templates/people/peopleList.xml', '[role="content"]', {people : people}, function() {
				$('[role="content"]').slideDown().addClass('active');
			});
		});
	};
})(window.jQuery);
