package cs5530.Models;

import cs5530.DataUtils;
import cs5530.Database;
import cs5530.ModelFailed;
import org.json.simple.JSONArray;
import org.json.simple.JSONAware;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.*;

public class UC
{
    /**
     * Returns true if the UC is/was available for the given time slot.
     */
    public static boolean IsAvailable(String vin, String begin, String end)
    {
        /*
        SELECT *
        FROM UC uc, UD ud, Available a, Period p
        WHERE uc.login=ud.login AND a.login=ud.login AND p.pid=a.pid AND uc.vin="12345"
        HAVING p.begin < "2018-03-24 14:00:50" AND (p.end > '2018-03-24 16:35:50' OR p.end IS NULL)
         */
        String sql = String.format("SELECT *\n" +
                "FROM UC uc, UD ud, Available a, Period p\n" +
                "WHERE uc.login=ud.login AND a.login=ud.login AND p.pid=a.pid AND uc.vin=\"%s\"" +
                "HAVING p.begin < \"%s\" AND (p.end > \"%s\" OR p.end IS NULL)", vin, begin, end);
        String res = Database.Main().RunQuery(sql);
        return res != null;
    }

    public static JSONObject Create(HashMap<String, String> fields) throws ModelFailed
    {
        String[] requiredFields = new String[]{"vin", "category", "make", "model", "year", "login"};
        // Checks that fields contains all the required fields.
        DataUtils.VerifyFields(fields.keySet(), requiredFields);

        // Gets the keys, comma delimited.
        String keys = DataUtils.SqlKeys(fields);
        // Gets the escaped values, comma delimited.
        String values = DataUtils.SqlValues(fields);
        // Gets a string containing the pair name="value" for each field.
        String update = DataUtils.SqlMatch(fields);

        /*
        BEGIN;

        INSERT INTO Ctypes (make, model) VALUES (LOWER("%s"), LOWER("%s")) ON DUPLICATE KEY UPDATE make=make;

        SET @CTYPE = (SELECT tid
        FROM Ctypes c
        WHERE c.make=LOWER("%s") AND c.model=LOWER("%s"));

        INSERT INTO UC (vin, category, ctype, year, login)
          VALUES ("%s", "%s", @CTYPE, "%s", "%s")
        ON DUPLICATE KEY UPDATE category="%s", ctype=@CTYPE, year="%s" login="%s";
        COMMIT;
         */

        // Build the sql string, runs it, and returns success. On fail the exception propagates from RunUpdate().
        String sql = String.format("BEGIN;\n" +
                        "\n" +
                        "INSERT INTO Ctypes (make, model) VALUES (LOWER(\"%s\"), LOWER(\"%s\")) ON DUPLICATE KEY UPDATE make=make;\n" +
                        "\n" +
                        "SET @CTYPE = (SELECT tid\n" +
                        "FROM Ctypes c\n" +
                        "WHERE c.make=LOWER(\"%s\") AND c.model=LOWER(\"%s\"));\n" +
                        "\n" +
                        "INSERT INTO UC (vin, category, ctype, year, login)\n" +
                        "  VALUES (\"%s\", \"%s\", @CTYPE, \"%s\", \"%s\") \n" +
                        "ON DUPLICATE KEY UPDATE category=\"%s\", ctype=@CTYPE, year=\"%s\", login=\"%s\";\n" +
                        "COMMIT;", fields.get("make"), fields.get("model"), fields.get("make"), fields.get("model"), fields.get("vin"), fields.get("category"),
                fields.get("year"), fields.get("login"), fields.get("category"), fields.get("year"), fields.get("login"));
        Database.Main().RunUpdate(sql);
        JSONObject result = new JSONObject();
        result.put("Success", true);
        return result;
    }

    public static JSONAware Browse(HashMap<String, String> fields) throws ModelFailed, ParseException
    {
        String[] opfields = new String[]{"address","category", "make", "model", "year"};
        for(String s : opfields){
            if (fields.get(s) == null || fields.get(s).isEmpty()){
                fields.remove(s);
            }
        }
        String matches = DataUtils.SqlMatch(fields);
        System.out.println(matches);
        matches = matches.replaceAll(",", " AND");
        // Build the sql string, runs it, and returns success. On fail the exception propagates from RunUpdate().
        String sql = String.format("SELECT vin,category,year,make,model,address,UD.login FROM (UC LEFT JOIN Ctypes ON UC.ctype = Ctypes.tid) join UD WHERE %s", matches);
        String result = Database.Main().RunQuery(sql);
        if (result == null) return new JSONObject();
        return (JSONAware) new JSONParser().parse(result);
    }

}
