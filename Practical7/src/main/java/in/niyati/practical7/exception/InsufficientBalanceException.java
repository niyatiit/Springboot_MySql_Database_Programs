package in.niyati.practical7.exception;

// Custom UNCHECKED exception (extends RuntimeException).
// This is important: @Transactional only triggers automatic rollback
// for RuntimeExceptions (unchecked) by default - NOT for checked exceptions.
public class InsufficientBalanceException extends RuntimeException {
    public InsufficientBalanceException(String message) {
        super(message);
    }
}