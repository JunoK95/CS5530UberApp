package cs5530;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

public class Database
{
    private static Database _main;

    public static Database Main()
    {
        if (_main == null) _main = new Database();
        return _main;
    }

    private static Connector _connector;

    private Database()
    {
    }

    public void Connect()
    {
        try
        {
            _connector = new Connector();
            System.out.println("Database connection established");
        }
        catch (Exception e)
        {
            System.out.println("Could not connect to the database");
        }
    }

    public String RunQuery(String sql)
    {
        StringBuilder output = new StringBuilder();
        ResultSet rs = null;
        System.out.println("executing " + sql);
        try
        {
            rs = _connector.stmt.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();
            int numCols = rsmd.getColumnCount();
            while (rs.next())
            {
                for (int i = 1; i <= numCols; i++)
                    System.out.print(rs.getString(i) + "  ");
                System.out.println("");
            }

            rs.close();
        }
        catch (Exception e)
        {
            System.out.println(e.toString());
        }
        finally
        {
            try
            {
                if (rs != null && !rs.isClosed())
                    rs.close();
            }
            catch (Exception e)
            {
                System.out.println("cannot close resultset");
            }
        }
        return output.toString();
    }

    public void Close()
    {
        try
        {
            _connector.stmt.close();
            if (_connector != null)
            {
                _connector.closeConnection();
                System.out.println("Database connection terminated");
            }
        }
        catch (Exception ignored)
        {

        }
    }
}
