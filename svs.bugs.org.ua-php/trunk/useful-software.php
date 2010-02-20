<?php
  ini_set("include_path", ini_get("include_path") . (substr($_SERVER["DOCUMENT_ROOT"],0,1) != "/" ? ";" : ":") . $_SERVER["DOCUMENT_ROOT"] . "/include");
  require_once("settings.inc.php");
  $page_title .= " - рекомендуемый софт";
  require_once("header.inc.php");
?>
<a href="../index.php">home</a>
<br/>
<br/>
<!--<a href="app/webcachedigger.jnlp" onclick="alert('Not yet.'); return false;">WebCacheDigger</a>-->

InfraRecorder
<br/>
BlogBridge
<br/>
JDiskReport
<br/>
CCleaner
<br/>
Defraggler
<br/>
BOINC
<br/>
Future Pinball
<br/>
7-Zip
<br/>
Avira AntiVir
<br/>
Free Download Manager
<br/>
LastPass
<br/>
Privoxy
<br/>
Firefox, Thunderbird, OpenOffice, VLC
<br/>

<?php require_once("footer.inc.php");?>
