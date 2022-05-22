package ir.dotin.bank.cms.business.tools;

import ir.dotin.bank.cms.business.dataobjects.entities.BankCustomerEntity;
import ir.dotin.bank.cms.business.dataobjects.values.BankCustomerVO;
import ir.dotin.bank.cms.business.dataobjects.values.CustomerType;
import ir.dotin.bank.cms.business.dataobjects.values.LegalCustomerVO;
import ir.dotin.bank.cms.business.dataobjects.values.RealCustomerVO;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.sql.Date;
import java.util.*;

public class DataMapper {

    public BankCustomerEntity convertBankCustomerVOToEntity(BankCustomerVO bankCustomerVO) {
        BankCustomerEntity bankCustomerEntity = new BankCustomerEntity();

        String name = null;
        Date date = null;
        String exclusiveId = null;

        if (bankCustomerVO.getCustomerType().equals(CustomerType.REAL)) {

            RealCustomerVO realCustomer = (RealCustomerVO) bankCustomerVO;
            name = realCustomer.getName();
            date = realCustomer.getBirthDate();
            exclusiveId = realCustomer.getNationalCode();
            bankCustomerEntity.setParentName(realCustomer.getFathersName());
            bankCustomerEntity.setSurname(realCustomer.getSurname());

        } else if (bankCustomerVO.getCustomerType().equals(CustomerType.LEGAL)) {

            LegalCustomerVO legalCustomer = (LegalCustomerVO) bankCustomerVO;
            name = legalCustomer.getCompanyName();
            date = legalCustomer.getRegistrationDate();
            exclusiveId = legalCustomer.getEconomicId();

        }

        bankCustomerEntity.setCustomerId(String.valueOf(bankCustomerVO.getCustomerId()));
        bankCustomerEntity.setCustomerType(bankCustomerVO.getCustomerType().name());
        bankCustomerEntity.setName(name);
        bankCustomerEntity.setDate(date);
        bankCustomerEntity.setExclusiveId(exclusiveId);

        return bankCustomerEntity;
    }

    public Map<CustomerType, List<BankCustomerVO>> getCustomerTypeToBankCustomersMapFromEntities(List<BankCustomerEntity> customerEntities) {
        Map<CustomerType, List<BankCustomerVO>> customerTypeToBankCustomersMap = new HashMap<>();

        List<BankCustomerVO> legalCustomerVOS = new ArrayList<>();
        List<BankCustomerVO> realCustomerVOS = new ArrayList<>();
        for (BankCustomerEntity bankCustomerEntity : customerEntities) {
            BankCustomerVO bankCustomerVO = convertCustomerEntityToBankCustomerVO(bankCustomerEntity);
            if (bankCustomerVO.getCustomerType().equals(CustomerType.REAL))
                realCustomerVOS.add(bankCustomerVO);
            else if (bankCustomerVO.getCustomerType().equals(CustomerType.LEGAL)) {
                legalCustomerVOS.add(bankCustomerVO);
            }
        }

        if (legalCustomerVOS.size() != 0)
            customerTypeToBankCustomersMap.put(CustomerType.LEGAL, legalCustomerVOS);
        if (realCustomerVOS.size() != 0)
            customerTypeToBankCustomersMap.put(CustomerType.REAL, realCustomerVOS);

        return customerTypeToBankCustomersMap;

    }

    public BankCustomerVO convertCustomerEntityToBankCustomerVO(BankCustomerEntity bankCustomerEntity) {
        String customerType = bankCustomerEntity.getCustomerType();
        long customerId = Long.parseLong(bankCustomerEntity.getCustomerId());

        BankCustomerVO bankCustomerVO = null;
        if (customerType.equalsIgnoreCase("legal")) {
            bankCustomerVO = new LegalCustomerVO.Builder()
                    .companyName(bankCustomerEntity.getName())
                    .economicId(bankCustomerEntity.getExclusiveId())
                    .registrationDate(bankCustomerEntity.getDate())
                    .build(customerId, CustomerType.LEGAL);

        } else if (customerType.equalsIgnoreCase("real")) {

            bankCustomerVO = new RealCustomerVO.Builder()
                    .name(bankCustomerEntity.getName())
                    .surname(bankCustomerEntity.getSurname())
                    .fathersName(bankCustomerEntity.getParentName())
                    .birthDate(bankCustomerEntity.getDate())
                    .nationalCode(bankCustomerEntity.getExclusiveId())
                    .build(customerId, CustomerType.REAL);
        }

        return bankCustomerVO;
    }

    public BankCustomerVO mapRequestParamsToLegalCustomerVO(HttpServletRequest request, long customerId) {
        Date registrationDate = Date.valueOf(getDateStringFromRequestParams(request));
        LegalCustomerVO legalCustomer = new LegalCustomerVO.Builder()
                .economicId(request.getParameter("economic-id"))
                .companyName(request.getParameter("company-name"))
                .registrationDate(registrationDate)
                .build(customerId, CustomerType.LEGAL);
        return legalCustomer;
    }

    public BankCustomerVO mapRequestParamsToRealCustomerVO(HttpServletRequest request, long customerId) {
        Date birthDate = Date.valueOf(getDateStringFromRequestParams(request));
        RealCustomerVO realCustomer = new RealCustomerVO.Builder()
                .name(request.getParameter("customer-name"))
                .nationalCode(request.getParameter("identity-number"))
                .fathersName(request.getParameter("fathers-name"))
                .surname(request.getParameter("surname"))
                .birthDate(birthDate)
                .build(customerId, CustomerType.REAL);
        return realCustomer;
    }

    public Map<String, String> mapRequestParamsToSearchKeyValues(HttpServletRequest request) throws IOException {
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
                date = getDateStringFromRequestParams(request);
                value = date.replace("-[1-9]-", "-0[1-9]-");
            } else {
                dbColumnName = (String) formDbMappingProperties.get(formInputName);
                value = request.getParameter(formInputName);
            }

            if (value != "")
                searchKeyValues.put(dbColumnName, value);


        }
        return searchKeyValues;
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

}

