package com.cogent.bank_transfer;

import com.cogent.checking_account.CheckingAccountDaoImp;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class BalanceTransferDaoImp implements BalanceTransferDao {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int transferId;
    private int toAccountNumber;
    private int fromAccountNumber;
    private int balance;
    private String status = "PENDING"; // ACCEPTED, REJECTED, PENDING

    public int getTransferId() {
        return transferId;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public int getToAccountNumber() {
        return toAccountNumber;
    }

    public void setToAccountNumber(int toAccountNumber) {
        this.toAccountNumber = toAccountNumber;
    }

    public int getFromAccountNumber() {
        return fromAccountNumber;
    }

    public void setFromAccountNumber(int fromAccountNumber) {
        this.fromAccountNumber = fromAccountNumber;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Balance Transfer : " +
                "transferId=" + transferId +
                ", toAccountNumber=" + toAccountNumber +
                ", fromAccountNumber=" + fromAccountNumber +
                ", balance=" + balance +
                ", status= " + status;
    }
}
