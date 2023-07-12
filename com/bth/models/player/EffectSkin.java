package com.bth.models.player;

import com.bth.models.item.Item;
import com.bth.models.mob.Mob;
import com.bth.models.skill.Skill;
import com.bth.services.*;
import com.bth.utils.Logger;
import com.bth.utils.Util;
import com.bth.services.*;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Heroes x BTH
 * 
 */
public class EffectSkin {

    private static final String[] textOdo = new String[]{
        "Hôi quá", "Tránh ra đi thằng ở dơ", "Mùi gì kinh quá vậy?",
        "Kinh tởm quá", "Biến đi thằng ở dơ", "Kính ngài ở dơ"
    };
    
     private static final String[] textDraburaFrost = new String[]{ //Cải trang Drabura Frost
        "Ui lạnh quá..", "Đông cứng rồi", "Tránh xa ta ra", "Ngài Drabura Frost", "Bất lực"
    };
    private static final String[] textDrabura = new String[]{ //Cải trang Drabura
        "Năng quá", "Chết tiệt", "Hóa đá rồi", "Tránh xa ta ra"
    };
    private static final String[] textThoDaiCa = new String[]{ //Cải trang ThoDaiCa
        "Cái củ cà rốt gì vậy?", "Tên thỏ chết tiệt", "Có tránh xa ta ra không?"
    };

    private static final String[] texttop = new String[]{
            "Á Đù Thằng Top Sức Mạnh Kìa","Đẹp Trai Quá","Em yêu Anh","Anh Top SM ơi cho em chữ Ký",
            "AAAAa","Salangheyo","Top Sức Mạnh mãi đỉnh"
    };
    public long Timehoada;

    private Player player;

    public EffectSkin(Player player) {
        this.player = player;
        this.xHPKI = 1;
    }
    
    private long lastTimeHoaBang;  //Cải trang Dracula Frost
    private long lastTimeHoaDa; //Cải trang Dracula 
    private long lastTimeHoaCaRot; //Cải trang Thỏ Đại Ca
    public long lastTimeAttack;
    private long lastTimeOdo;
    private long lastTimetop;
    public long lastTimehoada;
    private long lastTimehoasocola;
    private long lastTimeXenHutHpKi;

    public long lastTimeAddTimeTrainArmor;
    public long lastTimeSubTimeTrainArmor;

    public boolean isVoHinh;

    public long lastTimeXHPKI;
    public int xHPKI;

    public long lastTimeUpdateCTHT;

    public void update() {
        updateVoHinh();
        if (this.player.zone != null && !MapService.gI().isMapOffline(this.player.zone.map.mapId)) {
            updateOdo();
            updateXenHutXungQuanh();
            updateHieuUngTop();
            updateMabuHoaSocola();
            updateDraburaFrost();
            updateDrabura();
            updateThoDaiCa();
        }
        if (player.isPl()) {
            updateTrainArmor();
        }
        if (this.player.zone != null && !MapService.gI().isMapOffline(this.player.zone.map.mapId)) {
            updateOdo();
            updateXenHutXungQuanh();
        }
        if (!this.player.isBoss && !this.player.isPet &&!player.isNewPet) {
            updateTrainArmor();
        }
        if (xHPKI != 1 && Util.canDoWithTime(lastTimeXHPKI, 1800000)) {
            xHPKI = 1;
            Service.getInstance().point(player);
        }
        updateCTHaiTac();
    }
    
    private void updateDraburaFrost() {
        try {
            if (this.player.nPoint.isDraburaFrost == true) {
                if (Util.canDoWithTime(lastTimeHoaBang, 10000)) {
                    List<Player> players = new ArrayList<>();
                    List<Player> playersMap = this.player.zone.getNotBosses();
                    for (Player pl : playersMap) {
                        if (!this.player.equals(pl) && !pl.isPet && !pl.isNewPet && !pl.isBoss && !pl.isDie()
                                && Util.getDistance(this.player, pl) <= 200) {
                            players.add(pl);
                        }
                    }
                    for (Player pl : players) {
                         EffectSkillService.gI().SetHoaBang(pl, System.currentTimeMillis(), 50000);
                         EffectSkillService.gI().setBlindDCTT(pl, System.currentTimeMillis(), 5000);
                    EffectSkillService.gI().sendEffectPlayer(player, pl, EffectSkillService.TURN_ON_EFFECT, (byte) EffectSkillService.BLIND_EFFECT);
                         Service.getInstance().Send_Caitrang(pl);
                    ItemTimeService.gI().sendItemTime(pl, 11085, 5000 / 1000);
                    Service.getInstance().chat(player, "Chíu chíu");
                    Service.getInstance().chat(pl, textDraburaFrost[Util.nextInt(0, textDraburaFrost.length - 1)]);
                    PlayerService.gI().sendInfoHpMpMoney(pl);
                    Service.getInstance().Send_Info_NV(pl);
                    }
                    this.lastTimeHoaBang = System.currentTimeMillis();
                }
            } else {
            }
        } catch (Exception e) {
            Logger.error("");
        }
    }
    
