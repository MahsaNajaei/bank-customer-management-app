package ir.dotin.bank.cms.business.tools;

import ir.dotin.bank.cms.business.objects.entities.CustomerEntity;
import ir.dotin.bank.cms.business.objects.values.BankCustomer;
import ir.dotin.bank.cms.business.objects.values.CustomerType;
import ir.dotin.bank.cms.business.objects.values.LegalCustomer;
import ir.dotin.bank.cms.business.objects.values.RealCustomer;
import jakarta.servlet.http.HttpServletRequest;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class DataConversionHandler {

    public CustomerEntity convertBankCustomerToEntity(BankCustomer bankCustomer) {
        CustomerEntity customerEntity = new CustomerEntity();

        String name = null;
        Date date = null;
        String exclusiveId = null;

        if (customerEntity.getCustomerType().equalsIgnoreCase("real")) {

            RealCustomer realCustomer = (RealCustomer) bankCustomer;
            name = realCustomer.getName();
            date = realCustomer.getBirthDate();
            exclusiveId = realCustomer.getNationalCode();
            customerEntity.setParentName(realCustomer.getName());
            customerEntity.setSurname(realCustomer.getSurname());

        } else if (customerEntity.getCustomerType().equalsIgnoreCase("legal")) {

            LegalCustomer legalCustomer = (LegalCustomer) bankCustomer;
            name = legalCustomer.getCompanyName();
            date = legalCustomer.getRegistrationDate();
            exclusiveId = legalCustomer.getEconomicId();

        }

        customerEntity.setCustomerId(String.valueOf(bankCustomer.getCustomerId()));
        customerEntity.setCustomerType(bankCustomer.getCustomerType().name());
        customerEntity.setName(name);
        customerEntity.setDate(date);
        customerEntity.setExclusiveId(exclusiveId);

        return customerEntity;
    }

    public List<BankCustomer> convertCustomerEntitiesToBankCustomers(List<CustomerEntity> customerEntities) {
        List<BankCustomer> bankCustomers = new ArrayList<>();
        for (CustomerEntity customerEntity : customerEntities) {
            bankCustomers.add(convertCustomerEntityToBankCustomer(customerEntity));
        }
        return bankCustomers;
    }

    public BankCustomer convertCustomerEntityToBankCustomer(CustomerEntity customerEntity) {
        String customerType = customerEntity.getCustomerType();
        long customerId = Long.parseLong(customerEntity.getCustomerId());

        BankCustomer bankCustomer = null;
        if (customerType.equalsIgnoreCase("legal")) {
            bankCustomer = new LegalCustomer.Builder()
                    .companyName(customerEntity.getName())
                    .economicId(customerEntity.getExclusiveId())
                    .registrationDate(customerEntity.getDate())
                    .build(customerId, CustomerType.LEGAL);

        } else if (customerType.equalsIgnoreCase("real")) {

            bankCustomer = new RealCustomer.Builder()
                    .name(customerEntity.getName())
                    .surname(customerEntity.getSurname())
                    .fathersName(customerEntity.getParentName())
                    .birthDate(customerEntity.getDate())
                    .nationalCode(customerEntity.getExclusiveId())
                    .build(customerId, CustomerType.REAL);
        }

        return bankCustomer;
    }

    public BankCustomer getInitializedLegalCustomer(HttpServletRequest request, long customerId) {
        Date registrationDate = Date.valueOf(request.getParameter("year") + "-" + request.getParameter("month") + "-" + request.getParameter("day"));
        LegalCustomer legalCustomer = new LegalCustomer.Builder()
                .economicId(request.getParameter("economic-id"))
                .companyName(request.getParameter("company-name"))
                .registrationDate(registrationDate)
                .build(customerId, CustomerType.LEGAL);
        return legalCustomer;
    }

    public BankCustomer getInitializedRealCustomer(HttpServletRequest request, long customerId) {
        Date birthDate = Date.valueOf(request.getParameter("year") + "-" + request.getParameter("month") + "-" + request.getParameter("day"));
        RealCustomer realCustomer = new RealCustomer.Builder()
                .name(request.getParameter(request.getParameter("customer-name")))
                .nationalCode(request.getParameter("identity-number"))
                .fathersName(request.getParameter("fathers-name"))
                .surname(request.getParameter("surname"))
                .birthDate(birthDate)
                .build(customerId, CustomerType.REAL);
        return realCustomer;
    }
}
