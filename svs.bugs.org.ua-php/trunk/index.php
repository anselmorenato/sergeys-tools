<?php
  ini_set("include_path", ini_get("include_path") . (substr($_SERVER["DOCUMENT_ROOT"],0,1) != "/" ? ";" : ":") . $_SERVER["DOCUMENT_ROOT"] . "/include");
  require_once("settings.inc.php");
  require_once("header.inc.php");
?>
Привет.
<br/>
<br/>
<img src="images/userinfo.gif"><a href="http://baccara.livejournal.com"></img>baccara</a>
<br/>
<br/>
<a href="useful-software.php">рекомендуемый софт</a>
<br/>
<br/>
<a href="jws/index.php">webstart applications</a>
<br/>
<br/>
<?php /**/ ?>
<?php /*

<a href="pinfo.php">php info</a>
*/ ?>

<a href="test.php">test</a>
<br/>
<br/>


<?php require_once("footer.inc.php");?>
