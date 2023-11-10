<?php
    try{
        
        $conexion = new PDO("mysql:host=localhost; dbname=replace_db_name_here",
        "replace_db_user_here",
        "replace_db_password_here");
        
    }catch(PDOException $e){
        die("Error en la conexion".$e -> getMessage());
    }
        
?>