<?php
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
#050
    case 50: //Registo
        
    $nome = $_POST['nome'];
    $idade = $_POST['idade'];
    $morada = $_POST['morada'];
    $sexo = $_POST['sexo'];
    $email = $_POST['email'];
    $pwd = $_POST['pass'];
    $sql = "INSERT INTO to_regist (tr_email,tr_sexo,tr_morada,tr_idade,tr_nome,tr_pwd) VALUES ('$email','$sexo','$morada','$idade','$nome','$password')";
    $result = mysqli_query($conn,$sql);
    if(!$result){
        $responseObjectError->success = false;
        $responseObjectError->error = "Data not inserted";
        $json = json_encode($responseObjectError);
        echo $json;
        exit();
    }
    $responseObject->success = true;
    $json = json_encode($responseObject);
    echo $json;
    exit();

    break;
#100
    case 100: // caso seja para fazer um login (educadoras)
        $username = $_POST['u'];
        $password = $_POST['p'];
        //só faz login se não for administrador
        $sql = "SELECT u_id FROM users WHERE u_nome = '$username' AND u_pwd = '$password' AND NOT EXISTS ( SELECT * FROM admin a WHERE u.u_id = a.u_id ) ;";
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
                                        $responseObject->relatorio .= "".$row['e_id'].",".$row['a_id'].",".$row['r_curativos'].",".$row['r_necessidades'].",".$row['r_coment'].",".$row['r_dormir'].",".$row['r_comer'].";";
                                    }                                    
                                }else{
                                    $responseObjectError->success = false;
                                    $responseObjectError->error = "Database query error : faltas";
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
                                    $responseObjectError->error = "Database query error : faltas";
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
                        $json = json_encode($responseObjectError);
                        echo $json;
                        exit();
                    }
                }
            }else{
                $responseObjectError->success = false;
                $responseObjectError->error = "Username/password not match";
                $json = json_encode($responseObjectError);
                echo $json;
                exit();
            }
        }else{
            $responseObjectError->success = false;
            $responseObjectError->error = "Username/password not match";
            $json = json_encode($responseObjectError);
            echo $json;
            exit();
        }
    break;
#101
    case 101: //adicionar um utilizador (educadora)
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
#102
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
}



