<?php
require "DataBaseConfig.php";

class DataBase
{
    public $connect;
    public $data;
    private $sql;
    protected $servername;
    protected $username;
    protected $password;
    protected $databasename;

    public function __construct()
    {
        $this->connect = null;
        $this->data = null;
        $this->sql = null;
        $dbc = new DataBaseConfig();
        $this->servername = $dbc->servername;
        $this->username = $dbc->username;
        $this->password = $dbc->password;
        $this->databasename = $dbc->databasename;
    }

    function dbConnect()
    {
        $this->connect = mysqli_connect($this->servername, $this->username, $this->password, $this->databasename);
        return $this->connect;
    }

    function prepareData($data)
    {
        return mysqli_real_escape_string($this->connect, stripslashes(htmlspecialchars($data)));
    }


    function logIn($table, $t_no, $password) {
        $t_no = $this->prepareData($t_no);
        $password = $this->prepareData($password);
        $this->sql = "select * from " . $table . " where t_no = '" . $t_no . "'";
        $result = mysqli_query($this->connect, $this->sql);
        $row = mysqli_fetch_assoc($result);
        if (mysqli_num_rows($result) != 0) {
            $dbtno = $row['t_no'];
            $dbpassword = $row['password'];
            if ($dbtno == $t_no && password_verify($password, $dbpassword)) {
                $login = true;
            } else $login = false;
        } else $login = false;

        return $login;
    }
    function SlogIn($table, $std_no, $password) {
        $std_no = $this->prepareData($std_no);
        $password = $this->prepareData($password);
        $this->sql = "select * from " . $table . " where std_no = '" . $std_no . "'";
        $result = mysqli_query($this->connect, $this->sql);
        $row = mysqli_fetch_assoc($result);
        if (mysqli_num_rows($result) != 0) {
            $dbstdno = $row['std_no'];
            $dbpassword = $row['password'];
            if ($dbstdno == $std_no && password_verify($password, $dbpassword)) {
                $login = true;
            } else $login = false;
        } else $login = false;

        return $login;
    }


    public function getTeacher($t_no){
        $t_no = $this->prepareData($t_no);
        $this->sql = "select id,t_no,name,major from teacher_tb where t_no = '".$t_no."'";
        $result = mysqli_query($this->connect, $this->sql);
        $user = $result->fetch_assoc(); 
        return $user; 
        
    }

    public function getStudent($std_no){
        $std_no = $this->prepareData($std_no);
        $this->sql = "select id,std_no,name,major from student_tb where std_no = '".$std_no."'";
        $result = mysqli_query($this->connect, $this->sql);
        $user = $result->fetch_assoc(); 
        return $user; 
        
    }

    public function getTCourse($id){
        $id = $this->prepareData($id);
        $bbj = "select * from course_tb where t_id = '".$id."'";
        $result = mysqli_query($this->connect, $bbj);
        $rows = mysqli_fetch_all($result, MYSQLI_ASSOC); 
        if(!empty($rows)){
            return $this-> jsonMessage("Course",json_decode(json_encode($rows)));
        }
        return $this->jsonMessage("Course", "empty");

    }


    public function getCourse($s_no){
        $s_no = $this->prepareData($s_no);
        $bbj = "SELECT c_record.*, course_tb.credit_hour
        FROM c_record
        INNER JOIN course_tb
        ON c_record.course_name = course_tb.course_name
        WHERE c_record.s_no = '".$s_no."' AND course_tb.course_name = c_record.course_name;";

        $result = mysqli_query($this->connect, $bbj);
        $rows = mysqli_fetch_all($result, MYSQLI_ASSOC); 
        if(!empty($rows)){
            return $this-> jsonMessage("Course",json_decode(json_encode($rows)));
        }
        return $this->jsonMessage("Course", "empty");

    }

    public function getSCourse($major){
        $major = $this->prepareData($major);
        $bbj = "SELECT course_tb.*, teacher_tb.name 
        FROM course_tb 
        INNER JOIN teacher_tb 
        ON course_tb.t_id = teacher_tb.t_no 
        WHERE course_tb.major = '".$major."' AND teacher_tb.t_no = course_tb.t_id ;";
        $result = mysqli_query($this->connect, $bbj);
        $rows = mysqli_fetch_all($result, MYSQLI_ASSOC); 
        if(!empty($rows)){
            return $this-> jsonMessage("Course",json_decode(json_encode($rows)));
        }
        return $this->jsonMessage("Course", "empty");

    }

    public function getStudents($course_name,$t_name){
        $course_name = $this->prepareData($course_name);
        $t_name = $this->prepareData($t_name);
        $bbj = "SELECT c_record.*, student_tb.name
        FROM c_record
        INNER JOIN student_tb
        ON c_record.s_no = student_tb.std_no
        WHERE c_record.course_name = '".$course_name."' AND c_record.t_name = '".$t_name."';";
        $result = mysqli_query($this->connect, $bbj);
        $rows = mysqli_fetch_all($result, MYSQLI_ASSOC); 
        if(!empty($rows)){
            return $this-> jsonMessage("Student",json_decode(json_encode($rows)));
        }
        return $this->jsonMessage("Student", "empty");

    }
    
