package exceptions;

/**
 * @author André Ribeiro
 */
public class GeneratingKeyException extends ExceptionTemplate
{
    private static final String message = "Error while generating the key.";
    
    public GeneratingKeyException(Exception exc)
    {
       super(exc);
    }
    
    public void printMessage()
    {
        super.printMessage(message);
    }
}
