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

public class Feedback
{
    public Feedback()
    {}

    public String AllFeedback()
    {
        String sql = "SELECT * FROM Feedback";

        return Database.Main().RunQuery(sql);
    }

    public static JSONObject giveFeedback (HashMap<String, String> fields) throws ModelFailed, JSONException, NoSuchAlgorithmException
    {
        try
        {
            String[] requiredFields = new String[]{"login", "fid", "vin", "text", "score", "fbdate"};
            DataUtils.VerifyFields(fields.keySet(), requiredFields);

            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            Function<String, String> escape = s -> "\"" + s + "\"";

            String values = fields.values().stream()
                    .map(escape)
                    .collect(Collectors.joining(", "));
             //TODO: Needs to handle duplicates
            String sql = String.format("INSERT INTO Feedback (%s) VALUES (%s)", String.join(",", fields.keySet()), values);
            String res = Database.Main().RunUpdate(sql);
            JSONObject response = new JSONObject();
            response.put("User", fields.get("login"));
            return response;
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
