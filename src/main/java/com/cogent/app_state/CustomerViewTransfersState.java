package com.cogent.app_state;

import com.cogent.Bank;
import com.cogent.bank_customer.CustomerAccountDaoImp;
import com.cogent.bank_transfer.BalanceTransferDaoImp;
import com.cogent.checking_account.CheckingAccountDaoImp;
import jakarta.persistence.NoResultException;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CustomerViewTransfersState implements State {
    static CustomerAccountDaoImp account;

    public CustomerViewTransfersState(CustomerAccountDaoImp account) {
        CustomerViewTransfersState.account = account;
    }

    @Override
    public void execute() {
        ArrayList<BalanceTransferDaoImp> pendingTransfers = getAllPendingTransfers();

        while (true) {
            int selection = viewCustomerTransfers(pendingTransfers);

            if (selection == -1) { // return to main menu
                Bank.changeState(new CustomerLoggedInState(account));
                return;
            }

            if (selection >= 0 && selection < pendingTransfers.size()) {
                BalanceTransferDaoImp selectedTransfer = pendingTransfers.get(selection);

                if (selectedTransfer != null) {
                    ArrayList<CheckingAccountDaoImp> checkingAccounts = getAllCheckingAccounts();

                    if (checkingAccounts != null) {
                        List<Integer> accountNums = checkingAccounts.stream().map(CheckingAccountDaoImp::getAccountNumber).collect(Collectors.toList());
                        if (accountNums.contains(selectedTransfer.getFromAccountNumber())) { // this account is a FROM party in the balance transaction and can only cancel
                            Bank.changeState(new CustomerSelectedOutgoingTransferState(account, selectedTransfer));
                            return;
                        } else { // this account is in the TO party in the balance transaction and can accept/reject
                            Bank.changeState(new CustomerSelectedIncomingTransferState(account, selectedTransfer));
                            return;
                        }
                    } else {
                        System.out.println("ERROR this account has no checking accounts associated.");
                        System.out.println("Returning to selection.");
                        System.out.println("+   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +");
                        System.out.println();
                        Bank.changeState(new CustomerLoggedInState(account));
                        return;
                    }
                } else {
                    System.out.println("ERROR selected transfer not found.");
                    System.out.println("Returning to selection.");
                    System.out.println("+   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +");
                    System.out.println();
                    Bank.changeState(new CustomerLoggedInState(account));
                    return;
                }
            } else {
                System.out.println("INVALID SELECTION, TRY AGAIN.");
                System.out.println("+   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +");
                System.out.println();
            }


        }
    }

    private int viewCustomerTransfers(List<BalanceTransferDaoImp> pendingRequests) {
        System.out.println("PENDING CHECKING ACCOUNT REQUESTS");
        System.out.println("Select a pending checking account below, or enter -1 to return to selection.");
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        if (pendingRequests.isEmpty()) System.out.println("NO REQUESTS PENDING");
        for (int i = 0; i < pendingRequests.size(); i++) { System.out.println("PRESS " + i +": " + pendingRequests.get(i)); }
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.print("Select option: ");
        int selection =  Bank.scanner.nextInt();
        System.out.println("+   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +");
        System.out.println();
        return selection;
    }

    private ArrayList<BalanceTransferDaoImp> getAllPendingTransfers() {
        Bank.session = Bank.sessionFactory.openSession();
        Transaction transaction = Bank.session.beginTransaction();

        StoredProcedureQuery query = Bank.session.createStoredProcedureQuery("getTransferByCustomer", BalanceTransferDaoImp.class)
                .registerStoredProcedureParameter("custId", Integer.class, ParameterMode.IN)
                .setParameter("custId", account.getId());

        query.execute();

        ArrayList<BalanceTransferDaoImp> result = (ArrayList<BalanceTransferDaoImp>) query.getResultList();

        transaction.commit();
        Bank.session.close();
        return result;
    }

    private static ArrayList<CheckingAccountDaoImp> getAllCheckingAccounts() {
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
