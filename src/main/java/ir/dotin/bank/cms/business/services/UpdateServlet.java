package ir.dotin.bank.cms.business.services;

import ir.dotin.bank.cms.business.dto.*;
import ir.dotin.bank.cms.business.tools.FormDataToDTOConvertor;
import ir.dotin.bank.cms.dal.BankCustomerDAOFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "updateServlet")
public class UpdateServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long customerId = Long.parseLong(request.getParameter("customer-id"));
        CustomerType customerType = CustomerType.valueOf(request.getParameter("customer-type").toUpperCase());
        BankCustomer bankCustomer = new BankCustomerDAOFactory().getBankCustomerDAO(customerType).retrieveCustomerById(customerId);
        request.setAttribute("customer", bankCustomer);
        if (CustomerType.NATURAL == customerType)
            request.getRequestDispatcher("/presentation/naturalEditPage.jsp").include(request, response);
        else if (CustomerType.LEGAL == customerType)
            request.getRequestDispatcher("/presentation/legalEditPage.jsp").include(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("update is called with post method!");
        long customerId = Long.parseLong(request.getParameter("customer-id"));
        CustomerType customerType = CustomerType.valueOf(request.getParameter("customer-type").toUpperCase());
        BankCustomer updatedCustomer = new BankCustomerFactory().getBankCustomer(customerType, customerId);
        if (CustomerType.LEGAL == customerType)
            FormDataToDTOConvertor.setLegalCustomerAttributes(request, (LegalCustomer) updatedCustomer);
        else if (CustomerType.NATURAL == customerType)
            FormDataToDTOConvertor.setNaturalCustomerAttributes(request, (NaturalCustomer) updatedCustomer);
        new BankCustomerDAOFactory().getBankCustomerDAO(customerType).updateCustomer(updatedCustomer);
        response.getWriter().println("ok");
    }
}