    private void updateDrabura() { // Dracula Hóa Đá
        try {
            if (this.player.nPoint.isDrabura == true) {
                if (Util.canDoWithTime(lastTimeHoaDa, 30000)) {
                    List<Player> players = new ArrayList<>();
                    List<Player> playersMap = this.player.zone.getNotBosses();
                    for (Player pl : playersMap) {
                        if (!this.player.equals(pl) && !pl.isPet && !pl.isNewPet && !pl.isBoss && !pl.isDie()
                                && Util.getDistance(this.player, pl) <= 200) {
                            players.add(pl);
                        }
                    }
                    for (Player pl : players) {
                         EffectSkillService.gI().SetHoaDa(pl, System.currentTimeMillis(), 50000);
                         EffectSkillService.gI().setBlindDCTT(pl, System.currentTimeMillis(), 5000);
                    EffectSkillService.gI().sendEffectPlayer(player, pl, EffectSkillService.TURN_ON_EFFECT, EffectSkillService.BLIND_EFFECT);
                         Service.getInstance().Send_Caitrang(pl);
                    ItemTimeService.gI().sendItemTime(pl, 4392, 5000 / 1000);
                    Service.getInstance().chat(player, "Chíu chíu");
                        Service.getInstance().chat(pl, textDrabura[Util.nextInt(0, textDrabura.length - 1)]);
                        PlayerService.gI().sendInfoHpMpMoney(pl);
                        Service.getInstance().Send_Info_NV(pl);
                    }
                    this.lastTimeHoaDa = System.currentTimeMillis();
                }
            } else {
            }
        } catch (Exception e) {
            Logger.error("");
        }
    }
    
    private void updateThoDaiCa() { //Thỏ Đại Ca
        try {
            if (this.player.nPoint.isThoDaiCa == true) {
                if (Util.canDoWithTime(lastTimeHoaCaRot, 30000)) {
                    List<Player> players = new ArrayList<>();
                    List<Player> playersMap = this.player.zone.getNotBosses();
                    for (Player pl : playersMap) {
                        if (!this.player.equals(pl) && !pl.isNewPet && !pl.isBoss && !pl.isDie()
                                && Util.getDistance(this.player, pl) <= 300) {
                            players.add(pl);
                        }
                    }
                    for (Player pl : players) {
                         EffectSkillService.gI().SetHoaCarot(pl, System.currentTimeMillis(), 30000);
                         EffectSkillService.gI().setBlindDCTT(pl, System.currentTimeMillis(), 30000);
                    //EffectSkillService.gI().sendEffectPlayer(player, pl, EffectSkillService.TURN_ON_EFFECT, EffectSkillService.BLIND_EFFECT);
                    
                         Service.getInstance().Send_Caitrang(pl);
                    ItemTimeService.gI().sendItemTime(pl, 4075, 30000 / 1000);
                    Service.getInstance().chat(player, "Ta sẽ biến các người thành carot");
                        Service.getInstance().chat(pl, textThoDaiCa[Util.nextInt(0, textThoDaiCa.length - 1)]);
                        PlayerService.gI().sendInfoHpMpMoney(pl);
                        Service.getInstance().Send_Info_NV(pl);
                    }
                    this.lastTimeHoaCaRot = System.currentTimeMillis();
                }
            } else {
            }
        } catch (Exception e) {
            Logger.error("");
        }
    }

