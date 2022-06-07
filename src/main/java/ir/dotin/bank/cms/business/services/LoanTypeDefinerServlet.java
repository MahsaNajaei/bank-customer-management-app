package ir.dotin.bank.cms.business.services;

import ir.dotin.bank.cms.business.dataobjects.entities.LoanTypeEntity;
import ir.dotin.bank.cms.business.dataobjects.values.loans.GrantConditionVo;
import ir.dotin.bank.cms.business.dataobjects.values.loans.LoanTypeVo;
import ir.dotin.bank.cms.business.exceptions.GrantConditionNotExistsException;
import ir.dotin.bank.cms.business.exceptions.IllegalNumberOrderException;
import ir.dotin.bank.cms.business.exceptions.IllegalValueTypeException;
import ir.dotin.bank.cms.business.exceptions.NullLoanTypeException;
import ir.dotin.bank.cms.business.tools.CustomHttpStatusCode;
import ir.dotin.bank.cms.business.tools.DataExtractor;
import ir.dotin.bank.cms.business.tools.LoanDataMapper;
import ir.dotin.bank.cms.business.validatiors.LoanValidator;
import ir.dotin.bank.cms.dal.daos.implementations.hibernate.DefaultLoanDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "loanTypeDefinerServlet")
public class LoanTypeDefinerServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("loanTypeDefinerServlet is called with get method!");
        String redirectAddress = "presentation/loanConditionsDefinition.jsp";
        DataExtractor dataExtractor = new DataExtractor();
        if (request.getParameter("loan-name") != null) {
            LoanTypeVo loanType = dataExtractor.extractLoanTypeVoFromRequestParams(request);
            request.getSession().setAttribute("loan-type", loanType);
        } else if (request.getParameter("condition-name") != null) {
            GrantConditionVo grantCondition = dataExtractor.extractGrantConditionVoFromRequestParams(request);
            LoanTypeVo loanTypeVo = (LoanTypeVo) request.getSession().getAttribute("loan-type");
            loanTypeVo.getGrantConditions().add(grantCondition);
            redirectAddress += "#" + request.getParameter("display-Section-id");
        }
        response.sendRedirect(redirectAddress);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("loanTypeDefinerServlet is called with post method!");
        LoanTypeVo loanTypeVo = (LoanTypeVo) request.getSession().getAttribute("loan-type");
        System.out.println(loanTypeVo.getGrantConditions().size());
        try {
            new LoanValidator().validateLoanType(loanTypeVo);
            LoanTypeEntity loanTypeEntity = new LoanDataMapper().mapLoanTypeVoToLoanTypeEntity(loanTypeVo);
            System.out.println(loanTypeEntity.getGrantConditions().size());
            new DefaultLoanDao().add(loanTypeEntity);
            request.getSession().removeAttribute("loan-type");
            response.getWriter().println("نوع تسهیلات جدید با موفقیت ثبت شد!");
        } catch (NullLoanTypeException e) {
            response.setStatus(CustomHttpStatusCode.NULL_LOAN_TYPE);
            response.getWriter().println("خطا! \n نوع تسهیلات تعریف نشده است!");
            e.printStackTrace();
        } catch (GrantConditionNotExistsException e) {
            response.setStatus(CustomHttpStatusCode.GRANT_CONDITION_NOT_EXISTS);
            response.getWriter().println("خطا! \n برای تعریف تسهیلات جدید باید حداقل یک شرط اعطاء تعیین کنید!");
            e.printStackTrace();
        } catch (IllegalValueTypeException e) {
            response.setStatus(CustomHttpStatusCode.ILLEGAL_VALUE_TYPE);
            response.getWriter().println("خطا! \n مقدار وارد شده برای فیلدهای عددی باید یک عدد غیر منفی باشد!");
            e.printStackTrace();
        } catch (IllegalNumberOrderException e) {
            response.setStatus(CustomHttpStatusCode.ILLEGAL_NUMBER_ORDER);
            response.getWriter().println("خطا! \n حداقل مقدار وارد شده نباید از حداکثر مقدار وارد شده بیشتر باشد!");
            e.printStackTrace();
        }
    }
}
