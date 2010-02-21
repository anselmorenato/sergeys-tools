<?php
  ini_set("include_path", ini_get("include_path") . (substr($_SERVER["DOCUMENT_ROOT"],0,1) != "/" ? ";" : ":") . $_SERVER["DOCUMENT_ROOT"] . "/include");
  require_once("settings.inc.php");
  $page_title .= " - test";
  require_once("header.inc.php");
?>
<a href="index.php">home</a>
<br/>
<br/>
<?php /*
<a href="pinfo.php">pinfo</a>
<br/>
<br/>
*/?>
<?php
    foreach($_COOKIE as $cookie){    	
        print("cookie:<br/>");
        print_r($cookie);
        print("<br/>");
    };
        
    if(isset($_SERVER["HTTP_REFERER"])){
    	print("<br/>");
    	print("<br/>");
    	print("referer:<br/>");
    	print_r($_SERVER["HTTP_REFERER"]);
    }
?>


<?php require_once("footer.inc.php");?>