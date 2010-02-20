function onResize() {
	var iHeight = document.documentElement.clientHeight;
    var headerDiv = document.getElementById("header");
    var footerDiv = document.getElementById("footer");
    var contentDiv = document.getElementById("main");
    //    status = ("Header = " + headerDiv.offsetHeight +" & Footer = " + footerDiv.offsetHeight);
    try {
        iHeight = iHeight - headerDiv.offsetHeight - footerDiv.offsetHeight;
        if (iHeight > 0) {
            contentDiv.style.minHeight = iHeight + "px";
        }
    }
    catch (ex) {
        //do nothing
    }
}