package exceptions;

/**
 * @author André Ribeiro
 */
public class DecriptingMessageExcepetion extends ExceptionTemplate
{
    private static final String message = "Error while decriptying the message.";
    
    public DecriptingMessageExcepetion(Exception exc)
    {
       super(exc);
    }
    
    public void printMessage()
    {
        super.printMessage(message);
    }
}
