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
        CheckIfOwnsFeedback(fields);

        String keys = DataUtils.SqlKeys(fields);
        String values = DataUtils.SqlValues(fields);
        String matches = DataUtils.SqlMatch(fields);

        String sql2 = String.format("INSERT INTO Rates (%s) VALUES (%s) ON DUPLICATE KEY UPDATE %s", keys, values, matches);
        Database.Main().RunUpdate(sql2);
        JSONObject response = new JSONObject();
        response.put("Success", true);
        return response;
    }

    private static void CheckIfOwnsFeedback(HashMap<String, String> fields) throws ModelFailed
    {
        /*
        SELECT *
        FROM Feedback f
        WHERE f.fid=1 AND login="asdf"
        */
        String sql = String.format("SELECT * \n" +
                "FROM Feedback f\n" +
                "WHERE f.fid=\"%s\" AND login=\"%s\"", fields.get("fid"), fields.get("login"));

        String res = Database.Main().RunQuery(sql);
        if (res != null) throw new ModelFailed("Can't give rating for your own feedback.");
    }
}
