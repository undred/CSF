package exceptions;

/**
 * @author André Ribeiro
 */
public abstract class ExceptionTemplate extends Exception 
{
    private Exception exc;
    
    protected ExceptionTemplate(Exception exc)
    {
        this.exc = exc;
    }
    
    protected void printMessage(String message)
    {
         System.err.println(message);
         System.err.println("This was the cause " + exc.toString());
    }
}
