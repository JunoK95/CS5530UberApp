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

public class Trust
{
    public Trust()
    {}

    public static JSONObject trustUser(HashMap<String, String> fields) throws ModelFailed, JSONException, NoSuchAlgorithmException
    {
        String[] requiredFields = new String[]{"login1", "login2", "isTrusted"};
        DataUtils.VerifyFields(fields.keySet(), requiredFields);

        String values = DataUtils.SqlValues(fields);
        String keys = DataUtils.SqlKeys(fields);

        String sql = String.format("INSERT INTO Trust (%s) VALUES (%s)", keys, values);
        Database.Main().RunUpdate(sql);
        JSONObject response = new JSONObject();
        response.put("Success", true);
        return response;
    }
}
