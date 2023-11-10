<?php
    $data = json_decode(file_get_contents("php://input"), true);
    date_default_timezone_set('America/Mexico_City');

    if(isset($data["getArticles"]) && $data["getArticles"] == true){
        include_once("ConexionArticles.php");
         
        $stmt =$conexion ->prepare("SELECT * FROM article");
        $stmt -> execute();

        $response['articles'] = array();

        if(!$stmt){
            die("Acceso fallido a la base de datos:");
        }

        if($stmt){
            while($row = $stmt -> fetch()){
                $articleData = array();

              $articleData['id'] = $row['id'];
              $articleData['title'] = $row['title'];
              $articleData['autor'] = $row['autor'];
              $articleData['date'] = $row['date'];
              $articleData['content'] = $row['content'];
              
                array_push($response['articles'], $articleData);
            }

            $response ['success'] = 1;            
        }
        else{
              $articleData = array();
              
              $articleData['id'] = $articleData['id'];
              $articleData['title'] = null;
              $articleData['autor'] = null;
              $articleData['date'] = null;
              $articleData['content'] = null;             

            array_push($response['articles'], $articleData);

            $response['success'] = 0;
        }
        
        echo(json_encode($response, JSON_UNESCAPED_UNICODE));
        $conexion = null;
        $prepareSql = null;
        
    }

?>