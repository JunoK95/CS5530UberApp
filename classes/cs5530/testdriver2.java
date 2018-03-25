package cs5530;

import cs5530.Models.UC;
import cs5530.Models.UD;
import cs5530.Models.UU;
import cs5530.Models.Reserve;
import cs5530.Models.Favorites;
import cs5530.Models.Feedback;
import cs5530.Models.Trust;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

public class testdriver2
{
    private static String User = "";
    private static String UserType = "";

    /**
     *
     */
    public static void displayMenu()
    {
        System.out.println("\n=============================");
        switch (UserType)
        {
            case "UU":
                System.out.println(String.format("UUber user logged in as %s", User));
                System.out.println("1. logout");
                break;
            case "UD":
                System.out.println(String.format("UUber driver logged in as %s", User));
                System.out.println("1. logout");
                System.out.println("2. Create or Update UC");
                break;
            default:
                System.out.println("Welcome to UUber System");
                System.out.println("1. register UU");
                System.out.println("2. register UD");
                System.out.println("3. login UU");
                System.out.println("4. login UD");
                System.out.println("5. enter your own query");
                System.out.println("6. reserve");
                System.out.println("7. assign favorites");
                System.out.println("8. give feedback");
                System.out.println("9. assign trust level");
                System.out.println("10. browse UC");
                break;
        }
        System.out.println("=============================\n");
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
                    switch (UserType)
                    {
                        case "UU":
                            UberUserMenu(in, selection);
                            break;
                        case "UD":
                            UberDriverMenu(in, selection);
                            break;
                        default:
                            MainMenu(in, selection);
                            break;
                    }
                }
                catch (EndAppException e)
                {
                    break;
                }
                catch (InvalidInputException e)
                {
                    System.out.println("Invalid Input");
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

    private static void UberUserMenu(BufferedReader in, int selection) throws InvalidInputException
    {
        if (selection < 1 | selection > 1) throw new InvalidInputException();
        if (selection == 1)
        {
            User = "";
            UserType = "";
            System.out.println("Logged out");
        }
    }

    private static void UberDriverMenu(BufferedReader in, int selection) throws InvalidInputException
    {
        if (selection < 1 | selection > 2) throw new InvalidInputException();
        try
        {
            if (selection == 1)
            {
                User = "";
                UserType = "";
                System.out.println("Logged out");
            } else if (selection == 2)
            {
                HashMap<String, String> inputs = new HashMap<>();
                GetFieldsFromInput(in, inputs, new String[]{"vin", "category", "make", "model", "year"});
                inputs.put("login", User);
                JSONObject json = UC.Create(inputs);
                System.out.println(json);
            }
        }
        catch (ModelFailed e)
        {
            HandleModelFailed(e);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private static void MainMenu(BufferedReader in, int selection) throws EndAppException, InvalidInputException
    {
        if (selection < 1 | selection > 20) throw new InvalidInputException();
        try
        {
            if (selection == 1)
            {
                HashMap<String, String> inputs = new HashMap<>();
                GetFieldsFromInput(in, inputs, new String[]{"login", "name", "address", "phone", "password"});
                JSONObject json = UU.Register(inputs);
                System.out.println(String.format("Welcome %s", json.get("User")));
                User = json.get("User").toString();
                UserType = "UU";
            } else if (selection == 2)
            {
                HashMap<String, String> inputs = new HashMap<>();
                GetFieldsFromInput(in, inputs, new String[]{"login", "name", "address", "phone", "password"});
                JSONObject json = UD.Register(inputs);
                System.out.println(String.format("Welcome %s", json.get("User")));
                User = json.get("User").toString();
                UserType = "UD";
            } else if (selection == 3)
            {
                HashMap<String, String> inputs = new HashMap<>();
                GetFieldsFromInput(in, inputs, new String[]{"login", "password"});
                JSONObject json = UU.Login(inputs);
                System.out.println(String.format("Welcome %s", json.get("User")));
                User = json.get("User").toString();
                UserType = "UU";
            } else if (selection == 4)
            {
                HashMap<String, String> inputs = new HashMap<>();
                GetFieldsFromInput(in, inputs, new String[]{"login", "password"});
                JSONObject json = UD.Login(inputs);
                System.out.println(String.format("Welcome %s", json.get("User")));
                User = json.get("User").toString();
                UserType = "UD";
            } else if (selection == 5)
            {
                System.out.println("please enter your query below:");
                String sql;
                while ((sql = in.readLine()) == null && sql.length() == 0)
                    System.out.println(sql);

                System.out.println(Database.Main().RunQuery(sql));
            } else if (selection == 6){
                HashMap<String, String> inputs = new HashMap<>();
                GetFieldsFromInput(in, inputs, new String[]{"login", "vin", "pid", "cost", "date"});
                JSONObject json = Reserve.ReserveUC(inputs);
                System.out.println(String.format("Reserve added"));
            } else if (selection == 7){
                HashMap<String, String> inputs = new HashMap<>();
                GetFieldsFromInput(in, inputs, new String[]{"login", "vin", "fvdate"});
                JSONObject json = Favorites.favoriteUC(inputs);
                System.out.println(String.format("Added to Favorites"));
            } else if (selection == 8){
                HashMap<String, String> inputs = new HashMap<>();
                GetFieldsFromInput(in, inputs, new String[]{"login", "fid", "vin", "text", "score", "fbdate"});
                JSONObject json = Feedback.giveFeedback(inputs);
                System.out.println(String.format("Added to Feedback"));
            } else if (selection == 9){
                HashMap<String, String> inputs = new HashMap<>();
                GetFieldsFromInput(in, inputs, new String[]{"login1", "login2", "isTrusted"});
                JSONObject json = Trust.trustUser(inputs);
                System.out.println(String.format("Trust added"));
            } else if (selection == 10){
                HashMap<String, String> inputs = new HashMap<>();
                GetFieldsFromInput(in, inputs, new String[]{"category"});
                JSONObject json = UC.Browse(inputs);
                System.out.println(String.format("Browsing"));
            }


            else
            {
                System.out.println("Closing Application");
                Database.Main().Close();
                throw new EndAppException();
            }
        }
        catch (ModelFailed e)
        {
            HandleModelFailed(e);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    //Reading from Console
    private static void GetFieldsFromInput(BufferedReader in, HashMap<String, String> inputs, String[] requiredFields) throws IOException
    {
        for (String field : requiredFields)
        {
            System.out.println(String.format("Please enter a %s:", field));

            String input;
            while ((input = in.readLine()) == null && input.length() == 0) ;
            inputs.put(field, input);
        }
    }

    private static void HandleModelFailed(ModelFailed e)
    {
        try
        {
            JSONObject result = new JSONObject();
            result.put("Success", false);
            result.put("Error", e.getMessage());
            System.out.println(result);
        }
        catch (JSONException e1)
        {
            e1.printStackTrace();
        }
    }
}
