package com.cogent.app_state;

import com.cogent.Bank;
import com.cogent.bank_customer.CustomerAccountDaoImp;
import com.cogent.checking_account.CheckingAccountDaoImp;
import com.cogent.pending_checking_account.PendingCheckingAccountDaoImp;
import jakarta.persistence.NoResultException;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import org.hibernate.Transaction;

import java.util.ArrayList;

public class CustomerLoggedInState implements State{
    static CustomerAccountDaoImp account = null;
    private static boolean hasChecking = false;

    public CustomerLoggedInState(CustomerAccountDaoImp account) {
        CustomerLoggedInState.account = account;
    }

    @Override
    public void execute() {
        checkCheckingAccount(); // prompt user to request checking account if they have none

        while(hasChecking) {
            int selection = customerSelectionPrompt();

            switch (selection) {
                case 1: // check balance
                    Bank.changeState(new CustomerViewCheckingAccountState(account));
                    return;
                case 2: // view pending transfers
                    Bank.changeState(new CustomerViewTransfersState(account));
                    return;
                case 3: // create another checking account
                    promptRequestCheckingAccount();
                    break;
                case -1: // main menu
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

    private static int customerSelectionPrompt() {
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("+                                                                               +");
        System.out.println("+    HI " + account.getUser());
        System.out.println("+                                                                               +");
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("+                                                                               +");
        System.out.println("+    Select from the options below");
        System.out.println("+                                                                               +");
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("+                                                                               +");
        System.out.println("+    PRESS 1 : VIEW CHECKING ACCOUNTS (DEPOSIT, WITHDRAW, TRANSFER, VIEW)       +");
        System.out.println("+    PRESS 2 : VIEW PENDING BALANCE TRANSFERS                                   +");
        System.out.println("+    PRESS 3 : OPEN NEW CHECKING ACCOUNT                                        +");
        System.out.println("+    ENTER -1 : RETURN TO MAIN MENU                                             +");
        System.out.println("+                                                                               +");
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.print("Select option: ");
        int selection = Bank.scanner.nextInt();
        System.out.println("+   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +");
        System.out.println();
        return selection;
    }

    private static void checkCheckingAccount() {
        int customerId = account.getId();
        Transaction transaction = null;

        try {
            Bank.session = Bank.sessionFactory.openSession();
            transaction = Bank.session.beginTransaction();

            StoredProcedureQuery query = Bank.session.createStoredProcedureQuery("GetCheckingAccountByCustomerId", CheckingAccountDaoImp.class)
                    .registerStoredProcedureParameter("custId", Integer.class, ParameterMode.IN)
                    .setParameter("custId", customerId);

            query.execute();

            // Try to get a single result
            try {
                ArrayList<CheckingAccountDaoImp> result = (ArrayList<CheckingAccountDaoImp>) query.getResultList();
                hasChecking = !result.isEmpty();
                if (hasChecking) {
                    System.out.println("This account has checking accounts.");
                }
            } catch (NoResultException e) {
                hasChecking = false;
                System.out.println("No checking accounts found for the given customer ID.");
            }

            // Prompt to request a new checking account if none exists
            if (!hasChecking) { promptRequestCheckingAccount(); }

            transaction.commit();

        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.out.println("An error occurred: " + e.getMessage());
        } finally {
            if (Bank.session != null && Bank.session.isOpen()) Bank.session.close();
        }
    }

    private static void promptRequestCheckingAccount() {
        while (true) {
            System.out.println("Would you like to create request a new checking account?");
            System.out.print("CREATE ACCOUNT? (Y / N) : ");
            String selection = Bank.scanner.next();

            switch (selection) {
                case "Y":
                    requestCheckingAccount();
                    System.out.println("Returning to MAIN MENU.");
                    return;
                case "N":
                    System.out.println("Canceling checking account creation... Returning to MAIN MENU.");
                    return;
                default:
                    System.out.println("INVALID OPTION, TRY AGAIN.");
            }
        }
    }

    // generate fresh new checking account
    // accountNumber auto-increment generated
    private static void requestCheckingAccount() {
        Bank.session = Bank.sessionFactory.openSession();
        Transaction transaction = null;

        try {
            int id = account.getId();
            String hql = "FROM PendingCheckingAccountDaoImp C WHERE C.customerId = :id";
            PendingCheckingAccountDaoImp pendingRequest = (PendingCheckingAccountDaoImp) Bank.session.createQuery(hql)
                    .setParameter("id", id)
                    .uniqueResult();

            if (pendingRequest == null) { // Generate checking account request
                // Prompt user for starting balance
                System.out.print("Enter the starting balance for your new checking account: ");
                int startingBalance = -1;
                boolean validInput = false;

                while (!validInput) {
                    try {
                        startingBalance = Bank.scanner.nextInt();
                        if (startingBalance < 0) {
                            System.out.println("Invalid amount. Balance must be a non-negative integer. Please try again.");
                        } else {
                            validInput = true;
                        }
                    } catch (Exception e) {
                        System.out.println("Invalid input. Please enter a valid integer.");
                        Bank.scanner.next(); // Clear the invalid input
                    }
                }

                // Create new PendingCheckingAccountDaoImp and set the balance
                transaction = Bank.session.beginTransaction();
                pendingRequest = new PendingCheckingAccountDaoImp();
                pendingRequest.setCustomerId(account.getId());
                pendingRequest.setStartingBalance(startingBalance); // Assuming PendingCheckingAccountDaoImp has a setStartingBalance method
                Bank.session.save(pendingRequest);
                transaction.commit();
                System.out.println("Checking account requested with a starting balance of " + startingBalance + ". Please wait for approval.");
                Bank.logEvent("Checking account requested by " + account + " with a starting balance of " + startingBalance + ".");
            } else { // Account request already there
                System.out.println("Your checking account is currently PENDING.");
            }
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.out.println("An error occurred: " + e.getMessage());
        } finally {
            if (Bank.session != null && Bank.session.isOpen()) Bank.session.close();
        }

        // Return to the main menu after processing
        Bank.changeState(new MainMenuState());
    }
}
