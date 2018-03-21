package cs5530;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONString;

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

    public static Boolean Login(HashMap<String, String> fields, JSONObject response)
    {
        try
        {
            String[] requiredFields = new String[]{"login", "password"};
            String error = DataUtils.VerifyFields(requiredFields, fields.keySet());
            if (error != null)
            {
                response.put("Success", false);
                response.put("Error", error);
                return false;
            }

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(fields.get("password").getBytes(StandardCharsets.UTF_8));
            String password = bytesToHex(hash);

            String sql = String.format("SELECT * FROM UU WHERE login=\"%s\"", fields.get("login"));
            String res = Database.Main().RunQuery(sql);

            if (res != null)
            {
                JSONObject query = new JSONObject(res);
                if (query.get("password").toString().equals(password))
                {
                    response.put("Success", true);
                    response.put("User", query.get("login").toString());
                    return true;
                } else
                {
                    response.put("Success", false);
                    response.put("Error", "Invalid login or password");
                    return false;
                }
            } else
            {
                response.put("Success", false);
                response.put("Error", "Invalid login or password");
                return false;
            }
        }
        catch (Exception e)
        {
            System.out.println(e.toString());
            return false;
        }
    }

    public static Boolean Register(HashMap<String, String> fields, JSONObject response)
    {
        try
        {
            String[] requiredFields = new String[]{"login", "name", "address", "phone", "password"};
            String error = DataUtils.VerifyFields(requiredFields, fields.keySet());
            if (error != null)
            {
                response.put("Success", false);
                response.put("Error", error);
                return false;
            }

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(fields.get("password").getBytes(StandardCharsets.UTF_8));
            fields.put("password", bytesToHex(hash));

            Function<String, String> escape = s -> "\"" + s + "\"";

            String values = fields.values().stream()
                    .map(escape)
                    .collect(Collectors.joining(", "));

            String sql = String.format("INSERT INTO UU (%s) VALUES (%s)", String.join(",", fields.keySet()), values);
            String res = Database.Main().RunUpdate(sql);
            if (res.equals("Success"))
            {
                response.put("Success", true);
                response.put("User", fields.get("login"));
                return true;
            } else
            {
                response.put("Success", false);
                response.put("Error", res);
                return false;
            }
        }
        catch (Exception e)
        {
            System.out.println(e.toString());
            return false;
        }
    }

    /**
     * This is from here:
     * http://www.baeldung.com/sha-256-hashing-java
     */
    private static String bytesToHex(byte[] hash)
    {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++)
        {
            String hex = Integer.toHexString(0xff & hash[i]);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
