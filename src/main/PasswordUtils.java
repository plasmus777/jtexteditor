package main;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Optional;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class PasswordUtils {
  private static final SecureRandom RAND = new SecureRandom();
  private static final int ITERATIONS = 65536;
  private static final int KEY_LENGTH = 512;
  private static final String ALGORITHM = "PBKDF2WithHmacSHA512";

  public static Optional<String> generateSalt(final int length) {
    if (length < 1) {
      System.err.println("error in generateSalt: length must be > 0");
      return Optional.empty();
    }
    byte[] salt = new byte[length];
    RAND.nextBytes(salt);
    return Optional.of(Base64.getEncoder().encodeToString(salt));
  }

  public static Optional<String> hashPassword(String password, String salt) {
    char[] chars = password.toCharArray();
    byte[] bytes = salt.getBytes();
    PBEKeySpec spec = new PBEKeySpec(chars, bytes, ITERATIONS, KEY_LENGTH);
    Arrays.fill(chars, Character.MIN_VALUE);
    try {
      SecretKeyFactory fac = SecretKeyFactory.getInstance(ALGORITHM);
      byte[] securePassword = fac.generateSecret(spec).getEncoded();
      return Optional.of(Base64.getEncoder().encodeToString(securePassword));
    } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
      System.err.println("Exception encountered in hashPassword()");
      return Optional.empty();
    } finally {
      spec.clearPassword();
    }
  }

  public static boolean verifyPassword(String password, String key, String salt) {
    Optional<String> optEncrypted = hashPassword(password, salt);
    if (!optEncrypted.isPresent())
      return false;
    return optEncrypted.get().equals(key);
  }

  public static void main(String[] args) {
    String salt = generateSalt(512).get();
    String password = "Of Salesmen!";
    String key = hashPassword(password, salt).get();
    System.out.println(verifyPassword("Of Salesmen!", key, salt));
  }

}
