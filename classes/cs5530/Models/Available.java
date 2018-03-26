package cs5530.Models;

import cs5530.DataUtils;
import cs5530.Database;
import cs5530.ModelFailed;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.util.HashMap;

public class Available
{
    public static boolean IsAvailable(String login) throws ModelFailed
    {
        /*
        SELECT *
        FROM Available a, Period p
        WHERE a.pid = p.pid AND a.login="%s" AND end IS NULL
        */

        // Build the sql string, runs it, and returns success. On fail the exception propagates from RunUpdate().
        String sql = String.format("SELECT *\n" +
                "FROM Available a, Period p\n" +
                "WHERE a.pid = p.pid AND a.login=\"%s\" AND end IS NULL", login);
        String result = Database.Main().RunQuery(sql);
        return result != null;
    }

    public static JSONObject GoAvailble(HashMap<String, String> fields) throws ModelFailed, ParseException
    {
        String[] requiredFields = new String[]{"login"};
        DataUtils.VerifyFields(fields.keySet(), requiredFields);

        if (!IsAvailable(fields.get("login")))
        {
            String sql = String.format("BEGIN;\n" +
                    "INSERT INTO Period (begin)\n" +
                    "  VALUES(NOW());\n" +
                    "INSERT INTO Available (login, pid) \n" +
                    "  VALUES(\"%s\", LAST_INSERT_ID());\n" +
                    "COMMIT;", fields.get("login"));

            Database.Main().RunUpdate(sql);

            JSONObject response = new JSONObject();
            response.put("Success", true);
            return response;
        }
        throw new ModelFailed("You are already available.");
    }

    public static JSONObject GoUnavailble(HashMap<String, String> fields) throws ModelFailed
    {
        String[] requiredFields = new String[]{"login"};
        DataUtils.VerifyFields(fields.keySet(), requiredFields);

        if (IsAvailable(fields.get("login")))
        {
            /*
            UPDATE Period
            SET end=NOW()
            WHERE pid IN (
                SELECT a.pid
                FROM Available a, (SELECT * FROM Period) as p
                WHERE p.pid=a.pid AND a.login="%s" AND p.end IS NULL
            )
             */
            String sql = String.format("UPDATE Period\n" +
                    "SET end=NOW()\n" +
                    "WHERE pid IN (\n" +
                    "    SELECT a.pid \n" +
                    "    FROM Available a, (SELECT * FROM Period) as p\n" +
                    "    WHERE p.pid=a.pid AND a.login=\"%s\" AND p.end IS NULL\n" +
                    ")", fields.get("login"));

            Database.Main().RunUpdate(sql);

            JSONObject response = new JSONObject();
            response.put("Success", true);
            return response;
        }
        throw new ModelFailed("You are already set to unavailable.");
    }
}
