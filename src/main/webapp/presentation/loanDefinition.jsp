<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html dir="rtl" lang="fa_IR">
<head>
    <meta charset="UTF-8">
    <link href="BCM.css" rel="stylesheet" type="text/css">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <script type="text/javascript" src="validation.js"></script>
    <title> تعریف تسهیلات جدید </title>
</head>
<body>
<div id="loan-box-wrapper">

    <img id="loan-definition-bg" src="images/coins-background.jpg">

    <div id="loan-definition-content-wrapper">
        <h1> تعریف تسهیلات جدید </h1>
        <form action="../get-submitted-conditions">
            <input type="text" name="loan-name" placeholder="نام تسهیلات" required maxlength="25"
                   oninvalid="showNullAlert(this)" oninput="this.setCustomValidity('');">
            <input type="text" name="loan-interest-rate" pattern="^(0)?[0-9]([0-9])?( )?(%)?$|^100( )?(%)?$"
                   placeholder="نرخ سود به درصد" required maxlength="5" oninvalid="showWrongPercentageAlert(this)"
                   oninput="this.setCustomValidity('');">
            <input type="submit" name="loan-definition-submit" value="ادامه">
        </form>
    </div>

    <a href="../index.jsp" class="return_home_button"
       style=" position:absolute; top:100%; left: 50%; transform: translate(-50%, -180%); width: 100%;">
        <img alt="صفحه ی اصلی" src="images/home-icon-white.png" title="بازگشت به صفحه ی اصلی"> </a>
</div>
</body>
</html>