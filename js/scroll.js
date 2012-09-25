function isTouchDevice(){
	try{
		document.createEvent("TouchEvent");
		return true;
	}catch(e){
		return false;
	}
}

$(function(){
	if(isTouchDevice()){
		var startx = 0;

		$(".highlight").bind("touchstart", function(event){
			event.preventDefault();	
			startx = this.scrollLeft+event.originalEvent.touches[0].pageX;
		});

		$(".highlight").bind("touchmove", function(event){
			event.originalEvent.preventDefault();
			this.scrollLeft = startx - event.originalEvent.touches[0].pageX;
		});
	}
});

	
