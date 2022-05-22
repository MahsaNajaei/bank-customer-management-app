package ir.dotin.bank.cms.business.dataobjects.values;

import java.sql.Date;

public class RealCustomerVO extends BankCustomerVO {
    private String name;
    private String surname;
    private String fathersName;
    private Date birthDate;
    private String nationalCode;

    private RealCustomerVO(long customerId, CustomerType customerType, Builder builder) {
        super(customerId, customerType);
        this.name = builder.name;
        this.surname = builder.surname;
        this.fathersName = builder.fathersName;
        this.birthDate = builder.birthDate;
        this.nationalCode = builder.nationalCode;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getFathersName() {
        return fathersName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public String getNationalCode() {
        return nationalCode;
    }

    public static class Builder{
        private String name;
        private String surname;
        private String fathersName;
        private Date birthDate;
        private String nationalCode;

        public Builder name(String name){
            this.name = name;
            return this;
        }

        public Builder surname(String surname){
            this.surname = surname;
            return this;
        }

        public Builder fathersName(String fathersName){
            this.fathersName = fathersName;
            return this;
        }

        public Builder birthDate(Date birthDate){
            this.birthDate = birthDate;
            return this;
        }

        public Builder nationalCode(String nationalCode){
            this.nationalCode = nationalCode;
            return this;
        }

        public RealCustomerVO build(long customerId, CustomerType customerType){
            return new RealCustomerVO(customerId, customerType, this);
        }
    }

}
