package in.niyati.practical14.exception;

// Custom unchecked exception, thrown from the SERVICE layer whenever
// a requested resource (Employee, Customer, etc.) doesn't exist.
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}