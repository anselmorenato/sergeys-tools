set CP=bin
set CP=lib\mime-util-2.1.3.jar;%CP%
set CP=lib\slf4j-api-1.6.2.jar;%CP%
set CP=lib\slf4j-simple-1.6.2.jar;%CP%


java -cp %CP% org.sergeys.webcachedigger.ui.WebCacheDigger

