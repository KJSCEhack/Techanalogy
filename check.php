<?php
include "includes/connect.php";
$mac=$_POST['mac'];
$name=$_POST['name'];
$rssi=$_POST['rssi'];
echo $mac;

$sql="select * from users where mac='$mac'";
$pass_sql=mysqli_query($con,$sql);

$mnum=mysqli_num_rows($pass_sql);
if($mnum>0){
echo $mac."is present.";

$query="update users set verify='1'	 where mac='$mac'";
$pass_query=mysqli_query($con,$query);

$query2="update users set rssi='$rssi' where mac='$mac'";
$pass_query2=mysqli_query($con,$query2);
}
else{
echo "No device found.";	
}
?>