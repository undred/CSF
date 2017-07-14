package exceptions;

/**
 * @author André Ribeiro
 */
public class EncodingMessageException extends ExceptionTemplate
{
    private static final String message = "Error while enconding the message.";
    
    public EncodingMessageException(Exception exc)
    {
       super(exc, message);
    }
}
