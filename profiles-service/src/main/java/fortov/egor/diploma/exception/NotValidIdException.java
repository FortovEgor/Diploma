package fortov.egor.diploma.exception;

public class NotValidIdException extends RuntimeException {
    public NotValidIdException() {
        super("id должен быть > ноля.");
    }
}
