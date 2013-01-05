@echo off
start "Gallery Publisher" /b /min ^
 javaw ^
 -cp lib/*;GalleryPublisher.jar ^
 org.sergeys.gpublish.ui.GalleryPublisher

: -cp lib/log4j-1.2.17.jar;lib/slf4j-api-1.7.2.jar;lib/slf4j-log4j12-1.7.2.jar;GalleryPublisher.jar ^

: -agentlib:jdwp=transport=dt_socket,address=localhost:8000,server=y,suspend=y
