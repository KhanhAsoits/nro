package com.bth.Manager;

import com.bth.models.player.Archivement;
import com.bth.models.player.Player;
import com.girlkun.database.GirlkunDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Heroes x BTH
 * 
 */
public class ArchivementManager implements IManager<Archivement> {
    private static final ArchivementManager instance = new ArchivementManager();

    public static ArchivementManager getInstance() {
        return instance;
    }

    public List<Archivement> list = new ArrayList<>();


    public void init() {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try (Connection con = GirlkunDB.getConnection();) {
            ps = con.prepareStatement("select * from archivement");
            rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String info1 = rs.getString("info1");
                String info2 = rs.getString("info2");
                int money = rs.getInt("money");
                add(new Archivement(id, info1, info2, money));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void find(Archivement archivement) {

    }

    @Override
    public void add(Archivement archivement) {
        list.add(archivement);
    }

    @Override
    public void remove(Archivement archivement) {

    }
}
