(function($) {

	statistics = {};

	statistics.load = function() {
		$.getJSON('s/statistics/all', function(statistics) {
			$.loadTemplate('templates/statistics/statisticsAll.xml', '[role="content"]', statistics, function() {
				$('[role="content"]').slideDown().addClass('active');
			});
		});
	};

})(window.jQuery);
