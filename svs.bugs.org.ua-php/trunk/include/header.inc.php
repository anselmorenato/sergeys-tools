<?php print("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
  <head>
    <title><?php hprint($page_title); ?></title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <link href="/style/style.css" rel="stylesheet" type="text/css" />
    <!-- 
    <link rel="shortcut icon" href="/favicon.ico" />
     -->
    <script type="text/javascript">
    <!--
<?php  
  /**    
  #6040: The following JavaScript should be used to "break out" of any frames 
  and ensure that the site is loaded on the top window and not in any frame
  controlled by the attacker.
  */
 ?>  
    if(top != self){
      top.location.href = self.document.location;
    }
    if(parent != self){
      top.location.href = location.href;
    }
    if(top.frames.length != 0){
      top.location = self.document.location;
    }
    if(window != window.top){
      top.location.href = location.href;
    }

      /**
       * Function positions elements on the page
       * by setting width and height of the "content" element.
       * It is called when window.onload and window.onresize fired.
       */
      function onResize(){
        var content = null;
        var container = false;
        if(document.documentElement)
          container = document.documentElement;
        else if(document.body)
          container = document.body;
        if(container){
          content = document.getElementById("x_content");
          var height = (container.clientHeight > container.scrollHeight ? container.clientHeight : container.scrollHeight);
          content.style.height = (height - 172) + "px";
          var width = (container.clientWidth > container.scrollWidth ? container.clientWidth : container.scrollWidth);
          content.style.width = width + "px";
        }
      }
      window.onresize = onResize;
    //-->
    </script>
  </head>
  <body id="body">
