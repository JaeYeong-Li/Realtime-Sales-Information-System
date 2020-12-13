<?php  
error_reporting(E_ALL); 
ini_set('display_errors',1); 

include('dbcon.php');



//POST 값을 읽어온다.
//$openTime=isset($_POST['openTime']) ? $_POST['openTime'] : '';
$android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");



//$sql="select * from person where country='$country'";
$sql="select * from store";
$stmt = $con->prepare($sql);
$stmt->execute();
 
if ($stmt->rowCount() == 0){

    //echo $country;
    echo "가게를 찾을 수 없습니다.";
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