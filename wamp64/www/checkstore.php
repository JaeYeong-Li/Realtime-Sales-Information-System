<?php  
error_reporting(E_ALL); 
ini_set('display_errors',1); 

include('dbcon.php');



//POST 값을 읽어온다.
$ownerId=isset($_POST['ownerId']) ? $_POST['ownerId'] : '';
$android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");


if ($ownerId != "" ){ 

    $sql="select * from store where ownerId='$ownerId'";
    $stmt = $con->prepare($sql);
    $stmt->execute();
 
    if ($stmt->rowCount() == 0){

        echo "'";
        echo $ownerId;
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
                'openTime'=>$row["openTime"],
                'photoUrl1'=>$row["photoUrl1"]
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
    echo "검색할 Name이 없습니다";
}

?>



<?php

$android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");

if (!$android){
?>

<html>
   <body>
   
      <form action="<?php $_PHP_SELF ?>" method="POST">
         나라 이름: <input type = "text" name = "country" />
         <input type = "submit" />
      </form>
   
   </body>
</html>
<?php
}

   
?>