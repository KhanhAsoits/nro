package com.bth.consts;

import com.girlkun.database.GirlkunDB;
import com.girlkun.result.GirlkunResultSet;

/**
 *
 * @author Heroes x BTH
 * 
 */
public class Test {

    public static void main(String[] args) throws Exception {
        GirlkunResultSet rs = GirlkunDB.executeQuery("select * from account where id = 4");
        if (rs.next()) {
            System.out.println(rs.getString("username"));
        }
    }
}
