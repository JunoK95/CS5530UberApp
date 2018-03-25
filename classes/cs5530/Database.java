package cs5530;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.LinkedList;
import java.util.List;

public class Database
{
    private static Database _main;

    public static Database Main()
    {
        if (_main == null) _main = new Database();
        return _main;
    }

    private static Connector2 _connector;

    private Database()
    {
    }

    public void Connect()
    {
        try
        {
            _connector = new Connector2();
        }
        catch (Exception e)
        {
            System.out.println("Could not connect to the database");
        }
    }

    public String RunQuery(String sql)
    {
        List<String> objects = new LinkedList<>();

        ResultSet rs = null;
        System.out.println("executing " + sql);
        try
        {

            rs = _connector.stmt.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();
            int numCols = rsmd.getColumnCount();
            while (rs.next())
            {
                StringBuilder obj = new StringBuilder("{");
                for (int i = 1; i <= numCols; i++)
                {
                    // SEE: https://docs.oracle.com/javase/7/docs/api/constant-values.html#java.sql.Types
                    switch (rsmd.getColumnType(i))
                    {
                        case 4:
                        case 5:
                        case 6:
                        case 7:
                        case 8:
                        case 16:
                            obj.append(String.format("\"%s\": %s", rsmd.getColumnName(i), rs.getString(i)));
                            break;
                        default:
                            obj.append(String.format("\"%s\": \"%s\"", rsmd.getColumnName(i), rs.getString(i)));
                            break;
                    }
                    if (i < numCols) obj.append(",");
                }
                obj.append("}");
                objects.add(obj.toString());
            }

            rs.close();
        }
        catch (Exception e)
        {
            System.out.println(e.toString());
            return e.getLocalizedMessage();
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
        if (objects.size() > 1)
        {
            return "[" + String.join(",", objects) + "]";
        }
        else if (objects.size() == 1)
        {
            return objects.get(0);
        }
        return null;
    }

    public void Close()
    {
        try
        {
            _connector.stmt.close();
            if (_connector != null && !_connector.conn.isClosed())
            {
                _connector.closeConnection();
                System.out.println("Database connection terminated");
            }
        }
        catch (Exception ignored)
        {

        }
    }

    public void RunUpdate(String sql) throws ModelFailed
    {
        System.out.println("executing " + sql);
        try
        {
            _connector.stmt.executeUpdate(sql);
        }
        catch (Exception e)
        {
            throw new ModelFailed(e.getMessage());
        }
    }
}
