<%@ page import="java.util.Calendar" %>
<%@ page import="ir.dotin.bank.cms.business.dto.NaturalCustomer" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html dir="rtl" lang="fa_IR">
<head>
    <meta charset="UTF-8">
    <link href="presentation/BCM.css" rel="stylesheet" type="text/css">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title> ویرایش مشتری </title>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
</head>

<body>

<%
    NaturalCustomer naturalCustomer = (NaturalCustomer) request.getAttribute("customer");
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(naturalCustomer.getBirthDate());
%>

<div id="app_title"></div>
<div id="edit_form_wrapper" style="height:450px;">
    <span id="edit_image" style="height:450px;"> </span>
    <span id="edit_form" style="transform: translateY(7%);">
        <form class="form_wrapper" method="post" action="update?customer-type=natural" accept-charset="UTF-8">
            <label> شماره مشتری <input type="text" value="<%=request.getParameter("customer-id")%>" name="customer-id"
                                       readonly style=" color:#75BBDA"> </label>
            <label> نام <input type="text" name="customer_name" value="<%=naturalCustomer.getName()%>" required
                               onchange="this.setCustomValidity('')"
                               oninvalid="showNullAlert(this)"> </label>
            <label> نام خانوادگی<input type="text" name="surname" value="<%=naturalCustomer.getSurname()%>" required
                                       onchange="this.setCustomValidity('')"
                                       oninvalid="showNullAlert(this)"> </label>
            <label>
                 تاریخ تولد
                <div class="date-wrapper">
                    <input type="text" name="birth_day" placeholder="روز"
                           value="<%= calendar.get(Calendar.DAY_OF_MONTH)%>" pattern="[1-9]|[1-2][0-9]|3[0-1]" required
                           oninvalid="showDayInvalidAlert(this)" onchange="this.setCustomValidity('')">
                    <input type="text" name="birth_month" placeholder="ماه" value="<%=calendar.get(Calendar.MONTH)+1%>"
                           required pattern="((0)?[1-9]|1[0-2])"
                           oninvalid="showMonthInvalidAlert(this)" onchange="this.setCustomValidity('') "
                           style="border-radius: 0;">
                    <input type="text" name="birth_year" placeholder="سال" value="<%=calendar.get(Calendar.YEAR)%>"
                           required pattern="[1-9][0-9]{3}"
                           oninvalid="showYearInvalidAlert(this)" onchange="this.setCustomValidity('')">
                </div>
            </label>

            <label> نام پدر<input type="text" name="fathers_name" value="<%=naturalCustomer.getFathersName()%>" required
                                  onchange="this.setCustomValidity('')" oninvalid="showNullAlert(this)"></label>

            <label> کد ملی <input type="text" name="identity_number" value="<%=naturalCustomer.getNationalCode()%>"
                                  pattern="[0-9]{10}" required onchange="this.setCustomValidity('')"
                                  oninvalid="showNationalCodeAlert(this)"> </label>

            <input class="submit-button" type="submit" value="ویرایش">
        </form>
    </span>
</div>

<a href="index.html" class="return_home_button" style="  width: 100%; display: inline-block; margin-top: 2%; ">
    <img alt="صفحه ی اصلی" src="presentation/images/home-icon-white.png" title="بازگشت به صفحه ی اصلی">
</a>
</body>
</html>
