package ir.dotin.bank.cms.business.services;

import ir.dotin.bank.cms.business.dataobjects.entities.BankCustomerEntity;
import ir.dotin.bank.cms.business.dataobjects.values.customers.BankCustomerVo;
import ir.dotin.bank.cms.business.dataobjects.values.customers.CustomerType;
import ir.dotin.bank.cms.business.tools.CustomerDataMapper;
import ir.dotin.bank.cms.business.tools.DataExtractor;
import ir.dotin.bank.cms.dal.daos.implementations.hibernate.DefaultBankCustomerDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet(name = "searchServlet")
public class searchServlet extends HttpServlet {
    private static final Logger logger = LogManager.getLogger(searchServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("search-called-by-delete_servlet", true);
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, String> searchKeyValues = new DataExtractor().extractDbSearchKeyValuesFromRequestParams(req);
        if (req.getAttribute("search-called-by-delete_servlet") != null && req.getAttribute("search-called-by-delete_servlet").equals(true))
            searchKeyValues.remove("CUSTOMER_ID");
        List<BankCustomerEntity> customerEntities = new DefaultBankCustomerDao().searchDBFor(searchKeyValues);
        logger.info("Customer search for key-values" + searchKeyValues + " is fetched successfully with " + customerEntities.size() + " results!");
        Map<CustomerType, List<BankCustomerVo>> customerTypeToBankCustomerListMap = new CustomerDataMapper().convertCustomerEntitiesToCustomerTypeBankCustomerVoMap(customerEntities);
        req.setAttribute("legal-customers", customerTypeToBankCustomerListMap.get(CustomerType.LEGAL));
        req.setAttribute("real-customers", customerTypeToBankCustomerListMap.get(CustomerType.REAL));
        req.getRequestDispatcher("presentation/searchResultPage.jsp").forward(req, resp);
        logger.info("Search result has been sent to client successfully!");
    }
}