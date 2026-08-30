package in.niyati.practical7.service;

import in.niyati.practical7.entity.Account;

public interface AccountService {
    void transferFunds(int fromId, int toId, double amount);
    Account updateAccountHolder(int id, String newHolderName);
    void closeAccount(int id);
}