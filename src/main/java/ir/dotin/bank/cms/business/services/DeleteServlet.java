package ir.dotin.bank.cms.business.services;

import ir.dotin.bank.cms.business.exceptions.IllegalValueTypeException;
import ir.dotin.bank.cms.business.tools.CustomHttpStatusCode;
import ir.dotin.bank.cms.business.validatiors.GeneralValidator;
import ir.dotin.bank.cms.dal.daos.implementations.hibernate.DefaultBankCustomerDao;
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
        String customerId = request.getParameter("customer-id");
        try {
            GeneralValidator.checkNumericValueIntegrity(customerId);
            new DefaultBankCustomerDao().deleteCustomer(customerId);
        } catch (IllegalValueTypeException e) {
            response.setStatus(CustomHttpStatusCode.ILLEGAL_VALUE_TYPE);
            response.getWriter().write(e.getMessage() + "Customer id is not numeric!");
            e.printStackTrace();
        } catch (Exception e) {
            response.setStatus(CustomHttpStatusCode.INTERNAL_SERVER_ERROR);
            response.getWriter().write("Sorry! A problem has occurred in server. Please try later!");
            e.printStackTrace();
        }
        request.getRequestDispatcher("search").forward(request, response);
    }
}
