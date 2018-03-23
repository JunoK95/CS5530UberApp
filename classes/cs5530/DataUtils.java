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
        Function<String, String> escape = s -> "\"" + s + "\"";

        return fields.values().stream()
                .map(escape)
                .collect(Collectors.joining(", "));
    }

    public static String SqlMatch(HashMap<String, String> fields)
    {
        StringBuilder result = new StringBuilder();
        Iterator it = fields.entrySet().iterator();
        while (it.hasNext())
        {
            Map.Entry pair = (Map.Entry) it.next();
            result.append(String.format("%s=\"%s\"", pair.getKey(), pair.getValue()));
            if (it.hasNext()) result.append(", ");
        }
        return result.toString();
    }
}
