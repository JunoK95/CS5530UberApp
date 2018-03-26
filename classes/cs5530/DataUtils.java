package cs5530;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DataUtils
{
    public static void VerifyFields(Set<String> keys, String[] required) throws ModelFailed
    {
        for (String field : required)
        {
            if (!keys.contains(field))
            {
                throw new ModelFailed(String.format("%s is a required field.", field));
            }
        }
        for (String field : keys)
        {
            if (!Arrays.asList(required).contains(field))
            {
                throw new ModelFailed(String.format("%s is not a required field.", field));
            }
        }
    }

    public static void VerifyFields(Set<String> keys, String[] required, String[] optional) throws ModelFailed
    {
        for (String field : required)
        {
            if (!keys.contains(field))
            {
                throw new ModelFailed(String.format("%s is a required field.", field));
            }
        }
        for (String field : keys)
        {
            if (!Arrays.asList(optional).contains(field) && !Arrays.asList(required).contains(field))
            {
                throw new ModelFailed(String.format("%s is neither a required nor optional field.", field));
            }
        }
    }

    public static String SqlValues(HashMap<String, String> fields)
    {
        Function<String, String> escape = s -> {
            if (s.toLowerCase().equals("true") || s.toLowerCase().equals("false"))
            {
                return s.toUpperCase();
            }
            return "\"" + s + "\"";
        };

        return fields.values().stream()
                .map(escape)
                .collect(Collectors.joining(", "));
    }

    public static String SqlMatch(HashMap<String, String> fields)
    {
        Function<Map.Entry, String> escape = pair -> {
            String key = pair.getKey().toString();
            String val = pair.getValue().toString();
            if (val.toLowerCase().equals("true") || val.toLowerCase().equals("false"))
            {
                return String.format("%s=%s", key, val.toUpperCase());
        }
            return String.format("%s=\"%s\"", key, val);
        };

        return fields.entrySet().stream()
                .map(escape)
                .collect(Collectors.joining(", "));
    }

    public static String SqlKeys(HashMap<String, String> fields)
    {
        return String.join(",", fields.keySet());
    }
}
