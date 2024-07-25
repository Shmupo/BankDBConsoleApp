package com.cogent.app_state;

import com.cogent.Bank;
import com.cogent.bank_customer.CustomerAccountDaoImp;
import com.cogent.bank_transfer.BalanceTransferDaoImp;
import com.cogent.checking_account.CheckingAccountDaoImp;
import org.hibernate.Transaction;

public class CustomerSelectedOutgoingTransferState implements State {
    static CustomerAccountDaoImp account;
    static BalanceTransferDaoImp balanceTransfer;

    public CustomerSelectedOutgoingTransferState(CustomerAccountDaoImp account, BalanceTransferDaoImp balanceTransfer) {
        CustomerSelectedOutgoingTransferState.account = account;
        CustomerSelectedOutgoingTransferState.balanceTransfer = balanceTransfer;
    }

    @Override
    public void execute() {
        while (true) {
            int selection = optionsPrompt();

            switch (selection) {
                case 1: // cancel
                    cancelTransfer();
                    break;
                case -1: // back
                    Bank.changeState(new CustomerViewTransfersState(account));
                    System.out.println("Returning to selection.");
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

    private static int optionsPrompt() {
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("+                                                                               +");
        System.out.println("+                       - SELECTED INCOMING TRANSFER -                          +");
        System.out.println(balanceTransfer);
        System.out.println("+                                                                               +");
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("+                                                                               +");
        System.out.println("+                       Select from the options below                           +");
        System.out.println("+                                                                               +");
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("+                                                                               +");
        System.out.println("+    PRESS 1 : REJECT TRANSFER                                                  +");
        System.out.println("+    ENTER -1 : BACK                                                            +");
        System.out.println("+                                                                               +");
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.print("Select option: ");
        int selection = Bank.scanner.nextInt();
        System.out.println("+   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +");
        System.out.println();
        return selection;
    }

    private static void cancelTransfer() {
        try {
            if (balanceTransfer.getStatus().equals("REJECTED") || balanceTransfer.getStatus().equals("ACCEPTED")) {
                System.out.println("ERROR: cannot reject a non-pending transaction.");
                return;
            }

            Bank.session = Bank.sessionFactory.openSession();
            Transaction transaction = Bank.session.beginTransaction();

            balanceTransfer.setStatus("REJECTED");

            Bank.session.update(balanceTransfer);

            transaction.commit();
            System.out.println("TRANSFER SUCCESSFULLY REJECTED");
            System.out.println("+   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +");
            System.out.println();
            Bank.logTransaction("Customer " + account + " REJECTED balance transfer " + transaction);
        } finally {
            Bank.session.close();
        }
    }
}
