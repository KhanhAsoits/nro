package com.bth.models.boss;

import com.bth.models.boss.list_boss.BLACK.*;
import com.bth.models.boss.list_boss.Broly.Broly;
import com.bth.models.boss.list_boss.Cooler.Cooler;
import com.bth.models.boss.list_boss.FideBack.FideRobot;
import com.bth.models.boss.list_boss.FideBack.Kingcold;
import com.bth.models.boss.list_boss.Mabu;
import com.bth.models.boss.list_boss.Mabu12h.*;
import com.bth.models.boss.list_boss.NRD.*;
import com.bth.models.boss.list_boss.NgucTu.CoolerGold;
import com.bth.models.boss.list_boss.android.*;
import com.bth.models.boss.list_boss.cell.SieuBoHung;
import com.bth.models.boss.list_boss.cell.XenBoHung;
import com.bth.models.boss.list_boss.cell.Xencon;
import com.bth.models.boss.list_boss.fide.Fide;
import com.bth.models.boss.list_boss.ginyu.TDST;
import com.bth.models.boss.list_boss.nappa.Kuku;
import com.bth.models.boss.list_boss.nappa.MapDauDinh;
import com.bth.models.boss.list_boss.nappa.Rambo;
import com.bth.models.boss.list_boss.sontinthuytinh.sontinh;
import com.bth.models.boss.list_boss.sontinthuytinh.thuytinh;
import com.bth.models.player.Player;
import com.bth.server.Manager;
import com.bth.server.ServerManager;
import com.bth.services.ItemMapService;
import com.bth.services.MapService;
import com.girlkun.network.io.Message;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Heroes x BTH
 * 
 */
public class BossManager implements Runnable {

    private static BossManager I;
    public static final byte ratioReward = 10;

    public static BossManager gI() {
        if (BossManager.I == null) {
            BossManager.I = new BossManager();
        }
        return BossManager.I;
    }

    private BossManager() {
        this.bosses = new ArrayList<>();
    }

    private boolean loadedBoss;
    private final List<Boss> bosses;

    public void addBoss(Boss boss) {
        this.bosses.add(boss);
    }
    
    public List<Boss> getBosses(){
        return this.bosses;
    }
    
