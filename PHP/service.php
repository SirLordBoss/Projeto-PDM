<?php

use PHPMailer\PHPMailer\PHPMailer;
use PHPMailer\PHPMailer\Exception;

$servername ="localhost";
$dbUsername = "teachersfriend";
$dbPassword = "userpwd";
$maindb = "main";

$conn = mysqli_connect($servername, $dbUsername, $dbPassword);



if (!$conn) {
    $responseObject->success = false;
    $responseObject->error = "Unable to connect to MYSQL";
    $responseObject->debug = "Nº ".mysqli_connect_errno()." : ".mysqli_connect_error();
    $json = json_encode($responseObject);
    echo $json;
    exit();
}
if (!mysqli_set_charset($conn, "utf8")) {
    $responseObject->success = false;
    $responseObject->error = "Error loading character set utf8";
    $responseObject->debug = "Nº ".mysqli_error($conn);
    $json = json_encode($responseObject);
    echo $json;
    exit();
}

mysqli_select_db($conn,$maindb);

//if a user does not exist create one

$firstquery = "SELECT COUNT(u_nome) as cnt FROM users;";
$result = mysqli_query($conn,$firstquery);
if($result){
    if($row = mysqli_fetch_array($result, MYSQLI_ASSOC)){
        if($row['cnt'] == 0){
            $pwd = 'admin';
            $pwd_hashed = hash('md5',$pwd);
            mysqli_begin_transaction($conn);
            try{
                $sql = "INSERT INTO users (u_nome,u_email,u_idade,u_morada,u_pwd,u_sexo) VALUES ('admin','admin@email.com','18','Morada provisória','".$pwd_hashed."','1');";
                $result = mysqli_query($conn,$sql);
                if($result){
                    $sql = "INSERT INTO admin (u_id) SELECT u_id FROM users WHERE u_nome = 'admin';";
                    $result = mysqli_query($conn,$sql);
                    if($result){
                        $responseObject->success = true;
                        $json = json_encode($responseObject);
                        echo $json;
                        exit();
                    }else{
                        mysqli_rollback($conn);
                        $responseObjectError->success = false;
                        $responseObjectError->error = "Error inserting in admin table";
                        $json = json_encode($responseObjectError);
                        echo $json;
                        exit();
                    }
                    
                }else{
                    mysqli_rollback($conn);
                    $responseObjectError->success = false;
                    $responseObjectError->error = "Error inserting in users table";
                    $json = json_encode($responseObjectError);
                    echo $json;
                    exit();
                }
            }
            catch (mysqli_sql_exception $exception) {
                mysqli_rollback($conn);
                $responseObjectError->success = false;
                $responseObjectError->error = "Exception error";
                $json = json_encode($responseObjectError);
                echo $json;
                exit();
            }
            
        } 
    }
}

