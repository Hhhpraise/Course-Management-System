<?php
require "DataBase.php";
$db = new DataBase();
    if (isset($_POST['s_no'])) {
        if ($db->dbConnect()) {
           $get = $db->getCourse($_POST['s_no']); 
           echo  $get ;  
        }  
    } else   echo $db->jsonMessage("Courses", "Failed");