<?php
    ini_set("include_path", ini_get("include_path") . (substr($_SERVER["DOCUMENT_ROOT"],0,1) != "/" ? ";" : ":") . $_SERVER["DOCUMENT_ROOT"] . "/include");
    require_once("db.inc.php");


    function trackVisitor(){
      global $db_connect;
      global $log_visits;

//       if($_SERVER["HTTP_HOST"] == "bugz.localhost:8080"
//           || $_SERVER["HTTP_HOST"] == "192.168.56.1:8080"
//           || $_SERVER["HTTP_HOST"] == "localhost"
//           || $_SERVER["HTTP_HOST"] == "bugz.localhost"){
//         // do not log on development
//         return;
//       }

//       if(isset($_SERVER["HTTP_VIA"]) &&
//         (strpos($_SERVER["HTTP_VIA"], "zeppelin:3128") || strpos($_SERVER["HTTP_VIA"], "bugz.localhost:3128"))
//         ){
//         // do not log my own requests
//         return;
//       }

      // do not log my own requests
      if(isset($_SERVER["REMOTE_ADDR"]) &&
        (strpos($_SERVER["REMOTE_ADDR"], "93.77.76.143")
             || strpos($_SERVER["REMOTE_ADDR"], "77.122.97.32")
            || strpos($_SERVER["REMOTE_ADDR"], "93.77.76.121")
        )
      ){
          return;
      }

      db_connect();

      $sql = sprintf(
         "insert into visit
         (
    visittime
    , http_referer
    , http_user_agent
    , http_via
    , http_x_forwarded_for
    , remote_addr
    , remote_host
    , request_uri
         )
         values
         (
         now()
         , '%s'
         , '%s'
         , '%s'
         , '%s'
         , '%s'
         , '%s'
         , '%s'
         )
         ",
         mysql_real_escape_string(isset($_SERVER["HTTP_REFERER"]) ? $_SERVER["HTTP_REFERER"] : "", $db_connect)
         , mysql_real_escape_string(isset($_SERVER["HTTP_USER_AGENT"]) ? $_SERVER["HTTP_USER_AGENT"] : "", $db_connect)
         , mysql_real_escape_string(isset($_SERVER["HTTP_VIA"]) ? $_SERVER["HTTP_VIA"] : "", $db_connect)
         , mysql_real_escape_string(isset($_SERVER["HTTP_X_FORWARDED_FOR"]) ? $_SERVER["HTTP_X_FORWARDED_FOR"] : "", $db_connect)
         , mysql_real_escape_string(isset($_SERVER["REMOTE_ADDR"]) ? $_SERVER["REMOTE_ADDR"] : "", $db_connect)
         , mysql_real_escape_string(isset($_SERVER["REMOTE_HOST"]) ? $_SERVER["REMOTE_HOST"] : "", $db_connect)
         , mysql_real_escape_string(isset($_SERVER["REQUEST_URI"]) ? $_SERVER["REQUEST_URI"] : "", $db_connect)
      );

      //print($sql);
      db_query($sql);
    }
?>