switch ($_POST['q']){
#021
    case 21:
        $id1 = $_POST['name'];
        $id = $_POST['id'];

        $sql = "SELECT COUNT(u.u_nome) as c FROM users u INNER JOIN admin a ON ( u.u_id = a.u_id ) WHERE u.u_nome = '$id1';";
        $result = mysqli_query($conn,$sql);
        if($result){
            if($row = mysqli_fetch_array($result,MYSQLI_ASSOC)){
                if($row['c'] == 1){
                    mysqli_begin_transaction($conn);
                    try{
                        $sql = "INSERT INTO users (u_nome,u_morada,u_sexo,u_email,u_pwd) SELECT tr_nome,tr_morada,tr_sexo,tr_email,tr_pwd FROM to_regist WHERE tr_id = '$id'";
                        $result = mysqli_query($conn,$sql);
                        if(!$result){
                            mysqli_rollback($conn);
                            $responseObjectError->success = false;
                            $responseObjectError->error = "Error inserting in users";
                            $json = json_encode($responseObjectError);
                            echo $json;
                            exit();
                        }
                        $sql = "SELECT tr_nome FROM to_regist WHERE tr_id = '$id'";
                        $result = mysqli_query($conn,$sql);
                        if(!$result){
                            mysqli_rollback($conn);
                            $responseObjectError->success = false;
                            $responseObjectError->error = "Error selecting in to_regist";
                            $json = json_encode($responseObjectError);
                            echo $json;
                            exit();
                        }
                        if(!($row = mysqli_fetch_array($result,MYSQLI_ASSOC))){
                            mysqli_rollback($conn);
                            $responseObjectError->success = false;
                            $responseObjectError->error = "Error fetching array";
                            $json = json_encode($responseObjectError);
                            echo $json;
                            exit();
                        }
                        $nome = $row['tr_nome'];
                        $sql = "DELETE FROM to_regist WHERE tr_id = '$id'";
                        $result = mysqli_query($conn,$sql);
                        if(!$result){
                            mysqli_rollback($conn);
                            $responseObjectError->success = false;
                            $responseObjectError->error = "Deleting in to_regist";
                            $json = json_encode($responseObjectError);
                            echo $json;
                            exit();
                        }
                        
                        $uniqid = uniqid('t_',false);
                        $sql = "INSERT INTO turmas (t_token,t_utilizada,u_id) VALUES ('$uniqid',0, (SELECT u.u_id FROM users AS u WHERE u.u_nome = '$nome' LIMIT 1) );";
                        $result = mysqli_query($conn,$sql);
                        if(!$result){
                            mysqli_rollback($conn);
                            $responseObjectError->success = false;
                            $responseObjectError->error = "Error inserting data in turmas";
                            $json = json_encode($responseObjectError);
                            echo $json;
                            exit();
                        }

                        $sql = "CREATE DATABASE ".$uniqid.";";
                        $result = mysqli_query($conn,$sql);
                        if(!$result){
                            mysqli_rollback($conn);
                            $responseObjectError->success = false;
                            $responseObjectError->error = "Error creating database";
                            $json = json_encode($responseObjectError);
                            echo $json;
                            exit();
                        }

                        mysqli_select_db($conn, $uniqid);

                        $sql = "CREATE TABLE IF NOT EXISTS educando ( e_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT, e_nome VARCHAR(100) NOT NULL, e_idade INT NOT NULL, e_morada VARCHAR(200) NOT NULL, e_sexo INT NOT NULL, e_contacto VARCHAR(10) NOT NULL );";
                        $result = mysqli_query($conn, $sql);
                        if(!$result){
                            mysqli_rollback($conn);
                            $responseObjectError->success = false;
                            $responseObjectError->error = "Error creating table educando";
                            $json = json_encode($responseObjectError);
                            echo $json;
                            exit();
                        }
                        
                        $sql = "CREATE TABLE IF NOT EXISTS atividade( a_sumario VARCHAR(300) NOT NULL, a_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT, a_data VARCHAR(200) NOT NULL);";
                        $result = mysqli_query($conn,$sql);
                        if(!$result){
                            mysqli_rollback($conn);
                            $responseObjectError->success = false;
                            $responseObjectError->error = "Error creating table atividade";
                            $json = json_encode($responseObjectError);
                            echo $json;
                            exit();
                        }

                        $sql = "CREATE TABLE IF NOT EXISTS alergias( al_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT, al_nome VARCHAR(100) NOT NULL);";
                        $result = mysqli_query($conn,$sql);
                        if(!$result){
                            mysqli_rollback($conn);
                            $responseObjectError->success = false;
                            $responseObjectError->error = "Error creating table alergias";
                            $json = json_encode($responseObjectError);
                            echo $json;
                            exit();
                        }

                        $sql = "CREATE TABLE IF NOT EXISTS faltas ( e_id INT NOT NULL, a_id INT NOT NULL, PRIMARY KEY (e_id, a_id), FOREIGN KEY (e_id) REFERENCES educando(e_id), FOREIGN KEY (a_id) REFERENCES atividade(a_id));";
                        $result = mysqli_query($conn,$sql);
                        if(!$result){
                            mysqli_rollback($conn);
                            $responseObjectError->success = false;
                            $responseObjectError->error = "Error creating table faltas";
                            $json = json_encode($responseObjectError);
                            echo $json;
                            exit();
                        }

                        $sql = "CREATE TABLE IF NOT EXISTS relatorio ( r_comer INT NOT NULL, r_dormir INT NOT NULL, r_coment VARCHAR(500) NOT NULL, r_necessidades INT NOT NULL, r_curativos INT NOT NULL, e_id INT NOT NULL, a_id INT NOT NULL, PRIMARY KEY (e_id, a_id), FOREIGN KEY (e_id) REFERENCES educando(e_id), FOREIGN KEY (a_id) REFERENCES atividade(a_id) );";
                        $result = mysqli_query($conn,$sql);
                        if(!$result){
                            mysqli_rollback($conn);
                            $responseObjectError->success = false;
                            $responseObjectError->error = "Error creating table relatorio";
                            $json = json_encode($responseObjectError);
                            echo $json;
                            exit();
                        }

                        $sql = "CREATE TABLE IF NOT EXISTS contem ( al_id INT NOT NULL, e_id INT NOT NULL, PRIMARY KEY (al_id, e_id), FOREIGN KEY (al_id) REFERENCES alergias(al_id), FOREIGN KEY (e_id) REFERENCES educando(e_id) );";
                        $result = mysqli_query($conn,$sql);
                        if(!$result){
                            mysqli_rollback($conn);
                            $responseObjectError->success = false;
                            $responseObjectError->error = "Error creating table contem";
                            $json = json_encode($responseObjectError);
                            echo $json;
                            exit();
                        }

                        mysqli_commit($conn);
                        $responseObject->success = true;
                        $responseObject->user = $nome; 
                        $json = json_encode($responseObject);
                        echo $json;    
                        exit();  
                    }catch(mysqli_sql_exception $exception){
                        mysqli_rollback($conn);
                        $responseObjectError->success = false;
                        $responseObjectError->error = "Exception error";
                        $json = json_encode($responseObjectError);
                        echo $json;
                        exit();
                    }
                }else{
                    $responseObjectError->success = false;
                    $responseObjectError->error = "User not admin";
                    $json = json_encode($responseObjectError);
                    echo $json;
                    exit();
                }
            }else{
                $responseObjectError->success = false;
                $responseObjectError->error = "Error fetching array";
                $json = json_encode($responseObjectError);
                echo $json;
                exit();
            }
        }else{
            $responseObjectError->success = false;
            $responseObjectError->error = "Query error";
            $json = json_encode($responseObjectError);
            echo $json;
            exit();
        }

    break;
#022
    case 22:
        $id = $_POST['id'];

        $sql = "SELECT COUNT(u.u_nome) as c FROM users u INNER JOIN admin a ON ( u.u_id = a.u_id ) WHERE u.u_nome = '$id';";
        $result = mysqli_query($conn,$sql);
        if($result){
            if($row = mysqli_fetch_array($result,MYSQLI_ASSOC)){
                if($row['c'] == 1){
                    $u_id= $_POST['u_id'];
                    $username = $_POST['nome'];
                    $idade = $_POST['idade'];
                    $morada = $_POST['morada'];
                    $sexo = $_POST['sexo'];
                    $email = $_POST['email'];

                    $sql = "UPDATE users SET (u_nome = ,u_idade = ,u_morada = ,u_sexo = ,u_email = ) WHERE u_id = '$u_id'";
                    $result = mysqli_query($conn,$sql);
                    if($result){
                        $responseObject->success = true;
                        $json = json_encode($responseObject);
                        echo $json;
                        exit();
                    }else{
                        $responseObjectError->success = false;
                        $responseObjectError->error = "Error in query";
                        $json = json_encode($responseObjectError);
                        echo $json;
                        exit();
                    }
                }else{
                    $responseObjectError->success = false;
                    $responseObjectError->error = "User not admin";
                    $json = json_encode($responseObjectError);
                    echo $json;
                    exit();
                }
            }else{
                $responseObjectError->success = false;
                $responseObjectError->error = "Error fetching array";
                $json = json_encode($responseObjectError);
                echo $json;
                exit();
            }
        }else{
            $responseObjectError->success = false;
            $responseObjectError->error = "Error in query";
            $json = json_encode($responseObjectError);
            echo $json;
            exit();
        }
    break;
#023 - Eliminar um utilizador
    $id = $_POST['id'];

    $sql = "SELECT COUNT(u.u_nome) as c FROM users u INNER JOIN admin a ON ( u.u_id = a.u_id ) WHERE u.u_nome = '$id';";
    $result = mysqli_query($conn,$sql);
    if($result){
        if($row = mysqli_fetch_array($result,MYSQLI_ASSOC)){
            if($row['c'] == 1){
                $u_id= $_POST['u_id'];
                $username = $_POST['nome'];
                $idade = $_POST['idade'];
                $morada = $_POST['morada'];
                $sexo = $_POST['sexo'];
                $email = $_POST['email'];

                $sql = "UPDATE users SET (u_nome = ,u_idade = ,u_morada = ,u_sexo = ,u_email = ) WHERE u_id = '$u_id'";
                $result = mysqli_query($conn,$sql);
                if($result){
                    $responseObject->success = true;
                    $json = json_encode($responseObject);
                    echo $json;
                    exit();
                }else{
                    $responseObjectError->success = false;
                    $responseObjectError->error = "Error in query";
                    $json = json_encode($responseObjectError);
                    echo $json;
                    exit();
                }
            }else{
                $responseObjectError->success = false;
                $responseObjectError->error = "User not admin";
                $json = json_encode($responseObjectError);
                echo $json;
                exit();
            }
        }else{
            $responseObjectError->success = false;
            $responseObjectError->error = "Error fetching array";
            $json = json_encode($responseObjectError);
            echo $json;
            exit();
        }
    }else{
        $responseObjectError->success = false;
        $responseObjectError->error = "Error in query";
        $json = json_encode($responseObjectError);
        echo $json;
        exit();
    }
    break;
#050 - Registo
    case 50: //Registo
    $nome = $_POST['nome'];
    $idade = $_POST['idade'];
    $morada = $_POST['morada'];
    $sexo = $_POST['sexo'];
    $email = $_POST['email'];
    $pwd = $_POST['pass'];
    $sql = "SELECT COUNT(u_nome) as c FROM users WHERE u_nome = '$nome' ;";
    $result = mysqli_query($conn,$sql);
    if(!$result){
        $responseObjectError->success = false;
        $responseObjectError->error = "Error in query";
        $json = json_encode($responseObjectError);
        echo $json;
        exit();
    }
    if($row = mysqli_fetch_array($result,MYSQLI_ASSOC)){
        if($row['c'] > 0){
            $responseObjectError->success = false;
            $responseObjectError->error = "Username taken"; //erro quando o nome de utilizador está já a ser utilizado
            $json = json_encode($responseObjectError);
            echo $json;
            exit();
        }
    }else{
        $responseObjectError->success = false;
        $responseObjectError->error = "Error fetching array";
        $responseObjectError->debug = mysqli_error($conn);
        $json = json_encode($responseObjectError);
        echo $json;
        exit();
    }

    $sql = "INSERT INTO to_regist (tr_email,tr_sexo,tr_morada,tr_idade,tr_nome,tr_pwd) VALUES ('$email','$sexo','$morada','$idade','$nome','$pwd')";
    $result = mysqli_query($conn,$sql);
    if(!$result){
        $responseObjectError->success = false;
        $responseObjectError->error = "Data not inserted";
        $responseObjectError->debug = mysqli_error($conn);
        $json = json_encode($responseObjectError);
        echo $json;
        exit();
    }
    $responseObject->success = true;
    $json = json_encode($responseObject);
    echo $json;
    exit();

    break;
#100 - Login (educadora)
    case 100: 
        $username = $_POST['u'];
        $password = $_POST['p'];
        //só faz login se não for administrador
        $sql = "SELECT u.u_id FROM users as u WHERE u.u_nome = '$username' AND u.u_pwd = '$password' AND NOT EXISTS ( SELECT * FROM admin a WHERE u.u_id = a.u_id ) ;";
        $result = mysqli_query($conn,$sql);
        if($result){
            if(mysqli_num_rows($result)>0){
                if($row = mysqli_fetch_array($result,MYSQLI_ASSOC)){
                    $u_id = $row['u_id'];
                    /* Começar uma transação para retirar todos os dados e dizer que a tabela está a ser usada */
                    mysqli_begin_transaction($conn);
                    try {                      
                        $sql = "SELECT t_token FROM turmas WHERE u_id = '$u_id';";
                        $result = mysqli_query($conn,$sql);
                        if($result){
                            if($row = mysqli_fetch_array($result,MYSQLI_ASSOC)){
                                $database_name = $row['t_token'];

                                $sql = "UPDATE turmas SET t_utilizada = 1 WHERE u_id = '$u_id';";
                                $result = mysqli_query($conn,$sql);

                                if(!$result){
                                    $responseObjectError->success = false;
                                    $responseObjectError->error = "Database query error : turmas";
                                    $responseObjectError->debug = mysqli_error($conn);
                                    $json = json_encode($responseObjectError);
                                    echo $json;
                                    mysqli_rollback($conn);
                                    exit();
                                }

                                mysqli_select_db($conn,$database_name);

                                $responseObject->success = true;
                                $responseObject->educando = "none";
                                $responseObject->atividade = "none";
                                $responseObject->alergias = "none";
                                $responseObject->faltas = "none";
                                $responseObject->relatorio = "none";
                                $responseObject->contem = "none";

                                $sql = "SELECT * FROM educando ORDER BY e_id;";
                                $result = mysqli_query($conn,$sql);
                                if($result){
                                    $responseObject->educando = "";
                                    while($row = mysqli_fetch_array($result,MYSQLI_ASSOC)){
                                        $responseObject->educando .= "".$row['e_id'].",".$row['e_nome'].",".$row['e_idade'].",".$row['e_morada'].",".$row['e_sexo'].",".$row['e_contacto'].";";
                                    }                                    
                                }else{
                                    $responseObjectError->success = false;
                                    $responseObjectError->error = "Database query error : educando";
                                    $responseObjectError->debug = mysqli_error($conn);
                                    $json = json_encode($responseObjectError);
                                    echo $json;
                                    mysqli_rollback($conn);
                                    exit();
                                }

                                $sql = "SELECT * FROM atividade ORDER BY a_id;";
                                $result = mysqli_query($conn,$sql);
                                if($result){
                                    $responseObject->atividade = "";
                                    while($row = mysqli_fetch_array($result,MYSQLI_ASSOC)){
                                        $responseObject->atividade .= "".$row['a_id'].",".$row['a_sumario'].",".$row['a_data'].";";
                                    }                                    
                                }else{
                                    $responseObjectError->success = false;
                                    $responseObjectError->error = "Database query error : atividade";
                                    $responseObjectError->debug = mysqli_error($conn);
                                    $json = json_encode($responseObjectError);
                                    echo $json;
                                    mysqli_rollback($conn);
                                    exit();
                                }

                                $sql = "SELECT * FROM alergias ORDER BY al_id;";
                                $result = mysqli_query($conn,$sql);
                                if($result){
                                    $responseObject->alergias = "";
                                    while($row = mysqli_fetch_array($result,MYSQLI_ASSOC)){
                                        $responseObject->alergias .= "".$row['al_id'].",".$row['al_nome'].";";
                                    }                                    
                                }else{
                                    $responseObjectError->success = false;
                                    $responseObjectError->error = "Database query error : alergias";
                                    $responseObjectError->debug = mysqli_error($conn);
                                    $json = json_encode($responseObjectError);
                                    echo $json;
                                    mysqli_rollback($conn);
                                    exit();
                                }

                                $sql = "SELECT * FROM faltas ORDER BY e_id;";
                                $result = mysqli_query($conn,$sql);
                                if($result){
                                    $responseObject->faltas = "";
                                    while($row = mysqli_fetch_array($result,MYSQLI_ASSOC)){
                                        $responseObject->faltas .= "".$row['e_id'].",".$row['a_id'].";";
                                    }                                    
                                }else{
                                    $responseObjectError->success = false;
                                    $responseObjectError->error = "Database query error : faltas";
                                    $json = json_encode($responseObjectError);
                                    echo $json;
                                    mysqli_rollback($conn);
                                    exit();
                                }

                                $sql = "SELECT * FROM relatorio ORDER BY e_id;";
                                $result = mysqli_query($conn,$sql);
                                if($result){
                                    $responseObject->relatorio = "";
                                    while($row = mysqli_fetch_array($result,MYSQLI_ASSOC)){
                                        $responseObject->relatorio .= "".$row['r_comer'].",".$row['r_dormir'].",".$row['r_coment'].",".$row['r_necessidades'].",".$row['r_curativos'].",".$row['e_id'].",".$row['a_id'].";";
                                    }                                    
                                }else{
                                    $responseObjectError->success = false;
                                    $responseObjectError->error = "Database query error : relatorio";
                                    $responseObjectError->debug = mysqli_error($conn);
                                    $json = json_encode($responseObjectError);
                                    echo $json;
                                    mysqli_rollback($conn);
                                    exit();
                                }

                                $sql = "SELECT * FROM contem ORDER BY e_id;";
                                $result = mysqli_query($conn,$sql);
                                if($result){
                                    $responseObject->contem = "";
                                    while($row = mysqli_fetch_array($result,MYSQLI_ASSOC)){
                                        $responseObject->contem .= "".$row['al_id'].",".$row['e_id'].";";
                                    }                                    
                                }else{
                                    $responseObjectError->success = false;
                                    $responseObjectError->error = "Database query error : contem";
                                    $responseObjectError->debug = mysqli_error($conn);
                                    $json = json_encode($responseObjectError);
                                    echo $json;
                                    mysqli_rollback($conn);
                                    exit();
                                }

                                $json = json_encode($responseObject);
                                echo $json;

                            }else{
                                $responseObjectError->success = false;
                                $responseObjectError->error = "Username does not contain a class";
                                $json = json_encode($responseObjectError);
                                echo $json;
                                mysqli_rollback($conn);
                                exit();
                            }
                        }else{
                            $responseObjectError->success = false;
                            $responseObjectError->error = "Database query error";
                            $responseObjectError->debug = mysqli_error($conn);
                            $json = json_encode($responseObjectError);
                            echo $json;
                            mysqli_rollback($conn);
                            exit();
                        }
                        
                        mysqli_commit($conn);
                        exit();
                    } catch (mysqli_sql_exception $exception) {
                        mysqli_rollback($conn);
                        $responseObjectError->success = false;
                        $responseObjectError->debug = mysqli_error($conn);
                        $json = json_encode($responseObjectError);
                        echo $json;
                        exit();
                    }
                }
            }else{
                $responseObjectError->success = false;
                $responseObjectError->error = "Username/password not match2";
                $responseObjectError->debug = mysqli_error($conn);
                $json = json_encode($responseObjectError);
                echo $json;
                exit();
            }
        }else{
            $responseObjectError->success = false;
            $responseObjectError->error = "Username/password not match";
            $responseObjectError->debug = mysqli_error($conn);
            $json = json_encode($responseObjectError);
            echo $json;
            exit();
        }
    break;
#101 - Adicionar um utilizador (educadora)
    case 101: 
        $id = $_POST['id'];
        $sql = "SELECT COUNT(u.u_nome) as c FROM users u INNER JOIN admin a ON ( u.u_id = a.u_id ) WHERE u.u_nome = '$id';";
        $result = mysqli_query($conn,$sql);
        if($result){
            if($row = mysqli_fetch_array($result,MYSQLI_ASSOC)){
                if($row['c'] == 1){
                    mysqli_begin_transaction($conn);
                    try{
                        $nome = $_POST['un'];
                        $idade = $_POST['i'];
                        $morada = $_POST['m'];
                        $sexo = $_POST['s'];
                        $email = $_POST['e'];
                        $pwd = $_POST['pwd'];
                        $sql = "SELECT COUNT(u_nome) as c, COUNT(tr_nome) as ctr FROM users as u, to_regist as tr WHERE u_nome = '$nome' OR tr_nome = '$nome';";
                        $result = mysqli_query($conn,$sql);
                        if(!$result){
                            $responseObjectError->success = false;
                            $responseObjectError->error = "Error in query";
                            $json = json_encode($responseObjectError);
                            echo $json;
                            exit();
                        }
                        if($row = mysqli_fetch_array($result,MYSQLI_ASSOC)){
                            if($row['c'] > 0 || $row['ctr'] > 0){
                                $responseObjectError->success = false;
                                $responseObjectError->error = "Username taken"; //erro quando o nome de utilizador está já a ser utilizado
                                $json = json_encode($responseObjectError);
                                echo $json;
                                exit();
                            }
                        }else{
                            $responseObjectError->success = false;
                            $responseObjectError->error = "Error fetching array";
                            $json = json_encode($responseObjectError);
                            echo $json;
                            exit();
                        }
                        $sql = "INSERT INTO users (u_nome, u_idade,u_morada,u_sexo,u_email,u_pwd) VALUES ('$nome','$idade','$morada','$sexo','$email','$pwd');";
                        $result = mysqli_query($conn,$sql);
                        if(!$result){
                            mysqli_rollback($conn);
                            $responseObjectError->success = false;
                            $responseObjectError->error = "Error inserting in users";
                            $json = json_encode($responseObjectError);
                            echo $json;
                            exit();
                        }
                        $uniqid = uniqid('t_',false);
                        $sql = "INSERT INTO turmas (t_token,t_utilizada,u_id) VALUES ('$uniqid',0, (SELECT u.u_id FROM users AS u WHERE u.u_nome = '$nome' LIMIT 1) );";
                        $result = mysqli_query($conn,$sql);
                        if(!$result){
                            mysqli_rollback($conn);
                            $responseObjectError->success = false;
                            $responseObjectError->error = "Error inserting data in turmas";
                            $json = json_encode($responseObjectError);
                            echo $json;
                            exit();
                        }

                        $sql = "CREATE DATABASE ".$uniqid.";";
                        $result = mysqli_query($conn,$sql);
                        if(!$result){
                            mysqli_rollback($conn);
                            $responseObjectError->success = false;
                            $responseObjectError->error = "Error creating database";
                            $json = json_encode($responseObjectError);
                            echo $json;
                            exit();
                        }

                        mysqli_select_db($conn, $uniqid);

                        $sql = "CREATE TABLE IF NOT EXISTS educando ( e_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT, e_nome VARCHAR(100) NOT NULL, e_idade INT NOT NULL, e_morada VARCHAR(200) NOT NULL, e_sexo INT NOT NULL, e_contacto VARCHAR(10) NOT NULL );";
                        $result = mysqli_query($conn, $sql);
                        if(!$result){
                            mysqli_rollback($conn);
                            $responseObjectError->success = false;
                            $responseObjectError->error = "Error creating table educando";
                            $json = json_encode($responseObjectError);
                            echo $json;
                            exit();
                        }
                        
                        $sql = "CREATE TABLE IF NOT EXISTS atividade( a_sumario VARCHAR(300) NOT NULL, a_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT, a_data VARCHAR(200) NOT NULL);";
                        $result = mysqli_query($conn,$sql);
                        if(!$result){
                            mysqli_rollback($conn);
                            $responseObjectError->success = false;
                            $responseObjectError->error = "Error creating table atividade";
                            $json = json_encode($responseObjectError);
                            echo $json;
                            exit();
                        }

                        $sql = "CREATE TABLE IF NOT EXISTS alergias( al_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT, al_nome VARCHAR(100) NOT NULL);";
                        $result = mysqli_query($conn,$sql);
                        if(!$result){
                            mysqli_rollback($conn);
                            $responseObjectError->success = false;
                            $responseObjectError->error = "Error creating table alergias";
                            $json = json_encode($responseObjectError);
                            echo $json;
                            exit();
                        }

                        $sql = "CREATE TABLE IF NOT EXISTS faltas ( e_id INT NOT NULL, a_id INT NOT NULL, PRIMARY KEY (e_id, a_id), FOREIGN KEY (e_id) REFERENCES educando(e_id), FOREIGN KEY (a_id) REFERENCES atividade(a_id));";
                        $result = mysqli_query($conn,$sql);
                        if(!$result){
                            mysqli_rollback($conn);
                            $responseObjectError->success = false;
                            $responseObjectError->error = "Error creating table faltas";
                            $json = json_encode($responseObjectError);
                            echo $json;
                            exit();
                        }

                        $sql = "CREATE TABLE IF NOT EXISTS relatorio ( r_comer INT NOT NULL, r_dormir INT NOT NULL, r_coment VARCHAR(500) NOT NULL, r_necessidades INT NOT NULL, r_curativos INT NOT NULL, e_id INT NOT NULL, a_id INT NOT NULL, PRIMARY KEY (e_id, a_id), FOREIGN KEY (e_id) REFERENCES educando(e_id), FOREIGN KEY (a_id) REFERENCES atividade(a_id) );";
                        $result = mysqli_query($conn,$sql);
                        if(!$result){
                            mysqli_rollback($conn);
                            $responseObjectError->success = false;
                            $responseObjectError->error = "Error creating table relatorio";
                            $json = json_encode($responseObjectError);
                            echo $json;
                            exit();
                        }

                        $sql = "CREATE TABLE IF NOT EXISTS contem ( al_id INT NOT NULL, e_id INT NOT NULL, PRIMARY KEY (al_id, e_id), FOREIGN KEY (al_id) REFERENCES alergias(al_id), FOREIGN KEY (e_id) REFERENCES educando(e_id) );";
                        $result = mysqli_query($conn,$sql);
                        if(!$result){
                            mysqli_rollback($conn);
                            $responseObjectError->success = false;
                            $responseObjectError->error = "Error creating table contem";
                            $json = json_encode($responseObjectError);
                            echo $json;
                            exit();
                        }

                        mysqli_commit($conn);
                        $responseObject->success = true;
                        $responseObject->user = $nome; 
                        $json = json_encode($responseObject);
                        echo $json;    
                        exit();                   

                    }catch (mysqli_sql_exception $exception) {
                        mysqli_rollback($conn);
                        $responseObjectError->success = false;
                        $responseObjectError->error = "Exception error";
                        $json = json_encode($responseObjectError);
                        echo $json;
                        exit();
                    }
                }else{
                    $responseObjectError->success = false;
                    $responseObjectError->error = "User not admin";
                    $json = json_encode($responseObjectError);
                    echo $json;
                    exit();
                }
            }else{
                $responseObjectError->success = false;
                $responseObjectError->error = "Mysql fetch error";
                $json = json_encode($responseObjectError);
                echo $json;
                exit();
            }
        }else{
            $responseObjectError->success = false;
            $responseObjectError->error = "Database query error";
            $json = json_encode($responseObjectError);
            echo $json;
            exit();
        }
    break;
#102 - Adicionar um utilizador (administrador)
    case 102:
        $id = $_POST['id'];
        $sql = "SELECT COUNT(u.u_nome) as c FROM users u INNER JOIN admin a ON ( u.u_id = a.u_id ) WHERE u.u_nome = '$id';";
        $result = mysqli_query($conn,$sql);
        if($result){
            if($row = mysqli_fetch_array($result,MYSQLI_ASSOC)){
                if($row['c'] == 1){
                    mysqli_begin_transaction($conn);
                    try{
                        $nome = $_POST['un'];
                        $idade = $_POST['i'];
                        $morada = $_POST['m'];
                        $sexo = $_POST['s'];
                        $email = $_POST['e'];
                        $pwd = $_POST['pwd'];
                        $sql = "SELECT COUNT(u_nome) as c, COUNT(tr_nome) as ctr FROM users as u, to_regist as tr WHERE u_nome = '$nome' OR tr_nome = '$nome';";
                        $result = mysqli_query($conn,$sql);
                        if(!$result){
                            $responseObjectError->success = false;
                            $responseObjectError->error = "Error in query";
                            $json = json_encode($responseObjectError);
                            echo $json;
                            exit();
                        }
                        if($row = mysqli_fetch_array($result,MYSQLI_ASSOC)){
                            if($row['c'] > 0 || $row['ctr']>0){
                                $responseObjectError->success = false;
                                $responseObjectError->error = "Username taken"; //erro quando o nome de utilizador está já a ser utilizado
                                $json = json_encode($responseObjectError);
                                echo $json;
                                exit();
                            }
                        }else{
                            $responseObjectError->success = false;
                            $responseObjectError->error = "Error fetching array";
                            $json = json_encode($responseObjectError);
                            echo $json;
                            exit();
                        }
                        $sql = "INSERT INTO users (u_nome, u_idade,u_morada,u_sexo,u_email,u_pwd) VALUES ('$nome','$idade','$morada','$sexo','$email','$pwd');";
                        $result = mysqli_query($conn,$sql);
                        if(!$result){
                            mysqli_rollback($conn);
                            $responseObjectError->success = false;
                            $responseObjectError->error = "Error inserting in users";
                            $json = json_encode($responseObjectError);
                            echo $json;
                            exit();
                        }
                        $sql = "INSERT INTO admin (SELECT u.u_id FROM users WHERE u_nome = '$nome' LIMIT 1);";
                        $result = mysqli_query($conn,$sql);
                        if(!$result){
                            mysqli_rollback($conn);
                            $responseObjectError->success = false;
                            $responseObjectError->error = "Error inserting in admin";
                            $json = json_encode($responseObjectError);
                            echo $json;
                            exit();
                        }
                    }catch(mysqli_sql_exception $exception){
                        mysqli_rollback($conn);
                        $responseObjectError->success = false;
                        $responseObjectError->error = "Exception error";
                        $json = json_encode($responseObjectError);
                        echo $json;
                        exit();
                    }
                }
            }
        }
    break;

#104 - Update DB
    case 104:
        $u_name = $_POST['user'];
        $educando = $_POST['ed'];
        $atividade = $_POST['at'];
        $alergias = $_POST['al'];
        $faltas = $_POST['fa'];
        $relatorio = $_POST['rel'];
        $contem = $_POST['cont'];

        $educandolines = explode(";", $educando);
        $atividadelines = explode(";", $atividade);
        $alergiaslines = explode(";", $alergias);
        $faltaslines = explode(";", $faltas);
        $contemlines = explode(";", $contem);
        $relatoriolines = explode(";",$relatorio);

        mysqli_begin_transaction($conn);
        try{
            $sql = "SELECT t.t_token FROM users u INNER JOIN turmas t ON ( u.u_id = t.u_id ) WHERE u.u_nome = '$u_name';";
            $result = mysqli_query($conn,$sql);
            if(!$result){
                $responseObjectError->success = false;
                $responseObjectError->error = "Error in query";
                $json = json_encode($responseObjectError);
                echo $json;
                exit();
            }
            if(!($row = mysqli_fetch_array($result,MYSQLI_ASSOC))){
                $responseObjectError->success = false;
                $responseObjectError->error = "Error user not found";
                $responseObjectError->debug = mysqli_error($conn);
                $json = json_encode($responseObjectError);
                echo $json;
                exit();
            }
            $token = $row['t_token'];
            mysqli_select_db($conn,$token);
    

            #delete all tables 
            $sql = "DELETE FROM contem;";
            $result = mysqli_query($conn,$sql);
            if(!$result){
                mysqli_rollback($conn);
                $responseObjectError->success = false;
                $responseObjectError->error = "Error deleting table contem";
                $responseObjectError->debug = mysqli_error($conn);
                $json = json_encode($responseObjectError);
                echo $json;
                exit();
            }
            $sql = "DELETE FROM relatorio;";
            $result = mysqli_query($conn,$sql);
            if(!$result){
                mysqli_rollback($conn);
                $responseObjectError->success = false;
                $responseObjectError->error = "Error deleting table relatorio";
                $responseObjectError->debug = mysqli_error($conn);
                $json = json_encode($responseObjectError);
                echo $json;
                exit();
            }
            $sql = "DELETE FROM faltas;";
            $result = mysqli_query($conn,$sql);
            if(!$result){
                mysqli_rollback($conn);
                $responseObjectError->success = false;
                $responseObjectError->error = "Error deleting table faltas";
                $responseObjectError->debug = mysqli_error($conn);
                $json = json_encode($responseObjectError);
                echo $json;
                exit();
            }
            $sql = "DELETE FROM atividade;";
            $result = mysqli_query($conn,$sql);
            if(!$result){
                mysqli_rollback($conn);
                $responseObjectError->success = false;
                $responseObjectError->error = "Error deleting table atividade";
                $responseObjectError->debug = mysqli_error($conn);
                $json = json_encode($responseObjectError);
                echo $json;
                exit();
            }
            
            $sql = "DELETE FROM alergias;";
            $result = mysqli_query($conn,$sql);
            if(!$result){
                mysqli_rollback($conn);
                $responseObjectError->success = false;
                $responseObjectError->error = "Error deleting table alergias";
                $responseObjectError->debug = mysqli_error($conn);
                $json = json_encode($responseObjectError);
                echo $json;
                exit();
            }

            $sql = "DELETE FROM educando;";
            $result = mysqli_query($conn,$sql);
            if(!$result){
                mysqli_rollback($conn);
                $responseObjectError->success = false;
                $responseObjectError->error = "Error deleting table educando";
                $responseObjectError->debug = mysqli_error($conn);
                $json = json_encode($responseObjectError);
                echo $json;
                exit();
            }

            #INSERT into tables
            for($i=0;$i<count($educandolines);$i++){
                $educandocolumns = explode(",",$educandolines[$i]);
                $sql = "INSERT INTO educando (e_id,e_nome,e_idade,e_morada,e_sexo,e_contacto) VALUES  ('$educandocolumns[0]','$educandocolumns[1]','$educandocolumns[2]','$educandocolumns[3]','$educandocolumns[4]','$educandocolumns[5]');";
                $result = mysqli_query($conn,$sql);
                if(!$result){
                    mysqli_rollback($conn);
                    $responseObjectError->success = false;
                    $responseObjectError->error = "Error inserting into educando";
                    $responseObjectError->debug = mysqli_error($conn);
                    $json = json_encode($responseObjectError);
                    echo $json;
                    exit();
                }
            }
    
            for($i=0;$i<count($atividadelines);$i++){
                $atividadecolumns = explode(",",$atividadelines[$i]);
                $sql = "INSERT INTO atividade (a_sumario,a_id,a_data) VALUES  ('$atividadecolumns[0]','$atividadecolumns[1]','$atividadecolumns[2]');";
                $result = mysqli_query($conn,$sql);
                if(!$result){
                    mysqli_rollback($conn);
                    $responseObjectError->success = false;
                    $responseObjectError->error = "Error inserting into atividade";
                    $responseObjectError->debug = mysqli_error($conn);
                    $json = json_encode($responseObjectError);
                    echo $json;
                    exit();
                }
            }

            for($i=0;$i<count($alergiaslines);$i++){
                $alergiascolumns = explode(",",$alergiaslines[$i]);
                $sql = "INSERT INTO alergias (al_id,al_nome) VALUES  ('$alergiascolumns[0]','$alergiascolumns[1]');";
                $result = mysqli_query($conn,$sql);
                if(!$result){
                    mysqli_rollback($conn);
                    $responseObjectError->success = false;
                    $responseObjectError->error = "Error inserting into alergias";
                    $responseObjectError->debug = mysqli_error($conn);
                    $json = json_encode($responseObjectError);
                    echo $json;
                    exit();
                }
            }
    
            for($i=0;$i<count($faltaslines);$i++){
                $faltascolumns = explode(",",$faltaslines[$i]);
                $sql = "INSERT INTO faltas (e_id,a_id) VALUES  ('$faltascolumns[0]','$faltascolumns[1]');";
                $result = mysqli_query($conn,$sql);
                if(!$result){
                    mysqli_rollback($conn);
                    $responseObjectError->success = false;
                    $responseObjectError->error = "Error inserting into faltas";
                    $responseObjectError->debug = mysqli_error($conn);
                    $json = json_encode($responseObjectError);
                    echo $json;
                    exit();
                }
            }
    
            for($i=0;$i<count($relatoriolines);$i++){
                $relatoriocolumns = explode(",",$relatoriolines[$i]);
                $sql = "INSERT INTO relatorio (r_comer,r_dormir,r_coment,r_necessidades,r_curativos,e_id,a_id) VALUES  ('$relatoriocolumns[0]','$relatoriocolumns[1]','$relatoriocolumns[2]','$relatoriocolumns[3]','$relatoriocolumns[4]','$relatoriocolumns[5]','$relatoriocolumns[6]');";
                $result = mysqli_query($conn,$sql);
                if(!$result){
                    mysqli_rollback($conn);
                    $responseObjectError->success = false;
                    $responseObjectError->error = "Error inserting into relatorio";
                    $responseObjectError->debug = mysqli_error($conn);
                    if ($conn -> warning_count) {
                        if ($result = $conn -> query("SHOW WARNINGS")) {
                          $row = $result -> fetch_row();
                          print_r($row);
                          $result -> close();
                        }
                      }
                    $json = json_encode($responseObjectError);
                    echo $json;
                    exit();
                }
            }

            
            for($i=0;$i<count($contemlines);$i++){
                $contemcolumns = explode(",",$contemlines[$i]);
                $sql = "INSERT INTO contem (al_id,e_id) VALUES  ('$contemcolumns[0]','$contemcolumns[1]');";
                $result = mysqli_query($conn,$sql);
                if(!$result){
                    mysqli_rollback($conn);
                    $responseObjectError->success = false;
                    $responseObjectError->error = "Error inserting into contem";
                    $responseObjectError->debug = mysqli_error($conn);
                    $json = json_encode($responseObjectError);
                    echo $json;
                    exit();
                }
            }
    
            mysqli_commit($conn);
            $responseObject->success = true;
            $json = json_encode($responseObject);
            echo $json;
            exit();

        }catch(mysqli_sql_exception $exception){
            mysqli_rollback($conn);
            $responseObjectError->success = false;
            $responseObjectError->error = "Exception error";
            $json = json_encode($responseObjectError);
            echo $json;
            exit();
        }
    break;
#105 - Não utilizar mais a base de dados
    case 105:
        if ($_POST['cs'] != 1){
            $responseObjectError->success = false;
            $responseObjectError->error = "Exception error";
            $json = json_encode($responseObjectError);
            echo $json;
            exit();
        }
        $nome = $_POST['id'];
        $sql = "UPDATE turmas SET t_utilizada = '0' WHERE u_id = '$nome';";
        $result = mysqli_query($conn,$sql);
        if(!$result){
            $responseObjectError->success = false;
            $responseObjectError->error = "Query error";
            $json = json_encode($responseObjectError);
            echo $json;
            exit();
        }
        $responseObject->success = true;
        $json = json_encode($responseObject);
        echo $json;
        exit();
    break;
#200 - Retornar a tabela dos educadores
    case 200:
        $id = $_POST['id'];
        $sql = "SELECT COUNT(u.u_nome) as c FROM users u INNER JOIN admin a ON ( u.u_id = a.u_id ) WHERE u.u_nome = '$id';";
        $result = mysqli_query($conn,$sql);
        if($result){
            if($row = mysqli_fetch_array($result,MYSQLI_ASSOC)){
                if($row['c'] == 1){
                    $sql = "SELECT u_id, u_nome, u_idade, u_morada, u_sexo, u_email, t_token FROM users u INNER JOIN turmas t ON ( u.u_id = t.u_id );";
                    $result = mysqli_query($conn,$sql);
                    if($result){
                        $responseObject->success = true;
                        $responseObject->table = "";
                        while($row = mysqli_fetch_array($result)){
                            $responseObject->table .= $row['u_id'].",".$row['u_nome'].",".$row['u_idade'].",".$row['u_morada'].",".$row['u_sexo'].",".$row['u_email'].",".$row['t_token'].";";
                        }
                        $json = json_encode($responseObject);
                        echo $json;
                    }
                }
            }
        }
    break;
    
#402 - Forgot password 
    case 402:
        //TODO tratar da parte da verificação do utilizador e da palavra passe

        

        /* Exception class. */
        require 'PHPMailer\src\Exception.php';

        /* The main PHPMailer class. */
        require 'PHPMailer\src\PHPMailer.php';

        /* SMTP class, needed if you want to use SMTP. */
        require 'PHPMailer\src\SMTP.php';

        $mail = new PHPMailer(TRUE);

        /* Open the try/catch block. */
        try {
            /* Set the mail sender. */
            //texto : relarório de atividade do educando 
            //Body: A professora enviou-lhe um relatório com informações acerca da atividade do aluno durante o dia 
            $mail->setFrom('teachersfriend@gmail.com', 'Teachers Friend');

            /* Add a recipient. */
            $mail->addAddress('palpatine@empire.com', 'Emperor');

            /* Set the subject. */
            $mail->Subject = 'Force';

            /* Set the mail message body. */
            $mail->Body = 'There is a great disturbance in the Force.';

            /* Finally send the mail. */
            $mail->send();
        }
        catch (Exception $e)
        {
            /* PHPMailer exception. */
            echo $e->errorMessage();
        }
        catch (\Exception $e)
        {
            /* PHP exception (note the backslash to select the global namespace Exception class). */
            echo $e->getMessage();
        }

    break;

}



