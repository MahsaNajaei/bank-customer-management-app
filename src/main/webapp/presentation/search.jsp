<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html dir="rtl" lang="fa_IR">
<head>
    <meta charset="UTF-8">
    <link href="BCM.css" rel="stylesheet" type="text/css">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <script type="text/javascript" src="script.js"></script>
    <title> جستجو </title>
</head>
<body>
<div id="simple-blue-bg-wrapper">
    <div id="app-title"> سامانه مدیریت امور بانکی</div>

    <div id="gray_form_wrapper">
        <div id="form_title_wrapper"> جستجوی مشتری</div>

        <a href="../index.jsp" class="return_home_button" style="">
            <img alt="صفحه ی اصلی" src="images/home-icon-navy.png" title="بازگشت به صفحه ی اصلی"> </a>

        <div id="search_domain_wrapper">
            <content type="text"> نوع مشتری</content>
            <select name="search-domain">
                <option value="real"> مشتری حقیقی</option>
                <option value="legal"> مشتری حقوقی</option>
                <option value="all"> تمام مشتریان</option>
            </select>
        </div>

        <div id="search_form_wrapper">
            <script>
                loadSearchFormBasedOnSelection();
            </script>
        </div>

    </div>
</div>

</body>
</html>

