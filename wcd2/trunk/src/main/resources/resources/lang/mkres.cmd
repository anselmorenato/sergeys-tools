rem
rem Generate localized messages for app from human-edited translated file
rem

cd

d:
cd \workspace\wcd2\src\main\resources\resources\lang

copy messages.properties messages_en.properties
"%JDK_HOME%\bin\native2ascii" -encoding windows-1251 messages_ru.properties.txt messages_ru.properties

pause
