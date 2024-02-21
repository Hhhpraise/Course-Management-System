<?php
require "DataBase.php";
$db = new DataBase();
if($_SERVER['REQUEST_METHOD'] === 'POST'){
    if (isset($_POST['name']) && isset($_POST['std_no']) && isset($_POST['major']) && isset($_POST['password'])) {
        if ($db->dbConnect()) {
            if($db->uniqSno($_POST['std_no'])){
                if ($db->signSUp("student_tb", $_POST['name'], $_POST['std_no'], $_POST['password'],$_POST['major'])) { 
                    //return $db->jsonMessage("Signup", "Successfulvvvvvvv");
                }
                echo $db->jsonMessage("Signup", "Successful");
            }else  {
                echo $db->jsonMessage("Signup", "NUmber already exist");
            }
            
        }  
    } else   echo $db->jsonMessage("Signup", "not");
 
     
}

?>