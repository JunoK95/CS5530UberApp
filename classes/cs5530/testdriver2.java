package cs5530;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

public class testdriver2
{

    /**
     *
     */
    public static void displayMenu()
    {
        System.out.println("=============================");
        System.out.println("Welcome to UUber System");
        System.out.println("1. register");
        System.out.println("2. login");
        System.out.println("3. enter your own query");
        System.out.println("4. exit");
        System.out.println("=============================");
        System.out.println("please enter your choice:");
    }

    public static void main(String[] args)
    {
        String choice;
        try
        {
            Database.Main().Connect();

            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

            while (true)
            {
                displayMenu();
                int selection;
                while ((choice = in.readLine()) == null && choice.length() == 0) ;
                try
                {
                    selection = Integer.parseInt(choice);
                }
                catch (Exception e)
                {
                    continue;
                }

                try
                {
                    MainMenu(in, selection);
                }
                catch (EndAppException e)
                {
                    break;
                }
                catch (InvalidInputException e)
                {
                    System.out.println("Invalid Input");
                }
                catch (IOException e)
                {
                    System.out.println("Error reading input");
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.err.println("Either connection error or query execution error!");
        }
        finally
        {
            Database.Main().Close();
        }
    }

    private static void MainMenu(BufferedReader in, int selection) throws EndAppException, InvalidInputException, IOException
    {
        if (selection < 1 | selection > 4) throw new InvalidInputException();
        if (selection == 1)
        {
            HashMap<String, String> inputs = new HashMap<>();
            String[] requiredFields = new String[]{"login", "name", "address", "phone", "password"};
            for (String field : requiredFields)
            {
                System.out.println(String.format("Please enter a %s:", field));

                String input;
                while ((input = in.readLine()) == null && input.length() == 0) ;
                inputs.put(field, input);
            }
            System.out.println(UU.Register(inputs));
        } else if (selection == 2)
        {

        } else if (selection == 3)
        {
            System.out.println("please enter your query below:");
            String sql;
            while ((sql = in.readLine()) == null && sql.length() == 0)
                System.out.println(sql);

            System.out.println(Database.Main().RunQuery(sql));
        } else
        {
            System.out.println("Closing Application");
            Database.Main().Close();
            throw new EndAppException();
        }
    }
}
