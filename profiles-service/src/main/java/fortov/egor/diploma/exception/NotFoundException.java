package fortov.egor.diploma.exception;


import fortov.egor.diploma.user.User;

public class NotFoundException extends RuntimeException {
    private static final String message = "%s отсутствует в памяти программы.";

    public NotFoundException(User user) {
        super(String.format(message, user));
    }

//    public NotFoundException(Film film) {
//        super(String.format(message, film));
//    }

    public NotFoundException(String s) {
        super(s);
    }
}
