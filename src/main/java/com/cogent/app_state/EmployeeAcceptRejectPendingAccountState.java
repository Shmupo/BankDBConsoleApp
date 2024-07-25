package com.cogent.app_state;

import com.cogent.Bank;
import com.cogent.bank_customer.CustomerAccountDaoImp;
import com.cogent.bank_employee.EmployeeAccountDaoImp;
import com.cogent.checking_account.CheckingAccountDaoImp;
import com.cogent.pending_checking_account.PendingCheckingAccountDaoImp;
import org.hibernate.Transaction;

public class EmployeeAcceptRejectPendingAccountState implements State{
    EmployeeAccountDaoImp account;
    PendingCheckingAccountDaoImp pendingAccount;

    public EmployeeAcceptRejectPendingAccountState(EmployeeAccountDaoImp account, PendingCheckingAccountDaoImp pendingAccount) {
        this.account = account;
        this.pendingAccount = pendingAccount;
    }

    @Override
    public void execute() {
        while(true) {
            int selection = rejectOrAcceptAccountPrompt();

            switch (selection) {
                case 1: // accept
                    approvePendingAccount();
                    System.out.println("ACCOUNT APPROVED, RETURNING TO SELECTION.");
                    System.out.println("+   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +");
                    System.out.println();
                    Bank.changeState(new EmployeeLoggedInState(account));
                    return;
                case 2: // reject
                    rejectPendingAccount();
                    System.out.println("ACCOUNT REJECTED, RETURNING TO SELECTION.");
                    System.out.println("+   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +");
                    System.out.println();
                    Bank.changeState(new EmployeeLoggedInState(account));
                    return;
                case -1: // cancel, go back to employee selection
                    System.out.println("CANCELING, RETURNING TO SELECTION.");
                    System.out.println("+   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +");
                    System.out.println();
                    Bank.changeState(new EmployeeLoggedInState(account));
                    return;
                default:
                    System.out.println("INVALID OPTION, TRY AGAIN.");
                    System.out.println("+   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +");
                    System.out.println();
            }
        }
    }

    private int rejectOrAcceptAccountPrompt() {
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("Select from the options below for account : ");
        System.out.println(pendingAccount);
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("+    PRESS 1 : APPROVE PENDING CHECKING ACCOUNT                                 +");
        System.out.println("+    PRESS 2 : REJECT PENDING CHECKING ACCOUNT                                  +");
        System.out.println("+    ENTER -1 : CANCEL                                                          +");
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.print("Select option: ");
        int selection = Bank.scanner.nextInt();
        System.out.println("+   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +");
        System.out.println();
        return selection;
    }

    private void approvePendingAccount() {
        Transaction transaction = null;
        try {
            Bank.session = Bank.sessionFactory.openSession();
            transaction = Bank.session.beginTransaction();

            int customerId = pendingAccount.getCustomerId();
            CustomerAccountDaoImp customerAccount = Bank.session.get(CustomerAccountDaoImp.class, customerId);

            if (customerAccount != null) { // create new checking, link customer to this account number
                CheckingAccountDaoImp newChecking = new CheckingAccountDaoImp();
                newChecking.setCustomerId(customerId);
                newChecking.setBalance(pendingAccount.getStartingBalance());
                Bank.session.save(newChecking); // this should set the id of new checking

                // NOTE : there may be a bug here where customer account already has a checking account number
                Bank.session.save(customerAccount);
                Bank.session.delete(pendingAccount); // delete pending request
                Bank.logEvent("Employee " + account + " accepted pending checking account " + pendingAccount + ".");
                Bank.logEvent("Employee generated " + newChecking + " from " + pendingAccount);
            } else { // customer account is null
                System.out.println("ERROR: customer account with id " + customerId + " does not exist. Canceling approval.");
            }

            transaction.commit();
            Bank.session.close();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.out.println("ERROR could not accept pending account.");
            System.out.println(e);
        } finally {
            if (Bank.session != null && Bank.session.isOpen()) Bank.session.close();
        }
    }

    private void rejectPendingAccount() {
        Transaction transaction = null;
        try {
            Bank.session = Bank.sessionFactory.openSession();
            transaction = Bank.session.beginTransaction();

            Bank.session.delete(pendingAccount); // delete pending request

            transaction.commit();
            Bank.session.close();

            Bank.logEvent("Employee " + account + " rejected pending checking account " + pendingAccount + ".");
            Bank.logEvent("Employee deleted " + pendingAccount);
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.out.println("ERROR could not reject pending account.");
            System.out.println(e);
        } finally {
            if (Bank.session != null && Bank.session.isOpen()) Bank.session.close();
        }

    }
}
