package in.niyati.practical18.security;

// Shared secret key used BOTH to sign tokens (our test token generator)
// AND to validate them (ResourceServerConfig). In a real system, this key
// would live only on the Authorization Server, and the Resource Server
// would instead point to the Authorization Server's public issuer-uri/jwk-set-uri
// to fetch its public verification key - no shared secret needed.
public class JwtConstants {
    // Must be at least 256 bits (32+ characters) for HS256
    public static final String SECRET_KEY = "MySuperSecretKeyForJWTPracticalDemoPurposeOnly123456";
}