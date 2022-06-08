<%@ page import="java.util.Map" %>
<%@ page import="ir.dotin.bank.cms.business.dataobjects.values.customers.BankCustomerVo" %>
<%@ page import="ir.dotin.bank.cms.business.dataobjects.values.customers.RealCustomerVo" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    BankCustomerVo bankCustomerVo = (BankCustomerVo) request.getAttribute("bank-customer");
%>

<!DOCTYPE html>
<html dir="rtl" lang="fa_IR">
<head>
    <meta charset="UTF-8">
    <link href="presentation/BCM.css" rel="stylesheet" type="text/css">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <script type="text/javascript" src="presentation/validation.js"></script>
    <script type="text/javascript" src="presentation/script.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <title> تشکیل پرونده تسهیلاتی </title>
</head>
<body>
<div id="loan-request-wrapper">

    <div id="loan-request-header">
        <img src="presentation/images/loan-request-header.jpg">
        <h1 id="loan-request-title"> تشکیل پرونده تسهیلاتی </h1>
    </div>

    <div id="loan-request-form-wrapper">
        <form action="get-customer-info">
            <input type="text" name="identity-number" required placeholder="کد ملی" maxlength="10" required
                   oninput="this.setCustomValidity('')"
                   value='<%= (request.getParameter("identity-number") == null)? "" : request.getParameter("identity-number") %>'>
            <input type="submit" class="dark-blue-submit-button" style="margin-bottom: 14%" value="بازیابی"
                   onclick="checkNationalCodeIntegrity();">
            <input type="text" readonly placeholder="نام"
                   value='<%= (bankCustomerVo == null)? "" : ((RealCustomerVo)bankCustomerVo).getName() %>'>
            <input type="text" readonly placeholder="نام خانوادگی"
                   value='<%= (bankCustomerVo == null) ? "" : ((RealCustomerVo)bankCustomerVo).getSurname()%>'>
        </form>

        <form id="loan-contract-request-form">
            <select name="loan-id">
                <option selected disabled> نوع تسهیلات</option>
                <%
                    Map<Integer, String> loanIdToNameMap = (Map<Integer, String>) request.getAttribute("loan-idName-map");
                    for (int id : loanIdToNameMap.keySet()) {
                %>
                <option value='<%= id %>'>
                    <%= loanIdToNameMap.get(id) %>
                </option>
                <%
                    }
                %>
            </select>

            <input class="simple-line-input" type="text" name="contract-duration" required
                   placeholder="مدت قرارداد (به سال)" maxlength="10" pattern="^[1-9][0-9]*$"
                   oninvalid="displayPositiveNumberValidationAlert(this)" oninput="this.setCustomValidity('')">
            <input class="simple-line-input" type="text" name="contract-amount" required
                   placeholder="مبلغ قرارداد (به ریال)" maxlength="25" pattern="^[1-9][0-9]*$"
                   oninvalid="displayPositiveNumberValidationAlert(this)" oninput="this.setCustomValidity('')">
            <input class="dark-blue-submit-button" style="margin-bottom: 0" type="submit" value="ثبت">
            <input type="hidden" id="customer-id" name="customer-id"
                   value="<%= (bankCustomerVo == null)? "" : bankCustomerVo.getCustomerId() %>">
        </form>
            <div id="popup"></div>
        <%
            if (request.getAttribute("server-message") != null) {
                String serverMessage = (String) request.getAttribute("server-message");
                System.out.println(serverMessage);
        %>
            <script>
                let message = '<%= serverMessage%>';
                showPopUpMessage(message);
            </script>
        <%
            }
        %>

        <a href="index.jsp" class="return_home_button"
           style=" position:absolute; top:100%; left: 50%; transform: translate(-50%, -150%); width: 100%;">
            <img alt="صفحه ی اصلی" src="presentation/images/home-icon-white.png" title="بازگشت به صفحه ی اصلی"> </a>
    </div>

</div>
</body>
</html>
<script>
    postLoanRequestOnSubmit();
</script>