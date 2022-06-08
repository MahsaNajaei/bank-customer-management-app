package ir.dotin.bank.cms.business.services;

import ir.dotin.bank.cms.business.exceptions.IllegalValueTypeException;
import ir.dotin.bank.cms.business.exceptions.NullValueException;
import ir.dotin.bank.cms.business.tools.CustomHttpStatusCode;
import ir.dotin.bank.cms.business.validators.GeneralValidator;
import ir.dotin.bank.cms.dal.daos.implementations.hibernate.DefaultBankCustomerDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

@WebServlet(name = "deleteServlet")

public class DeleteServlet extends HttpServlet {
    private static final Logger logger = LogManager.getLogger(DeleteServlet.class);
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String customerId = request.getParameter("customer-id");
        try {
            GeneralValidator.checkNumericValueIntegrity(customerId);
            new DefaultBankCustomerDao().deleteCustomer(customerId);
            logger.info("customer with customerId = " + customerId + " has been deleted!");
        } catch (IllegalValueTypeException e) {
            logger.warn("Entered Customer ID Is Not Numeric!");
            response.setStatus(CustomHttpStatusCode.ILLEGAL_VALUE_TYPE);
            response.getWriter().write(e.getMessage() + "Customer id is not numeric!");
            e.printStackTrace();
        } catch (NullValueException e) {
            logger.warn("Entered Customer ID Is Null!");
            response.setStatus(CustomHttpStatusCode.ILLEGAL_VALUE_TYPE);
            e.printStackTrace();
        }
        request.getRequestDispatcher("search").forward(request, response);
    }
}
