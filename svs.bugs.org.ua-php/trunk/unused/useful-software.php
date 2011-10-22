<?php
  ini_set("include_path", ini_get("include_path") . (substr($_SERVER["DOCUMENT_ROOT"],0,1) != "/" ? ";" : ":") . $_SERVER["DOCUMENT_ROOT"] . "/include");
  require_once("settings.inc.php");
  $page_title .= " - рекомендуемый софт";
  require_once("header.inc.php");
?>
<script language="javascript" type="text/javascript">
  function openPopup(url){
	  window.open(url);
  };  
</script>

<a href="index.php">home</a>

<div class="section">
<p>
Программы, которыми я пользуюсь и могу рекомендовать.  
Всё работает на Windows 7 x64.
</p>
</div>

<div style="margin-left: 20px;">

<div class="sectiontitle">7-Zip</div>
<div class="section">
<img src="images/apps/7zip.png" class="app-thumbnail" align="left" onclick="javascript:openPopup('images/apps/7zip.png');"/>
<p>
ZIP-совместимый архиватор, множество других форматов. x64 
</p>
</div>
<div class="sectionfooter"><a href="http://www.7-zip.org/">www.7-zip.org</a></div>

<div class="separator">&nbsp;</div>

<div class="sectiontitle">InfraRecorder</div>
<div class="section">
<img src="images/apps/infrarecorder.png" class="app-thumbnail" align="left" onclick="javascript:openPopup('images/apps/infrarecorder.png');"/>
<p>
Бесплатный аналог Nero Burning Rom. x64 
</p>
<p>
Единственный недостаток &ndash; из готовых образов умеет прожигать только CD, не DVD. Как альтернативу можно попробовать 
<a href="http://www.imgburn.com/">ImgBurn</a>. 
</p>
</div>
<div class="sectionfooter"><a href="http://infrarecorder.org/">infrarecorder.org</a></div>
<div class="separator">&nbsp;</div>

<div class="sectiontitle">BlogBridge</div>
<div class="section">
<img src="images/apps/blogbridge.png" class="app-thumbnail" align="left" onclick="javascript:openPopup('images/apps/blogbridge.png');"/>
<p>
RSS-читалка. Java Web Start. 
</p>
</div>
<div class="sectionfooter"><a href="http://www.blogbridge.com/">www.blogbridge.com</a></div>
<div class="separator">&nbsp;</div>



<div class="sectiontitle">JDiskReport</div>
<div class="section">
<img src="images/apps/jdiskreport.png" class="app-thumbnail" align="left" onclick="javascript:openPopup('images/apps/jdiskreport.png');"/>
<p>
Подсчитывает статистику по размерам, датам и типам файлов, крайне полезен при чистке диска. Java Web Start. 
</p>
</div>
<div class="sectionfooter"><a href="http://jgoodies.com/freeware/jdiskreport/">jgoodies.com/freeware/jdiskreport</a></div>
<div class="separator">&nbsp;</div>





</div>



TODO:
<br/>
<br/>

CCleaner
<br/>
Defraggler, JkDefrag/MyDefrag
<br/>
BOINC
<br/>
Future Pinball

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
MetarWeather, gismeteo rss
<br/>

<?php require_once("footer.inc.php");?>
