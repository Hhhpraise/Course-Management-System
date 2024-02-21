<?php
require "DataBase.php";
$db = new DataBase();
    if (isset($_POST['major'])) {
        if ($db->dbConnect()) {
           $get = $db->getSCourse($_POST['major']); 
           echo  $get ;  
        }  
    } else   echo $db->jsonMessage("Courses", "Failed");