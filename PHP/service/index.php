<?php

    print_r($_POST);
    $dbhostname = "http://sql112.epizy.com";
    $dbusername = "epiz_27310703";
    $dbpassword = "1U5YlgUCvNufy";

// Create connection
$conn = new mysqli($servername, $username, $password);

// Check connection
if ($conn->connect_error) {
  die("Connection failed: " . $conn->connect_error);
}
echo "Connected successfully";

    switch ($_POST['q']){
        case 1:
            echo 'O query e o 1';
        break;
    }
        