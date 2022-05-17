package ir.dotin.bank.cms.business.services;

import ir.dotin.bank.cms.business.objects.entities.CustomerEntity;
import ir.dotin.bank.cms.business.objects.values.BankCustomer;
import ir.dotin.bank.cms.business.tools.DataConversionHandler;
import ir.dotin.bank.cms.dal.DefaultBankCustomerDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "searchServlet")
public class searchServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("search is called");
        String searchKey = request.getParameter("search-domain");
        String searchValue = request.getParameter("search-content");
        try {
            List<CustomerEntity> customerEntities = new DefaultBankCustomerDAO().searchDBFor(searchKey, searchValue);
            List<BankCustomer> bankCustomers = new DataConversionHandler().convertCustomerEntitiesToBankCustomers(customerEntities);
            if (request.getServletPath().contains("legal")) {
                request.setAttribute("customer-type", "legal");
            } else if (request.getServletPath().contains("real")) {
                request.setAttribute("customer-type", "real");
            }
            //Todo check if there is no problem here in front side
            request.setAttribute("customer-list", bankCustomers);
            request.getRequestDispatcher("presentation/searchResultPage.jsp").forward(request, response);

        } catch (SQLException e) {
            //Todo send a message to user
            e.printStackTrace();
        }

    }

}