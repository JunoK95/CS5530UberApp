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

public class Admin
{
    public Admin()
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

        if (fields.get("password").length() < 3)
        {
            throw new ModelFailed("Password too short. Must be at least 3 characters");
        }
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(fields.get("password").getBytes(StandardCharsets.UTF_8));
        fields.put("password", bytesToHex(hash));

        String keys = DataUtils.SqlKeys(fields);
        String values = DataUtils.SqlValues(fields);

        String sql = String.format("INSERT INTO UU (%s) VALUES (%s)", keys, values);
        Database.Main().RunUpdate(sql);
        JSONObject response = new JSONObject();
        response.put("User", fields.get("login"));
        return response;
    }

    public static JSONObject getTrustedUsers(HashMap<String, String> fields) throws ModelFailed, NoSuchAlgorithmException, ParseException
    {
        String[] requiredFields = new String[]{"number"};
        DataUtils.VerifyFields(fields.keySet(), requiredFields);
        String sql = String.format("SELECT COUNT(*) as trustCount, login2 as login FROM Trust t WHERE t.isTrusted = 1 GROUP BY login2 ORDER BY trustCount DESC LIMIT %s %n", fields.get("number"));
        String res = Database.Main().RunQuery(sql);
        System.out.println(res);
        JSONObject response = new JSONObject();
        response.put("Success", true);
        return response;
    }

    public static JSONObject getUsefulUsers(HashMap<String, String> fields) throws ModelFailed, NoSuchAlgorithmException, ParseException
    {
        String[] requiredFields = new String[]{"number"};
        DataUtils.VerifyFields(fields.keySet(), requiredFields);
        String sql = String.format("SELECT f1.login, AVG(r.rating) as usefulnessRating FROM UD ud, UC uc, Feedback f1, Rates r WHERE uc.login=ud.login AND uc.vin=f1.vin AND r.fid=f1.fid GROUP BY f1.login ORDER BY AVG(r.rating) DESC LIMIT %s %n", fields.get("number"));
        String res = Database.Main().RunQuery(sql);
        System.out.println(res);
        JSONObject response = new JSONObject();
        response.put("Success", true);
        return response;
    }

    public static JSONObject Statistics() throws ModelFailed, NoSuchAlgorithmException, ParseException
    {
        String sql = String.format("SELECT uc.category, uc.vin, COUNT(*) as rides FROM UC uc, Ride r WHERE uc.vin=r.vin GROUP BY uc.category, uc.vin HAVING rides = (SELECT MAX(q2.rides) FROM (SELECT uc.category, uc.vin, COUNT(*) as rides FROM UC uc, Ride r WHERE uc.vin=r.vin GROUP BY uc.category, uc.vin) q2 WHERE q2.category = uc.category)");
        String res = Database.Main().RunQuery(sql);
        System.out.println(res);
        JSONObject response = new JSONObject();
        response.put("Success", true);
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
