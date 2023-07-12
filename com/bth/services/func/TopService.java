package com.bth.services.func;

import com.bth.models.player.Player;
import com.girlkun.database.GirlkunDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Heroes x BTH
 * 
 */
public class TopService {

//    private static final String QUERY_TOP_POWER = "select player.id, player.name,"
//            + "player.power, player.head, player.gender, player.have_tennis_space_ship,"
//            + "player.clan_id_sv" + Manager.SERVER + ", player.data_inventory,"
//            + "player.data_location, player.data_point, player.data_magic_tree,"
//            + "player.items_body, player.items_bag, player.items_box, player.items_box_lucky_round,"
//            + "player.friends, player.enemies, player.data_intrinsic,player.data_item_time,"
//            + "player.data_task, player.data_mabu_egg, player.data_charm, player.skills,"
//            + "player.skills_shortcut, player.pet_info, player.pet_power, player.pet_point,"
//            + "player.pet_body, player.pet_skill, player.data_black_ball from player join "
//            + "account on player.account_id = account.id where account.is_admin = 0 order by "
//            + "player.power desc limit 20";

    private static final int TIME_TARGET_GET_TOP_POWER = 1800000;

    private static TopService i;

    private long lastTimeGetTopPower;
    private List<Player> listTopPower;

    private TopService() {
        this.listTopPower = new ArrayList<>();
    }

    public static TopService gI() {
        if (i == null) {
            i = new TopService();
        }
        return i;
    }

    public static String getTopPower() {
        StringBuffer sb = new StringBuffer("");

        String SELECT_TOP_POWER = "SELECT name, power FROM player ORDER BY power DESC LIMIT 10;";
        PreparedStatement ps;
        ResultSet rs;
        try {
            Connection conn = GirlkunDB.getConnection();
            ps = conn.prepareStatement(SELECT_TOP_POWER);
            conn.setAutoCommit(false);

            rs = ps.executeQuery();
            byte i = 1;
            while(rs.next()) {
                sb.append(i).append(".").append(rs.getString("name")).append(": ").append(rs.getString("power")).append("\b");
                i++;
            }
            conn.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    public static String getTopPvp() {
        StringBuffer sb = new StringBuffer("");
        String SELECT_TOP_POWER = "SELECT name, pointPvp FROM player ORDER BY pointPvp DESC LIMIT " + 15;
        PreparedStatement ps;
        ResultSet rs;
        try {
            Connection conn = GirlkunDB.getConnection();
            ps = conn.prepareStatement(SELECT_TOP_POWER);
            conn.setAutoCommit(false);

            rs = ps.executeQuery();
            byte i = 1;
            while(rs.next()) {
                sb.append(i).append(".").append(rs.getString("name")).append(": ").append(rs.getString("pointPvp")).append("\b");
                i++;
            }
            conn.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    public static String getTopNHS() {
        StringBuffer sb = new StringBuffer("");
        String SELECT_TOP_POWER = "SELECT name, NguHanhSonPoint FROM player ORDER BY NguHanhSonPoint DESC LIMIT " + 15;
        PreparedStatement ps;
        ResultSet rs;
        try {
            Connection conn = GirlkunDB.getConnection();
            ps = conn.prepareStatement(SELECT_TOP_POWER);
            conn.setAutoCommit(false);

            rs = ps.executeQuery();
            byte i = 1;
            while(rs.next()) {
                sb.append(i).append(".").append(rs.getString("name")).append(": ").append(rs.getString("NguHanhSonPoint")).append("\b");
                i++;
            }
            conn.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

}
