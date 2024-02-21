<?php
require "DataBase.php";
$db = new DataBase();

if (isset($_POST['t_no']) && isset($_POST['password'])) {
    if ($db->dbConnect()) {
        if ($db->logIn("teacher_tb", $_POST['t_no'], $_POST['password'])) { 
            $user = $db->getTeacher($_POST['t_no']);
            $data = [
            'msg' => "Successful",
            'data' => [
                    'id' => $user['id'],
                    'name' => $user['name'],
                    't_no' => $user['t_no'],
                    'major' => $user['major'],
                    ] 
            ];
            echo $db->jsonMessage("Login", $data);
        } else  echo $db->jsonMessage("Login", "Number or Password wrong");
    } else  echo $db->jsonMessage("Login", "Error: Database connection");
} else  echo $db->jsonMessage("Login", "All fields are required");
?>