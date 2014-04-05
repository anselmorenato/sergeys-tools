rem
rem Copy missing new entries from eclipse-maintained messages to human-editable russian temp file
rem One need to edit and translate that txt file, then run mkres.cmd to produce encoded file for application.
rem

cd

d:
cd \workspace\wcd2\src\main\resources\resources\lang

java ^
-classpath d:\workspace\wcd2\bin ^
org.sergeys.library.app.ComparePropertyFiles ^
-edit messages.properties messages_ru.properties.txt

pause
