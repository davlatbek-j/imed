package uz.imed.config.security;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@RequiredArgsConstructor

@Service
public class JwtTokenService
{
    private final UserRepository userRepository;

    @Value("${jwt.token.secretKey}")
    private String secretKey;

    @Value("${jwt.token.expireDateInMilliSeconds}")
    private Long expireDate;

    public String generateToken(String subject)
    {
        Date now = new Date();
        return Jwts
                .builder()
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expireDate))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public boolean validateToken(String token)
    {
        try
        {
            Jwts
                    .parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e)
        {
            System.err.println("Muddati o'tgan");
        } catch (MalformedJwtException malformedJwtException)
        {
            System.err.println("Buzilgan token");
        } catch (SignatureException s)
        {
            System.err.println("Kalit so'z xato");
        } catch (UnsupportedJwtException unsupportedJwtException)
        {
            System.err.println("Qo'llanilmagan token");
        } catch (IllegalArgumentException ex)
        {
            System.err.println("Bo'sh token");
        }
        return false;
    }

    public User getUserFromToken(String token)
    {
        try
        {
            String subject = Jwts
                    .parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
            return userRepository.findByUsername(subject).orElseThrow(RuntimeException::new);
        } catch (Exception e)
        {
            throw new AuthorizationFailedException("Authorization failed. Please login again");
        }
    }

}