    public void removeBoss(Boss boss) {
        this.bosses.remove(boss);
    }
    public void loadBoss() {
        if (this.loadedBoss) {
            return;
        }
        try {

            this.createBoss(BossID.TDST);
            this.createBoss(BossID.BROLY);
            this.createBoss(BossID.PIC);
            this.createBoss(BossID.POC);
            this.createBoss(BossID.KING_KONG);
            this.createBoss(BossID.SONGOKU_TA_AC);
            if(Manager.SUKIEN == 2) {
                this.createBoss(BossID.SON_TINH);
                this.createBoss(BossID.THUY_TINH);
                this.createBoss(BossID.SON_TINH);
                this.createBoss(BossID.THUY_TINH);
                this.createBoss(BossID.SON_TINH);
                this.createBoss(BossID.THUY_TINH);
                this.createBoss(BossID.SON_TINH);
                this.createBoss(BossID.THUY_TINH);
                this.createBoss(BossID.SON_TINH);
                this.createBoss(BossID.THUY_TINH);
            }
            this.createBoss(BossID.CUMBER);
            this.createBoss(BossID.COOLER_GOLD);
            this.createBoss(BossID.XEN_BO_HUNG);
            this.createBoss(BossID.SIEU_BO_HUNG);
            this.createBoss(BossID.XEN_CON_1);
            this.createBoss(BossID.XEN_CON_1);
            this.createBoss(BossID.XEN_CON_1);
            this.createBoss(BossID.XEN_CON_1);

            this.createBoss(BossID.THIEN_SU_VADOS);
            this.createBoss(BossID.THIEN_SU_WHIS);
            this.createBoss(BossID.THIEN_SU_VADOS);
            this.createBoss(BossID.THIEN_SU_WHIS);
            this.createBoss(BossID.THIEN_SU_VADOS);
            this.createBoss(BossID.THIEN_SU_WHIS);
            this.createBoss(BossID.THIEN_SU_VADOS);
            this.createBoss(BossID.THIEN_SU_WHIS);
            
            
            this.createBoss(BossID.DORAEMON);
            this.createBoss(BossID.NOBITA);
            this.createBoss(BossID.XUKA);
            this.createBoss(BossID.CHAIEN);
            this.createBoss(BossID.XEKO);
            this.createBoss(BossID.BLACK);
            this.createBoss(BossID.ZAMASZIN);
            this.createBoss(BossID.BLACK2);
            this.createBoss(BossID.ZAMASMAX);
            this.createBoss(BossID.BLACK);
            this.createBoss(BossID.BLACK3);
            this.createBoss(BossID.KUKU);
            this.createBoss(BossID.MAP_DAU_DINH);
            this.createBoss(BossID.RAMBO);

            this.createBoss(BossID.FIDE);

            this.createBoss(BossID.DR_KORE);

            this.createBoss(BossID.ANDROID_14);
            this.createBoss(BossID.SUPER_ANDROID_17); 
            this.createBoss(BossID.MABU);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        this.loadedBoss = true;
        new Thread(BossManager.I, "Update boss").start();
    }

    public static Boss createBoss(int bossID) {
        try {
            switch (bossID) {
                case BossID.KUKU:
                    return new Kuku();
                case BossID.MAP_DAU_DINH:
                    return new MapDauDinh();
                case BossID.RAMBO:
                    return new Rambo();
                case BossID.DRABURA:
                    return new Drabura();
                case BossID.DRABURA_2:
                    return new Drabura2();
                case BossID.BUI_BUI:
                    return new BuiBui();
                case BossID.BUI_BUI_2:
                    return new BuiBui2();
                case BossID.YA_CON:
                    return new Yacon();
                case BossID.MABU_12H:
                    return new MabuBoss();
                case BossID.Rong_1Sao:
                    return new Rong1Sao();
                case BossID.Rong_2Sao:
                    return new Rong2Sao();
                case BossID.Rong_3Sao:
                    return new Rong3Sao();
                case BossID.Rong_4Sao:
                    return new Rong4Sao();
                case BossID.Rong_5Sao:
                    return new Rong5Sao();
                case BossID.Rong_6Sao:
                    return new Rong6Sao();
                case BossID.Rong_7Sao:
                    return new Rong7Sao();
                case BossID.FIDE:
                    return new Fide();
                case BossID.DR_KORE:
                    return new DrKore();
                case BossID.ANDROID_19:
                    return new Android19();
                case BossID.ANDROID_13:
                    return new Android13();
                case BossID.ANDROID_14:
                    return new Android14();
                case BossID.ANDROID_15:
                    return new Android15();
                    case BossID.SUPER_ANDROID_17:
                    return new SuperAndroid17();
                case BossID.PIC:
                    return new Pic();
                case BossID.POC:
                    return new Poc();
                case BossID.KING_KONG:
                    return new KingKong();
                case BossID.XEN_BO_HUNG:
                    return new XenBoHung();
                case BossID.SIEU_BO_HUNG:
                    return new SieuBoHung();
                case BossID.VUA_COLD:
                    return new Kingcold();
                case BossID.FIDE_ROBOT:
                    return new FideRobot();
                case BossID.COOLER:
                    return new Cooler();
                case BossID.ZAMASMAX:
                    return new ZamasMax();
                case BossID.ZAMASZIN:
                    return new ZamasKaio();
                case BossID.BLACK2:
                    return new SuperBlack2();
                case BossID.BLACK1:
                    return new BlackGokuTl();
                case BossID.BLACK:
                    return new Black();
                 case BossID.BLACK3:
                    return new BlackGokuBase();   
                case BossID.XEN_CON_1:
                    return new Xencon();
                case BossID.MABU:
                    return new Mabu();
                case BossID.TDST:
                    return new TDST();
                case BossID.COOLER_GOLD:
                    return new CoolerGold();
                case BossID.SON_TINH:
                    return new sontinh();
                case BossID.THUY_TINH:
                    return new thuytinh();
                case BossID.BROLY:
                    return new Broly();
                default:
                    return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public boolean existBossOnPlayer(Player player) {
        return player.zone.getBosses().size() > 0;
    }

    public void showListBoss(Player player) {
        if (!player.isAdmin()) {
            return;
        }
        Message msg;
        try {
            msg = new Message(-96);
            msg.writer().writeByte(0);
            msg.writer().writeUTF("Danh Sách Boss");
            msg.writer().writeByte((int) bosses.stream().filter(boss -> !MapService.gI().isMapMaBu(boss.data[0].getMapJoin()[0]) && !MapService.gI().isMapBlackBallWar(boss.data[0].getMapJoin()[0])).count());
            for (int i = 0; i < bosses.size(); i++) {
                Boss boss = this.bosses.get(i);
                if (MapService.gI().isMapMaBu(boss.data[0].getMapJoin()[0]) || MapService.gI().isMapBlackBallWar(boss.data[0].getMapJoin()[0])) {
                    continue;
                } 
                msg.writer().writeInt(i);
                msg.writer().writeInt(0);
                msg.writer().writeShort(boss.data[0].getOutfit()[0]);
                if (player.getSession().version > 214) {
                    msg.writer().writeShort(-1);
                }
                msg.writer().writeShort(boss.data[0].getOutfit()[1]);
                msg.writer().writeShort(boss.data[0].getOutfit()[2]);
                msg.writer().writeUTF(boss.data[0].getName());              
                if (boss.zone != null) {
                    msg.writer().writeUTF("On");
                    msg.writer().writeUTF(boss.zone.map.mapName + "(" + boss.zone.map.mapId + ") khu " + boss.zone.zoneId + "");
                } else {
                    msg.writer().writeUTF("Off");
                    msg.writer().writeUTF("Chưa có thông tin !");
                }
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void callBoss(Player player, int mapId) {
        try {
            if (BossManager.gI().existBossOnPlayer(player) ||
                    player.zone.items.stream().anyMatch(itemMap -> ItemMapService.gI().isBlackBall(itemMap.itemTemplate.id)) ||
                    player.zone.getPlayers().stream().anyMatch(p -> p.iDMark.isHoldBlackBall())) {
                return;
            }
            Boss k = null;
            switch (mapId) {
                case 85:
                    k = BossManager.gI().createBoss(BossID.Rong_1Sao);
                    break;
                case 86:
                    k = BossManager.gI().createBoss(BossID.Rong_2Sao);
                    break;
                case 87:
                    k = BossManager.gI().createBoss(BossID.Rong_3Sao);
                    break;
                case 88:
                    k = BossManager.gI().createBoss(BossID.Rong_4Sao);
                    break;
                case 89:
                    k = BossManager.gI().createBoss(BossID.Rong_5Sao);
                    break;
                case 90:
                    k = BossManager.gI().createBoss(BossID.Rong_6Sao);
                    break;
                case 91:
                    k = BossManager.gI().createBoss(BossID.Rong_7Sao);
                    break;
            }
            if (k != null) {
                k.currentLevel = 0;
                k.joinMapByZone(player);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Boss getBossById(int bossId) {
        return BossManager.gI().bosses.stream().filter(boss -> boss.id == bossId && !boss.isDie()).findFirst().orElse(null);
    }

    @Override
    public void run() {
        while (ServerManager.isRunning) {
            try {
                long st = System.currentTimeMillis();
                for (Boss boss : this.bosses) {
                    boss.update();
                }
                Thread.sleep(150 - (System.currentTimeMillis() - st));
            } catch (Exception ignored) {
            }

        }
    }
}
