package cs5530.Models;

import cs5530.DataUtils;
import cs5530.Database;
import cs5530.ModelFailed;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Favorites
{
    public Favorites()
    {}

    public static JSONObject favoriteUC(HashMap<String, String> fields) throws ModelFailed, JSONException, NoSuchAlgorithmException
    {
        String[] requiredFields = new String[]{"login", "vin", "fvdate"};
        DataUtils.VerifyFields(fields.keySet(), requiredFields);

        String values = DataUtils.SqlValues(fields);

        //TODO: Needs to handle duplicates
        String sql = String.format("INSERT INTO Favorites (%s) VALUES (%s)", String.join(",", fields.keySet()), values);
        Database.Main().RunUpdate(sql);
        JSONObject response = new JSONObject();
        response.put("User", fields.get("login"));
        return response;
    }
}
