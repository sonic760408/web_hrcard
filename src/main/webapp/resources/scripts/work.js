function display(id, kind) {
//alert(id +":"+ kind);
  if (kind == 'D' || kind == 'L' || kind == 'N' || kind == 'S') {
    document.getElementById(id + '_time1').style.display = "inline";
    document.getElementById(id + '_time2').style.display = "none";
    document.getElementById(id + '_total').style.display = "inline";   
    document.getElementById(id + '_amm').style.display = "inline";
    document.getElementById(id + '_pmm').style.display = "inline";
	}
  if (kind == 'OF') {
    document.getElementById(id + '_time1').style.display = "none";
    document.getElementById(id + '_time2').style.display = "none";
    document.getElementById(id + '_total').style.display = "inline"; 
    document.getElementById(id + '_amm').style.display = "none";
    document.getElementById(id + '_pmm').style.display = "none";
	}
  if (kind == 'OM' || kind == 'OB' || kind == 'OG' || kind == 'OC' || kind == 'OP') {
    document.getElementById(id + '_time1').style.display = "none";
    document.getElementById(id + '_time2').style.display = "none";
    document.getElementById(id + '_total').style.display = "none";   
    document.getElementById(id + '_amm').style.display = "none";
    document.getElementById(id + '_pmm').style.display = "none";
	}
  if (kind == 'OT') {
    document.getElementById(id + '_time1').style.display = "none";
    document.getElementById(id + '_time2').style.display = "none";
    document.getElementById(id + '_total').style.display = "none";   
    document.getElementById(id + '_amm').style.display = "none";
    document.getElementById(id + '_pmm').style.display = "none";
	}
  if (kind == 'T' || kind == 'OS') {
    document.getElementById(id + '_time1').style.display = "inline";
    document.getElementById(id + '_time2').style.display = "inline";
    document.getElementById(id + '_total').style.display = "inline";   
    document.getElementById(id + '_amm').style.display = "inline";
    document.getElementById(id + '_pmm').style.display = "inline";
	}
}