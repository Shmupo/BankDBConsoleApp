package com.cogent.bank_transfer;

interface BalanceTransferDao {
    public int getTransferId();
    public void setTransferId(int transferId);
    public int getToAccountNumber();
    public void setToAccountNumber(int toAccountNumber);
    public int getFromAccountNumber();
    public void setFromAccountNumber(int fromAccountNumber);
    public int getBalance();
    public void setBalance(int balance);
}
