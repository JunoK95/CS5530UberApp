package cs5530;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DataUtils
{
    public static String VerifyFields(String[] required, Set<String> keys)
    {
        for (String field :
                required)
        {
            if (!keys.contains(field))
            {
                return String.format("%s is a required field.", field);
            }
        }
        return null;
    }

    public static String SqlValues(HashMap<String, String> fields)
    {
        Function<String, String> escape = s -> "\"" + s + "\"";

        String values = fields.values().stream()
                .map(escape)
                .collect(Collectors.joining(", "));
        return values;
    }

    public static String SqlMatch(HashMap<String, String> fields)
    {
        StringBuilder result = new StringBuilder();
        Iterator it = fields.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            result.append(String.format("%s=\"%s\"", pair.getKey(), pair.getValue()));
            if (it.hasNext()) result.append(", ");
        }
        return result.toString();
    }
}
