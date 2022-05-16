package ir.dotin.bank.cms.business.services;

import ir.dotin.bank.cms.dal.LegalCustomerDAO;
import ir.dotin.bank.cms.dal.RealCustomerDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "searchServlet")
public class searchServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("search is called");
        String searchKey = request.getParameter("search-domain");
        String searchValue = request.getParameter("search-content");
        List bankCustomers = null;

        if (request.getServletPath().contains("legal")) {

            bankCustomers = new LegalCustomerDAO().searchDBFor(searchKey, searchValue);
            request.setAttribute("customer-type", "legal");
        } else if (request.getServletPath().contains("real")) {
            bankCustomers = new RealCustomerDAO().searchDBFor(searchKey, searchValue);
            request.setAttribute("customer-type", "real");
        }

        request.setAttribute("customer-list", bankCustomers);
        request.getRequestDispatcher("presentation/searchResultPage.jsp").forward(request, response);

    }

}