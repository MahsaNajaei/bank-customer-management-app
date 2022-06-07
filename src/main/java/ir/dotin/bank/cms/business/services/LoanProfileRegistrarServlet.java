package ir.dotin.bank.cms.business.services;

import ir.dotin.bank.cms.business.dataobjects.entities.BankCustomerEntity;
import ir.dotin.bank.cms.business.dataobjects.entities.LoanTypeEntity;
import ir.dotin.bank.cms.business.dataobjects.values.customers.BankCustomerVo;
import ir.dotin.bank.cms.business.dataobjects.values.customers.RealCustomerVo;
import ir.dotin.bank.cms.business.tools.CustomHttpStatusCode;
import ir.dotin.bank.cms.business.tools.CustomerDataMapper;
import ir.dotin.bank.cms.business.tools.DataExtractor;
import ir.dotin.bank.cms.dal.daos.implementations.hibernate.DefaultBankCustomerDao;
import ir.dotin.bank.cms.dal.daos.implementations.hibernate.DefaultLoanDao;
import ir.dotin.bank.cms.dal.exceptions.CustomerNotFoundException;
import ir.dotin.bank.cms.dal.exceptions.NoLoanTypeFoundException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebServlet(name = "LoanProfileRegistrarServlet")
public class LoanProfileRegistrarServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Loan profile registrar is called with get method");

        if (request.getParameter("national-code") != null) {
            try {
                System.out.println("get-customer-info");
                BankCustomerEntity bankCustomerEntity = new DefaultBankCustomerDao().retrieveCustomerByExclusiveId(request.getParameter("national-code"));
                BankCustomerVo bankCustomerVo = new CustomerDataMapper().mapCustomerEntityToBankCustomerVO(bankCustomerEntity);
                request.setAttribute("bank-customer", bankCustomerVo);
            } catch (CustomerNotFoundException e) {
                response.setStatus(CustomHttpStatusCode.CUSTOMER_NOT_FOUND);
                response.getWriter().println("هیچ مشتری ای با کد ملی وارد شده یافت نشد!");
                e.printStackTrace();
            }
        }

        try {
            List<LoanTypeEntity> loanTypeEntities = new DefaultLoanDao().retrieveLoanNamesAndIds();
            Map<Integer, String> loanIdToNameMap = new DataExtractor().extractLoanIdToNameMapFromLoanTypeEntities(loanTypeEntities);
            request.setAttribute("loan-idName-map", loanIdToNameMap);
        } catch (NoLoanTypeFoundException e) {
            response.setStatus(CustomHttpStatusCode.NULL_LOAN_TYPE);
            response.getWriter().println("در حال حاضر هیچ نوع تسهیلاتی برای ارایه تعریف نشده است!");
            e.printStackTrace();
        }

        request.getRequestDispatcher("presentation/loanRequest.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Loan profile registrar is called with post method");
        //todo validate
        String loanTypeId = request.getParameter("loan-id");
        String contractDuration = request.getParameter("contract-duration");
        String contractAmount = request.getParameter("contract-amount");
        String customerId = request.getParameter("customer-id");

        try {
            LoanTypeEntity loanTypeEntity = new DefaultLoanDao().retrieveLoanTypeIfContractConditionIsAcceptable(Integer.parseInt(loanTypeId), new BigDecimal(contractAmount), Integer.parseInt(contractDuration));
            //Todo
            new DefaultBankCustomerDao().updateCustomerLoans(customerId, loanTypeEntity);
            response.getWriter().println("درخواست تسهیلات با موفقیت ثبت شد!");
        } catch (NoLoanTypeFoundException e) {
            response.setStatus(CustomHttpStatusCode.NULL_LOAN_TYPE);
            response.getWriter().println("خطا! \n نوع تسهیلات انتخابی حذف شده است!");
            e.printStackTrace();
        }
    }
}
