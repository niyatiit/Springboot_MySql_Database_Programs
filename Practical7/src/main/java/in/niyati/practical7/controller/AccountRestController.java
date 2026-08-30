package in.niyati.practical7.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import in.niyati.practical7.entity.Account;
import in.niyati.practical7.repository.AccountRepository;
import in.niyati.practical7.service.AccountService;

@RestController
@RequestMapping("/api/accounts")
public class AccountRestController {

    private AccountRepository accountRepository;
    private AccountService accountService;

    public AccountRestController(AccountRepository accountRepository, AccountService accountService) {
        this.accountRepository = accountRepository;
        this.accountService = accountService;
    }

    // POST - Create a new account (needed to seed test data)
    @PostMapping
    public Account createAccount(@RequestBody Account account) {
        return accountRepository.save(account);
    }

    // GET - Fetch all accounts
    @GetMapping
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    // GET - View a single account's balance
    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccountById(@PathVariable int id) {
        Optional<Account> account = accountRepository.findById(id);
        return account.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // POST - Transfer funds between two accounts (transactional)
    @PostMapping("/transfer")
    public ResponseEntity<String> transferFunds(@RequestBody Map<String, Object> request) {
        int fromId = (int) request.get("fromId");
        int toId = (int) request.get("toId");
        double amount = ((Number) request.get("amount")).doubleValue();

        accountService.transferFunds(fromId, toId, amount);
        return ResponseEntity.ok("Transfer of " + amount + " from account " + fromId + " to account " + toId + " successful.");
    }

    // PUT - Update non-transactional account details (accountHolder name only)
    @PutMapping("/{id}")
    public ResponseEntity<Account> updateAccountHolder(@PathVariable int id, @RequestBody Account updatedAccount) {
        Account saved = accountService.updateAccountHolder(id, updatedAccount.getAccountHolder());
        return ResponseEntity.ok(saved);
    }

    // DELETE - Close an account (only permitted when balance is zero)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> closeAccount(@PathVariable int id) {
        accountService.closeAccount(id);
        return ResponseEntity.noContent().build(); // 204
    }
}