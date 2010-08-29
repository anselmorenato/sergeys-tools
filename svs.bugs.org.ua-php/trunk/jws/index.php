<?php
  ini_set("include_path", ini_get("include_path") . (substr($_SERVER["DOCUMENT_ROOT"],0,1) != "/" ? ";" : ":") . $_SERVER["DOCUMENT_ROOT"] . "/include");
  require_once("settings.inc.php");
  $page_title .= " - Java Web Start applications";
  require_once("header.inc.php");
?>
<a href="../index.php">home</a>
<br/>
<br/>
<!--<a href="app/webcachedigger.jnlp" onclick="alert('Not yet.'); return false;">WebCacheDigger</a>-->
<a href="app/webcachedigger.jnlp">WebCacheDigger</a>

<?php require_once("footer.inc.php");?>
