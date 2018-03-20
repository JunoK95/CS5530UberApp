package cs5530;

import java.sql.*;

public class UU
{
    public UU()
    {}

    public String RegisterUser(String login, String name, String address, String phone)
    {
        String sql = "";

        return Database.Main().RunQuery(sql);
    }

    public String getCourse(String cname, String dname)
    {
        String sql = "select * from course where cname like '%" + cname + "%' and dname like '%" + dname + "%'";
        return Database.Main().RunQuery(sql);
    }
}