    private void updateCTHaiTac() {
        if (this.player.setClothes.ctHaiTac != -1
                && this.player.zone != null
                && Util.canDoWithTime(lastTimeUpdateCTHT, 5000)) {
            int count = 0;
            int[] cts = new int[9];
            cts[this.player.setClothes.ctHaiTac - 618] = this.player.setClothes.ctHaiTac;
            List<Player> players = new ArrayList<>();
            players.add(player);
            try {
                for (Player pl : player.zone.getNotBosses()) {
                    if (!player.equals(pl) && pl.setClothes.ctHaiTac != -1 && Util.getDistance(player, pl) <= 300) {
                        cts[pl.setClothes.ctHaiTac - 618] = pl.setClothes.ctHaiTac;
                        players.add(pl);
                    }
                }
            } catch (Exception e) {
            }
            for (int i = 0; i < cts.length; i++) {
                if (cts[i] != 0) {
                    count++;
                }
            }
            for (Player pl : players) {
                Item ct = pl.inventory.itemsBody.get(5);
                if (ct.isNotNullItem() && ct.template.id >= 618 && ct.template.id <= 626) {
                    for (Item.ItemOption io : ct.itemOptions) {
                        if (io.optionTemplate.id == 147
                                || io.optionTemplate.id == 77
                                || io.optionTemplate.id == 103) {
                            io.param = count * 3;
                        }
                    }
                }
                if (!pl.isPet &&!pl.isNewPet&& Util.canDoWithTime(lastTimeUpdateCTHT, 5000)) {
                    InventoryServiceNew.gI().sendItemBody(pl);
                }
                pl.effectSkin.lastTimeUpdateCTHT = System.currentTimeMillis();
            }
        }
    }

    private void updateXenHutXungQuanh() {
        try {
            int param = this.player.nPoint.tlHutHpMpXQ;
            if (param > 0) {
                if (!this.player.isDie() && Util.canDoWithTime(lastTimeXenHutHpKi, 5000)) {
                    int hpHut = 0;
                    int mpHut = 0;
                    List<Player> players = new ArrayList<>();
                    List<Player> playersMap = this.player.zone.getNotBosses();
                    for (Player pl : playersMap) {
                        if (!this.player.equals(pl) && !pl.isBoss && !pl.isDie()
                                && Util.getDistance(this.player, pl) <= 200) {
                            players.add(pl);
                        }

                    }
                    for (Mob mob : this.player.zone.mobs) {
                        if (mob.point.gethp() > 1) {
                            if (Util.getDistance(this.player, mob) <= 200) {
                                int subHp = mob.point.getHpFull() * param / 100;
                                if (subHp >= mob.point.gethp()) {
                                    subHp = mob.point.gethp() - 1;
                                }
                                hpHut += subHp;
                                mob.injured(null, subHp, false);
                            }
                        }
                    }
                    for (Player pl : players) {
                        int subHp = pl.nPoint.hpMax * param / 100;
                        int subMp = pl.nPoint.mpMax * param / 100;
                        if (subHp >= pl.nPoint.hp) {
                            subHp = pl.nPoint.hp - 1;
                        }
                        if (subMp >= pl.nPoint.mp) {
                            subMp = pl.nPoint.mp - 1;
                        }
                        hpHut += subHp;
                        mpHut += subMp;
                        PlayerService.gI().sendInfoHpMpMoney(pl);
                        Service.getInstance().Send_Info_NV(pl);
                        pl.injured(null, subHp, true, false);
                    }
                    this.player.nPoint.addHp(hpHut);
                    this.player.nPoint.addMp(mpHut);
                    PlayerService.gI().sendInfoHpMpMoney(this.player);
                    Service.getInstance().Send_Info_NV(this.player);
                    this.lastTimeXenHutHpKi = System.currentTimeMillis();
                }
            }
        } catch (Exception e) {
            Logger.error("");
        }
    }


    private void updateHieuUngTop() {
        try {
            int param = this.player.nPoint.tlSdTop;
            if (param > 0) {
                if (Util.canDoWithTime(lastTimetop, 1000)) {
                    List<Player> players = new ArrayList<>();

                    List<Player> playersMap = this.player.zone.getNotBosses();
                    for (Player pl : playersMap) {
                        if (!this.player.equals(pl) && !pl.isBoss && !pl.isDie()
                                && Util.getDistance(this.player, pl) <= 200) {
                            players.add(pl);
                        }

                    }
                    for (Player pl : players) {
                        int subHp = pl.nPoint.hpMax * 10 / 100;
                        Service.getInstance().chat(pl, texttop[Util.nextInt(0, texttop.length - 1)]);
                        PlayerService.gI().sendInfoHpMpMoney(pl);
                        Service.getInstance().Send_Info_NV(pl);
                        pl.injured(null, subHp, true, false);
                    }
                    this.lastTimetop = System.currentTimeMillis();
                }
            }
        } catch (Exception e) {
            Logger.error("");
        }
    }

