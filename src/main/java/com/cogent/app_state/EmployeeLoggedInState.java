package com.cogent.app_state;

import com.cogent.Bank;
import com.cogent.bank_employee.EmployeeAccountDaoImp;
import com.cogent.checking_account.CheckingAccountDaoImp;
import com.cogent.pending_checking_account.PendingCheckingAccountDaoImp;
import jakarta.persistence.NoResultException;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EmployeeLoggedInState implements State{
    EmployeeAccountDaoImp account;

    public EmployeeLoggedInState(EmployeeAccountDaoImp account) {
        this.account = account;
    }

    @Override
    public void execute() {
        while(true) {
            int selection = optionsPrompt();

            switch (selection) {
                case 1: // view checking accounts of customer id
                    System.out.println("+   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +");
                    System.out.println();
                    printAllCheckingAccountsOfCustomerId();
                    System.out.println("+   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +");
                    System.out.println();
                    break;
                case 2: // view pending account creation requests
                    System.out.println("+   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +");
                    System.out.println();
                    if (selectPendingAccounts()) {
                        System.out.println("+   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +");
                        System.out.println();
                        return;
                    } // true if valid selection was made
                    break;
                case 3: // view transaction log
                    Bank.changeState(new EmployeeViewTransactionLogs(account));
                    System.out.println("RETURNING TO SELECTION");
                    System.out.println("+   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +");
                    System.out.println();
                    return;
                case -1:
                    Bank.changeState(new MainMenuState());
                    System.out.println("RETURNING TO MAIN MENU");
                    System.out.println("+   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +");
                    System.out.println();
                    return;
                default:
                    System.out.println("INVALID SELECTION, TRY AGAIN");
                    System.out.println("+   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +");
                    System.out.println();
                    break;
            }
        }
    }

    private int optionsPrompt() {
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("+                                                                               +");
        System.out.println("+    HI " + account.getUser());
        System.out.println("+                                                                               +");
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("+                                                                               +");
        System.out.println("+                       Select from the options below                           +");
        System.out.println("+                                                                               +");
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("+                                                                               +");
        System.out.println("+    PRESS 1 : VIEW CHECKING ACCOUNTS OF CUSTOMER ID                            +");
        System.out.println("+    PRESS 2 : VIEW PENDING ACCOUNT CREATION REQUESTS                           +");
        System.out.println("+    PRESS 3 : VIEW TRANSACTION LOGS                                            +");
        System.out.println("+    ENTER -1 : RETURN TO MAIN MENU                                             +");
        System.out.println("+                                                                               +");
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.print("Select option: ");
        int selection = Bank.scanner.nextInt();
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        return selection;
    }

    private boolean selectPendingAccounts() {
        List<PendingCheckingAccountDaoImp> pendingRequests = getPendingCheckingAccounts();

        while(true) {
            int selection = pendingAccountsPrompt(pendingRequests);

            if (selection == -1) return false;

            if (selection >= 0 && selection < pendingRequests.size()) {
                // Adjust the index for 0-based access
                PendingCheckingAccountDaoImp pendingAccount = pendingRequests.get(selection);
                System.out.println("YOU HAVE SELECTED: ");
                System.out.println(pendingAccount);
                Bank.changeState(new EmployeeAcceptRejectPendingAccountState(account, pendingAccount));
                return true;
            } else {
                System.out.println("INVALID SELECTION, TRY AGAIN.");
                System.out.println("+   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +");
                System.out.println();
            }
        }
    }

    private int pendingAccountsPrompt(List<PendingCheckingAccountDaoImp> pendingRequests) {
        System.out.println("PENDING CHECKING ACCOUNT REQUESTS");
        System.out.println("Select a pending checking account below, or enter -1 to return to selection.");
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        for (int i = 0; i < pendingRequests.size(); i++) { System.out.println("PRESS " + i + ": " + pendingRequests.get(i)); }
        if (pendingRequests.isEmpty()) System.out.println("THERE ARE NO PENDING CHECKING ACCOUNT CREATION REQUESTS");
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.print("Select option: ");
        int selection =  Bank.scanner.nextInt();
        System.out.println("+   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +");
        System.out.println();
        return selection;
    }

    private List<PendingCheckingAccountDaoImp> getPendingCheckingAccounts() {
        Bank.session = Bank.sessionFactory.openSession();
        Transaction transaction = Bank.session.beginTransaction();

        String hql = "FROM PendingCheckingAccountDaoImp";
        List<PendingCheckingAccountDaoImp> pendingRequests = (List<PendingCheckingAccountDaoImp>) Bank.session.createQuery(hql).list();

        transaction.commit();
        Bank.session.close();

        return pendingRequests;
    }

    private void printAllCheckingAccountsOfCustomerId() {
        try {
            System.out.print("Enter a customerId: ");
            int customerId = Bank.scanner.nextInt();

            ArrayList<CheckingAccountDaoImp> customersChecking = getAllCheckingAccounts(customerId);

            if (customersChecking.isEmpty()) {
                System.out.println("ERROR: no checking accounts found for " + customerId+ ".");
                return;
            }

            System.out.println("ALL CHECKING ACCOUNTS FOR " + customerId + ".");
            customersChecking.forEach(System.out::println);

        } catch (Exception e) {
            System.out.println(e);
        } finally {

        }
    }

    private ArrayList<CheckingAccountDaoImp> getAllCheckingAccounts(int customerId) {
        Bank.session = Bank.sessionFactory.openSession();
        Transaction transaction = Bank.session.beginTransaction();

        StoredProcedureQuery query = Bank.session.createStoredProcedureQuery("GetCheckingAccountByCustomerId", CheckingAccountDaoImp.class)
                .registerStoredProcedureParameter("custId", Integer.class, ParameterMode.IN)
                .setParameter("custId", customerId);

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
