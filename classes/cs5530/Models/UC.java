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

        // Build the sql string, runs it, and returns success. On fail the exception propagates from RunUpdate().
        String sql = String.format("INSERT INTO UC (%s) VALUES (%s) ON DUPLICATE KEY UPDATE %s", keys, values, update);
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
