package exceptions;

/**
 * @author André Ribeiro
 */
public class DecodingMessageException extends ExceptionTemplate
{
    private static final String message = "Error while deconding the message.";

    public DecodingMessageException(Exception exc)
    {
       super(exc, message);
    }
}
