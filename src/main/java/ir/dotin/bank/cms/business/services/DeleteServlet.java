package ir.dotin.bank.cms.business.services;

import ir.dotin.bank.cms.business.exceptions.IllegalValueTypeException;
import ir.dotin.bank.cms.business.validations.GeneralValidator;
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
        String customerId = request.getParameter("customer-id");
        try {
            GeneralValidator.isNumeric(customerId);
            new DefaultBankCustomerDAO().deleteCustomer(customerId);
        } catch (IllegalValueTypeException e) {
            response.getWriter().write(e.getMessage() + "Customer id is not numeric!");
            e.printStackTrace();
        } catch (SQLException e) {
            response.getWriter().write("Sorry! A problem has occurred in server. Please try later!");
            e.printStackTrace();
        }
        request.getRequestDispatcher("search").forward(request, response);
    }
}
