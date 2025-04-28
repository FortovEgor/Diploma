package fortov.egor.diploma.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DBException extends RuntimeException {
    private static final String message = "Error while processing request at DB";

    public DBException() {
        super(message);
    }

    public DBException(String s) {
        super(s);
    }
}
