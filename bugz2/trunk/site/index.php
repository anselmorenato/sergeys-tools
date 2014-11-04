<?php
  ini_set("include_path", ini_get("include_path") . (substr($_SERVER["DOCUMENT_ROOT"],0,1) != "/" ? ";" : ":") . $_SERVER["DOCUMENT_ROOT"] . "/include");
  require_once("settings.inc.php");
  require_once("header.inc.php");
?>

<script src="http://www.java.com/js/deployJava.js"></script>

Привет.
<br/>
<br/>

<a href="http://baccara.livejournal.com/profile">
<img src="images/userinfo.gif">
</a>
<a href="http://baccara.livejournal.com">baccara</a>

<br/>
<br/>
<br/>

<!-- ---------------------------- -->
<h2>FreeMind 1.0.1</h2>
<br>
<p>
FreeMind 1.0.1 repacked for Webstart.
<p>
This webstart app registers association with *.mm files, so it will launch FreeMind
and open file if you'll double-click on .mm file.
<p>
Here, FreeMind files are repacked from original binary distribution, not recompiled. I added the launcher.
Note that all files are signed with self-signed generated certificate,
so you will need to enable certain security settings. Or just buy me real certificate :).
<p>
Home page: <a href="http://freemind.sourceforge.net">http://freemind.sourceforge.net</a>
<p>

<br>

<script>
    var dir = location.href.substring(0, location.href.lastIndexOf('/')+1);
    var url = dir + "freemind/freemind.jnlp";
    deployJava.createWebStartLaunchButton(url, '1.6.0');
</script>
<br/>

<div class="note">
(<a href='freemind/freemind.jnlp'>direct link</a>)
</div>

<br/>
<br/>
<br/>


<!-- ---------------------------- -->
<h2>Gallery Publisher</h2>
<br>
<p>
Обработчик картинок для <a href="http://russos.livejournal.com/991863.html">http://russos.livejournal.com/991863.html</a>
<p>
Работает на Windows, Linux, MacOS X, везде, где есть Java SE 1.6.
<p>
Требования: Java 1.6 и выше, 32 или 64 бит.
<p>
<br>

<script>
    var dir = location.href.substring(0, location.href.lastIndexOf('/')+1);
    var url = dir + "gp/gallerypublisher.jnlp";
    deployJava.createWebStartLaunchButton(url, '1.6.0');
</script>
<br/>

<div class="note">
(<a href='gp/gallerypublisher.jnlp'>прямая ссылка</a>)
</div>

<br/>
<br/>
<br/>


<!--  img src="cookbook/largeicon.png" -->
<h2>Cook Book</h2>
<br>
<p>
Сборник рецептов. Можно импортировать сохраненные веб-страницы,
расставлять тэги типа там "супы", "овощи", "курятина", "favorites" и потом
удобно всё это просматривать.
<p>
Работает на Windows, Linux, MacOS X.
<p>
Требования: Java 1.7 (rev. 06) и выше, 32 или 64 бит.

<p>
<br>

<script>
    var dir = location.href.substring(0, location.href.lastIndexOf('/')+1);
    var url = dir + "cookbook/cookbook.jnlp";
    deployJava.createWebStartLaunchButton(url, '1.7.0');
</script>
<br/>

<div class="note">
(<a href='cookbook/cookbook.jnlp'>прямая ссылка</a>)
</div>

<br/>
<br/>
<br/>

<h2>Web Cache Digger</h2>
<br>
<p>
Ищет и сохраняет музыку, видео, картинки и прочее из кэшей браузеров.
<p>
Отлично, например, сохраняются MP3 файлы после прослушивания музыки во вконтактике, постоянно пользуюсь.<br/>
Как правило, после слушания музыки или просмотра роликов на любом сайте, если сайт отдавал это как файл,<br/>
то оно уже на вашей машине в кэшах, не надо никаких даунлоадеров и плагинов в браузер. Ну, не всегда, конечно.
<p>
Работает на Windows, Linux, MacOS X.
<p>
Браузеры: Firefox, Chrome, Opera везде и Internet Explorer на Windows.
<p>
Требования: Java 1.6 и выше, 32 или 64 бит.
<p>
<br>

<script>
    // using JavaScript to get location of JNLP file relative to HTML page
    var dir = location.href.substring(0, location.href.lastIndexOf('/')+1);
    var url = dir + "wcd2/wcd2.jnlp";
    deployJava.createWebStartLaunchButton(url, '1.6.0');
</script>
<br>

<div class="note">
(<a href='wcd2/wcd2.jnlp'>прямая ссылка</a>) (<a href='wcd/wcd.jnlp'>старая версия</a>)
</div>

<br/>
<br/>
<br/>

<h2>Cover Finder</h2>
<br>
<p>
Простой редактор тегов для аудио файлов. Редактирует название, альбом, артиста.<br/>
Ищет и вставляет в мп3 файл картинку обложки диска из Google Images и Bing.<br/>
Распознает неопознанную музыку через <a href="http://acoustid.org">acoustid.org</a>
<p>
Внимание: при добавлении объемных каталогов с музыкой, по нескольку тысяч файлов,<br/>
будет сканировать файлы несколько минут. Наберитесь терпения.
<p>
Работает на Windows, Linux, MacOS X.
<p>
Требования: Java 1.6 и выше, 32 или 64 бит.
<p>

<br/>

<script>
    // using JavaScript to get location of JNLP file relative to HTML page
    var dir = location.href.substring(0, location.href.lastIndexOf('/')+1);
    var url = dir + "cf/cf.jnlp";
    deployJava.createWebStartLaunchButton(url, '1.6.0');
</script>
<br>
<div class="note">
(<a href='cf/cf.jnlp'>прямая ссылка</a>)
</div>

<br/>
<br/>
<br/>

<h2>Another way</h2>
<br>
Если оранжевая кнопка почему-то не будет срабатывать (отключен JavaSript, например), тогда делаем по-другому.
<p>
Если не уверены, что java установлена, сходите на <a href="http://java.com">java.com</a> и проверьте:
<p>
<br/>
<a href="http://java.com/en/download/installed.jsp?detect=jre&try=1">Проверить</a>
<br/>
<br/>
<p>
оттуда же её и установите, если что.
<p>
<br/>
После установки попробуйте оранжевые кнопочки снова. Если опять что-то не так, запускайте приложения через прямые ссылки.
<p>
Если браузер будет спрашивать, чем открывать файлы типа .jnlp, нужно найти программку по имени javaws<br/>
из комплекта Java (Java Runtime Environment, JRE).
<p>
Для Windows это будет где-то в районе C:\Program Files\Java\jre7\bin\javaws.exe.
<p>
<br/>
Если в дальнейшем не предполагаете регулярно пользоваться поддержкой Java в браузере, рекомендую выключить Java-плагин<br/>
в настройках браузера для безопасности. Относится, кстати, ко всем плагинам, рекомендую отключать всё неиспользуемое.<br/>
Уязвимости есть всегда и везде.
<p>


<br/>
<br/>
<br/>

<h3>Problem?</h3>
<br>
<p>
Вопросы можно пока задавать там:
<p>
<a href="http://baccara.livejournal.com/389485.html">http://baccara.livejournal.com/389485.html</a>


<br/>
<br/>
<br/>

<p>
<div class="gray12">
<pre>
$LastChangedDate$
</pre>
</div>
<?php require_once("footer.inc.php");?>
