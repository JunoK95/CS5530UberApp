package cs5530.Models;

import cs5530.DataUtils;
import cs5530.Database;
import cs5530.ModelFailed;
import org.json.simple.JSONObject;

import java.util.HashMap;

public class Rates
{
    public static JSONObject RateFeedback(HashMap<String, String> fields) throws ModelFailed
    {
        String[] requiredFields = new String[]{"login", "fid", "rating"};
        DataUtils.VerifyFields(fields.keySet(), requiredFields);

        String keys = DataUtils.SqlKeys(fields);
        String values = DataUtils.SqlValues(fields);
        String matches = DataUtils.SqlMatch(fields);

        String sql = String.format("INSERT INTO Rates (%s) VALUES (%s) ON DUPLICATE KEY UPDATE %s", keys, values, matches);
        Database.Main().RunUpdate(sql);
        JSONObject response = new JSONObject();
        response.put("Success", true);
        return response;
    }
}
