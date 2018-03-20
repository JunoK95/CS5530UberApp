package cs5530;



import java.lang.*;
import java.io.*;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;

public class testdriver2
{

    /**
     *
     */
    public static void displayMenu()
    {
        System.out.println("        Welcome to UUber System     ");
        System.out.println("1. register a new user:");
        System.out.println("2. enter your own query:");
        System.out.println("3. exit:");
        System.out.println("please enter your choice:");
    }

    public static void main(String[] args)
    {
        // TODO Auto-generated method stub
        System.out.println("Example for cs5530");
        String choice;
        String cname;
        String dname;
        String sql = null;
        int c = 0;
        try
        {
            Database.Main().Connect();

            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

            while (true)
            {
                displayMenu();
                while ((choice = in.readLine()) == null && choice.length() == 0) ;
                try
                {
                    c = Integer.parseInt(choice);
                } catch (Exception e)
                {

                    continue;
                }
                if (c < 1 | c > 3)
                    continue;
                if (c == 1)
                {
                    HashMap<String, String> inputs = new HashMap<>();
                    String[] requiredFields = new String[] {"login", "name", "address", "phone", "password"};
                    for (String field : requiredFields)
                    {
                        System.out.println(String.format("Please enter a %s:", field));

                        String input;
                        while ((input = in.readLine()) == null && input.length() == 0);
                        inputs.put(field, input);
                    }
                    System.out.println(UU.Register(inputs));
                } else if (c == 2)
                {
                    System.out.println("please enter your query below:");
                    while ((sql = in.readLine()) == null && sql.length() == 0)
                        System.out.println(sql);

                    System.out.println(Database.Main().RunQuery(sql));
                } else
                {
                    System.out.println("EoM");
                    Database.Main().Close();
                    break;
                }
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            System.err.println("Either connection error or query execution error!");
        } finally
        {
            Database.Main().Close();
        }
    }
}
