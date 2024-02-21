<?php
require "DataBase.php";
$db = new DataBase();
    if (isset($_POST['course_name'])&& isset($_POST['t_name'])) {
        if ($db->dbConnect()) {
           $get = $db->getCount($_POST['course_name'],$_POST['t_name']); 
           echo  $get ;  
        }  
    } else   echo $db->jsonMessage("Count", "Failed");