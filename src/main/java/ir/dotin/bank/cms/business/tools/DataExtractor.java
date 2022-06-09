package ir.dotin.bank.cms.business.tools;

import ir.dotin.bank.cms.business.dataobjects.entities.LoanTypeEntity;
import ir.dotin.bank.cms.business.dataobjects.values.customers.BankCustomerVo;
import ir.dotin.bank.cms.business.dataobjects.values.customers.CustomerType;
import ir.dotin.bank.cms.business.dataobjects.values.customers.LegalCustomerVo;
import ir.dotin.bank.cms.business.dataobjects.values.customers.RealCustomerVo;
import ir.dotin.bank.cms.business.dataobjects.values.loans.GrantConditionVo;
import ir.dotin.bank.cms.business.dataobjects.values.loans.LoanTypeVo;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.sql.Date;
import java.util.*;

public class DataExtractor {

    public BankCustomerVo extractRealCustomerVoFromRequestParams(HttpServletRequest request, long customerId) {
        Date birthDate = Date.valueOf(getDateStringFromRequestParams(request));
        RealCustomerVo realCustomer = new RealCustomerVo.Builder()
                .name(request.getParameter("customer-name"))
                .nationalCode(request.getParameter("identity-number"))
                .fathersName(request.getParameter("fathers-name"))
                .surname(request.getParameter("surname"))
                .birthDate(birthDate)
                .build(customerId, CustomerType.REAL);
        return realCustomer;
    }

    public BankCustomerVo extractLegalCustomerVOFromRequestParams(HttpServletRequest request, long customerId) {
        Date registrationDate = Date.valueOf(getDateStringFromRequestParams(request));
        LegalCustomerVo legalCustomer = new LegalCustomerVo.Builder()
                .economicId(request.getParameter("economic-id"))
                .companyName(request.getParameter("company-name"))
                .registrationDate(registrationDate)
                .build(customerId, CustomerType.LEGAL);
        return legalCustomer;
    }

    private String getDateStringFromRequestParams(HttpServletRequest request) {
        String date = "";
        if (request.getParameter("year") != "")
            date = request.getParameter("year");
        if (request.getParameter("month") != "") {
            String month = request.getParameter("month");
            if (month.length() == 1) {
                month = 0 + month;
            }
            date += "-" + month + "-";
        }
        if (request.getParameter("day") != "") {
            String day = request.getParameter("day");
            if (day.length() == 1) {
                day = 0 + day;
            }
            date += day;
        }
        return date;
    }

    public Map<String, String> extractDbSearchKeyValuesFromRequestParams(HttpServletRequest request) throws IOException {
        Map<String, String> searchKeyValues = new HashMap<>();
        Properties formDbMappingProperties = new Properties();
        formDbMappingProperties.load(getClass().getResourceAsStream("/form-db-mapping.properties"));
        Enumeration requestParameterNames = request.getParameterNames();
        String date = null;
        while (requestParameterNames.hasMoreElements()) {
            String formInputName = requestParameterNames.nextElement().toString();
            String dbColumnName;
            String value;

            if (formInputName.equalsIgnoreCase("search-domain") && request.getParameter(formInputName).equalsIgnoreCase("all"))
                continue;

            if (formInputName.equalsIgnoreCase("day") || formInputName.equalsIgnoreCase("month") || formInputName.equalsIgnoreCase("year")) {
                if (date != null)
                    continue;
                dbColumnName = "date";
                value = date = getDateStringFromRequestParams(request);
            } else {
                dbColumnName = (String) formDbMappingProperties.get(formInputName);
                value = request.getParameter(formInputName);
            }

            if (value != "")
                searchKeyValues.put(dbColumnName, value);
        }
        return searchKeyValues;
    }

    public LoanTypeVo extractLoanTypeVoFromRequestParams(HttpServletRequest request) {
        String loanName = request.getParameter("loan-name");
        String interestRate = request.getParameter("loan-interest-rate").split("%")[0];
        List<GrantConditionVo> grantConditions = new ArrayList<>();
        LoanTypeVo loanType = new LoanTypeVo.Builder()
                .setName(loanName)
                .setInterestRate(interestRate)
                .setGrantConditions(grantConditions)
                .build();
        return loanType;
    }

    public GrantConditionVo extractGrantConditionVoFromRequestParams(HttpServletRequest request) {
        GrantConditionVo grantCondition = new GrantConditionVo.Builder()
                .setName(request.getParameter("condition-name"))
                .setMaxContractAmount(request.getParameter("max-contract-amount"))
                .setMinContractAmount(request.getParameter("min-contract-amount"))
                .setMaxContractDuration(request.getParameter("max-contract-duration"))
                .setMinContractDuration(request.getParameter("min-contract-duration"))
                .build();
        return grantCondition;
    }

    public Map<Integer, String> extractLoanIdToNameMapFromLoanTypeEntities(List<LoanTypeEntity> loanTypeEntities) {
        Map<Integer, String> loanIdoNameMap = new HashMap<>();
        for (LoanTypeEntity loanType : loanTypeEntities)
            loanIdoNameMap.put(loanType.getLoanId(), loanType.getName());

        return loanIdoNameMap;
    }

}