    private void updateMabuHoaSocola() {
        try {
            if (player.nPoint.hoasocola) {
                if (Util.canDoWithTime(lastTimehoasocola, 30000)) {
                    List<Player> players = new ArrayList<>();

                    List<Player> playersMap = this.player.zone.getNotBosses();
                    for (Player pl : playersMap) {
                        if (!this.player.equals(pl) && !pl.isBoss && !pl.isDie()
                                && Util.getDistance(this.player, pl) <= 200) {
                            players.add(pl);
                        }
                    }
                    for (Player pl : players) {
                        int timeSocola = 10000;
                        EffectSkillService.gI().setSocola(pl, System.currentTimeMillis(), timeSocola);
                        EffectSkillService.gI().sendEffectUseSkill(player, Skill.SOCOLA);
                        Service.getInstance().Send_Caitrang(pl);
                        ItemTimeService.gI().sendItemTime(pl, 3780, timeSocola / 1000);
                    }
                    this.lastTimehoasocola = System.currentTimeMillis();
                }
            }
        } catch (Exception e) {
            Logger.error("");
        }
    }

    private void updateOdo() {
        try {
            int param = this.player.nPoint.tlHpGiamODo;
            if (param > 0) {
                if (Util.canDoWithTime(lastTimeOdo, 10000)) {
                    List<Player> players = new ArrayList<>();

                    List<Player> playersMap = this.player.zone.getNotBosses();
                    for (Player pl : playersMap) {
                        if (!this.player.equals(pl) && !pl.isBoss && !pl.isDie()
                                && Util.getDistance(this.player, pl) <= 200) {
                            players.add(pl);
                        }

                    }
                    for (Player pl : players) {
                        int subHp = pl.nPoint.hpMax * param / 100;
                        if (subHp >= pl.nPoint.hp) {
                            subHp = pl.nPoint.hp - 1;
                        }
                        Service.getInstance().chat(pl, textOdo[Util.nextInt(0, textOdo.length - 1)]);
                        PlayerService.gI().sendInfoHpMpMoney(pl);
                        Service.getInstance().Send_Info_NV(pl);
                        pl.injured(null, subHp, true, false);
                    }
                    this.lastTimeOdo = System.currentTimeMillis();
                }
            }
        } catch (Exception e) {
            Logger.error("");
        }
    }

    //giáp tập luyện
    private void updateTrainArmor() {
        if (Util.canDoWithTime(lastTimeAddTimeTrainArmor, 60000) && !Util.canDoWithTime(lastTimeAttack, 30000)) {
            if (this.player.nPoint.wearingTrainArmor) {
                for (Item.ItemOption io : this.player.inventory.trainArmor.itemOptions) {
                    if (io.optionTemplate.id == 9) {
                        if (io.param < 1000) {
                            io.param++;
                            InventoryServiceNew.gI().sendItemBody(player);
                        }
                        break;
                    }
                }
            }
            this.lastTimeAddTimeTrainArmor = System.currentTimeMillis();
        }
        if (Util.canDoWithTime(lastTimeSubTimeTrainArmor, 60000)) {
            for (Item item : this.player.inventory.itemsBag) {
                if (item.isNotNullItem()) {
                    if (ItemService.gI().isTrainArmor(item)) {
                        for (Item.ItemOption io : item.itemOptions) {
                            if (io.optionTemplate.id == 9) {
                                if (io.param > 0) {
                                    io.param--;
                                }
                            }
                        }
                    }
                } else {
                    break;
                }
            }
            for (Item item : this.player.inventory.itemsBox) {
                if (item.isNotNullItem()) {
                    if (ItemService.gI().isTrainArmor(item)) {
                        for (Item.ItemOption io : item.itemOptions) {
                            if (io.optionTemplate.id == 9) {
                                if (io.param > 0) {
                                    io.param--;
                                }
                            }
                        }
                    }
                } else {
                    break;
                }
            }
            this.lastTimeSubTimeTrainArmor = System.currentTimeMillis();
            InventoryServiceNew.gI().sendItemBags(player);
            Service.getInstance().point(this.player);
        }
    }

    private void updateVoHinh() {
        if (this.player.nPoint.wearingVoHinh) {
            if (Util.canDoWithTime(lastTimeAttack, 5000)) {
                isVoHinh = true;
            } else {
                isVoHinh = false;
            }
        }
    }

    public void dispose() {
        this.player = null;
    }
}
