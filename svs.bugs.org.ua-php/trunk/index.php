<?php
  ini_set("include_path", ini_get("include_path") . (substr($_SERVER["DOCUMENT_ROOT"],0,1) != "/" ? ";" : ":") . $_SERVER["DOCUMENT_ROOT"] . "/include");
  require_once("settings.inc.php");
  require_once("header.inc.php");
?>
Привет.
<br/>
<br/>

<img src="images/userinfo.gif"><a href="http://baccara.livejournal.com">baccara</a>
<br/>
<br/>

<!-- <a href="useful-software.php">рекомендуемый софт</a> -->
<!-- <br/> -->
<!-- <br/> -->

<!-- <a href="jws/index.php">webstart applications</a> -->
<!-- <br/> -->
<!-- <br/> -->

<h2>Introduction</h2>
<br>
<p>
WebCacheDigger: ищет и сохраняет музыку, видео, картинки и прочее из браузеров.
<p>
Работает на Windows XP, 7, Linux, MacOS X.
<p>
Браузеры: Firefox, Chrome, Opera везде и Internet Explorer на Windows.
<p>
Требования: Java 1.6 и выше, 32 или 64 бит - все равно.
<p>
<br>

Запустить:
<script src="http://www.java.com/js/deployJava.js"></script>
    <script>
        // using JavaScript to get location of JNLP file relative to HTML page
        var dir = location.href.substring(0, location.href.lastIndexOf('/')+1);
        var url = dir + "wcd/wcd.jnlp";
        deployJava.createWebStartLaunchButton(url, '1.6.0');
    </script>
<br>
<br>
Если оранжевая кнопка не срабатывает, тогда делаем по-другому.
Если не уверены, что java установлена, сходите на <a href="http://java.com">http://java.com</a> и проверьте:
<p>
<a href="http://java.com/en/download/installed.jsp?detect=jre&try=1">Проверить</a>
<p>
оттуда же её и установите, если что.
<p>
<br/>
<br/>

<h2>Run</h2>
<br>
<p>
Если java установлена, запускайте и пользуйтесь:
<p>
<a href="wcd/wcd.jnlp">WebCacheDigger</a>
<p>
<br/>
<br/>

<h3>Problem?</h3>
<br>
<p>
Вопросы можно пока задавать там:
<p>
<a href="http://baccara.livejournal.com/389485.html">http://baccara.livejournal.com/389485.html</a>


<?php /**/ ?>
<?php /*

<a href="pinfo.php">php info</a>


<a href="test.php">test</a>
<br/>
<br/>

*/ ?>
<?php require_once("footer.inc.php");?>
