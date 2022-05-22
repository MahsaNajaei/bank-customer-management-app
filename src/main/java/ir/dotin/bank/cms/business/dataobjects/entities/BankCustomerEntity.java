package ir.dotin.bank.cms.business.dataobjects.entities;

import java.sql.Date;

public class BankCustomerEntity {
    private String customerId;
    private String customerType;
    private String name;
    private String surname;
    private String parentName;
    private Date date;
    private String exclusiveId;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getExclusiveId() {
        return exclusiveId;
    }

    public void setExclusiveId(String exclusiveId) {
        this.exclusiveId = exclusiveId;
    }
}
