package cs5530;


import javax.xml.crypto.Data;
import java.lang.*;
import java.sql.*;
import java.io.*;

public class testdriver2
{

    /**
     * @param args
     */
    public static void displayMenu()
    {
        System.out.println("        Welcome to UUber System     ");
        System.out.println("1. register a new user:");
        System.out.println("2. enter your own query:");
        System.out.println("3. exit:");
        System.out.println("pleasse enter your choice:");
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
                    System.out.println("please enter a cname:");
                    while ((cname = in.readLine()) == null && cname.length() == 0) ;
                    System.out.println("please enter a dname:");
                    while ((dname = in.readLine()) == null && dname.length() == 0) ;
                    UU course = new UU();
                    System.out.println(course.getCourse(cname, dname));
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
