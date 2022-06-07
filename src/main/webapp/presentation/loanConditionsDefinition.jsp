<%@ page import="ir.dotin.bank.cms.business.dataobjects.values.loans.GrantConditionVo" %>
<%@ page import="java.util.List" %>
<%@ page import="ir.dotin.bank.cms.business.dataobjects.values.loans.LoanTypeVo" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html dir="rtl" lang="fa_IR">
<head>
    <meta charset="UTF-8">
    <link href="BCM.css" rel="stylesheet" type="text/css">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <script type="text/javascript" src="script.js"></script>
    <script type="text/javascript" src="validation.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <title> تعریف شروط اعطا </title>
</head>
<body style="background: #dddbdb">

<div id="loan-box-wrapper">
    <div id="loan-conditions-header">
        <img src="images/loan-definition-header.jpg">
        <h1> تعریف <br> شروط اعطاء </h1>
    </div>
    <div id="conditions-form-wrapper">
        <p>
            توجه: برای تعریف تسهیلات جدید باید حداقل یک شرط اعطا برای آن تعریف کنید!
        </p>

        <form id="loan-conditions-form" action="../get-submitted-conditions">
            <input type="text" name="condition-name" placeholder="نام شرط" maxlength="25"
                   oninvalid="showNullAlert(this)"
                   oninput="this.setCustomValidity('')" required>
            <input type="text" name="min-contract-duration" pattern="^[1-9][0-9]*$"
                   placeholder="حداقل مدت قرارداد (به سال) " maxlength="10"
                   oninvalid="displayPositiveNumberValidationAlert(this)"
                   oninput="this.setCustomValidity('')" required>
            <input type="text" name="max-contract-duration" pattern="^[1-9][0-9]*$"
                   placeholder="حداکثر مدت قرارداد (به سال)" maxlength="10"
                   oninvalid="displayPositiveNumberValidationAlert(this)"
                   oninput="this.setCustomValidity('')" required>
            <input type="text" name="min-contract-amount" pattern="^[1-9][0-9]*$"
                   placeholder="حداقل مبلغ قرارداد (به ریال)" maxlength="20"
                   oninvalid="displayPositiveNumberValidationAlert(this)"
                   oninput="this.setCustomValidity('')" required>
            <input type="text" name="max-contract-amount" pattern="^[1-9][0-9]*$"
                   placeholder="حداکثر مبلغ قرارداد (به ریال)" maxlength="20"
                   oninvalid="displayPositiveNumberValidationAlert(this)"
                   oninput="this.setCustomValidity('')" required>
            <input type="hidden" name="display-Section-id" value="conditions-display-wrapper">
            <input class="dark-blue-submit-button" type="submit" value="ثبت" onclick="checkConditionDataIntegrity()">
        </form>

    </div>

    <div id="conditions-display-wrapper">
        <table>
            <tr>
                <th>نام شرط</th>
                <th>حداقل مدت قرارداد</th>
                <th>حداکثر مدت قرارداد</th>
                <th>حداقل مبلغ قرارداد</th>
                <th>حداکثر مبلغ قرارداد</th>
            </tr>

            <%
                if (request.getSession().getAttribute("loan-type") != null) {
                    LoanTypeVo loanType = (LoanTypeVo) request.getSession().getAttribute("loan-type");
                    if (loanType.getGrantConditions().size() != 0) {
                        List<GrantConditionVo> grantConditions = loanType.getGrantConditions();
                        for (GrantConditionVo grantCondition :
                                grantConditions) {
            %>

            <tr>
                <td>
                    <%= grantCondition.getName()%>
                </td>
                <td>
                    <%= grantCondition.getMinContractDuration() %>
                </td>
                <td>
                    <%= grantCondition.getMaxContractDuration() %>
                </td>
                <td>
                    <%= grantCondition.getMinContractAmount() %>
                </td>
                <td>
                    <%= grantCondition.getMaxContractAmount() %>
                </td>
            </tr>

            <%
                    }
                }
            %>
        </table>

        <%
            if (loanType.getGrantConditions().size() == 0) {
        %>
        <p> هیچ شرطی وارد نشده است! </p>
        <%
                }
            }
        %>

        <button type="submit" id="loan-registration-submit" class="rectangle-blue-button" onclick="postLoanType()"> ثبت
            تسهیلات
        </button>
        <div id="popup" style="position: relative;"></div>
    </div>
    <div id="curved-footer">
        <img src="images/curved-footer.png">
        <img src="images/dotin-icon.png">
    </div>

</div>

</body>
</html>
