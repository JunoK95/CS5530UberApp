package cs5530.Models;

import cs5530.DataUtils;
import cs5530.Database;
import cs5530.ModelFailed;
import org.json.JSONException;
import org.json.JSONObject;

import javax.xml.crypto.Data;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Reserve
{
    public Reserve()
    {}

    public static JSONObject ReserveUC(HashMap<String, String> fields) throws ModelFailed, JSONException
    {
        String[] requiredFields = new String[]{"login", "vin", "pid", "cost", "date"};
        DataUtils.VerifyFields(fields.keySet(), requiredFields);

        String keys = DataUtils.SqlKeys(fields);
        String values = DataUtils.SqlValues(fields);

        String sql = String.format("INSERT INTO Reserve (%s) VALUES (%s)", keys, values);
        Database.Main().RunUpdate(sql);
        JSONObject response = new JSONObject();
        response.put("Success", true);
        return response;
    }
}
