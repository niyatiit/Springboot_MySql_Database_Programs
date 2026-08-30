package in.niyati.practical7.exception;

public class AccountNotEmptyException extends RuntimeException {
    public AccountNotEmptyException(String message) {
        super(message);
    }
}