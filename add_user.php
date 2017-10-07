<?php
include "../includes/connect.php";
$name=$_POST['name'];
$email=$_POST['email'];
$fname=$_POST['fname'];
$lname=$_POST['lname'];
$address=$_POST['address'];
$city=$_POST['city'];
$mac=$_POST['mac'];
$sql="insert into users(name,email,firstname,lastname,address,city,mac) values('$name','$email','$fname','$lname','$address','$city','$mac')";
$pass_sql=mysqli_query($con,$sql);

?>