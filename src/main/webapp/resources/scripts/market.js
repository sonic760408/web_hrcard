var ajax;
function createHttpRequest() {
    if (window.ActiveXObject) {
        try {
            return new ActiveXObject("Msxml2.XMLHTTP");
        } catch (e) {
            try {
                return new ActiveXObject("Microsoft.XMLHTTP");
            } catch (e2) {
                return null;
            }
        }
    } else if (window.XMLHttpRequest) {
        return new XMLHttpRequest();
    } else {
        return null;
    }
}
function ajaxSendRequest(url) {
    var un = document.getElementById('un').value;
    document.getElementById('msg').style.display = "inline";
    document.getElementById('check').style.display = "none";
    ajax = createHttpRequest();
    ajax.onreadystatechange = showResult;
    ajax.open("GET", url + '?pdcode=' + un);
    ajax.send("");
}
function showResult() {
    if (ajax.readyState == 4 && ajax.status == 200) {
        document.getElementById('msg').style.display = "none";
        document.getElementById('check').style.display = "none";
		//document.getElementById('check').disabled = true;
	    document.getElementById('echo_msg').innerHTML = ajax.responseText;
    }
}
function clearmsg() {
	document.getElementById('echo_msg').innerHTML = '';
	document.getElementById('check').style.display = "inline";
	//document.getElementById('check').disabled = false;
}
