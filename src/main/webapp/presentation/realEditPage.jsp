<%@ page import="java.util.Calendar" %>
<%@ page import="ir.dotin.bank.cms.business.dataobjects.values.customers.RealCustomerVo" %>
<%@ page import="ir.dotin.bank.cms.business.dataobjects.values.customers.RealCustomerVo" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html dir="rtl" lang="fa_IR">
<head>
    <meta charset="UTF-8">
    <link href="presentation/BCM.css" rel="stylesheet" type="text/css">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title> ویرایش مشتری </title>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <script type="text/javascript" src="presentation/script.js"></script>
    <script type="text/javascript" src="presentation/validation.js"></script>
</head>

<body style=" background: url('presentation/images/background.jpg') fixed center / cover no-repeat;">

<%
    RealCustomerVo realCustomer = (RealCustomerVo) request.getAttribute("customer");
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(realCustomer.getBirthDate());
%>
<div id="app-title"> سامانه مدیریت امور بانکی</div>
<div id="popup"></div>
<div id="edit_form_wrapper" style="height:450px;">
    <span id="edit_image" style="height:450px;"> </span>
    <span id="edit_form" style="transform: translateY(7%);">

         <script>
            $("#edit_form").load("presentation/reusableHTMLCodes/real-person-form.html", function () {
                document.getElementsByClassName("submit-button")[0].value = "ویرایش";

                let innerHtml = document.getElementById("real_form").innerHTML;
                document.getElementById("real_form").innerHTML = "<lable> شماره مشتری <input type='text' value='<%=request.getParameter("customer-id")%>' name='customer-id' required readonly style=' margin-bottom:2%; color: #75BBDA'> </lable>" + innerHtml;

                document.getElementsByName("customer-name")[0].value = "<%=realCustomer.getName()%>";
                document.getElementsByName("surname")[0].value = "<%=realCustomer.getSurname()%>";
                document.getElementsByName("day")[0].value = <%= calendar.get(Calendar.DAY_OF_MONTH)%>;
                document.getElementsByName("month")[0].value = <%=calendar.get(Calendar.MONTH)+1%>;
                document.getElementsByName("year")[0].value = <%=calendar.get(Calendar.YEAR)%>;
                document.getElementsByName("fathers-name")[0].value = "<%= realCustomer.getFathersName()%>";
                document.getElementsByName("identity-number")[0].value = "<%= realCustomer.getNationalCode()%>";

                postRealUpdateRequestOnSubmit();
            });
        </script>

    </span>
</div>

<a href="index.jsp" class="return_home_button" style="  width: 100%; display: inline-block; margin-top: 2%; ">
    <img alt="صفحه ی اصلی" src="presentation/images/home-icon-white.png" title="بازگشت به صفحه ی اصلی">
</a>
</body>
</html>
