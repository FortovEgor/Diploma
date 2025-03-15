package fortov.egor.diploma.exception;


public class NotFoundException extends RuntimeException {
    private static final String message = "%s отсутствует в памяти программы.";

    public NotFoundException(Object obj) {
        super(String.format(message, obj));
    }

    public NotFoundException(String s) {
        super(s);
    }
}
