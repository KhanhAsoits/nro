package com.bth.jdbc.daos;

import com.bth.utils.Logger;
import com.girlkun.database.GirlkunDB;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 *
 * @author Heroes x BTH
 * 
 */
public class GiftDAO {

    public static void insertHistoryGift(int playerId, String code) {
        try (Connection con = GirlkunDB.getConnection()){
            PreparedStatement ps = con.prepareStatement("insert into history_gift (player_id,name_gift) values (?,?)");
            ps.setInt(1, playerId);
            ps.setString(2, code);
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            Logger.logException(GiftDAO.class, e);
        }
    }
}
