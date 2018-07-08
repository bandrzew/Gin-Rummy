$(function() {
	var playerHand = $('.hand').find('a');

	function setHint() {
		var hint = 'wybierz karte do odrzucenia lub '.concat(
				'<button onclick="location.href=',
				"'http://localhost:8080/Gin-Rummy'",
				'"type="button">zastukaj</button>');
		$('#hint').html(hint);
	}

	$("#discard").on(
			'click',
			function(event) {
				setHint();
				playerHand.each(function(index, element) {
					var id = element.getAttribute('id');
					element.setAttribute('href',
							'http://localhost:8080/Gin-Rummy/discard/' + id);
				})
			});

	$("#stock").on(
			'click',
			function(event) {
				setHint();
				playerHand.each(function(index, element) {
					var id = element.getAttribute('id');
					element.setAttribute('href',
							'http://localhost:8080/Gin-Rummy/stock/' + id);
				})
			});
});