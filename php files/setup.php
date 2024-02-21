<?php
require "DataBase.php";
$db = new DataBase();
if($_SERVER['REQUEST_METHOD'] === 'POST'){
    if (isset($_POST['major']) && isset($_POST['course_name']) && isset($_POST['credit_hour']) && isset($_POST['t_id'])) {
        if ($db->dbConnect()) {
            //checking if course already exist
            if($db->uniqCourse($_POST['course_name'],$_POST['major'])){
                if ($db->addCourse("course_tb", $_POST['major'], $_POST['course_name'], $_POST['credit_hour'],$_POST['t_id'])) { 
                    //return $db->jsonMessage("Signup", "Successfulvvvvvvv");
                }
                echo $db->jsonMessage("Setup", "Successful");
            }else  {
                echo $db->jsonMessage("Setup", "Course already exist in this major");
            }
            
        }  
    } else   echo $db->jsonMessage("Setup", "not");
 
     
}

?>