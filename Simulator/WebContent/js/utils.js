/**
 * 
 */
/*function secToDate(seconds) {
	var hour = seconds / 3600;
	seconds = seconds - hour * 3600;
	var min = seconds / 60;
	seconds = seconds - min * 60;
	var sec = seconds;
	return new Date(2016, 3, 30, hour, min, sec);
}*/

function secToDate(seconds) {
	var hour = Math.floor( (seconds/(60*60)) % 24 );
	var min = Math.floor( (seconds/60) % 60 );
	var sec = Math.floor( seconds % 60 );
	return new Date(1970, 0, 1, hour, min, sec);
}
