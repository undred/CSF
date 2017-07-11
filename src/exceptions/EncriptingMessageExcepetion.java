package exceptions;

/**
 * @author Paulo Martins
 */
public class EncriptingMessageExcepetion extends ExceptionTemplate
{
    private static final String message = "Error while encriptying the message.";
    
    public EncriptingMessageExcepetion(Exception exc)
    {
       super(exc);
    }
    
    public void printMessage()
    {
        super.printMessage(message);
    }
}
