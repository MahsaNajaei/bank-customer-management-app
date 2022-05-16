package ir.dotin.bank.cms.business.services;

import ir.dotin.bank.cms.business.dto.CustomerType;
import ir.dotin.bank.cms.dal.BankCustomerDAOFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "deleteServlet")
public class DeleteServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        CustomerType customerType = CustomerType.valueOf(request.getParameter("customer-type").toUpperCase());
        long customerId = Long.parseLong(request.getParameter("customer-id"));
        new BankCustomerDAOFactory().getBankCustomerDAO(customerType).deleteCustomer(customerId);
        if (CustomerType.LEGAL.equals(customerType))
            request.getRequestDispatcher("legal-search").forward(request, response);
        else if (CustomerType.REAL.equals(customerType))
            request.getRequestDispatcher("real-search").forward(request, response);
    }
}
