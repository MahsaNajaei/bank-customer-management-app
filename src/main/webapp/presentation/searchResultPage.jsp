<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html dir="rtl" lang="fa_IR">
<head>
    <meta charset="UTF-8">
    <link href="presentation/BCM.css" rel="stylesheet" type="text/css">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>نتایج جستجو</title>

</head>
<body>
<div id="app_title"></div>
<div id="result_table_wrapper">
    <table>
        <% String customerType = (String) request.getAttribute("customer-type");
            if (customerType == "legal") { %>
        <tr>
            <th> نام شرکت</th>
            <th> شماره مشتری</th>
            <th> تاریخ ثبت</th>
            <th> کد اقتصادی</th>
            <th colspan="2"><img src="presentation/images/settings-icon.png"></th>
        </tr>
            <%} else if (customerType == "natural") { %>
        <tr>
            <th>  کد ملی </th>
            <th> نام پدر </th>
            <th> نام خانوادگی </th>
            <th>  شماره مشتری </th>
            <th> نام </th>
            <th> تاریخ تولد </th>
            <th colspan="2"><img src="presentation/images/settings-icon.png"></th>
        </tr>
            <% } %>

        <%
            List<Map<String, String>> bankCustomers = (List) request.getAttribute("customer-list");

            for (Map<String, String> customerInfo : bankCustomers) {
                String customerTypeParam = "customer-type=" + customerType;
                String customerIdParam = "customer-id=" + customerInfo.get("customerId");
                String updateUrl = "update?" + customerIdParam + "&" + customerTypeParam;
                String deleteUrl = "delete?" + customerIdParam + "&" + customerTypeParam +
                        "&search-domain=" + request.getParameter("search-domain") +
                        "&search-content=" + request.getParameter("search-content");
        %>
        <tr>
            <%
                for (String key : customerInfo.keySet()) { %>
            <td>
                <%=customerInfo.get(key) %>
            </td>
            <% }
            %>
            <td><a href="<%=updateUrl%>"> <img src="presentation/images/edit-icon.png" title="ویرایش اطلاعات"> </a></td>
            <td><a href="<%=deleteUrl%>"> <img src="presentation/images/remove-icon.png" title="حذف مشتری"> </a></td>
            <% }
            %>
        </tr>
    </table>
</div>

<a href="index.html" class="return_home_button" style="  width: 100%; display: inline-block; margin-top: 2%; ">
    <img alt="صفحه ی اصلی" src="presentation/images/home-icon-white.png" title="بازگشت به صفحه ی اصلی"> </a>
</body>
</html>