package cs5530;

import cs5530.Models.*;
import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
                System.out.println("2. make reservation");
                System.out.println("3. record a favorite UC");
                System.out.println("4. record a ride");
                System.out.println("5. give feedback for an UC");
                System.out.println("6. give a usefulness rating");
                System.out.println("7. record trust for a UU");
                System.out.println("8. browse UC");
                System.out.println("9. get useful feedback for UC");
                System.out.println("10. UC suggestions");
                System.out.println("11. degree separation");
                System.out.println("12. statistics");
                System.out.println("13. user awards");
                break;
            case "UD":
                System.out.println(String.format("UUber driver logged in as %s", User));
                System.out.println("1. logout");
                System.out.println("2. Create or Update UC");
                System.out.println("3. Go Available");
                System.out.println("4. Go Unavailable");
                break;
            default:
                System.out.println("Welcome to UUber System");
                System.out.println("1. register UU");
                System.out.println("2. register UD");
                System.out.println("3. login UU");
                System.out.println("4. login UD");
                System.out.println("5. enter your own query");
                System.out.println("6. exit");
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


    /*
        1. logout
        2. make reservation
        3. record a favorite UC
        4. record a ride
        5. give feedback for an UC
        6. give a usefulness rating
        7. record trust for a UU
        8. browse UC
        9. get useful feedback for UC
        10. UC suggestions
        11. degree separation
        12. statistics
        13. user awards
     */
    private static void UberUserMenu(BufferedReader in, int selection) throws InvalidInputException, ModelFailed
    {
        if (selection < 1 | selection > 13) throw new InvalidInputException();
        try
        {
            HashMap<String, String> inputs = new HashMap<>();
            inputs.put("login", User);
            if (selection == 1)
            {
                User = "";
                UserType = "";
                System.out.println("Logged out");
            } else if (selection == 2)
            {
                GetFieldsFromInput(in, inputs, new String[]{"vin", "pid", "cost", "date"});
                if (ConfirmInputs(in, inputs))
                {
                    JSONObject json = Reserve.ReserveUC(inputs);
                    System.out.println(json);
                }
            } else if (selection == 3)
            {
                GetFieldsFromInput(in, inputs, new String[]{"vin"});
                JSONObject json = Favorites.FavoriteUC(inputs);
                System.out.println(json);
            } else if (selection == 4)
            {
                GetFieldsFromInput(in, inputs, new String[]{"vin", "cost", "begin", "end"});
                if (ConfirmInputs(in, inputs))
                {
                    JSONObject json = Ride.Record(inputs);
                    System.out.println(json);
                }
            } else if (selection == 5)
            {
                GetFieldsFromInput(in, inputs, new String[]{"vin", "text", "score"});
                JSONObject json = Feedback.GiveFeedback(inputs);
                System.out.println(json);

            } else if (selection == 6)
            {
                GetFieldsFromInput(in, inputs, new String[]{"fid", "rating [useless|useful|very useful]"});
                inputs.put("rating", inputs.get("rating [useless|useful|very useful]"));
                inputs.remove("rating [useless|useful|very useful]");
                JSONObject json = Rates.RateFeedback(inputs);
                System.out.println(json);
            } else if (selection == 7)
            {
                GetFieldsFromInput(in, inputs, new String[]{"userToTrust", "isTrusted [true/false]"});
                inputs.put("login1", User);
                inputs.put("login2", inputs.get("userToTrust"));
                inputs.put("isTrusted", inputs.get("isTrusted [true/false]"));
                inputs.remove("userToTrust");
                inputs.remove("login");
                inputs.remove("isTrusted [true/false]");
                JSONObject json = Trust.TrustUser(inputs);
                System.out.println(json);
            } else if (selection == 8)
            {
                GetFieldsFromInput(in, inputs, new String[]{"address","category","make","model","year"});
                inputs.remove("login");
                System.out.println(inputs);
                JSONAware json = UC.Browse(inputs);
                System.out.println(json.toJSONString());
            } else if (selection == 9)
            {
                GetFieldsFromInput(in, inputs, new String[]{"ud", "count"});
                inputs.remove("login");
                JSONAware json = Feedback.MostUsefulFeedback(inputs);
                System.out.println(json.toJSONString());
            } else if (selection == 10)
            {

            } else if (selection == 11)
            {

            } else if (selection == 12)
            {

            } else if (selection == 13)
            {
                GetFieldsFromInput(in, inputs, new String[]{"number"});
                inputs.remove("login");
                JSONAware json = Admin.getTrustedUsers(inputs);
                System.out.println(json.toJSONString());
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

    private static boolean ConfirmInputs(BufferedReader in, HashMap<String, String> inputs) throws IOException
    {
        Iterator it = inputs.entrySet().iterator();
        System.out.println("----------------------");
        while (it.hasNext())
        {
            Map.Entry pair = (Map.Entry) it.next();
            System.out.println(String.format("%s: %s", pair.getKey(), pair.getValue()));
        }
        System.out.println("----------------------");
        System.out.println("Confirm? [y/n]");
        String input;
        while ((input = in.readLine()) == null && input.length() == 0) ;
        input = input.toLowerCase();
        if (!input.contains("y")) System.out.println("Cancelled.");
        return input.contains("y");
    }

    private static void UberDriverMenu(BufferedReader in, int selection) throws InvalidInputException
    {
        if (selection < 1 | selection > 4) throw new InvalidInputException();
        try
        {
            HashMap<String, String> inputs = new HashMap<>();
            inputs.put("login", User);
            if (selection == 1)
            {
                User = "";
                UserType = "";
                System.out.println("Logged out");
            } else if (selection == 2)
            {
                GetFieldsFromInput(in, inputs, new String[]{"vin", "category", "make", "model", "year"});
                JSONObject json = UC.Create(inputs);
                System.out.println(json);
            } else if (selection == 3)
            {
                JSONObject json = Available.GoAvailble(inputs);
                System.out.println(json);
            } else if (selection == 4)
            {
                JSONObject json = Available.GoUnavailble(inputs);
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
            } else if (selection == 6)
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
        JSONObject result = new JSONObject();
        result.put("Success", false);
        result.put("Error", e.getMessage());
        System.out.println(result);
    }
}
