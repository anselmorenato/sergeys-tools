<?php
  ini_set("include_path", ini_get("include_path") . (substr($_SERVER["DOCUMENT_ROOT"],0,1) != "/" ? ";" : ":") . $_SERVER["DOCUMENT_ROOT"] . "/include");
  require_once("settings.inc.php");
  require_once("header.inc.php");
?>

<a href="index.php">home</a>
<br/>
<br/>
<?php
    phpinfo();
?>

<?php require_once("footer.inc.php");?>