<?php

include "./database.php";
include "./user.php";

function test_input($data) {
    $data = trim($data);
    $data = stripslashes($data);
    $data = htmlspecialchars($data);
    return $data;
}

function validate_date($date, $format = "Y-m-d") {
    return (bool)strtotime($date) && date($format, strtotime($date)) == $date;
}

function add_user_to_database(){
    global $HOST, $DB_NAME, $USERNAME, $PASSWORD;

    if ($_SERVER["REQUEST_METHOD"] != "POST") {
        return;
    }

    $err = false;

    $first_name = test_input($_POST["first_name"]);
    if (!preg_match("/[а-яА-Яa-zA-Z]+/", $first_name)) {
        echo "The first name should not be blank and should not contain any numbers or special symbols.</br>";
        $err = true;
    }

    $second_name = test_input($_POST["second_name"]);
    if (!preg_match("/[а-яА-Яa-zA-Z]+/", $second_name)) {
        echo "The second name should not be blank and should not contain any numbers or special symbols.</br>";
        $err = true;
    }

    $course_year = test_input($_POST["course_year"]);
    if (($course_year == "") || (!preg_match("/^[1-9][0-9]*$/", $course_year))) {
        echo "The course year should contain numeric values bigger than 0.</br>";
        $err = true;
    }

    $course_name = test_input($_POST["course_name"]);
    if (!preg_match("/[а-яА-Яa-zA-Z]+/", $course_name)) {
        echo "The course name should not be blank and should not contain any numbers or special symbols.</br>";
        $err = true;
    }

    $fn = test_input($_POST["fn"]);
    if (($fn == "") || (!preg_match("/^[1-9][0-9]*$/", $fn))) {
        echo "Please Enter Numeric Values bigger than 0.</br>";
        $err = true;
    }

    $group_number = test_input($_POST["group_number"]);
    if (($group_number == "") || (!preg_match("/^[1-9][0-9]*$/", $group_number))) {
        echo "Please Enter Numeric Values bigger than 0.</br>";
        $err = true;
    }

    $birth_date = test_input($_POST["birth_date"]);
    $current_date = date('Y-m-d');
    if (!validate_date($birth_date)) {
        echo "Invalid date format.</br>";
        $err = true;
    } else if ($birth_date > $current_date) {
        echo "Birth date cannot be bigger than the current date.</br>";
        $err = true;
    }

    $zodiac_sign = test_input($_POST["zodiac_sign"]);
    $valid_signs = ['capricorn','aquarius','pisces','aries','taurus','gemini','cancer','leo','virgo','libra','scorpio','sagittarius'];
    if (!in_array($zodiac_sign, $valid_signs)) {
        echo "Invalid zodiac sign.</br>";
        $err = true;
    }

    $link = test_input($_POST["link"]);
    if (($link == "") || (!preg_match("/https?:\/\/(?:www\.|(?!www))[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\.[^\s]{2,}|www\.[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\.[^\s]{2,}|https?:\/\/(?:www\.|(?!www))[a-zA-Z0-9]+\.[^\s]{2,}|www\.[a-zA-Z0-9]+\.[^\s]{2,}/", $link))) {
        echo "Invalid link address.</br>";
        $err = true;
    }

    $target_dir = "../images/";
    $target_file = $target_dir . time() . '_' . basename($_FILES["picture"]["name"]);

    if(isset($_POST["submit"])) {
        $check = getimagesize($_FILES["picture"]["tmp_name"]);
        if($check == false) {
            echo "Uploaded file is not a picture.</br>";
            $err = true;
        }
    }

    if ($_FILES["picture"]["size"] > 1024000) {
        echo "The picture is too large. Max upload size is 1MB.";
        $err = true;
    }

    $imageFileType = strtolower(pathinfo($target_file,PATHINFO_EXTENSION));
    if($imageFileType != "jpg" && $imageFileType != "png" && $imageFileType != "jpeg"
        && $imageFileType != "gif" ) {
        echo "Only JPG, JPEG, PNG & GIF files are allowed.";
        $err = true;
    }

    $motivation = test_input($_POST["motivation"]);
    if ($motivation == "") {
        echo "Motivation cannot be blank.</br>";
        $err = true;
    }

    if ($err) {
        echo "Did not fill out form correctly.";
        die();
    } else if (!move_uploaded_file($_FILES["picture"]["tmp_name"], $target_file)) {
        echo "There was an error uploading your picture.";
        die();
    }

    $user = new User();
    $user->first_name = $first_name;
    $user->second_name = $second_name;
    $user->course_year = $course_year;
    $user->course_name = $course_name;
    $user->fn = $fn;
    $user->group_number = $group_number;
    $user->birth_date = $birth_date;
    $user->zodiac_sign = $zodiac_sign;
    $user->link = $link;
    $user->picture = $target_file;
    $user->motivation = $motivation;

    $database = new Database($HOST, $DB_NAME, $USERNAME, $PASSWORD);
    $database->add_user($user);
}

add_user_to_database()
?>