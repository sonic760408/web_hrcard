function display(url, str, kind,defaultValue) {
var eCustom = document.getElementById('custom');
var eMsg = document.getElementById('msg');
var eErrorMsg = document.getElementById('echo_msg');
if (str == 'none') {
  if(eErrorMsg)
      eErrorMsg.innerHTML = '';
  if(eCustom)
      eCustom.style.display = "none";
  document.getElementById('level').style.display = "none";
} else if (str == 'custom') {
  if(eCustom)
      eCustom.style.display = "inline";
  document.getElementById('level').style.display = "inline";
  if(eErrorMsg)
      eErrorMsg.innerHTML = '';
} else {
  if(eCustom)
      eCustom.style.display = "none";
  document.getElementById('level').style.display = "inline";
  if(eMsg)
      eMsg.style.display = "inline";
  if(eErrorMsg)
      eErrorMsg.innerHTML = '';

  // insert AJAX code here
  var http_request = false;
  if(window.XMLHttpRequest) { // Mozilla, Safari, ....
http_request = new XMLHttpRequest();
  } else if(window.ActiveXObject) { // IE
try {
  http_request = new ActiveXObject("Msxml2.XMLHTTP");
} catch (e) {
  try {
	http_request = new ActiveXObject("Microsoft.XMLHTTP");
  } catch (e) {}
}
  }

  if (!http_request) {
	alert('Giving up :( Cannot create an XMLHTTP instance');
	return false;
  }
  // 定義事件處理函數為 alterContents()
  http_request.onreadystatechange = function() { 
									alertContents(http_request); };

  http_request.open('GET', url + '?grpno=' + str + '&kind=' + kind + '&idno='+defaultValue, true);
  http_request.send(null);
}
}

function alertContents(http_request) {
if (http_request.readyState == 4) {
  if (http_request.status == 200) { 
        var eMsg = document.getElementById('msg');
	var eErrorMsg = document.getElementById('echo_msg');
	if(eMsg)
	  eMsg.style.display = "none";
	if(eErrorMsg)
	  eErrorMsg.innerHTML = http_request.responseText;
  } else {
	alert('There was a problem with the request.');
  }
}
}
