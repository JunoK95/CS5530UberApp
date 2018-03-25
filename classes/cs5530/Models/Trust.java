package cs5530.Models;

import cs5530.DataUtils;
import cs5530.Database;
import cs5530.ModelFailed;
import org.json.simple.JSONObject;

import java.util.HashMap;

public class Trust
{
    public Trust()
    {}

    public static JSONObject TrustUser(HashMap<String, String> fields) throws ModelFailed
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
