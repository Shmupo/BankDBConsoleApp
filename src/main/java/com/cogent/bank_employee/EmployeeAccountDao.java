package com.cogent.bank_employee;

interface EmployeeAccountDao {
    public void getAllCustomerAccounts();
    public void getCustomerAccountById();
    public void getAllCheckingAccounts();
    public void getCheckingAccountById();
    public void viewAllTransfers();
    public void viewTransferById();
}
