<?php

    $page_title = "svs.bugz.org.ua";

    if($_SERVER["HTTP_HOST"] == "127.0.0.1"){
	    $db_host = "localhost";
	    $db_database = "svsdb";
	    $db_user = "svsdbuser";
	    $db_password = "svsdbuser";
    }
    else{
        $db_host = "localhost";
        $db_database = "svsbugzo_db";
        $db_user = "svsbugzo_svsdbus";
        $db_password = "ejN0WdDu6rPA";    	
    }
?>