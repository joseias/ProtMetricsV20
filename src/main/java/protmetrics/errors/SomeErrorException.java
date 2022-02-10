package protmetrics.errors;

/**
 * Class to inform about exceptions.
 */
public class SomeErrorException extends Exception {

    /**
     * @param msg error message.
     */
    public SomeErrorException(String msg) {
        super(msg);
    }
}
