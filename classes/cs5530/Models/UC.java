package cs5530.Models;

import cs5530.DataUtils;
import cs5530.Database;
import cs5530.ModelFailed;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class UC
{
    public static JSONObject Create(HashMap<String, String> fields) throws JSONException, ModelFailed
    {
        try
        {
            String[] requiredFields = new String[]{"vin", "category", "make", "model", "year", "login"};
            String error = DataUtils.VerifyFields(requiredFields, fields.keySet());
            if (error != null)
            {
                JSONObject response = new JSONObject();
                response.put("Error", error);
                return response;
            }

            String values = DataUtils.SqlValues(fields);
            String update = DataUtils.SqlMatch(fields);

            String sql = String.format("INSERT INTO UC (%s) VALUES (%s) ON DUPLICATE KEY UPDATE %s", String.join(",", fields.keySet()), values, update);
            Database.Main().RunUpdate(sql);
            JSONObject result = new JSONObject();
            result.put("Success", true);
            return result;
        }
        catch (Exception e)
        {
            throw e;
        }
    }
}
