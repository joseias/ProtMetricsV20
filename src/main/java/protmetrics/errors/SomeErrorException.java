package protmetrics.errors;

/**
 * Class to inform about exceptions.
 */
public class SomeErrorException extends Exception {

    /**
     *
     * @param a_messageError
     */
    public SomeErrorException(String a_messageError) {
        super(a_messageError);
    }
}
