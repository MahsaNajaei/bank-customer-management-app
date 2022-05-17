package ir.dotin.bank.cms.business.services;

import ir.dotin.bank.cms.business.exceptions.DuplicatedEconomicIdException;
import ir.dotin.bank.cms.business.exceptions.DuplicatedNationalCodeException;
import ir.dotin.bank.cms.business.objects.entities.CustomerEntity;
import ir.dotin.bank.cms.business.objects.values.BankCustomer;
import ir.dotin.bank.cms.business.objects.values.CustomerType;
import ir.dotin.bank.cms.business.tools.DataConversionHandler;
import ir.dotin.bank.cms.business.tools.UniqueIdGenerator;
import ir.dotin.bank.cms.business.validations.CustomerValidator;
import ir.dotin.bank.cms.dal.DefaultBankCustomerDAO;
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

            DataConversionHandler dataConversionHandler = new DataConversionHandler();
            BankCustomer bankCustomer = null;
            if (customerType.equals(CustomerType.LEGAL))
                bankCustomer = dataConversionHandler.getInitializedLegalCustomer(request, customerId);
            else if (customerType.equals(CustomerType.REAL))
                bankCustomer = dataConversionHandler.getInitializedRealCustomer(request, customerId);

            customerValidator.validateCustomer(bankCustomer);
            CustomerEntity customerEntity = dataConversionHandler.convertBankCustomerToEntity(bankCustomer);
            new DefaultBankCustomerDAO().addCustomer(customerEntity);
            //Todo check these lenes
            response.setContentType("text/plain");
            response.getWriter().println(customerId);
            response.getWriter().flush();

        } catch (DuplicatedEconomicIdException e) {
            response.getWriter().println(e.getMessage());
        } catch (DuplicatedNationalCodeException e) {
            response.getWriter().println(e.getMessage());
        } catch (SQLException e) {
            response.getWriter().println("Sorry a problem has occurred in server. Please try again!");
            e.printStackTrace();
        }
    }

    private CustomerType extractCustomerType(String servletPath) {
        if (servletPath.contains("legal"))
            return CustomerType.LEGAL;
        if (servletPath.contains("real"))
            return CustomerType.REAL;
        return null;
    }

}
