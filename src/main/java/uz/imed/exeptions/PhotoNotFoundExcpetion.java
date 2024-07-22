package uz.imed.exeptions;

public class PhotoNotFoundExcpetion extends RuntimeException
{
    public PhotoNotFoundExcpetion(String message)
    {
        super(message);
    }
}
