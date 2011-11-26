<?php
  ini_set("include_path", ini_get("include_path") . (substr($_SERVER["DOCUMENT_ROOT"],0,1) != "/" ? ";" : ":") . $_SERVER["DOCUMENT_ROOT"] . "/include");
  require_once("settings.inc.php");
  require_once("header.inc.php");
?>
Привет.
<br/>
<br/>

<a href="http://baccara.livejournal.com/profile">
<img src="images/userinfo.gif">
</a>
<a href="http://baccara.livejournal.com">baccara</a>
<br/>
<br/>

<h2>Web Cache Digger</h2>
<br>
<p>
Ищет и сохраняет музыку, видео, картинки и прочее из браузеров.
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
<br>
<h2>Cover Finder</h2>
<br>
<p>
Простой редактор тегов для аудио файлов. Редактирует название, альбом, артиста. 
Ищет и вставляет картинку обложки диска. 
Распознает неопознанную музыку.
<p>
Внимание: при добавлении объемных каталогов с музыкой, по нескольку тысяч файлов,
будет сканировать файлы несколько минут. Наберитесь терпения.
<p>
Работает на Windows XP, 7, Linux, MacOS X.

<p>
Требования: Java 1.6 и выше, 32 или 64 бит.
<p>
<br>

Запустить:
<script src="http://www.java.com/js/deployJava.js"></script>
    <script>
        // using JavaScript to get location of JNLP file relative to HTML page
        var dir = location.href.substring(0, location.href.lastIndexOf('/')+1);
        var url = dir + "cf/cf.jnlp";
        deployJava.createWebStartLaunchButton(url, '1.6.0');
    </script>
<br>
<br>
<br>

<h2>Another way</h2>
<br>
Если оранжевая кнопка почему-то не сработает, тогда делаем по-другому.
Если не уверены, что java установлена, сходите на <a href="http://java.com">http://java.com</a> и проверьте:
<p>
<a href="http://java.com/en/download/installed.jsp?detect=jre&try=1">Проверить</a>
<p>
оттуда же её и установите, если что.
<p>
<br/>
Если java установлена, запускайте приложения через прямые ссылки:
<p>
<br/>
<a href="wcd/wcd.jnlp">WebCacheDigger</a>
<br/>
<br/>
<p>
<a href="cf/cf.jnlp">CoverFinder</a>
<br/>
<p>


<br/>
<br>
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
