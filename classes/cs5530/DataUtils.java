package cs5530;

import java.util.Set;

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
}
