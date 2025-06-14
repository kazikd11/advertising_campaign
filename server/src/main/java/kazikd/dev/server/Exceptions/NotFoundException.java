package kazikd.dev.server.Exceptions;

//used for handling not found resources, users, campaigns etc.
public class NotFoundException extends RuntimeException {
    public NotFoundException(String s) {
        super(s);
    }
}
