package uz.imed.exception;

public class NoUniqueNameException extends RuntimeException
{
    public NoUniqueNameException(String lang)
    {
        super(lang);
    }
}
