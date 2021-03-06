<%@ page import="ir.dotin.bank.cms.business.dataobjects.values.customers.BankCustomerVo" %>
<%@ page import="ir.dotin.bank.cms.business.dataobjects.values.customers.LegalCustomerVo" %>
<%@ page import="ir.dotin.bank.cms.business.dataobjects.values.customers.RealCustomerVo" %>
<%@ page import="java.util.List" %>
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
<div id="simple-blue-bg-wrapper">
    <div id="app-title"> سامانه مدیریت امور بانکی</div>
    <div id="result_table_wrapper">
        <%
            String searchQueryRequest = request.getQueryString();
            List<BankCustomerVo> legalCustomers = (List<BankCustomerVo>) request.getAttribute("legal-customers");
            List<BankCustomerVo> realCustomers = (List<BankCustomerVo>) request.getAttribute("real-customers");

            if (legalCustomers != null) {
        %>
        <h2> مشتریان حقوقی </h2>
        <table>
            <tr>
                <th> شماره مشتری</th>
                <th> نام شرکت</th>
                <th> تاریخ ثبت</th>
                <th> کد اقتصادی</th>
                <th colspan="2"><img src="presentation/images/settings-icon.png"></th>
            </tr>
            <%
                for (BankCustomerVo bankCustomer : legalCustomers) {
                    LegalCustomerVo legalCustomer = (LegalCustomerVo) bankCustomer;
            %>
            <tr>
                <td>
                    <%= legalCustomer.getCustomerId()%>
                </td>
                <td>
                    <%= legalCustomer.getCompanyName()%>
                </td>
                <td>
                    <%= legalCustomer.getRegistrationDate()%>
                </td>
                <td>
                    <%= legalCustomer.getEconomicId()%>
                </td>

                <td><a href="update?customer-id=<%= legalCustomer.getCustomerId()%>"> <img
                        src="presentation/images/edit-icon.png" title="ویرایش اطلاعات"> </a></td>

                <td><a href="delete?customer-id=<%= legalCustomer.getCustomerId() + "&" + searchQueryRequest %> "> <img
                        src="presentation/images/remove-icon.png" title="حذف مشتری"> </a></td>
            </tr>

            <%
                }
            %>
        </table>
        <%
            }

            if (realCustomers != null) {
        %>
        <h2> مشتریان حقیقی </h2>
        <table>
            <tr>
                <th> شماره مشتری</th>
                <th> نام</th>
                <th> نام خانوادگی</th>
                <th> کد ملی</th>
                <th> نام پدر</th>
                <th> تاریخ تولد</th>
                <th colspan="2"><img src="presentation/images/settings-icon.png"></th>
            </tr>
            <%
                for (BankCustomerVo bankCustomer : realCustomers) {
                    RealCustomerVo realCustomerVO = (RealCustomerVo) bankCustomer;
            %>
            <tr>
                <td>
                    <%= realCustomerVO.getCustomerId()%>
                </td>
                <td>
                    <%= realCustomerVO.getName()%>
                </td>
                <td>
                    <%= realCustomerVO.getSurname()%>
                </td>
                <td>
                    <%= realCustomerVO.getNationalCode()%>
                </td>
                <td>
                    <%= realCustomerVO.getFathersName()%>
                </td>
                <td>
                    <%= realCustomerVO.getBirthDate()%>
                </td>
                <td><a href="update?customer-id=<%= realCustomerVO.getCustomerId()%>"> <img
                        src="presentation/images/edit-icon.png" title="ویرایش اطلاعات"> </a></td>
                <td><a href="delete?customer-id=<%= realCustomerVO.getCustomerId() + "&" + searchQueryRequest %> "> <img
                        src="presentation/images/remove-icon.png" title="حذف مشتری"> </a></td>
            </tr>
            <%
                }
            %>
        </table>
        <%
            }
            if (legalCustomers == null && realCustomers == null) {
        %>
        <h2> هیچ موردی یافت نشد! </h2>
        <%
            }
        %>

    </div>

    <a href="index.jsp" class="return_home_button" style="  width: 100%; display: inline-block; margin-top: 1%;">
        <img alt="صفحه ی اصلی" src="presentation/images/home-icon-white.png" title="بازگشت به صفحه ی اصلی"> </a>
</div>
</body>
</html>