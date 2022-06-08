<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html dir="rtl" lang="fa_IR">
<head>
    <meta charset="UTF-8">
    <link href="BCM.css" rel="stylesheet" type="text/css">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <script type="text/javascript" src="script.js"></script>
    <script type="text/javascript" src="validation.js"></script>
    <title> ثبت نام مشتری حقیقی </title>
</head>
<body>
<div id="simple-blue-bg-wrapper">
    <div id="app-title"> سامانه مدیریت امور بانکی</div>
    <div id="popup"></div>
    <div id="gray_form_wrapper">

        <a href="../index.jsp" class="return_home_button" style="">
            <img alt="صفحه ی اصلی" src="images/home-icon-navy.png" title="بازگشت به صفحه ی اصلی"> </a>

        <div id="form_title_wrapper"> تعریف مشتری جدید</div>

        <div id="registration_form_wrapper" class="form_content">
            <script>
                $("#registration_form_wrapper").load("reusableHTMLCodes/real-person-form.html", function () {
                    postRealRegistrationRequestOnSubmit();
                })
            </script>
        </div>

    </div>

</div>
</body>
</html>