package exceptions;

/**
 * @author André Ribeiro
 */
public abstract class ExceptionTemplate extends Exception 
{
    private Exception exc;
    private String message;
    
    public ExceptionTemplate(Exception exc, String message)
    {
        this.exc = exc;
        this.message = message;
    }
    
    public void printMessage()
    {
         System.err.println(message);
         System.err.println("This was the cause " + exc.toString());
    }
}
