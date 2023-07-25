$('#sidebarCollapse').on('click', function () {
    $('#sidebar').toggleClass('active');
    
    if($('#sidebar').hasClass('active')) {
		$('.overlay').fadeIn();
	} else {
		$('.overlay').fadeOut();
	}
});

function sidebarHide() {
	$('#sidebar').removeClass('active');
	$('.overlay').fadeOut();
}