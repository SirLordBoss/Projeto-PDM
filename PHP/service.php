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

$responseObject;
//if a user does not exist create one

$firstquery = "SELECT COUNT(u_nome) as cnt FROM users;";
$result = mysqli_query($conn,$firstquery);
if($result){
    if($row = mysqli_fetch_array($result, MYSQLI_ASSOC)){
        if($row['cnt'] == 0){
            $pwd = 'admin';
            $pwd_hashed = hash('md5',$pwd);
            $sql = "INSERT INTO users (u_nome,u_email,u_idade,u_morada,u_pwd,u_sexo) VALUES ('admin','admin@email.com','18','Morada provisória','".$pwd_hashed."','1');";
            $result = mysqli_query($conn,$sql);
            if($result){
                $responseObject->success = true;
                $json = json_encode($responseObject);
                echo $json;
                exit();
            }else{
                $responseObject->success = false;
                $json = json_encode($responseObject);
                echo $json;
                exit();
            }
        } 
    }
}

switch ($_POST['q']){
    case 100: // caso seja para fazer um login (educadoras)
        $username = $_POST['u'];
        $password = $_POST['p'];
        $sql = "SELECT u_id FROM users WHERE u_nome = '$username' AND u_pwd = '$password';";
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
                                $responseObject->success = false;
                                $responseObject->error = "Username does not contain a class";
                                $json = json_encode($responseObject);
                                echo $json;
                                mysqli_rollback($conn);
                                exit();
                            }
                        }else{
                            $responseObject->success = false;
                            $responseObject->error = "Database query error";
                            $json = json_encode($responseObject);
                            echo $json;
                            mysqli_rollback($conn);
                            exit();
                        }
                        
                        mysqli_commit($conn);
                        exit();
                    } catch (mysqli_sql_exception $exception) {
                        mysqli_rollback($conn);
                        $responseObject->success = false;
                        $json = json_encode($responseObject);
                        echo $json;
                        exit();
                    }
                }
            }else{
                $responseObject->success = false;
                $responseObject->error = "Username/password not match";
                $json = json_encode($responseObject);
                echo $json;
                exit();
            }
        }
    break;
    case 101:
        
    break;

}



