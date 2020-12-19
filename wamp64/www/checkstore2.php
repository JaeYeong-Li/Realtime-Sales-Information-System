<?php  
error_reporting(E_ALL); 
ini_set('display_errors',1); 

include('dbcon.php');



//POST 값을 읽어온다.
$storeName=isset($_POST['storeName']) ? $_POST['storeName'] : '';
$android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");


if ($storeName != "" ){ 

    $sql="select * from store where storeName='$storeName'";
    $stmt = $con->prepare($sql);
    $stmt->execute();
 
    if ($stmt->rowCount() == 0){

        echo "'";
        echo $storeName;
        echo "'은 찾을 수 없습니다.";
    }
	else{

   		$data = array(); 

        while($row=$stmt->fetch(PDO::FETCH_ASSOC)){

        	extract($row);

            array_push($data, 
                array('storeId'=>$row["storeId"],
                'address'=>$row["address"],
                'menu'=>$row["menu"],
                'openTime'=>$row["openTime"],
                'openDate'=>$row["openDate"],
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
    echo "검색할 id가 없습니다";
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

   
?><?php  
error_reporting(E_ALL); 
ini_set('display_errors',1); 

include('dbcon.php');



//POST 값을 읽어온다.
$storeId=isset($_POST['storeId']) ? $_POST['storeId'] : '';
$android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");


if ($storeId!= "" ){ 

    $sql="select * from store where storeId='$storeId'";
    $stmt = $con->prepare($sql);
    $stmt->execute();
 
    if ($stmt->rowCount() == 0){

        echo "'";
        echo $storeId;
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
    echo "검색할 id가 없습니다";
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
