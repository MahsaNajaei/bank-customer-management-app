<%@ page import="ir.dotin.bank.cms.business.dataobjects.values.customers.LegalCustomerVo" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="ir.dotin.bank.cms.business.dataobjects.values.customers.LegalCustomerVo" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html dir="rtl" lang="fa_IR">
<head>
    <meta charset="UTF-8">
    <link href="presentation/BCM.css" rel="stylesheet" type="text/css">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ویرایش مشتری</title>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <script type="text/javascript" src="presentation/script.js"></script>
    <script type="text/javascript" src="presentation/validation.js"></script>
</head>

<body>
<div id="app-title"> سامانه مدیریت امور بانکی </div>
<div id="edit_form_wrapper">
    <span id="edit_image"> </span>
    <span id="edit_form" style="transform: translateY(18%);">
        <%
            LegalCustomerVo legalCustomer = (LegalCustomerVo) request.getAttribute("customer");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(legalCustomer.getRegistrationDate());
        %>
        <script>
            $("#edit_form").load("presentation/reusableHTMLCodes/legal-person-form.html", function () {
                document.getElementsByClassName("submit-button")[0].value = "ویرایش";
                let innerHtml = document.getElementById("legal_form").innerHTML;
                document.getElementById("legal_form").innerHTML = "<lable> شماره مشتری <input type='text' value='<%=request.getParameter("customer-id")%>' name='customer-id' required readonly style=' margin-bottom:3%; color: #75BBDA'> </lable>" + innerHtml;
                document.getElementsByName("company-name")[0].value = "<%=legalCustomer.getCompanyName()%>";
                document.getElementsByName("economic-id")[0].value = <%=legalCustomer.getEconomicId()%>;
                document.getElementsByName("day")[0].value = <%= calendar.get(Calendar.DAY_OF_MONTH)%>;
                document.getElementsByName("month")[0].value = <%=calendar.get(Calendar.MONTH)+1%>;
                document.getElementsByName("year")[0].value = <%=calendar.get(Calendar.YEAR)%>;
                postLegalUpdateRequestOnSubmit();
            });
        </script>

    </span>
</div>

<a href="index.jsp" class="return_home_button" style="  width: 100%; display: inline-block; margin-top: 2%; ">
    <img alt="صفحه ی اصلی" src="presentation/images/home-icon-white.png" title="بازگشت به صفحه ی اصلی">
</a>
</body>
</html>
