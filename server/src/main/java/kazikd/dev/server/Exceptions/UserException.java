package kazikd.dev.server.Exceptions;

// mostly for "authentication" errors and balance checks
public class UserException extends RuntimeException {
    public UserException(String s) {
        super(s);
    }
}