    public function getCount($course_name,$t_name){
        $course_name = $this->prepareData($course_name);
        $t_name = $this->prepareData($t_name);
        $bbj = "SELECT COUNT(*) 
        FROM c_record
        INNER JOIN student_tb
        ON c_record.s_no = student_tb.std_no
        WHERE c_record.course_name = '".$course_name."' AND c_record.t_name = '".$t_name."';";
        $result = mysqli_query($this->connect, $bbj);
        $rows = mysqli_fetch_all($result, MYSQLI_ASSOC); 
        if(!empty($rows)){
            return $this-> jsonMessage("Count",json_decode(json_encode($rows)));
        }
        return $this->jsonMessage("Count", "empty");

    }



    public function jsonMessage($action, $data){

        $data = [
            "data" => $data,
            "from" => $action,
        ];

        return json_encode($data);
    }

    public function uniqTno($t_no){
        $t_no = $this->prepareData($t_no);
        $this->sql = "select t_no from teacher_tb where t_no = '".$t_no."'";
        $result = mysqli_query($this->connect, $this->sql);
        $row = mysqli_fetch_assoc($result);
        if(empty($row)){
            return  true;
        }
        return  false;
    }

    public function uniqSno($std_no){
        $std_no = $this->prepareData($std_no);
        $this->sql = "select std_no from student_tb where std_no = '".$std_no."'";
        $result = mysqli_query($this->connect, $this->sql);
        $row = mysqli_fetch_assoc($result);
        if(empty($row)){
            return  true;
        }
        return  false;
    }


    public function uniqCourse($course_name,$major){
        $course_name = $this->prepareData($course_name);
        $major = $this->prepareData($major);
        $this->sql = "SELECT * FROM depiro_database.course_tb where course_name ='".$course_name."' AND major = '".$major."'";
        $result = mysqli_query($this->connect, $this->sql);
        $row = mysqli_fetch_assoc($result);
        if(empty($row)){
            return  true;
        }
        return  false;
    }

    
    public function uniqReg($s_no,$course_name){
        $course_name = $this->prepareData($course_name);
        $s_no = $this->prepareData($s_no);
        $this->sql = "SELECT * FROM depiro_database.c_record where course_name ='".$course_name."' AND s_no = '".$s_no."'";
        $result = mysqli_query($this->connect, $this->sql);
        $row = mysqli_fetch_assoc($result);
        if(empty($row)){
            return  true;
        }
        return  false;
    }

    

    function signTUp($table, $name, $t_no, $password,$major)
    {
        $name = $this->prepareData($name);
        $password = $this->prepareData($password);
        $t_no = $this->prepareData($t_no);
        $major = $this->prepareData($major);
        $password = password_hash($password, PASSWORD_DEFAULT);
        $this->sql =
            "INSERT INTO " . $table . " (name, t_no, password,major) VALUES ('" . $name . "','" . $t_no . "','"  . $password . "','"  . $major ."')";
        if (mysqli_query($this->connect, $this->sql)) {
            json_encode(true);
        } else return json_encode(false);
    }

    
    function signSUp($table, $name, $std_no, $password,$major)
    {
        $name = $this->prepareData($name);
        $password = $this->prepareData($password);
        $std_no = $this->prepareData($std_no);
        $major = $this->prepareData($major);
        $password = password_hash($password, PASSWORD_DEFAULT);
        $this->sql =
            "INSERT INTO " . $table . " (name, std_no, password,major) VALUES ('" . $name . "','" . $std_no . "','"  . $password . "','"  . $major ."')";
        if (mysqli_query($this->connect, $this->sql)) {
            json_encode(true);
        } else return json_encode(false);
    }



    function addCourse($table, $major, $course_name, $credit_hour,$t_id)
    {
        $major = $this->prepareData($major);
        $course_name = $this->prepareData($course_name);
        $credit_hour = $this->prepareData($credit_hour);
        $t_id = $this->prepareData($t_id);
        $this->sql =
            "INSERT INTO " . $table . " (major, course_name, credit_hour,t_id) VALUES ('" . $major . "','" . $course_name . "','"  . $credit_hour . "','"  . $t_id ."')";
        if (mysqli_query($this->connect, $this->sql)) {
            json_encode(true);
        } else return json_encode(false);
    }

    function regCourse($table, $s_no, $course_name, $t_name)
    {
        $s_no = $this->prepareData($s_no);
        $course_name = $this->prepareData($course_name);
        $t_name = $this->prepareData($t_name);
       
        $this->sql =
            "INSERT INTO " . $table . " (s_no, course_name, t_name) VALUES ('" . $s_no . "','" . $course_name . "','"  . $t_name . "')";
        if (mysqli_query($this->connect, $this->sql)) {
            json_encode(true);
        } else return json_encode(false);
    }

}

?>