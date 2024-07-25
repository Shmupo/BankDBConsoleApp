package com.cogent.app_state;

import com.cogent.Bank;
import com.cogent.bank_employee.EmployeeAccountDaoImp;
import com.cogent.checking_account.CheckingAccountDaoImp;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class EmployeeViewTransactionLogs implements State {
    static EmployeeAccountDaoImp account;
    private final String transactionLogsDirectory = "src/main/transactionlogs";

    public EmployeeViewTransactionLogs(EmployeeAccountDaoImp account) {
        EmployeeViewTransactionLogs.account = account;
    }

    @Override
    public void execute() {
        ArrayList<String> filePaths = getTransactionLogFilePaths();

        while (true) {
            int selection = optionSelection(filePaths);

            if (selection == -1) {
                Bank.changeState(new EmployeeLoggedInState(account));
                System.out.println("RETURNING TO SELECTION");
                System.out.println("+   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +");
                System.out.println();
                return;
            }

            if (selection >= 0 && selection < filePaths.size()) {
                String selectedFilePath = filePaths.get(selection);
                Bank.changeState(new EmployeeSelectedTransactionLog(account, selectedFilePath));
                System.out.println("SELECTING TRANSACTION LOG");
                System.out.println("+   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +");
                System.out.println();
                return;
            } else {
                System.out.println("INVALID SELECTION, TRY AGAIN.");
                System.out.println("+   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +");
                System.out.println();
            }

        }
    }

    private int optionSelection(ArrayList<String> filePaths) {
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("+                                                                               +");
        System.out.println("+    HI " + account.getUser());
        System.out.println("+                                                                               +");
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("+                                                                               +");
        System.out.println("+                      SELECT A FILE BELOW TO BEGIN                             +");
        System.out.println("+                                                                               +");
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        if (filePaths.isEmpty()) System.out.println("NO FILES FOUND");
        for (int i = 0; i < filePaths.size(); i++) { System.out.println("+    PRESS " + (i) + ": " + filePaths.get(i).substring(filePaths.get(i).lastIndexOf("\\") + 1)); }
        System.out.println("+    ENTER -1 : BACK");
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.print("Select option: ");
        int selection =  Bank.scanner.nextInt();
        System.out.println("+   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +");
        System.out.println();
        return selection;
    }

    private ArrayList<String> getTransactionLogFilePaths() {
        ArrayList<String> filePaths = new ArrayList<>();
        File directory = new File(transactionLogsDirectory);

        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();

            if (files != null) {
                for (File file : files) {
                    // Check if it's a file (not a directory)
                    if (file.isFile()) {
                        // Add the file path to the list
                        filePaths.add(file.getAbsolutePath());
                    }
                }
            }
        } else {
            System.out.println("The specified path is not a directory or does not exist.");
        }

        return filePaths;
    }
}
