package ir.dotin.bank.cms.business.services;

import ir.dotin.bank.cms.business.dataobjects.entities.BankCustomerEntity;
import ir.dotin.bank.cms.business.dataobjects.values.customers.BankCustomerVo;
import ir.dotin.bank.cms.business.dataobjects.values.customers.CustomerType;
import ir.dotin.bank.cms.business.tools.CustomHttpStatusCode;
import ir.dotin.bank.cms.business.tools.CustomerDataMapper;
import ir.dotin.bank.cms.business.tools.DataExtractor;
import ir.dotin.bank.cms.dal.daos.implementations.hibernate.DefaultBankCustomerDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet(name = "searchServlet")
public class searchServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("search-called-by-delete_servlet", true);
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Map<String, String> searchKeyValues = new DataExtractor().extractDbSearchKeyValuesFromRequestParams(req);
            if (req.getAttribute("search-called-by-delete_servlet") != null && req.getAttribute("search-called-by-delete_servlet").equals(true))
                searchKeyValues.remove("CUSTOMER_ID");
            List<BankCustomerEntity> customerEntities = new DefaultBankCustomerDao().searchDBFor(searchKeyValues);
            Map<CustomerType, List<BankCustomerVo>> customerTypeToBankCustomerListMap = new CustomerDataMapper().convertCustomerEntitiesToCustomerTypeBankCustomerVoMap(customerEntities);
            req.setAttribute("legal-customers", customerTypeToBankCustomerListMap.get(CustomerType.LEGAL));
            req.setAttribute("real-customers", customerTypeToBankCustomerListMap.get(CustomerType.REAL));

            req.getRequestDispatcher("presentation/searchResultPage.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(CustomHttpStatusCode.INTERNAL_SERVER_ERROR);
            resp.getWriter().println("Sorry, a problem has occurred in server! Please try later!");
        }
    }
}