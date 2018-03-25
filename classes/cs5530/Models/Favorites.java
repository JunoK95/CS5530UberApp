package cs5530.Models;

import cs5530.DataUtils;
import cs5530.Database;
import cs5530.ModelFailed;
import org.json.simple.JSONObject;

import java.time.Clock;
import java.util.HashMap;

public class Favorites
{
    public Favorites()
    {}

    public static JSONObject FavoriteUC(HashMap<String, String> fields) throws ModelFailed
    {
        String[] requiredFields = new String[]{"login", "vin"};
        DataUtils.VerifyFields(fields.keySet(), requiredFields);

        fields.put("fvdate", Clock.systemUTC().instant().toString());

        String values = DataUtils.SqlValues(fields);

        //TODO: Needs to handle duplicates
        String sql = String.format("INSERT INTO Favorites (%s) VALUES (%s)", String.join(",", fields.keySet()), values);
        Database.Main().RunUpdate(sql);
        JSONObject response = new JSONObject();
        response.put("Success", true);
        return response;
    }
}
