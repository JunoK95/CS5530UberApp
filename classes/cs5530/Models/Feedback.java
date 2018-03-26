package cs5530.Models;

import cs5530.DataUtils;
import cs5530.Database;
import cs5530.ModelFailed;
import org.json.simple.JSONObject;

import java.time.Clock;
import java.util.HashMap;

public class Feedback
{
    public Feedback()
    {}

    public static JSONObject GiveFeedback(HashMap<String, String> fields) throws ModelFailed
    {
        String[] requiredFields = new String[]{"login", "vin", "text", "score"};
        DataUtils.VerifyFields(fields.keySet(), requiredFields);

        fields.put("fbdate", Clock.systemUTC().instant().toString());

        String keys = DataUtils.SqlKeys(fields);
        String values = DataUtils.SqlValues(fields);

        String sql = String.format("INSERT INTO Feedback (%s) VALUES (%s)", keys, values);
        Database.Main().RunUpdate(sql);
        JSONObject response = new JSONObject();
        response.put("Success", true);
        return response;
    }
}
