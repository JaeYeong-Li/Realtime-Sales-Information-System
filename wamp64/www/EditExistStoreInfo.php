<?php 

    error_reporting(E_ALL); 
    ini_set('display_errors',1); 

    include('dbcon.php');

    $android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");


    if( (($_SERVER['REQUEST_METHOD'] == 'POST') && isset($_POST['submit'])) || $android )
    {
        // 안드로이드 코드의 postParameters 변수에 적어준 이름을 가지고 값을 전달 받습니다.
        $storeName=$_POST['storeName'];
        $category=$_POST['category'];
        $lat=$_POST['lat'];
        $lang=$_POST['lang'];
        $address=$_POST['address'];
        $menu=$_POST['menu'];
        $openDate=$_POST['openDate'];
        $openTime=$_POST['openTime'];
        $ownerId=$_POST['ownerId'];
        $specialBool=$_POST['specialBool'];
        $specialTime=$_POST['specialTime'];
        $photoUrl1=$_POST['photoUrl1'];
        if(!isset($errMSG)) // id, pw, 이름 모두 입력이 되었다면 
        {
            try{
                // SQL문을 실행하여 데이터를 MySQL 서버의 person 테이블에 저장합니다. 
                $sql="UPDATE store SET storeName='$storeName', category='$category', lat='$lat', lang='$lang', address='$address', menu='$menu', openDate='$openDate', 
                openTime='$openTime', specialBool='$specialBool', specialTime='$specialTime', photoUrl1='$photoUrl1' where ownerId='$ownerId'";

                $stmt = $con->prepare($sql);
                
                if($stmt->execute())
                {
                    $successMSG = "가게정보를 수정했습니다.";
                }
                else
                {
                    $errMSG = "가게정보 수정 에러";
                }

            } catch(PDOException $e) {
                die("Database error: " . $e->getMessage()); 
            }
        }

    }

?>

<?php 
    if (isset($errMSG)) echo $errMSG;
    if (isset($successMSG)) echo $successMSG;

	$android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");
   
    if( !$android )
    {
?>
    <html>
       <body>

            <form action="<?php $_PHP_SELF ?>" method="POST">
                id: <input type = "text" name = "id" />
                pw: <input type = "text" name = "pw" />
                name: <input type = "text" name = "name" />
                <input type = "submit" name = "submit" />
            </form>
       
       </body>
    </html>

<?php 
    }
?>