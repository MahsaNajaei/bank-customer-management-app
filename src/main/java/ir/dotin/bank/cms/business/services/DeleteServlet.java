package ir.dotin.bank.cms.business.services;

import ir.dotin.bank.cms.business.objects.values.CustomerType;
import ir.dotin.bank.cms.dal.DefaultBankCustomerDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "deleteServlet")
public class DeleteServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        CustomerType customerType = CustomerType.valueOf(request.getParameter("customer-type").toUpperCase());
        String customerId = request.getParameter("customer-id");
        try {
            new DefaultBankCustomerDAO().deleteCustomer(customerId);
        } catch (SQLException e) {
            //ToDO its better to send a messeage to UI!
            e.printStackTrace();
        }
        if (CustomerType.LEGAL.equals(customerType))
            request.getRequestDispatcher("legal-search").forward(request, response);
        else if (CustomerType.REAL.equals(customerType))
            request.getRequestDispatcher("real-search").forward(request, response);
    }
}
