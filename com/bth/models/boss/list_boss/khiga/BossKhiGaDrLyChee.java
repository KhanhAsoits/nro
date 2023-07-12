package com.bth.models.boss.list_boss.khiga;

import com.bth.consts.ConstPlayer;
import com.bth.consts.ConstRatio;
import com.bth.models.boss.BossData;
import com.bth.models.boss.BossID;
import com.bth.models.item.Item;
import com.bth.models.map.ItemMap;
import com.bth.models.map.khiga.KhiGa;
import com.bth.models.player.Player;
import com.bth.models.skill.Skill;
import com.bth.services.Service;
import com.bth.services.SkillService;
import com.bth.utils.Util;

/**
 *
 * @author Heroes x BTH
 * 
 */
public class BossKhiGaDrLyChee extends BossKhiGa {
    private static final int[][] FULL_DEMON = new int[][]{{Skill.DEMON, 1}, {Skill.DEMON, 2}, {Skill.DEMON, 3}, {Skill.DEMON, 4}, {Skill.DEMON, 5}, {Skill.DEMON, 6}, {Skill.DEMON, 7}};

    public BossKhiGaDrLyChee(KhiGa khiGa) {
        super((byte) BossID.DR_LYCHEE, new BossData(
                "Dr Lychee", //name
                ConstPlayer.TRAI_DAT, //gender
                new short[]{742, 743, 744, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
                (100000* khiGa.level) , //dame
                new int[]{50000000 *khiGa.level}, //hp
                new int[]{150}, //map join
                (int[][]) Util.addArray(FULL_DEMON), //skill
                new String[]{}, //text chat 1
                new String[]{"|-1|Nhóc con"}, //text chat 2
                new String[]{}, //text chat 3
                60
        ), khiGa);
    }

    @Override
    public void attack() {
        super.attack();
    }

    @Override
    public void reward(Player plKill) {
        if (Util.isTrue(100, 100)) {
            int ctlyche = 738;
            int maxP = playerTarger.clan.khiga.level-30;
            int HSD = Util.nextInt(1, 8);
            ItemMap it = new ItemMap(this.zone, ctlyche, 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 100), plKill.id);
            if(playerTarger.clan.khiga.level< 70) {
                it.options.add(new Item.ItemOption(50, Util.nextInt(40, 50)));
                it.options.add(new Item.ItemOption(77, Util.nextInt(60, 65)));
                it.options.add(new Item.ItemOption(103, Util.nextInt(60, 65)));
                it.options.add(new Item.ItemOption(5, Util.nextInt(60, 65)));
                it.options.add(new Item.ItemOption(30, 0)); //KHONG THE GIAO DICH
                it.options.add(new Item.ItemOption(93, HSD));
            }else{
                it.options.add(new Item.ItemOption(50, Util.nextInt(40, maxP)));
                it.options.add(new Item.ItemOption(77, Util.nextInt(60, maxP)));
                it.options.add(new Item.ItemOption(103, Util.nextInt(60, maxP)));
                it.options.add(new Item.ItemOption(5, Util.nextInt(60, 70)));
                it.options.add(new Item.ItemOption(30, 0)); //KHONG THE GIAO DICH
                it.options.add(new Item.ItemOption(93, HSD));
            }
            ItemMap ss = new ItemMap(this.zone, ctlyche, 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 150), plKill.id);
            if(playerTarger.clan.khiga.level< 70) {
                ss.options.add(new Item.ItemOption(50, Util.nextInt(40, 50)));
                ss.options.add(new Item.ItemOption(77, Util.nextInt(60, 65)));
                ss.options.add(new Item.ItemOption(103, Util.nextInt(60, 65)));
                ss.options.add(new Item.ItemOption(5, Util.nextInt(60, 65)));
                ss.options.add(new Item.ItemOption(30, 0)); //KHONG THE GIAO DICH
                ss.options.add(new Item.ItemOption(93, HSD));
            }else{
                ss.options.add(new Item.ItemOption(50, Util.nextInt(40, maxP)));
                ss.options.add(new Item.ItemOption(77, Util.nextInt(60, maxP)));
                ss.options.add(new Item.ItemOption(103, Util.nextInt(60, maxP)));
                ss.options.add(new Item.ItemOption(5, Util.nextInt(60, 70)));
                ss.options.add(new Item.ItemOption(30, 0)); //KHONG THE GIAO DICH
                ss.options.add(new Item.ItemOption(93, HSD));
            }
            ItemMap ssw = new ItemMap(this.zone, ctlyche, 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 50), plKill.id);
            if(playerTarger.clan.khiga.level< 70) {
                ssw.options.add(new Item.ItemOption(50, Util.nextInt(40, 50)));
                ssw.options.add(new Item.ItemOption(77, Util.nextInt(60, 65)));
                ssw.options.add(new Item.ItemOption(103, Util.nextInt(60, 65)));
                ssw.options.add(new Item.ItemOption(5, Util.nextInt(60, 65)));
                ssw.options.add(new Item.ItemOption(30, 0)); //KHONG THE GIAO DICH
                ssw.options.add(new Item.ItemOption(93, HSD));
                ssw.options.add(new Item.ItemOption(93, HSD));
            }else{
                ssw.options.add(new Item.ItemOption(50, Util.nextInt(40, maxP)));
                ssw.options.add(new Item.ItemOption(77, Util.nextInt(60, maxP)));
                ssw.options.add(new Item.ItemOption(103, Util.nextInt(60, maxP)));
                ssw.options.add(new Item.ItemOption(5, Util.nextInt(60, 70)));
                ssw.options.add(new Item.ItemOption(30, 0)); //KHONG THE GIAO DICH
                ssw.options.add(new Item.ItemOption(93, HSD));
            }
            Service.getInstance().dropItemMap(this.zone, it);
            Service.getInstance().dropItemMap(this.zone, ss);
            Service.getInstance().dropItemMap(this.zone, ssw);
        }
    }
}