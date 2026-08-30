package in.niyati.practical7.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.niyati.practical7.entity.Account;
import in.niyati.practical7.exception.AccountNotEmptyException;
import in.niyati.practical7.exception.AccountNotFoundException;
import in.niyati.practical7.exception.InsufficientBalanceException;
import in.niyati.practical7.repository.AccountRepository;

@Service
public class AccountServiceImpl implements AccountService {

    private AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    // @Transactional wraps this entire method in a single database transaction.
    // If ANY RuntimeException is thrown anywhere inside this method (like
    // InsufficientBalanceException below), Spring automatically ROLLS BACK
    // every change made so far in this method - including the debit step,
    // even though it already called save() on the source account.
    @Override
    @Transactional
    public void transferFunds(int fromId, int toId, double amount) {

        Account fromAccount = accountRepository.findById(fromId)
                .orElseThrow(() -> new AccountNotFoundException("Source account not found: " + fromId));

        Account toAccount = accountRepository.findById(toId)
                .orElseThrow(() -> new AccountNotFoundException("Destination account not found: " + toId));

        // Check balance BEFORE debiting
        if (fromAccount.getBalance() < amount) {
            throw new InsufficientBalanceException(
                    "Insufficient balance in account " + fromId + ". Available: " + fromAccount.getBalance());
        }

        // Step 1: Debit source account
        fromAccount.setBalance(fromAccount.getBalance() - amount);
        accountRepository.save(fromAccount);

        // Step 2: Credit destination account
        toAccount.setBalance(toAccount.getBalance() + amount);
        accountRepository.save(toAccount);

        // If everything above succeeds, @Transactional commits both changes together.
        // If an exception is thrown ANYWHERE above (even after the debit save()),
        // both changes are rolled back - the debit never actually persists to the DB.
    }

    @Override
    public Account updateAccountHolder(int id, String newHolderName) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + id));
        account.setAccountHolder(newHolderName);
        return accountRepository.save(account);
    }

    @Override
    public void closeAccount(int id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + id));

        if (account.getBalance() != 0) {
            throw new AccountNotEmptyException(
                    "Cannot close account " + id + " - balance must be zero. Current balance: " + account.getBalance());
        }

        accountRepository.deleteById(id);
    }
}