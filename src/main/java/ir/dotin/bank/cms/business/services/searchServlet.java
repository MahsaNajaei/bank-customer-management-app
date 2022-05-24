package ir.dotin.bank.cms.business.services;

import ir.dotin.bank.cms.business.dataobjects.entities.BankCustomerEntity;
import ir.dotin.bank.cms.business.dataobjects.values.BankCustomerVO;
import ir.dotin.bank.cms.business.dataobjects.values.CustomerType;
import ir.dotin.bank.cms.business.tools.DataMapper;
import ir.dotin.bank.cms.dal.DefaultBankCustomerDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@WebServlet(name = "searchServlet")
public class searchServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("search-called-by-delete_servlet", true);
        doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            DataMapper dataMapper = new DataMapper();
            Map<String, String> searchKeyValues = dataMapper.mapRequestParamsToSearchKeyValues(req);
            if (req.getAttribute("search-called-by-delete_servlet") != null && req.getAttribute("search-called-by-delete_servlet").equals(true))
                searchKeyValues.remove("customerId");
            List<BankCustomerEntity> customerEntities = new DefaultBankCustomerDAO().searchDBFor(searchKeyValues);
            Map<CustomerType, List<BankCustomerVO>> customerTypeToBankCustomerListMap = dataMapper.getCustomerTypeToBankCustomersMapFromEntities(customerEntities);
            req.setAttribute("legal-customers", customerTypeToBankCustomerListMap.get(CustomerType.LEGAL));
            req.setAttribute("real-customers", customerTypeToBankCustomerListMap.get(CustomerType.REAL));
            req.getRequestDispatcher("presentation/searchResultPage.jsp").forward(req, resp);
        } catch (SQLException e) {
            resp.getWriter().println("Sorry, a problem has occurred in server! Please try later!");
            e.printStackTrace();
        }
    }
}