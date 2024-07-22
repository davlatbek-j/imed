package uz.imed.exeptions;

public class LanguageNotSupportException extends RuntimeException
{
    public LanguageNotSupportException(String message)
    {
        super(message);
    }
}
