package cs5530.Models;

import cs5530.DataUtils;
import cs5530.Database;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class UC
{
    public static JSONObject Create(HashMap<String, String> fields) throws JSONException
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

            String sql = String.format("SELECT * FROM UD WHERE login=\"%s\"", fields.get("login"));
            String res = Database.Main().RunQuery(sql);

        }
        catch (Exception e)
        {
            throw e;
        }
        return null;
    }
}
