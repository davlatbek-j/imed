package uz.imed.config.security;

public class AuthorizationFailedException extends RuntimeException
{
    public AuthorizationFailedException(String message)
    {
        super(message);
    }
}
