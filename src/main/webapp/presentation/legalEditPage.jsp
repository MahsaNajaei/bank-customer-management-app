<%@ page import="ir.dotin.bank.cms.business.dto.LegalCustomer" %>
<%@ page import="java.util.Calendar" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html dir="rtl" lang="fa_IR">
<head>
    <meta charset="UTF-8">
    <link href="presentation/BCM.css" rel="stylesheet" type="text/css">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ویرایش مشتری</title>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
</head>

<body>
<div id="app_title"></div>
<div id="edit_form_wrapper">
    <span id="edit_image"> </span>
    <span id="edit_form" style="transform: translateY(18%);">
        <form class="form_wrapper" action="update?customer-type=legal" method="post" accept-charset="UTF-8">
            <label> شماره مشتری <input type="text" value="<%=request.getParameter("customer-id")%>" name="customer-id"
                                       readonly style=" color:#75BBDA"> </label>
            <label> نام شرکت <input type="text" name="company_name" required oninvalid="showNullAlert(this)"> </label>
            <label> تاریخ ثبت
                <div class="date-wrapper">
                    <input type="text" name="registration_day" placeholder="روز" pattern="[1-9]|[1-2][0-9]|3[0-1]"
                           required oninvalid="showDayInvalidAlert(this)" onchange="this.setCustomValidity('')">
                    <input type="text" name="registration_month" placeholder="ماه" required pattern="((0)?[1-9]|1[0-2])"
                           oninvalid="showMonthInvalidAlert(this)" onchange="this.setCustomValidity('') "
                           style="border-radius: 0;">
                    <input type="text" name="registration_year" placeholder="سال" required pattern="[1-9][0-9]{3}"
                           oninvalid="showYearInvalidAlert(this)" onchange="this.setCustomValidity('')">
                </div>
            </label>

            <label> کد اقتصادی <input type="text" name="economic_id" pattern="[0-9]{12}" required
                                      oninvalid="showEconomicCodeAlert(this)"> </label>
            <input type="submit" class="submit-button" value="ویرایش">
        </form>
        <%
            LegalCustomer legalCustomer = (LegalCustomer) request.getAttribute("customer");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(legalCustomer.getRegistrationDate());
        %>
        <script>
            document.getElementsByName("company_name")[0].value = "<%=legalCustomer.getCompanyName()%>";
            document.getElementsByName("economic_id")[0].value = <%=legalCustomer.getEconomicId()%>;
            document.getElementsByName("registration_day")[0].value = <%= calendar.get(Calendar.DAY_OF_MONTH)%>;
            document.getElementsByName("registration_month")[0].value = <%=calendar.get(Calendar.MONTH)+1%>;
            document.getElementsByName("registration_year")[0].value = <%=calendar.get(Calendar.YEAR)%>;
        </script>
    </span>
</div>

<a href="index.html" class="return_home_button" style="  width: 100%; display: inline-block; margin-top: 2%; ">
    <img alt="صفحه ی اصلی" src="presentation/images/home-icon-white.png" title="بازگشت به صفحه ی اصلی">
</a>
</body>
</html>
