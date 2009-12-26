set CP=logic.4.jar
set CP=ui.jar;%CP%
set CP=mime-util-2.1.2.jar;%CP%
set CP=slf4j-api-1.5.8.jar;%CP%
set CP=slf4j-jdk14-1.5.8.jar;%CP%

java -cp %CP% org.sergeys.webcachedigger.ui.WebCacheDigger

