<?php
  ini_set("include_path", ini_get("include_path") . (substr($_SERVER["DOCUMENT_ROOT"],0,1) != "/" ? ";" : ":") . $_SERVER["DOCUMENT_ROOT"] . "/include");
  require_once("settings.inc.php");
  $page_title .= " - рекомендуемый софт";
  require_once("header.inc.php");
?>
<a href="index.php">home</a>
<br/>
<br/>

InfraRecorder, ImgBurn
<br/>
BlogBridge
<br/>
JDiskReport
<br/>
CCleaner
<br/>
Defraggler, JkDefrag/MyDefrag
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
Firefox, OpenOffice, VLC media player
<br/>
Thunderbird + Lightning (Google, Yahoo calendars)
<br/>
Gajim (ICQ, Messenger gateways, GoogleTalk)
<br/>
Easy Duplicate Finder
<br/>
MetarWeather, gismeteo
<br/>

<?php require_once("footer.inc.php");?>
