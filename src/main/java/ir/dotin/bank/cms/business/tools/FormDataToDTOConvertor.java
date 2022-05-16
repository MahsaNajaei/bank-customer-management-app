package ir.dotin.bank.cms.business.tools;

import ir.dotin.bank.cms.business.dto.LegalCustomer;
import ir.dotin.bank.cms.business.dto.RealCustomer;
import jakarta.servlet.http.HttpServletRequest;

import java.sql.Date;

public class FormDataToDTOConvertor {

    public synchronized static void setLegalCustomerAttributes(HttpServletRequest request, LegalCustomer legalCustomer) {
        legalCustomer.setEconomicId(request.getParameter("economic-id"));
        legalCustomer.setCompanyName(request.getParameter("company-name"));
        legalCustomer.setRegistrationDate(Date.valueOf(request.getParameter("registration-year") + "-"
                + request.getParameter("registration-month") + "-" + request.getParameter("registration-day")));
    }

    public synchronized static void setRealCustomerAttributes(HttpServletRequest request, RealCustomer realCustomer) {
        realCustomer.setNationalCode(request.getParameter("identity-number"));
        realCustomer.setName(request.getParameter("customer-name"));
        realCustomer.setSurname(request.getParameter("surname"));
        realCustomer.setFathersName(request.getParameter("fathers-name"));
        realCustomer.setBirthDate(Date.valueOf(request.getParameter("birth-year") + "-"
                + request.getParameter("birth-month") + "-" + request.getParameter("birth-day")));
    }
}
