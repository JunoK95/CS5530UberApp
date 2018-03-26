package cs5530.Models;

import cs5530.DataUtils;
import cs5530.Database;
import cs5530.ModelFailed;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class UU
{
    public UU()
    {}

    public String AllUsers()
    {
        String sql = "SELECT * FROM UU";

        return Database.Main().RunQuery(sql);
    }

    public static JSONObject Login(HashMap<String, String> fields) throws ModelFailed, NoSuchAlgorithmException, ParseException
    {
        String[] requiredFields = new String[]{"login", "password"};
        DataUtils.VerifyFields(fields.keySet(), requiredFields);

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(fields.get("password").getBytes(StandardCharsets.UTF_8));
        String password = bytesToHex(hash);

        String sql = String.format("SELECT * FROM UU WHERE login=\"%s\"", fields.get("login"));
        String res = Database.Main().RunQuery(sql);

        if (res != null)
        {
            JSONObject query = (JSONObject) new JSONParser().parse(res);
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

    public static JSONObject Register(HashMap<String, String> fields) throws ModelFailed, NoSuchAlgorithmException
    {
        String[] requiredFields = new String[]{"login", "name", "address", "phone", "password"};
        DataUtils.VerifyFields(fields.keySet(), requiredFields);

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(fields.get("password").getBytes(StandardCharsets.UTF_8));
        fields.put("password", bytesToHex(hash));

        String keys = DataUtils.SqlKeys(fields);
        String values = DataUtils.SqlValues(fields);

        String sql = String.format("INSERT INTO UD (%s) VALUES (%s)", keys, values);
        Database.Main().RunUpdate(sql);
        JSONObject response = new JSONObject();
        response.put("User", fields.get("login"));
        return response;
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
