package ir.dotin.bank.cms.business.services;

import ir.dotin.bank.cms.business.exceptions.DuplicatedEconomicIdException;
import ir.dotin.bank.cms.business.exceptions.DuplicatedNationalCodeException;
import ir.dotin.bank.cms.business.objects.entities.CustomerEntity;
import ir.dotin.bank.cms.business.objects.values.*;
import ir.dotin.bank.cms.business.tools.DataConversionHandler;
import ir.dotin.bank.cms.business.validations.CustomerValidator;
import ir.dotin.bank.cms.dal.DefaultBankCustomerDAO;
import ir.dotin.bank.cms.dal.exceptions.CustomerIdDoesNotExistsException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "updateServlet")
public class UpdateServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String customerId = request.getParameter("customer-id");
            CustomerType customerType = CustomerType.valueOf(request.getParameter("customer-type").toUpperCase());

            CustomerEntity customerEntity = new DefaultBankCustomerDAO().retrieveCustomerById(customerId);
            BankCustomer bankCustomer = new DataConversionHandler().convertCustomerEntityToBankCustomer(customerEntity);

            request.setAttribute("customer", bankCustomer);
            if (CustomerType.REAL == customerType)
                request.getRequestDispatcher("/presentation/realEditPage.jsp").include(request, response);
            else if (CustomerType.LEGAL == customerType)
                request.getRequestDispatcher("/presentation/legalEditPage.jsp").include(request, response);

        } catch (SQLException e) {
            //TOdo send message to server
            e.printStackTrace();
        } catch (CustomerIdDoesNotExistsException e) {
            //TOdo send message to server
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("update is called with post method!");
        long customerId = Long.parseLong(request.getParameter("customer-id"));
        CustomerType customerType = CustomerType.valueOf(request.getParameter("customer-type").toUpperCase());

        DataConversionHandler dataConversionHandler = new DataConversionHandler();

        BankCustomer updatedCustomer = null;
        if (CustomerType.LEGAL == customerType)
            updatedCustomer = dataConversionHandler.getInitializedRealCustomer(request, customerId);
        else if (CustomerType.REAL == customerType)
            updatedCustomer = dataConversionHandler.getInitializedRealCustomer(request, customerId);
        try {

            new CustomerValidator().validateCustomer(updatedCustomer);
            CustomerEntity updatedCustomerEntity = dataConversionHandler.convertBankCustomerToEntity(updatedCustomer);
            new DefaultBankCustomerDAO().updateCustomer(updatedCustomerEntity);
            response.getWriter().println("update succeeded!");

        } catch (DuplicatedEconomicIdException e) {
            response.getWriter().println(e.getMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().println("Sorry a problem has occurred in server. Please try again!");
        } catch (DuplicatedNationalCodeException e) {
            e.printStackTrace();
            response.getWriter().println(e.getMessage());
        }

    }
}
