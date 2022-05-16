package ir.dotin.bank.cms.business.tools;

import ir.dotin.bank.cms.business.dto.LegalCustomer;
import ir.dotin.bank.cms.business.dto.NaturalCustomer;
import jakarta.servlet.http.HttpServletRequest;

import java.sql.Date;

public class FormDataToDTOConvertor {

    public synchronized static void setLegalCustomerAttributes(HttpServletRequest request, LegalCustomer legalCustomer) {
        legalCustomer.setEconomicId(request.getParameter("economic_id"));
        legalCustomer.setCompanyName(request.getParameter("company_name"));
        legalCustomer.setRegistrationDate(Date.valueOf(request.getParameter("registration_year") + "-"
                + request.getParameter("registration_month") + "-" + request.getParameter("registration_day")));
    }
    public synchronized static void setNaturalCustomerAttributes(HttpServletRequest request, NaturalCustomer naturalCustomer) {
        naturalCustomer.setNationalCode(request.getParameter("identity_number"));
        naturalCustomer.setName(request.getParameter("name"));
        naturalCustomer.setSurname(request.getParameter("surname"));
        naturalCustomer.setFathersName(request.getParameter("fathers_name"));
        naturalCustomer.setBirthDate(Date.valueOf(request.getParameter("birth_year") + "-" +
                request.getParameter("birth_month") + "-" + request.getParameter("birth_day")));
    }
}
