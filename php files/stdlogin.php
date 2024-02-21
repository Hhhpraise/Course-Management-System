<?php
require "DataBase.php";
$db = new DataBase();

if (isset($_POST['std_no']) && isset($_POST['password'])) {
    if ($db->dbConnect()) {
        if ($db->SlogIn("student_tb", $_POST['std_no'], $_POST['password'])) { 
            $user = $db->getStudent($_POST['std_no']);
            $data = [
            'msg' => "Successful",
            'data' => [
                    'id' => $user['id'],
                    'name' => $user['name'],
                    'std_no' => $user['std_no'],
                    'major' => $user['major'],
                    ] 
            ];
            echo $db->jsonMessage("Login", $data);
        } else  echo $db->jsonMessage("Login", "Number or Password wrong");
    } else  echo $db->jsonMessage("Login", "Error: Database connection");
} else  echo $db->jsonMessage("Login", "All fields are required");
?>