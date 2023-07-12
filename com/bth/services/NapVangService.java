/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bth.services;

import com.bth.models.player.Player;
import com.girlkun.database.GirlkunDB;
import com.bth.jdbc.daos.PlayerDAO;
import com.bth.models.item.Item;

/**
 *
 * @author Heroes x BTH
 * 
 */
public class NapVangService {

    public static void ChonGiaTien(int chon, Player p) throws Exception{
        switch (chon){
            case 10:
            {//(20k)
                if (p.session.vnd < 10000) {
                    Service.getInstance().sendThongBao(p, "Số tiền tối thiểu: là 10,000 vnđ");
                    return;
                }
                if (InventoryServiceNew.gI().getCountEmptyBag(p) == 0) {
                    Service.getInstance().sendThongBao(p, "Hành trang không đủ chỗ trống");
                    return;
                }
                Item thoivang = ItemService.gI().createNewItem((short) 457, 35);
                if (thoivang != null) {
                    p.session.vnd -= 10000;
                    InventoryServiceNew.gI().addItemBag(p, thoivang);
                    InventoryServiceNew.gI().sendItemBags(p);
                    GirlkunDB.executeUpdate("update account set vnd = '" + p.session.vnd + "' where id = " + p.session.userId);
                    Service.getInstance().sendThongBao(p, "Bạn vừa rút thành công 35 thỏi vàng và tăng 10k điểm nạp tiền");
                    PlayerDAO.diemnaptien(p,10000);
                }
                break;
            }
            case 20: {//(20k)
                if (p.session.vnd < 20000) {
                    Service.getInstance().sendThongBao(p, "Số tiền tối thiểu: là 20,000 vnđ");
                    return;
                }
                if (InventoryServiceNew.gI().getCountEmptyBag(p) == 0) {
                    Service.getInstance().sendThongBao(p, "Hành trang không đủ chỗ trống");
                    return;
                }
                Item thoivang = ItemService.gI().createNewItem((short) 457, 70);
                if (thoivang != null) {
                    p.session.vnd -= 20000;
                    InventoryServiceNew.gI().addItemBag(p, thoivang);
                    InventoryServiceNew.gI().sendItemBags(p);
                    GirlkunDB.executeUpdate("update account set vnd = '" + p.session.vnd + "' where id = " + p.session.userId);
                    Service.getInstance().sendThongBao(p, "Bạn vừa rút thành công 70 thỏi vàng và tăng 20k điểm nạp tiền");
                    PlayerDAO.diemnaptien(p,20000);
                }
                break;
            }
            case 50: {
                if (p.session.vnd < 50000) {
                    Service.getInstance().sendThongBao(p, "Số tiền tối thiểu: là 50,000vnđ");
                    return;
                }
                if (InventoryServiceNew.gI().getCountEmptyBag(p) == 0) {
                    Service.getInstance().sendThongBao(p, "Hành trang không đủ chỗ trống");
                    return;
                }
                Item thoivang = ItemService.gI().createNewItem((short) 457, 200);
                if (thoivang != null) {
                    p.session.vnd -= 50000;
                    InventoryServiceNew.gI().addItemBag(p, thoivang);
                    InventoryServiceNew.gI().sendItemBags(p);
                    GirlkunDB.executeUpdate("update account set vnd = '" + p.session.vnd + "' where id = " + p.session.userId);
                    Service.getInstance().sendThongBao(p, "Bạn vừa rút thành công 200 thỏi vàng và tăng 50k điểm nạp tiền");
                    PlayerDAO.diemnaptien(p,50000);
                }
                break;
            }
            case 100: {
                if (p.session.vnd < 100000) {
                    Service.getInstance().sendThongBao(p, "Số tiền tối thiểu: là 100,000vnđ");
                    return;
                }
                if (InventoryServiceNew.gI().getCountEmptyBag(p) == 0) {
                    Service.getInstance().sendThongBao(p, "Hành trang không đủ chỗ trống");
                    return;
                }
                Item thoivang = ItemService.gI().createNewItem((short) 457, 400);
                if (thoivang != null) {
                    p.session.vnd -= 100000;
                    InventoryServiceNew.gI().addItemBag(p, thoivang);
                    InventoryServiceNew.gI().sendItemBags(p);
                    GirlkunDB.executeUpdate("update account set vnd = '" + p.session.vnd + "' where id = " + p.session.userId);
                    Service.getInstance().sendThongBao(p, "Bạn vừa rút thành công 400 thỏi vàng và tăng 100k điểm nạp tiền");
                    PlayerDAO.diemnaptien(p,100000);
                }
                break;
            }
            case 500: {
                if (p.session.vnd < 500000) {
                    Service.getInstance().sendThongBao(p, "Số tiền tối thiểu: là 500,000vnđ");
                    return;
                }
                if (InventoryServiceNew.gI().getCountEmptyBag(p) == 0) {
                    Service.getInstance().sendThongBao(p, "Hành trang không đủ chỗ trống");
                    return;
                }
                Item thoivang = ItemService.gI().createNewItem((short) 457, 2000);
                if (thoivang != null) {
                    p.session.vnd -= 500000;
                    InventoryServiceNew.gI().addItemBag(p, thoivang);
                    InventoryServiceNew.gI().sendItemBags(p);
                    GirlkunDB.executeUpdate("update account set vnd = '" + p.session.vnd + "' where id = " + p.session.userId);
                    Service.getInstance().sendThongBao(p, "Bạn vừa rút thành công 2000 thỏi vàng và tăng 500k điểm nạp tiền");
                    PlayerDAO.diemnaptien(p,500000);
                }
                break;
            }
        }
    }
}
