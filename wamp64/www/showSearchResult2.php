<?php  
error_reporting(E_ALL); 
ini_set('display_errors',1); 

include('dbcon.php');
error_reporting(E_ALL ^ E_DEPRECATED);



//POST 값을 읽어온다.
$category=isset($_POST['category']) ? $_POST['category'] : '';
$android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");


if ($category != "" ){ 

    $sql="select * from store where category='$category'";
    $stmt = $con->prepare($sql);
    $stmt->execute();
 
    if ($stmt->rowCount() == 0){

        echo "'";
        echo $country,", ",$name;
        echo "'은 찾을 수 없습니다.";
    }
	else{

   		$data = array(); 

        while($row=$stmt->fetch(PDO::FETCH_ASSOC)){


            extract($row);

            array_push($data, 
                array('storeId'=>$row["storeId"],
                'storeName'=>$row["storeName"],
                'category'=>$row["category"],
                'lat'=>$row["lat"],
                'lang'=>$row["lang"],
                'address'=>$row["address"],
                'menu'=>$row["menu"],
                'openDate'=>$row["openDate"],
                'openTime'=>$row["openTime"]             
            ));
        }


        if (!$android) {
            echo "<pre>"; 
            print_r($data); 
            echo '</pre>';
        }else
        {
            header('Content-Type: application/json; charset=utf8');
            $json = json_encode(array("storeInfo"=>$data), JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
            echo $json;
        }
    }
}
else {
    echo "등록된 가게가 없습니다";
}

?>



<?php

$android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");

if (!$android){
?>

<html>
   <body>
   
      <form action="<?php $_PHP_SELF ?>" method="POST">
         id: <input type = "text" name = "id" />
         pw: <input type = "text" name = "pw" />
         <input type = "submit" />
      </form>
   
   </body>
</html>
<?php
}

   
?>