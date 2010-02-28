<?php
    ini_set("include_path", ini_get("include_path") . (substr($_SERVER["DOCUMENT_ROOT"],0,1) != "/" ? ";" : ":") . $_SERVER["DOCUMENT_ROOT"] . "/include");
    require_once("settings.inc.php");

    $db_connect = null;
    
    function db_connect(){
    	global $db_connect;
    	global $db_host, $db_database, $db_user, $db_password;
        
    	$db_connect = mysql_connect($db_host, $db_user, $db_password);
//    	if(mysql_errno() != 0){
//           print(mysql_error());
//        } 
    	mysql_select_db($db_database, $db_connect);
//    	if(mysql_errno() != 0){
//           print(mysql_error());
//        } 
    }
    
    function db_disconnect(){
    	global $db_connect;
    	
        if(!is_null($db_connect)){
            mysql_close($db_connect);
        }
    }
    
    function db_query($sql){
    	global $db_connect;
    	
    	if(is_null($db_connect)){
    		db_connect();
    	}
    	
    	$result = mysql_query($sql, $db_connect);
    	//print("result: " . mysql_error($db_connect));
    	if(mysql_errno($db_connect) != 0){
    	   //print(mysql_error($db_connect));
    	} 
    	return $result;
    }
?>