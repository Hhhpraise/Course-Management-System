<?php
require "DataBase.php";
$db = new DataBase();
if($_SERVER['REQUEST_METHOD'] === 'POST'){
    if (isset($_POST['s_no']) && isset($_POST['course_name']) && isset($_POST['t_name'])) {
        if ($db->dbConnect()) {
            if($db->uniqReg($_POST['s_no'],$_POST['course_name'])){
                if ($db->regCourse("c_record", $_POST['s_no'], $_POST['course_name'], $_POST['t_name'])) { 
                    //return $db->jsonMessage("Signup", "Successfulvvvvvvv");
                }
                echo $db->jsonMessage("Reg", "Successful");
            }else  {
                echo $db->jsonMessage("Reg", "already registered");
            }
            
        }  
    } else   echo $db->jsonMessage("Reg", "not");
 
     
}

?>