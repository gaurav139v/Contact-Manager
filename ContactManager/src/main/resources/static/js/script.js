console.log("This is script file");

const toggleSidebar = () => {
	if($('.sidebar').is(":visible")){
		$(".sidebar").css("display", "none");
		$(".content").css("margin-left", "0%");
		//$(".content").css("width", "100%");
	} else {
		$(".sidebar").css("display", "block");
		$(".content").css("margin-left", "18%");
		//$(".content").css("width", "82%");
	}
};