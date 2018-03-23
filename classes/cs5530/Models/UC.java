package cs5530.Models;

import cs5530.DataUtils;
import cs5530.Database;
import cs5530.ModelFailed;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

public class UC
{
    public static JSONObject Create(HashMap<String, String> fields) throws JSONException, ModelFailed
    {
        try
        {
            String[] requiredFields = new String[] {"vin", "category", "make", "model", "year", "login"};
            // Checks that fields contains all the required fields.
            DataUtils.VerifyFields(fields.keySet(), requiredFields);

            // Gets the escaped values, comma delimited.
            String values = DataUtils.SqlValues(fields);
            // Gets a string containing the pair name="value" for each field.
            String update = DataUtils.SqlMatch(fields);

            // Build the sql string, runs it, and returns success. On fail the exception propagates from RunUpdate().
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
