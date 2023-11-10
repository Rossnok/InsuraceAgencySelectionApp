<?php
     $data = json_decode(file_get_contents("php://input"), true);
     date_default_timezone_set('America/Mexico_City');

    if(isset($data["insertArticle"]) && $data["insertArticle"] == true){
        
        include_once("ConexionArticles.php");
        
         $values = [
            "id" => 0,
            "title" => $data['title'],
            "autor" => $data['autor'],
            "date" => $data['date'],
            "content" => $data['content']];
        
        $stmt =  $conexion -> prepare("INSERT INTO article(
            id,
            title,
            autor,
            date,
            content
            )
            VALUES (
                    :id,
                    :title,
                    :autor,
                    :date,
                    :content)");
       
            
      if(!$stmt){
            $response['success'] = 0;
        }else if(!$stmt -> execute($values)){
           $response['success'] = 0;
        }else{
            $response['success'] = 1;
        }
       
        
        echo (json_encode($response, JSON_UNESCAPED_UNICODE));
        $stmt = null;
        $conexion = null;
    }
?>