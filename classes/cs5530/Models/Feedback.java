package cs5530.Models;

import cs5530.DataUtils;
import cs5530.Database;
import cs5530.ModelFailed;
import org.json.simple.JSONArray;
import org.json.simple.JSONAware;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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

        String keys = DataUtils.SqlKeys(fields);
        String values = DataUtils.SqlValues(fields);

        String sql = String.format("INSERT INTO Feedback (%s, fbdate) VALUES (%s, NOW())", keys, values);
        Database.Main().RunUpdate(sql);
        JSONObject response = new JSONObject();
        response.put("Success", true);
        return response;
    }

    public static JSONAware MostUsefulFeedback(HashMap<String, String> fields) throws ModelFailed, ParseException
    {
        String[] requiredFields = new String[]{"ud", "count"};
        DataUtils.VerifyFields(fields.keySet(), requiredFields);

        String sql = String.format("SELECT *\n" +
                "FROM Feedback f\n" +
                "ORDER BY (SELECT AVG(r.rating)\n" +
                "FROM UD ud, UC uc, Feedback f1, Rates r\n" +
                "WHERE ud.login=\"%s\" AND uc.login=ud.login AND uc.vin=f.vin AND r.fid=f1.fid AND f1.fid=f.fid\n" +
                "GROUP BY r.rating) DESC\n" +
                "LIMIT %s", fields.get("ud"), fields.get("count"));

        String res = Database.Main().RunQuery(sql);
        if (res != null)
        {
            return (JSONAware) new JSONParser().parse(res);
        }
        return new JSONArray();
    }
}
