package exceptions;

/**
 * @author André Ribeiro
 */
public class EncriptingMessageExcepetion extends ExceptionTemplate
{
    private static final String message = "Error while encriptying the message.";
    
    public EncriptingMessageExcepetion(Exception exc)
    {
       super(exc, message);
    }
    
}
