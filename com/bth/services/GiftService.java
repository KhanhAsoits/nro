package com.bth.services;

import com.bth.models.player.Player;
import com.girlkun.database.GirlkunDB;
import com.bth.jdbc.daos.GiftDAO;
import com.bth.models.item.Item;
import com.girlkun.result.GirlkunResultSet;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;

import java.sql.Timestamp;
import java.util.ArrayList;


/**
 *
 * @author Heroes x BTH
 * 
 */
public class GiftService {

    private static GiftService i;
    
    private GiftService(){
        
    }
    public int gold;
    public int gem;
    public int dayexits;
    public Timestamp timecreate;
    public ArrayList<Item> listItem = new ArrayList<>();
    public static ArrayList<GiftService> gifts = new ArrayList<>();
    public static GiftService gI(){
        if(i == null){
            i = new GiftService();
        }
        return i;
    }

    public void giftCode(Player player, String code) throws Exception {
        try {
            GirlkunResultSet rs = GirlkunDB.executeQuery("SELECT * FROM `gift` WHERE name = ?", code);
            GirlkunResultSet check = GirlkunDB.executeQuery("SELECT * FROM `history_gift` WHERE `player_id` = ? AND `name_gift` = ?", player.id, code);
            if (check.first()) {
                Service.getInstance().sendThongBaoOK(player, "Bạn đã nhập gift code này");
            } else {
                if (rs.first()) {
                    JSONArray jar = (JSONArray) JSONValue.parse(rs.getString("item_id"));
                    int j;
                    int[] itemId = new int[jar.size()];
                    for (j = 0; j < jar.size(); j++) {
                        itemId[j] = Integer.parseInt(jar.get(j).toString());
                    }
                    jar = (JSONArray) JSONValue.parse(rs.getString("item_quantity"));
                    long[] itemQuantity = new long[jar.size()];
                    for (j = 0; j < jar.size(); j++) {
                        itemQuantity[j] = Long.parseLong(jar.get(j).toString());
                    }
                    int power = rs.getInt("power");
                    if (player.nPoint.power >= power) {
                        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                            if (itemId.length == itemQuantity.length) {
                                int i;
                                for (i = 0; i < itemId.length; i++) {
                                    if (itemId[i] == -3) {
                                        player.inventory.gold += (itemQuantity[i]);
                                        Service.getInstance().sendMoney(player);
                                        Service.getInstance().sendThongBao(player, "Bạn đã nhận được " + (itemQuantity[i]) + " vàng ");
                                    } else if (itemId[i] == -2) {
                                        player.inventory.gem += (itemQuantity[i]);
                                        Service.getInstance().sendMoney(player);
                                        Service.getInstance().sendThongBao(player, "Bạn đã nhận được " + (itemQuantity[i]) + " ngọc xanh ");
                                    } else if (itemId[i] == -1) {
                                        player.inventory.ruby += (itemQuantity[i]);
                                        Service.getInstance().sendMoney(player);
                                        Service.getInstance().sendThongBao(player, "Bạn đã nhận được " + (itemQuantity[i]) + " ngọc hồng ");
                                    } else {
                                        Item itemup = ItemService.gI().createNewItem((short) itemId[i], (int) itemQuantity[i]);
                                        InventoryServiceNew.gI().addItemBag(player, itemup);
                                        Service.getInstance().sendMoney(player);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        Service.getInstance().sendThongBao(player, "Bạn đã nhận được " + (itemQuantity[i]) + " " + itemup.template.name);
                                    }
                                }
                            }
                        } else {
                            Service.getInstance().sendThongBao(player, "Hành trang đầy.");
                        }
                        GiftDAO.insertHistoryGift((int) player.id, code);
                    }else{
                        Service.getInstance().sendThongBaoOK(player, "Yêu cầu sức mạnh lớn hơn "+power+" mới có thể nhập code này");
                }
                } else {
                    Service.getInstance().sendThongBaoOK(player, "Gift code không chính xác vui lòng thử lại");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
}
