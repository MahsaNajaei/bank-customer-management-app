package ir.dotin.bank.cms.business.services;

import ir.dotin.bank.cms.business.dto.*;
import ir.dotin.bank.cms.business.exceptions.DuplicatedEconomicId;
import ir.dotin.bank.cms.business.exceptions.DuplicatedNationalCode;
import ir.dotin.bank.cms.business.tools.FormDataToDTOConvertor;
import ir.dotin.bank.cms.business.tools.UniqueIdGenerator;
import ir.dotin.bank.cms.business.validations.CustomerValidator;
import ir.dotin.bank.cms.dal.BankCustomerDAOFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "registrationServlet")
public class RegistrationServlet extends HttpServlet {

    private UniqueIdGenerator uniqueIdGenerator;
    private CustomerValidator customerValidator;

    @Override
    public void init() throws ServletException {
        super.init();
        uniqueIdGenerator = new UniqueIdGenerator(10);
        customerValidator = new CustomerValidator();

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            CustomerType customerType = extractCustomerType(request.getServletPath());
            long customerId = uniqueIdGenerator.generateNumberId();
            BankCustomer customer = new BankCustomerFactory().getBankCustomer(customerType, customerId);

            if (customerType.equals(CustomerType.LEGAL)) {
                customerValidator.checkEconomicIdUniqueness(request.getParameter("economic_id"));
                FormDataToDTOConvertor.setLegalCustomerAttributes(request, (LegalCustomer) customer);

            } else if (customerType.equals(CustomerType.NATURAL)) {
                customerValidator.checkNationalCodeUniqueness(request.getParameter("identity_number"));
                FormDataToDTOConvertor.setNaturalCustomerAttributes(request, (NaturalCustomer) customer);
            }

            new BankCustomerDAOFactory().getBankCustomerDAO(customerType).addCustomer(customer);
            response.setContentType("text/plain");
            response.getWriter().println(customerId);
            response.getWriter().flush();

        } catch (DuplicatedEconomicId e) {
            response.getWriter().println(e.getMessage());
        } catch (DuplicatedNationalCode e) {
            response.getWriter().println(e.getMessage());
        } catch (SQLException e) {
            response.getWriter().println("Sorry a problem has occurred in server. Please try again!");
            e.printStackTrace();
        }
    }

    private CustomerType extractCustomerType(String servletPath) {
        if (servletPath.contains("legal"))
            return CustomerType.LEGAL;
        if (servletPath.contains("natural"))
            return CustomerType.NATURAL;
        return null;
    }


}
