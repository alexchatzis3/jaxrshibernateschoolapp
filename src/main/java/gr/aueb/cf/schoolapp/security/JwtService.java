package gr.aueb.cf.schoolapp.security;

import gr.aueb.cf.schoolapp.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.enterprise.context.ApplicationScoped;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

/**
 * Service class for handling JSON Web Tokens (JWT) generation, validation, and claims extraction.
 *
 * <p>This class provides methods to create JWT tokens for authenticated users,
 * validate tokens, and extract claims such as username and expiration date.</p>
 *
 * <p>Tokens are signed with a secret key using the HS256 algorithm.
 * The expiration time is configurable (default 3 hours).</p>
 */
@ApplicationScoped
public class JwtService {

    /** Secret key used to sign the JWT. Must be kept safe and secure. */
    private String secretKey =  "5ce98d378ec88ea09ba8bcd511ef23645f04cc8e70b9134b98723a53c275bbc5";

    /** JWT expiration time in milliseconds (default 3 hours). */
    private long jwtExpiration = 10800000;

    /**
     * Generates a JWT token for the given username and role.
     *
     * @param username the username to include in the token subject
     * @param role the user's role to include as a custom claim
     * @return a signed JWT token as a {@link String}
     */
    public String generateToken(String username, String role) {
        var claims = new HashMap<String, Object>();
        claims.put("role", role);

        return Jwts.builder()
                .setIssuer("self") // optional issuer
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Validates a JWT token against the provided user.
     *
     * <p>The token is valid if the subject matches the user's username
     * and the token has not expired.</p>
     *
     * @param token the JWT token
     * @param user the {@link User} to validate against
     * @return {@code true} if the token is valid, {@code false} otherwise
     */
    public boolean isTokenValid(String token, User user) {
        final String subject = extractSubject(token);
        return (subject.equals(user.getName())) && !isTokenExpired(token);
    }

    /**
     * Extracts the subject (username) from a JWT token.
     *
     * @param token the JWT token
     * @return the username stored in the token
     */
    public String extractSubject(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts a specific claim from a JWT token.
     *
     * @param <T> the type of the claim
     * @param token the JWT token
     * @param claimsResolver a function to extract a specific claim
     * @return the extracted claim value
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /** Checks if the JWT token has expired. */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /** Extracts the expiration date from a JWT token. */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /** Extracts all claims from the JWT token. */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Generates the signing key used for HS256.
     *
     * <p>The secret key is decoded from Base64 and converted into
     * a {@link javax.crypto.SecretKey} which implements {@link Key}.</p>
     *
     * @return a {@link Key} used to sign and validate JWT tokens
     */
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
