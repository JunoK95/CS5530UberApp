package cs5530;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public class UU
{
    public UU()
    {}

    public String AllUsers()
    {
        String sql = "SELECT * FROM UU";

        return Database.Main().RunQuery(sql);
    }

    /**
     * This is from here:
     * http://www.baeldung.com/sha-256-hashing-java
     */
    private static String bytesToHex(byte[] hash) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static String Register(HashMap<String, String> fields)
    {
        String[] requiredFields = new String[] {"login", "name", "address", "phone", "password"};
        String error = DataUtils.VerifyFields(requiredFields, fields.keySet());
        if (error != null)
        {
            return error;
        }

        try
        {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(fields.get("password").getBytes(StandardCharsets.UTF_8));
            fields.put("password", bytesToHex(hash));

            Function<String,String> escape = s -> "\"" + s + "\"";

            String values = fields.values().stream()
                    .map(escape)
                    .collect(Collectors.joining(", "));

            String sql = String.format("INSERT INTO UU (%s) VALUES (%s)", String.join(",", fields.keySet()), values);
            return Database.Main().RunUpdate(sql);
        }
        catch (NoSuchAlgorithmException e)
        {
            return "ERROR - Hashing Algorithm does not exist!";
        }
    }
}
