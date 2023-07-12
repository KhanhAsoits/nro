package com.bth.models.boss.list_boss.khiga;

import com.bth.consts.ConstPlayer;
import com.bth.consts.ConstRatio;
import com.bth.models.boss.Boss;
import com.bth.models.boss.BossData;
import com.bth.models.boss.BossManager;
import com.bth.models.clan.Clan;
import com.bth.models.clan.ClanMember;
import com.bth.models.item.Item;
import com.bth.models.map.ItemMap;
import com.bth.models.map.doanhtrai.DoanhTrai;
import com.bth.models.map.khiga.KhiGa;
import com.bth.models.player.Player;
import com.bth.services.EffectSkillService;
import com.bth.services.PlayerService;
import com.bth.services.Service;
import com.bth.services.SkillService;
import com.bth.services.func.ChangeMapService;
import com.bth.utils.Util;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Heroes x BTH
 * 
 */
public abstract class BossKhiGa extends Boss {


    protected KhiGa khiGa;

    public BossKhiGa(byte id, BossData data, KhiGa khiga) {
        super(id, data);
        this.khiGa = khiga;
    }



    @Override
    public void joinMap() {
        this.zone = this.khiGa.getMapById(150);
        ChangeMapService.gI().changeMap(this, this.zone, 435, this.zone.map.yPhysicInTop(1065, 0));
    }

    @Override
    public void attack() {
        super.attack();
    }


    @Override
    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 1000)) {
                this.chat("Xí hụt");
                return 0;
            }
            damage = this.nPoint.subDameInjureWithDeff(damage);
            if (!piercing && effectSkill.isShielding) {
                if (damage > nPoint.hpMax) {
                    EffectSkillService.gI().breakShield(this);
                }
                damage = damage;
            }
            this.nPoint.subHP(damage);
            if (isDie()) {
                this.setDie(plAtt);
                die(plAtt);
            }
            return damage;
        } else {
            return 0;
        }
    }


    @Override
    public void leaveMap() {
        super.leaveMap();
        BossManager.gI().removeBoss(this);
    }
}
