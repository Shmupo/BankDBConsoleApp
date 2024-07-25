package com.cogent.app_state;

import com.cogent.Bank;
import com.cogent.bank_customer.CustomerAccountDaoImp;
import com.cogent.checking_account.CheckingAccountDaoImp;
import jakarta.persistence.NoResultException;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

public class CustomerViewCheckingAccountState implements State {
    static CustomerAccountDaoImp account;

    public CustomerViewCheckingAccountState(CustomerAccountDaoImp account) {
        this.account = account;
    }

    @Override
    public void execute() {
        ArrayList<CheckingAccountDaoImp> checkingAccounts = getAllCheckingAccounts();

        while (true) {
            int selection = optionSelection(checkingAccounts);

            if (selection == -1) {
                Bank.changeState(new CustomerLoggedInState(account));
                System.out.println("RETURNING TO SELECTION");
                System.out.println("+   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +");
                System.out.println();
                return;
            }

            if (selection >= 0 && selection < checkingAccounts.size()) {
                CheckingAccountDaoImp selectedAccount = checkingAccounts.get(selection);
                Bank.changeState(new CustomerSelectedCheckingAccountState(account, selectedAccount));
                return;
            } else {
                System.out.println("INVALID SELECTION, TRY AGAIN.");
                System.out.println("+   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +");
                System.out.println();
            }

        }
    }

    private static int optionSelection(ArrayList<CheckingAccountDaoImp> checkingAccounts) {
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("+                                                                               +");
        System.out.println("+    HI " + account.getUser());
        System.out.println("+                                                                               +");
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("+                                                                               +");
        System.out.println("+    SELECT A CHECKING ACCOUNT BELOW TO BEGIN                                   +");
        System.out.println("+                                                                               +");
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        for (int i = 0; i < checkingAccounts.size(); i++) { System.out.println("+    PRESS " + (i) + ": " + checkingAccounts.get(i)); }
        System.out.println("+    ENTER -1 : BACK");
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.print("Select option: ");
        int selection = Bank.scanner.nextInt();
        System.out.println("+   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +");
        System.out.println();
        return selection;
    }

    private void getCheckingAccountById() {
        System.out.println("VIEW CHECKING ACCOUNT BY ID");
        System.out.print("Enter account number: ");
        int accountNumber = Bank.scanner.nextInt();

        Bank.session = Bank.sessionFactory.openSession();
        Transaction transaction = Bank.session.beginTransaction();

        CheckingAccountDaoImp foundAccount = Bank.session.get(CheckingAccountDaoImp.class, accountNumber);

        if (account != null) {
            System.out.println(foundAccount);
        } else {
            System.out.println("ERROR: no checking account with the given ID was found linked to your account.");
        }
    }

    private ArrayList<CheckingAccountDaoImp> getAllCheckingAccounts() {
        Bank.session = Bank.sessionFactory.openSession();
        Transaction transaction = Bank.session.beginTransaction();

        StoredProcedureQuery query = Bank.session.createStoredProcedureQuery("GetCheckingAccountByCustomerId", CheckingAccountDaoImp.class)
                .registerStoredProcedureParameter("custId", Integer.class, ParameterMode.IN)
                .setParameter("custId", account.getId());

        query.execute();

        // Try to get a single result
        try {
            return (ArrayList<CheckingAccountDaoImp>) query.getResultList();
        } catch (NoResultException e) {
            System.out.println("No checking account found for the given customer ID.");
            return null;
        } finally {
            transaction.commit();
            Bank.session.close();
        }
    }
}
