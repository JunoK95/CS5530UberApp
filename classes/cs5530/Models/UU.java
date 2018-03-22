package cs5530.Models;

import cs5530.DataUtils;
import cs5530.Database;
import cs5530.ModelFailed;
import org.json.JSONException;
import org.json.JSONObject;

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

    public static JSONObject Login(HashMap<String, String> fields) throws ModelFailed, JSONException, NoSuchAlgorithmException
    {
        try
        {
            String[] requiredFields = new String[]{"login", "password"};
            String error = DataUtils.VerifyFields(requiredFields, fields.keySet());
            if (error != null)
            {
                throw new ModelFailed(error);
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
                    JSONObject response = new JSONObject();
                    response.put("User", query.get("login").toString());
                    return response;
                } else
                {
                    throw new ModelFailed("Invalid login or password");
                }
            } else
            {
                throw new ModelFailed("Invalid login or password");
            }
        }
        catch (Exception e)
        {
            throw e;
        }
    }

    public static JSONObject Register(HashMap<String, String> fields) throws ModelFailed, JSONException, NoSuchAlgorithmException
    {
        try
        {
            String[] requiredFields = new String[]{"login", "name", "address", "phone", "password"};
            String error = DataUtils.VerifyFields(requiredFields, fields.keySet());
            if (error != null)
            {
                throw new ModelFailed(error);
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
                JSONObject response = new JSONObject();
                response.put("User", fields.get("login"));
                return response;
            } else
            {
                throw new ModelFailed(res);
            }
        }
        catch (Exception e)
        {
            throw e;
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
