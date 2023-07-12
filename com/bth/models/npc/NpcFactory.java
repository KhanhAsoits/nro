package com.bth.models.npc;

import com.bth.consts.ConstMap;
import com.bth.consts.ConstNpc;
import com.bth.consts.ConstPlayer;
import com.bth.consts.ConstTask;
import com.bth.jdbc.daos.PlayerDAO;
import com.bth.kygui.ShopKyGuiService;
import com.bth.models.boss.Boss;
import com.bth.models.boss.BossData;
import com.bth.models.boss.BossID;
import com.bth.models.boss.BossManager;
import com.bth.models.boss.list_boss.NhanBan;
import com.bth.models.boss.list_boss.bahatmit.BossBahatmit;
import com.bth.models.clan.Clan;
import com.bth.models.clan.ClanMember;
import com.bth.models.item.Item;
import com.bth.models.map.Map;
import com.bth.models.map.MapMaBu.MapMaBu;
import com.bth.models.map.NgocRongNamec.NgocRongNamecService;
import com.bth.models.map.Zone;
import com.bth.models.map.bando.BanDoKhoBau;
import com.bth.models.map.bando.BanDoKhoBauService;
import com.bth.models.map.blackball.BlackBallWar;
import com.bth.models.map.daihoi.DaiHoiManager;
import com.bth.models.map.daihoilanthu23.DaiHoi23Service;
import com.bth.models.map.doanhtrai.DoanhTrai;
import com.bth.models.map.doanhtrai.DoanhTraiService;
import com.bth.models.map.khiga.KhiGa;
import com.bth.models.map.khiga.KhiGaService;
import com.bth.models.matches.PVPService;
import com.bth.models.npc.specialnpc.DuaHau;
import com.bth.models.player.*;
import com.bth.models.shop.ShopServiceNew;
import com.bth.models.skill.Skill;
import com.bth.server.Client;
import com.bth.server.Maintenance;
import com.bth.server.Manager;
import com.bth.services.*;
import com.bth.services.func.*;
import com.bth.utils.Logger;
import com.bth.utils.TimeUtil;
import com.bth.utils.Util;
import com.girlkun.network.io.Message;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

/**
 * @author Heroes x BTH
 */
public class NpcFactory {

    private static final int COST_HD = 50000000;

    public static long timebahatmit;

    private static boolean nhanVang = false;
    private static boolean nhanDeTu = false;

    //playerid - object
    public static final java.util.Map<Long, Object> PLAYERID_OBJECT = new HashMap<Long, Object>();

    private NpcFactory() {

    }

    private static Npc poTaGe(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 140) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Đa vũ trụ song song \b|7|Con muốn gọi con trong đa vũ trụ \b|1|Với giá 200tr vàng không?", "Gọi Boss\nNhân bản", "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 140) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0: {
                                    Boss oldBossClone = BossManager.gI().getBossById(Util.createIdBossClone((int) player.id));
                                    if (oldBossClone != null) {
                                        this.npcChat(player, "Nhà ngươi hãy tiêu diệt Boss lúc trước gọi ra đã, con boss đó đang ở khu " + oldBossClone.zone.zoneId);
                                    } else if (player.inventory.gold < 200_000_000) {
                                        this.npcChat(player, "Nhà ngươi không đủ 200 Triệu vàng ");
                                    } else {
                                        List<Skill> skillList = new ArrayList<>();
                                        for (byte i = 0; i < player.playerSkill.skills.size(); i++) {
                                            Skill skill = player.playerSkill.skills.get(i);
                                            if (skill.point > 0) {
                                                skillList.add(skill);
                                            }
                                        }
                                        int[][] skillTemp = new int[skillList.size()][3];
                                        for (byte i = 0; i < skillList.size(); i++) {
                                            Skill skill = skillList.get(i);
                                            if (skill.point > 0) {
                                                skillTemp[i][0] = skill.template.id;
                                                skillTemp[i][1] = skill.point;
                                                skillTemp[i][2] = skill.coolDown;
                                            }
                                        }
                                        BossData bossDataClone = new BossData(
                                                "Nhân Bản" + player.name,
                                                player.gender,
                                                new short[]{player.getHead(), player.getBody(), player.getLeg(), player.getFlagBag(), player.getAura(), player.getEffFront()},
                                                player.nPoint.dame *= 2,
                                                new int[]{player.nPoint.hpMax *= 2},
                                                new int[]{140},
                                                skillTemp,
                                                new String[]{"|-2|Boss nhân bản đã xuất hiện rồi"}, //text chat 1
                                                new String[]{"|-1|Ta sẽ chiếm lấy thân xác của ngươi hahaha!"}, //text chat 2
                                                new String[]{"|-1|Lần khác ta sẽ xử đẹp ngươi"}, //text chat 3
                                                60
                                        );

                                        try {
                                            new NhanBan(Util.createIdBossClone((int) player.id), bossDataClone, player.zone);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        //trừ vàng khi gọi boss
                                        player.inventory.gold -= 200_000_000;
                                        Service.getInstance().sendMoney(player);
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        };
    }


    private static Npc popo(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Có đâu ai ngờ, một ngày tình cờ như giấc mơ\n" +
                            "\n" +
                            "Biết đâu bất ngờ một ngày nào đời như ý thơ\n" +
                            "\n" +
                            "Ước mong anh về lại\n" +
                            "\n" +
                            "Tựa nắng chiếu lên ngày mai ai ngờ ngang trái\n" +
                            "\n" +
                            "Gió đưa anh về nơi sớm mai", "Khí Ga", "Đóng");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                if (player.clan != null) {
                                    if (player.clan.khiga != null) {
                                        this.createOtherMenu(player, ConstNpc.MENU_OPENED_KHIGA,
                                                "Bang hội của con đang đi khí ga cấp độ "
                                                        + player.clan.khiga.level + "\nCon có muốn đi theo không?",
                                                "Đồng ý", "Từ chối");
                                    } else {

                                        this.createOtherMenu(player, ConstNpc.MENU_OPEN_KHIGA, "Và vì sao lại thế anh\n" +
                                                        "\n" +
                                                        "Vì sao đánh mất nhau\n" +
                                                        "\n" +
                                                        "Vì sao sau nỗi đau cố hàn gắn như chẳng bền lâu\n" +
                                                        "\n" +
                                                        "Đọng lại trong nhau 1 từ ‘ nhớ ‘\n" +
                                                        "\n" +
                                                        "Để gặp lại mình bất ngờ\n" +
                                                        "\n" +
                                                        "Ngắm thu qua chiều đông mà lòng chẳng mơ",
                                                "Chọn\ncấp độ", "Từ chối");
                                    }
                                } else {
                                    this.npcChat(player, "Con phải có bang hội ta mới có thể cho con đi");
                                }
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPENED_KHIGA) {
                        switch (select) {
                            case 0:
                                if (player.isAdmin() || player.nPoint.power >= KhiGa.POWER_CAN_GO_TO_KHIGA) {
                                    ChangeMapService.gI().goToKhiGa(player);
                                } else {
                                    this.npcChat(player, "Sức mạnh của con phải ít nhất phải đạt "
                                            + Util.numberToMoney(KhiGa.POWER_CAN_GO_TO_KHIGA));
                                }
                                break;

                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPEN_KHIGA) {
                        switch (select) {
                            case 0:
                                if (player.isAdmin() || player.nPoint.power >= KhiGa.POWER_CAN_GO_TO_KHIGA) {
                                    Input.gI().createFormChooseLevelKhiGa(player);
                                } else {
                                    this.npcChat(player, "Sức mạnh của con phải ít nhất phải đạt "
                                            + Util.numberToMoney(KhiGa.POWER_CAN_GO_TO_KHIGA));
                                }
                                break;
                        }

                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_ACCEPT_GO_TO_KHIGA) {
                        switch (select) {
                            case 0:
                                KhiGaService.gI().openKhiGa(player, Byte.parseByte(String.valueOf(PLAYERID_OBJECT.get(player.id))));
                                break;
                        }

                    }
                }
            }
        };
    }

    private static Npc quyLaoKame(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                Item ruacon = InventoryServiceNew.gI().findItemBag(player, 874);
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        switch (Manager.SUKIEN) {
                            case 0: //Sự kiện Tết
                                if (ruacon != null && ruacon.quantity >= 1) {
                                    this.createOtherMenu(player, 1, "Chào con, con muốn ta giúp gì nào?\n Chúc con năm mới 2024 vui vẻ bên gia đình người thân!",
                                            "Giao\nRùa con", "Nói Chuyện", "Sự Kiện\nTết", "BXH\n Sự Kiện");
                                } else {
                                    this.createOtherMenu(player, 2, "Chào con, con muốn ta giúp gì nào?\n Chúc con năm mới 2024 vui vẻ bên gia đình người thân!",
                                            "Nói chuyện", "Sự Kiện\nTết", "BXH\n Sự Kiện");
                                }
                                break;
                            case 1: // Sự kiện 08/03
                                if (ruacon != null && ruacon.quantity >= 1) {
                                    this.createOtherMenu(player, 1, "Chào con, con muốn ta giúp gì nào?",
                                            "Giao\nRùa con", "Nói Chuyện", "Sự Kiện\n8 tháng 3", "BXH\n Sự Kiện");
                                } else {
                                    this.createOtherMenu(player, 2, "Chào con, ta rất vui khi gặp con\n Con muốn làm gì nào ?",
                                            "Nói chuyện", "Sự Kiện\n8 tháng 3", "BXH\n Sự Kiện");
                                }
                                break;
                            case 2: // Sự kiện giỗ tổ
                                if (ruacon != null && ruacon.quantity >= 1) {
                                    this.createOtherMenu(player, 1, "Chào con, con muốn ta giúp gì nào?",
                                            "Giao\nRùa con", "Nói Chuyện", "Sự Kiện\nGiỗ tổ\nHùng Vương", "BXH\n Sự Kiện");
                                } else {
                                    this.createOtherMenu(player, 2, "Chào con, ta rất vui khi gặp con\n Con muốn làm gì nào ?",
                                            "Nói chuyện", "Sự Kiện\nGiỗ tổ\nHùng Vương", "BXH\n Sự Kiện");
                                }
                                break;
                            case 99: // Không có sự Kiện
                                if (ruacon != null && ruacon.quantity >= 1) {
                                    this.createOtherMenu(player, 1, "Chào con, con muốn ta giúp gì nào?",
                                            "Giao\nRùa con", "Nói Chuyện");
                                } else {
                                    this.createOtherMenu(player, 2, "Chào con, ta rất vui khi gặp con",
                                            "Nói chuyện");
                                }
                                break;
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.getIndexMenu() == 1) {
                        switch (select) {
                            case 0:
                                this.createOtherMenu(player, 11,
                                        "Cảm ơn cậu đã cứu con rùa của ta\n Để cảm ơn ta sẽ tặng cậu món quà.",
                                        "Nhận quà", "Đóng");
                                break;
                            case 1:
                                this.createOtherMenu(player, 22,
                                        "Chào con, ta rất vui khi gặp con\n Con muốn làm gì nào ?",
                                        "Về khu\nvực bang", "Giải tán\nBang hội", "Kho Báu\ndưới biển");
                                break;
                            case 2:
                                switch (Manager.SUKIEN) {
                                    case 0:
                                        Service.getInstance().sendThongBao(player, "Hiiii");
                                        if (player.getSession().is_gift_box) {
                                            if (PlayerDAO.setIs_gift_box(player)) {
                                                player.getSession().is_gift_box = false;
                                                player.inventory.coupon += 5;
                                                Service.getInstance().sendThongBao(player, "Bạn vừa nhận được 5 điểm Coupon");
                                                Service.getInstance().sendMoney(player);
                                            }
                                        }
                                        break;
                                    case 1:
                                        NpcService.gI().createMenuConMeo(player, ConstNpc.MENUSUKIENTET, 13, "Chào con, con muốn ta giúp gì nào?",
                                                "Đổi xô cá xanh", "Đổi xô cá vàng", "Từ chối");
                                        break;
                                    case 2:
                                        Service.getInstance().sendThongBao(player, "Hiiii");
                                        if (player.getSession().is_gift_box) {
                                            if (PlayerDAO.setIs_gift_box(player)) {
                                                player.getSession().is_gift_box = false;
                                                player.inventory.coupon += 5;
                                                Service.getInstance().sendThongBao(player, "Bạn vừa nhận được 5 điểm Coupon");
                                                Service.getInstance().sendMoney(player);
                                            }
                                        }
                                        break;
                                }
                                break;
                            case 3:
                                this.npcChat(player, "Hôhô.. ta chưa xây dụng tính năng này!");
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == 11) {
                        switch (select) {
                            case 0:
                                try {
                                    Item RuaCon = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 874);
                                    if (RuaCon != null) {
                                        if (RuaCon.quantity >= 1 && InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                                            int randomItem = Util.nextInt(1); // Random giữa 0 và 1
                                            if (randomItem == 0) {
                                                Item VatPham = ItemService.gI().createNewItem((short) (865));
                                                VatPham.itemOptions.add(new Item.ItemOption(50, 20));
                                                VatPham.itemOptions.add(new Item.ItemOption(77, 10));
                                                VatPham.itemOptions.add(new Item.ItemOption(103, 10));
                                                VatPham.itemOptions.add(new Item.ItemOption(14, 5));
                                                VatPham.itemOptions.add(new Item.ItemOption(93, 7));
                                                InventoryServiceNew.gI().addItemBag(player, VatPham);
                                                createOtherMenu(player, ConstNpc.IGNORE_MENU, "Ta tặng cậu Kiếm Z", "Ok");
                                            } else {
                                                Item VatPham = ItemService.gI().createNewItem((short) 16);
                                                InventoryServiceNew.gI().addItemBag(player, VatPham);
                                                createOtherMenu(player, ConstNpc.IGNORE_MENU, "Ta tặng cậu Ngọc rồng 3 sao", "Ok");
                                            }
                                            InventoryServiceNew.gI().subQuantityItemsBag(player, RuaCon, 1);
                                            InventoryServiceNew.gI().sendItemBags(player);
                                        }
                                    }
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                                break;
                            default:
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == 22) {
                        switch (select) {
                            case 0:
                                if (player.getSession().player.nPoint.power >= 80000000000L) {
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 153, -1, 432);
                                } else {
                                    this.npcChat(player, "Bạn chưa đủ 80 tỷ sức mạnh để vào");
                                }
                                break;
                            case 1:
                                Clan clan = player.clan;
                                if (clan != null) {
                                    ClanMember cm = clan.getClanMember((int) player.id);
                                    if (cm != null) {
                                        if (clan.members.size() > 1) {
                                            Service.getInstance().sendThongBao(player, "Bang phải còn một người");
                                            break;
                                        }
                                        if (!clan.isLeader(player)) {
                                            Service.getInstance().sendThongBao(player, "Phải là bảng chủ");
                                            break;
                                        }
                                        NpcService.gI().createMenuConMeo(player, ConstNpc.CONFIRM_DISSOLUTION_CLAN, -1, "Con có chắc chắn muốn giải tán bang hội không? Ta cho con 2 lựa chọn...",
                                                "Đồng ý", "Từ chối!");
                                    }
                                    break;
                                }
                                Service.getInstance().sendThongBao(player, "bạn đã có bang hội đâu!!!");
                                break;
                            case 2:
                                if (player.clan != null) {
                                    if (player.clan.banDoKhoBau != null) {
                                        this.createOtherMenu(player, ConstNpc.MENU_OPENED_DBKB,
                                                "Bang hội của con đang đi tìm kho báu dưới biển cấp độ "
                                                        + player.clan.banDoKhoBau.level + "\nCon có muốn đi theo không?",
                                                "Đồng ý", "Từ chối");
                                    } else {
                                        this.createOtherMenu(player, ConstNpc.MENU_OPEN_DBKB,
                                                "Đây là bản đồ kho báu x4 tnsm\nCác con cứ yên tâm lên đường\n"
                                                        + "Ở đây có ta lo\nNhớ chọn cấp độ vừa sức mình nhé",
                                                "Chọn\ncấp độ", "Từ chối");
                                    }
                                } else {
                                    this.npcChat(player, "Con phải có bang hội ta mới có thể cho con đi");
                                }
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPENED_DBKB) {
                        switch (select) {
                            case 0:
                                if (player.isAdmin() || player.nPoint.power >= BanDoKhoBau.POWER_CAN_GO_TO_DBKB) {
                                    ChangeMapService.gI().goToDBKB(player);
                                } else {
                                    this.npcChat(player, "Sức mạnh của con phải ít nhất phải đạt "
                                            + Util.numberToMoney(BanDoKhoBau.POWER_CAN_GO_TO_DBKB));
                                }
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPEN_DBKB) {
                        switch (select) {
                            case 0:
                                if (player.isAdmin() || player.nPoint.power >= BanDoKhoBau.POWER_CAN_GO_TO_DBKB) {
                                    Input.gI().createFormChooseLevelBDKB(player);
                                } else {
                                    this.npcChat(player, "Sức mạnh của con phải ít nhất phải đạt "
                                            + Util.numberToMoney(BanDoKhoBau.POWER_CAN_GO_TO_DBKB));
                                }
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_ACCEPT_GO_TO_BDKB) {
                        switch (select) {
                            case 0:
                                BanDoKhoBauService.gI().openBanDoKhoBau(player, Byte.parseByte(String.valueOf(PLAYERID_OBJECT.get(player.id))));
                                break;
                        }
                    }
                }
                if (player.iDMark.getIndexMenu() == 2) {
                    switch (select) {
                        case 0:
                            this.createOtherMenu(player, 22,
                                    "Chào con, ta rất vui khi gặp con\n Con muốn làm gì nào ?",
                                    "Về khu\nvực bang", "Giải tán\nBang hội", "Kho Báu\ndưới biển");
                            break;
                        case 1:
                            switch (Manager.SUKIEN) {
                                case 0:
                                    Service.getInstance().sendThongBao(player, "Hiiii");
                                    if (player.getSession().is_gift_box) {
                                        if (PlayerDAO.setIs_gift_box(player)) {
                                            player.getSession().is_gift_box = false;
                                            player.inventory.coupon += 5;
                                            Service.getInstance().sendThongBao(player, "Bạn vừa nhận được 5 điểm Coupon");
                                            Service.getInstance().sendMoney(player);
                                        }
                                    }
                                    break;
                                case 1:
                                    NpcService.gI().createMenuConMeo(player, ConstNpc.MENUSUKIENTET, 13, "Chào con, con muốn ta giúp gì nào?", "Đổi xô cá xanh", "Đổi xô cá vàng", "Từ chối");
                                    break;
                                case 2:
                                    Service.getInstance().sendThongBao(player, "Hiiii");
                                    if (player.getSession().is_gift_box) {
                                        if (PlayerDAO.setIs_gift_box(player)) {
                                            player.getSession().is_gift_box = false;
                                            player.inventory.coupon += 5;
                                            Service.getInstance().sendThongBao(player, "Bạn vừa nhận được 5 điểm Coupon");
                                            Service.getInstance().sendMoney(player);
                                        }
                                    }
                                    break;
                            }
                            break;
                        case 2:
                            this.npcChat(player, "Hôhô.. ta chưa xây dụng tính năng này!");
                            break;
                    }
                } else if (player.iDMark.getIndexMenu() == 7) {
                    switch (select) {
                        case 0:
                            if (player.getSession().player.nPoint.power >= 80000000000L) {
                                ChangeMapService.gI().changeMapBySpaceShip(player, 153, -1, 432);
                            } else {
                                this.npcChat(player, "Bạn chưa đủ 80 tỷ sức mạnh để vào");
                            }
                            break;
                        case 1:
                            Clan clan = player.clan;
                            if (clan != null) {
                                ClanMember cm = clan.getClanMember((int) player.id);
                                if (cm != null) {
                                    if (clan.members.size() > 1) {
                                        Service.getInstance().sendThongBao(player, "Bang phải còn một người");
                                        break;
                                    }
                                    if (!clan.isLeader(player)) {
                                        Service.getInstance().sendThongBao(player, "Phải là bảng chủ");
                                        break;
                                    }
                                    NpcService.gI().createMenuConMeo(player, ConstNpc.CONFIRM_DISSOLUTION_CLAN, -1, "Con có chắc chắn muốn giải tán bang hội không? Ta cho con 2 lựa chọn...",
                                            "Đồng ý", "Từ chối!");
                                }
                                break;
                            }
                            Service.getInstance().sendThongBao(player, "bạn đã có bang hội đâu!!!");
                            break;
                        case 2:
                            if (player.clan != null) {
                                if (player.clan.banDoKhoBau != null) {
                                    this.createOtherMenu(player, ConstNpc.MENU_OPENED_DBKB,
                                            "Bang hội của con đang đi tìm kho báu dưới biển cấp độ "
                                                    + player.clan.banDoKhoBau.level + "\nCon có muốn đi theo không?",
                                            "Đồng ý", "Từ chối");
                                } else {
                                    this.createOtherMenu(player, ConstNpc.MENU_OPEN_DBKB,
                                            "Đây là bản đồ kho báu x4 tnsm\nCác con cứ yên tâm lên đường\n"
                                                    + "Ở đây có ta lo\nNhớ chọn cấp độ vừa sức mình nhé",
                                            "Chọn\ncấp độ", "Từ chối");
                                }
                            } else {
                                this.npcChat(player, "Con phải có bang hội ta mới có thể cho con đi");
                            }
                            break;
                    }
                } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPENED_DBKB) {
                    switch (select) {
                        case 0:
                            if (player.isAdmin() || player.nPoint.power >= BanDoKhoBau.POWER_CAN_GO_TO_DBKB) {
                                ChangeMapService.gI().goToDBKB(player);
                            } else {
                                this.npcChat(player, "Sức mạnh của con phải ít nhất phải đạt "
                                        + Util.numberToMoney(BanDoKhoBau.POWER_CAN_GO_TO_DBKB));
                            }
                            break;
                    }
                } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPEN_DBKB) {
                    switch (select) {
                        case 0:
                            if (player.isAdmin() || player.nPoint.power >= BanDoKhoBau.POWER_CAN_GO_TO_DBKB) {
                                Input.gI().createFormChooseLevelBDKB(player);
                            } else {
                                this.npcChat(player, "Sức mạnh của con phải ít nhất phải đạt "
                                        + Util.numberToMoney(BanDoKhoBau.POWER_CAN_GO_TO_DBKB));
                            }
                            break;
                    }
                } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_ACCEPT_GO_TO_BDKB) {
                    switch (select) {
                        case 0:
                            BanDoKhoBauService.gI().openBanDoKhoBau(player, Byte.parseByte(String.valueOf(PLAYERID_OBJECT.get(player.id))));
                            break;
                    }
                }
            }
        };
    }

    public static Npc truongLaoGuru(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        super.openBaseMenu(player);
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {

                }
            }
        };
    }

    public static Npc vuaVegeta(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        super.openBaseMenu(player);
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {

                }
            }
        };
    }

    ///////////////////////////////////////////NPC Ký Gửi///////////////////////////////////////////
    private static Npc kyGui(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    createOtherMenu(player, 0, "Cửa hàng chúng tôi chuyên mua bán hàng hiệu, hàng độc, cảm ơn bạn đã ghé thăm.", "Hướng\ndẫn\nthêm", "Mua bán\nKý gửi", "Từ chối");
                }
            }

            @Override
            public void confirmMenu(Player pl, int select) {
                if (canOpenNpc(pl)) {
                    switch (select) {
                        case 0:
                            Service.getInstance().sendPopUpMultiLine(pl, tempId, avartar, "Cửa hàng chuyên nhận ký gửi mua bán vật phẩm\bChỉ với 5 hồng ngọc\bGiá trị ký gửi 10k-200Tr vàng hoặc 2-2k ngọc\bMột người bán, vạn người mua, mại dô, mại dô");
                            break;
                        case 1:
                            ShopKyGuiService.gI().openShopKyGui(pl);
                            break;
                    }
                }
            }
        };
    }

    ///////////////////////////////////////////NPC Ông Gohan, Ông Moori, Ông Paragus///////////////////////////////////////////
    public static Npc ongGohan_ongMoori_ongParagus(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Con cố gắng theo %1 học thành tài, đừng lo lắng cho ta.\nTa có bán vật phẩm hỗ trợ cho con, yêu cầu sức mạnh con dưới 150 tr."
                                        .replaceAll("%1", player.gender == ConstPlayer.TRAI_DAT ? "Quy lão Kamê"
                                                : player.gender == ConstPlayer.NAMEC ? "Trưởng lão Guru" : "Vua Vegeta"),
                                "Cửa hàng\nhỗ trợ", "Giftcode");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                ShopServiceNew.gI().opendShop(player, "SHOPHOME", false);
                                break;
                            case 1:
                                Input.gI().createFormGiftCode(player);
                                break;
                        }
                    }
                }
            }
        };
    }

    ///////////////////////////////////////////NPC Tori-Bot///////////////////////////////////////////
    public static Npc toribot(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {


            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Tôi là Tori-Bot, bạn muốn tôi giúp đỡ gì cho bạn?",
                                "Cửa hàng\nHồng Ngọc", "Chức năng\nHeroes Z", "Learn Skill");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (!canOpenNpc(player)) {
                    return;
                }
                if (player.iDMark.isBaseMenu()) {
                    switch (select) {
                        case 0:
                            ShopServiceNew.gI().opendShop(player, "RUBY", true);
                            break;
                        case 1:
                            this.createOtherMenu(player, 5,
                                    "Hiện tại bạn đang có " + player.session.vnd + " Xu\n"
                                            + "Mở thẻ Heroes Z cần 20 Xu\n"
                                            + "Đổi đệ tử cần 10 Xu",
                                    "Mở thẻ\nHeroes Z", "Đổi\nĐệ tử", "Đổi\nThỏi vàng", "Đổi\nNgọc Xanh", "Đổi\nHồng Ngọc", "Hướng dẫn\nĐổi xu");
                            break;
                        case 2:
                            Item sach = InventoryServiceNew.gI().findItemBag(player, 1134);
                            if (sach != null && sach.quantity >= 1) {
                                SkillService.gI().learSkillSpecial(player, Skill.LIEN_HOAN_CHUONG);
                                InventoryServiceNew.gI().subQuantityItem(player.inventory.itemsBag, sach, 1);
                                InventoryServiceNew.gI().sendItemBags(player);
                            }
                            Service.getInstance().sendThongBao(player, "Oh hell no");
                            return;
                    }
                } else if (player.iDMark.getIndexMenu() == 5) {
                    switch (select) {
                        case 0:
                            if (player.getSession().actived) {
                                createOtherMenu(player, ConstNpc.IGNORE_MENU, "Bạn đã mở thẻ Heroes Z rồi, không thể mở nữa !", "Tạm biệt");
                                break;
                            }
                            if (player.session.vnd >= 20) {
                                player.getSession().actived = true;
                                if (PlayerDAO.diemnaptien(player, 20)) {
                                } else {
                                    createOtherMenu(player, ConstNpc.IGNORE_MENU, "Chúc mừng bạn đã mở thẻ Heroes Z thành công!", "Tạm biệt");
                                }
                            } else {
                                createOtherMenu(player, ConstNpc.IGNORE_MENU, "Không đủ Xu, bạn còn thiếu " + (20 - player.session.vnd) + " Xu nữa !", "Tạm biệt");
                            }
                            break;
                        case 1:
                            if (player.session.vnd < 10) {
                                createOtherMenu(player, ConstNpc.IGNORE_MENU, "Không đủ Xu, bạn còn thiếu " + (10 - player.session.vnd) + " Xu nữa !", "Tạm biệt");
                                break;
                            }
                            if (player.pet != null) {
                                createOtherMenu(player, ConstNpc.IGNORE_MENU, "Bạn có đệ tử rồi, không thể đổi nữa !", "Tạm biệt");
                                break;
                            }
                            if (!player.getSession().actived) {
                                createOtherMenu(player, ConstNpc.IGNORE_MENU, "Vui lòng mở thẻ Heroes Z để sử dụng chức năng này !", "Tạm biệt");
                                break;
                            }
                            PetService.gI().createNormalPet(player);
                            createOtherMenu(player, ConstNpc.IGNORE_MENU, "Chúc mừng bạn đã đổi đệ tử thành công!", "Tạm biệt");
                            if (PlayerDAO.diemnaptien(player, 10)) {
                                Service.getInstance().sendMoney(player);
                            }
                            break;
                        case 2:
                            Input.gI().createFormQDTV(player);
                            break;
                        case 3:
                            Input.gI().createFormQDNX(player);
                            break;
                        case 4:
                            Input.gI().createFormQDHN(player);
                            break;
                    }
                }
            }
        };
    }

    public static Npc mrpopo(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Có đâu ai ngờ, một ngày tình cờ như giấc mơ\n" +
                            "\n" +
                            "Biết đâu bất ngờ một ngày nào đời như ý thơ\n" +
                            "\n" +
                            "Ước mong anh về lại\n" +
                            "\n" +
                            "Tựa nắng chiếu lên ngày mai ai ngờ ngang trái\n" +
                            "\n" +
                            "Gió đưa anh về nơi sớm mai", "Khí Ga", "Đóng");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                if (player.clan != null) {
                                    if (player.clan.khiga != null) {
                                        this.createOtherMenu(player, ConstNpc.MENU_OPENED_KHIGA,
                                                "Bang hội của con đang đi khí ga cấp độ "
                                                        + player.clan.khiga.level + "\nCon có muốn đi theo không?",
                                                "Đồng ý", "Từ chối");
                                    } else {

                                        this.createOtherMenu(player, ConstNpc.MENU_OPEN_KHIGA, "Và vì sao lại thế anh\n" +
                                                        "\n" +
                                                        "Vì sao đánh mất nhau\n" +
                                                        "\n" +
                                                        "Vì sao sau nỗi đau cố hàn gắn như chẳng bền lâu\n" +
                                                        "\n" +
                                                        "Đọng lại trong nhau 1 từ ‘ nhớ ‘\n" +
                                                        "\n" +
                                                        "Để gặp lại mình bất ngờ\n" +
                                                        "\n" +
                                                        "Ngắm thu qua chiều đông mà lòng chẳng mơ",
                                                "Chọn\ncấp độ", "Từ chối");
                                    }
                                } else {
                                    this.npcChat(player, "Con phải có bang hội ta mới có thể cho con đi");
                                }
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPENED_KHIGA) {
                        switch (select) {
                            case 0:
                                if (player.isAdmin() || player.nPoint.power >= KhiGa.POWER_CAN_GO_TO_KHIGA) {
                                    ChangeMapService.gI().goToKhiGa(player);
                                } else {
                                    this.npcChat(player, "Sức mạnh của con phải ít nhất phải đạt "
                                            + Util.numberToMoney(KhiGa.POWER_CAN_GO_TO_KHIGA));
                                }
                                break;

                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPEN_KHIGA) {
                        switch (select) {
                            case 0:
                                if (player.isAdmin() || player.nPoint.power >= KhiGa.POWER_CAN_GO_TO_KHIGA) {
                                    Input.gI().createFormChooseLevelKhiGa(player);
                                } else {
                                    this.npcChat(player, "Sức mạnh của con phải ít nhất phải đạt "
                                            + Util.numberToMoney(KhiGa.POWER_CAN_GO_TO_KHIGA));
                                }
                                break;
                        }

                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_ACCEPT_GO_TO_KHIGA) {
                        switch (select) {
                            case 0: {
                                try {
                                    KhiGaService.gI().openKhiGa(player, Byte.parseByte(String.valueOf(PLAYERID_OBJECT.get(player.id))));
                                } catch (Exception ex) {
                                    java.util.logging.Logger.getLogger(NpcFactory.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                            break;

                        }

                    }
                }
            }
        };
    }

    ///////////////////////////////////////////NPC Chopper///////////////////////////////////////////
    public static Npc chopper(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "|1|Êi êi cậu có muốn cùng Chopper đi đến Đảo Kho Báu không,\nnhóm Hải Tặc Mũ Rơm đang chờ đợi cậu đến đó\n Có rất nhiều phần quà mùa hấp dẫn ở đó.\n Đi thôi nào....",
                                "Đi đến\nĐảo Kho Báu", "Chi tiết", "Từ chối");
                    }
                    if (this.mapId == 170) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "|1|Cậu muốn quay về Đảo kame à,\nChopper tôi sẽ đưa cậu đi",
                                "Đi thôi", "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 170, -1, 1560);
                                    break;
                            }
                        }
                    }
                    if (this.mapId == 170) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 5, -1, 312);
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    ///////////////////////////////////////////NPC Nami///////////////////////////////////////////
    public static Npc nami(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "|1|Oh hoan nghên bạn đến với của hàng của tôi\n bạn có muốn đổi vỏ ốc, cua đỏ\nlấy các món đồ mùa hè không?.",
                                "Cửa hàng\nNami");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                ShopServiceNew.gI().opendShop(player, "EVENT_MUA_HE", true);
                                break;
                        }
                    }
                }
            }
        };
    }

    ///////////////////////////////////////////NPC Franky///////////////////////////////////////////
    public static Npc franky(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 170) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "|1|Cậu muốn đi ra khơi khám phá?\n Nghe nói Luffy và mọi người đang tìm tên\ngấu tướng cướp ở ngoài đó.",
                                "Ra khơi\nthôi nào", "Từ chối");
                    }
                    if (this.mapId == 0) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "|1|Cậu muốn quay về Đảo kame à,\nđể Franky tôi đưa cậu đi",
                                "Đi thôi", "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 170) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapInYard(player, 171, -1, 48);
                                    break;
                            }
                        }
                    }
                    if (this.mapId == 0) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 5, -1, 312);
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    ///////////////////////////////////////////Tổ Sư Kaio///////////////////////////////////////////////
    public static Npc tosukaio(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 50) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Con thấy thanh kiếm đằng kia không, chỉ những ai được chọn mới có thể nhấc thanh kiếm đó lên.",
                                "Rèn\nKiếm");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 50) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.REN_KIEM_Z);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                            switch (player.combineNew.typeCombine) {
                                case CombineServiceNew.REN_KIEM_Z:
                                    if (select == 0) {
                                        CombineServiceNew.gI().startCombine(player);
                                    }
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    ///////////////////////////////////////////NPC Bumma///////////////////////////////////////////
    public static Npc bulmaQK(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 0) {
                        if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                    "Cậu cần trang bị gì cứ đến chỗ tôi nhé", "Cửa\nhàng", "Đi đến\nSau làng");
                        }
                    } else if (this.mapId == 164) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Cậu muốn quay lại làng Aru?", "Đồng ý", "Tạm Biệt");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 0) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0: // Shop
                                    if (player.gender == ConstPlayer.TRAI_DAT) {
                                        ShopServiceNew.gI().opendShop(player, "BUNMA", true);
                                    } else {
                                        this.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Xin lỗi cưng, chị chỉ bán đồ cho cư dân Trái Đất.", "Tạm biệt");
                                    }
                                    break;
                                case 1:
                                    this.createOtherMenu(player, 1,
                                            "Cưng muốn đến khu sau làng à,",
                                            "Sau làng\nAru",
                                            "Hướng dẫn");
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == 1) {
                            switch (select) {
                                case 0:
                                    if (player.gender == ConstPlayer.TRAI_DAT) {
                                        if (player.getSession().actived) {
                                            if (player.nPoint.power >= 500000 && player.nPoint.power < 5000000L) {
                                                ChangeMapService.gI().changeMapBySpaceShip(player, 164, -1, 144);
                                            } else {
                                                this.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Xin lỗi cưng, chị chỉ cho cư dân đã đạt sức mạnh từ 500k đến 5 triệu để đi đến Sau làng Aru.", "Tạm biệt");
                                            }
                                        } else {
                                            this.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Xin lỗi cưng, chị chỉ cho cư dân đã kích hoạt thành viên đi đến Sau làng Aru.", "Tạm biệt");
                                        }
                                    } else {
                                        this.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Xin lỗi cưng, chị chỉ cho cư dân Trái Đất đi đến Sau làng Aru.", "Tạm biệt");
                                    }
                                    break;
                                case 1:
                                    if (player.gender == ConstPlayer.TRAI_DAT) {
                                        ChangeMapService.gI().changeMapBySpaceShip(player, 165, -1, 144);
                                    } else {
                                        this.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Xin lỗi cưng, chị chỉ cho cư dân Trái Đất đi thôi nha.", "Tạm biệt");
                                    }
                                    break;
                                case 2:
                                    if (player.gender == ConstPlayer.TRAI_DAT) {
                                        NpcService.gI().createTutorial(player, this.avartar, ConstNpc.NPC_BUMMA);
                                    } else {
                                        this.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Xin lỗi cưng, chị chỉ cho cư dân Trái Đất đi thôi nha.", "Tạm biệt");
                                    }
                                    break;
                            }
                        }
                    } else if (this.mapId == 164) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    if (player.gender == ConstPlayer.TRAI_DAT) {
                                        ChangeMapService.gI().changeMapBySpaceShip(player, 0, -1, 192);
                                    } else {
                                        this.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Xin lỗi cưng, chị chỉ cho cư dân Trái Đất đi thôi nha.", "Đóng");
                                    }
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }


    public static Npc dende(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        if (player.idNRNM != -1) {
                            if (player.zone.map.mapId == 7) {
                                this.createOtherMenu(player, 1, "Ồ, ngọc rồng namếc, bạn thật là may mắn\nnếu tìm đủ 7 viên sẽ được Rồng Thiêng Namếc ban cho điều ước", "Hướng\ndẫn\nGọi Rồng", "Gọi rồng", "Từ chối");
                            }
                        } else {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                    "Anh cần trang bị gì cứ đến chỗ em nhé", "Cửa\nhàng");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0://Shop
                                if (player.gender == ConstPlayer.NAMEC) {
                                    ShopServiceNew.gI().opendShop(player, "DENDE", true);
                                } else {
                                    this.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Xin lỗi anh, em chỉ bán đồ cho dân tộc Namếc", "Đóng");
                                }
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == 1) {
                        if (player.zone.map.mapId == 7 && player.idNRNM != -1) {
                            if (player.idNRNM == 353) {
                                NgocRongNamecService.gI().tOpenNrNamec = System.currentTimeMillis() + 86400000;
                                NgocRongNamecService.gI().firstNrNamec = true;
                                NgocRongNamecService.gI().timeNrNamec = 0;
                                NgocRongNamecService.gI().doneDragonNamec();
                                NgocRongNamecService.gI().initNgocRongNamec((byte) 1);
                                NgocRongNamecService.gI().reInitNrNamec((long) 86399000);
                                SummonDragon.gI().summonNamec(player);
                            } else {
                                Service.getInstance().sendThongBao(player, "Anh phải có viên ngọc rồng Namếc 1 sao");
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc appule(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Ngươi cần trang bị gì cứ đến chỗ ta nhé", "Cửa\nhàng");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0://Shop
                                if (player.gender == ConstPlayer.XAYDA) {
                                    ShopServiceNew.gI().opendShop(player, "APPULE", true);
                                } else {
                                    this.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Về hành tinh hạ đẳng của ngươi mà mua đồ cùi nhé. Tại đây ta chỉ bán đồ cho người Xayda thôi", "Đóng");
                                }
                                break;
                        }
                    }
                }
            }
        };
    }

    public static Npc drDrief(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player pl) {
                if (canOpenNpc(pl)) {
                    if (this.mapId == 84) {
                        this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                "Tàu Vũ Trụ của ta có thể đưa cậu đến hành tinh khác chỉ trong 3 giây. Cậu muốn đi đâu?",
                                pl.gender == ConstPlayer.TRAI_DAT ? "Đến\nTrái Đất" : pl.gender == ConstPlayer.NAMEC ? "Đến\nNamếc" : "Đến\nXayda");
                    } else if (pl.getSession().player.nPoint.power >= 1500000000L) {
                        this.createOtherMenu(pl, 2, "Tàu Vũ Trụ của ta có thể đưa cầu thủ đến hành tinh khác chỉ trong 3 giây. Cầu muốn đi đâu?",
                                "Đến\nNamếc", "Đến\nXayda", "Siêu thị");
                    } else {
                        this.createOtherMenu(pl, 3,
                                "Tàu Vũ Trụ của ta có thể đưa cầu thủ đến hành tinh khác chỉ trong 3 giây. Cầu muốn đi đâu?",
                                "Đến\nNamếc", "Đến\nXayda");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 84) {
                        ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 24, -1, -1);
                    } else if (player.iDMark.getIndexMenu() == 2) {
                        switch (select) {
                            case 0:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 25, -1, -1);
                                break;
                            case 1:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 26, -1, -1);
                                break;
                            case 2:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 84, -1, -1);
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == 3) {
                        switch (select) {
                            case 0:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 25, -1, -1);
                                break;
                            case 1:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 26, -1, -1);
                                break;
                        }
                    }
                }
            }
        };
    }


    public static Npc cargo(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        if (player.playerTask.taskMain.id == 7) {
                            NpcService.gI().createTutorial(player, this.avartar, "Hãy lên con đường cầu vồng đưa bé nhà tôi\n"
                                    + "Chắc bây giờ nó sẽ không còn sợ hãi nữa");
                        } else if (player.getSession().player.nPoint.power >= 1500000000L) {
                            this.createOtherMenu(player, 2, "Tàu Vũ Trụ của ta có thể đưa cầu thủ đến hành tinh khác chỉ trong 3 giây. Cầu muốn đi đâu?",
                                    "Đến\nTrái đất", "Đến\nXayda", "Siêu thị");
                        } else {
                            this.createOtherMenu(player, 3,
                                    "Tàu Vũ Trụ của ta có thể đưa cầu thủ đến hành tinh khác chỉ trong 3 giây. Cầu muốn đi đâu?",
                                    "Đến\nTrái đất", "Đến\nXayda");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.getIndexMenu() == 2) {
                        switch (select) {
                            case 0:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 24, -1, -1);
                                break;
                            case 1:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 26, -1, -1);
                                break;
                            case 2:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 84, -1, -1);
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == 3) {
                        switch (select) {
                            case 0:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 24, -1, -1);
                                break;
                            case 1:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 26, -1, -1);
                                break;
                        }
                    }
                }
            }
        };
    }


    public static Npc cui(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            private final int COST_FIND_BOSS = 50000000;

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        if (player.playerTask.taskMain.id == 7) {
                            NpcService.gI().createTutorial(player, this.avartar, "Hãy lên đường cứu đứa bé nhà tôi\n"
                                    + "Chắc bây giờ nó đang sợ hãi lắm rồi");
                        } else {
                            if (this.mapId == 19) {

                                int taskId = TaskService.gI().getIdTask(player);
                                switch (taskId) {
                                    case ConstTask.TASK_19_0:
                                        this.createOtherMenu(player, ConstNpc.MENU_FIND_KUKU,
                                                "Đội quân của Fide đang ở Thung lũng Nappa, ta sẽ đưa ngươi đến đó",
                                                "Đến chỗ\nKuku\n(" + Util.numberToMoney(COST_FIND_BOSS) + " vàng)", "Đến Cold", "Đến\nNappa", "Từ chối");
                                        break;
                                    case ConstTask.TASK_19_1:
                                        this.createOtherMenu(player, ConstNpc.MENU_FIND_MAP_DAU_DINH,
                                                "Đội quân của Fide đang ở Thung lũng Nappa, ta sẽ đưa ngươi đến đó",
                                                "Đến chỗ\nMập đầu đinh\n(" + Util.numberToMoney(COST_FIND_BOSS) + " vàng)", "Đến Cold", "Đến\nNappa", "Từ chối");
                                        break;
                                    case ConstTask.TASK_19_2:
                                        this.createOtherMenu(player, ConstNpc.MENU_FIND_RAMBO,
                                                "Đội quân của Fide đang ở Thung lũng Nappa, ta sẽ đưa ngươi đến đó",
                                                "Đến chỗ\nRambo\n(" + Util.numberToMoney(COST_FIND_BOSS) + " vàng)", "Đến Cold", "Đến\nNappa", "Từ chối");
                                        break;
                                    default:
                                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                                "Đội quân của Fide đang ở Thung lũng Nappa, ta sẽ đưa ngươi đến đó",
                                                "Đến Cold", "Đến\nNappa", "Từ chối");

                                        break;
                                }
                            } else if (this.mapId == 68) {
                                this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                        "Ngươi muốn về Thành Phố Vegeta", "Đồng ý", "Từ chối");
                            } else if (player.getSession().player.nPoint.power >= 1500000000L) {
                                this.createOtherMenu(player, 2, "Tàu Vũ Trụ của ta có thể đưa cầu thủ đến hành tinh khác chỉ trong 3 giây. Cầu muốn đi đâu?",
                                        "Đến\nTrái Đất", "Đến\nNamếc", "Siêu thị");
                            } else {
                                this.createOtherMenu(player, 3,
                                        "Tàu Vũ Trụ của ta có thể đưa cầu thủ đến hành tinh khác chỉ trong 3 giây. Cầu muốn đi đâu?",
                                        "Đến\nTrái Đất", "Đến\nNamếc");
                            }
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 26) {
                        if (player.iDMark.getIndexMenu() == 2) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 24, -1, -1);
                                    break;
                                case 1:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 25, -1, -1);
                                    break;
                                case 2:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 84, -1, -1);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == 3) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 24, -1, -1);
                                    break;
                                case 1:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 25, -1, -1);
                                    break;
                            }
                        }
                    }
                }
                if (this.mapId == 19) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                if (player.getSession().player.nPoint.power >= 80000000000L) {
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 109, -1, 295);
                                } else {
                                    this.npcChat(player, "Bạn chưa đủ 80 tỷ sức mạnh để vào");
                                }
                                break;
                            case 1:
                                if (player.getSession().player.nPoint.power >= 2000000L) {
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 68, -1, -90);
                                } else {
                                    this.npcChat(player, "Bạn chưa đủ 2 triệu sức mạnh để đi đến đây.");
                                    break;
                                }
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_FIND_KUKU) {
                        switch (select) {
                            case 0:
                                Boss boss = BossManager.gI().getBossById(BossID.KUKU);
                                if (boss != null && !boss.isDie()) {
                                    if (player.inventory.gold >= COST_FIND_BOSS) {
                                        Zone z = MapService.gI().getMapCanJoin(player, boss.zone.map.mapId, boss.zone.zoneId);
                                        if (z != null && z.getNumOfPlayers() < z.maxPlayer) {
                                            player.inventory.gold -= COST_FIND_BOSS;
                                            ChangeMapService.gI().changeMap(player, boss.zone, boss.location.x, boss.location.y);
                                            Service.getInstance().sendMoney(player);
                                        } else {
                                            Service.getInstance().sendThongBao(player, "Khu vực đang full.");
                                        }
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Không đủ vàng, còn thiếu "
                                                + Util.numberToMoney(COST_FIND_BOSS - player.inventory.gold) + " vàng");
                                    }
                                    break;
                                }
                                Service.getInstance().sendThongBao(player, "Chết rồi ba...");
                                break;
                            case 1:
                                if (player.getSession().player.nPoint.power >= 80000000000L) {
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 109, -1, 295);
                                } else {
                                    this.npcChat(player, "Bạn chưa đủ 80 tỷ sức mạnh để vào");
                                }
                                break;
                            case 2:
                                if (player.getSession().player.nPoint.power >= 2000000L) {
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 68, -1, -90);
                                } else {
                                    this.npcChat(player, "Bạn chưa đủ 2 triệu sức mạnh để đi đến đây.");
                                    break;
                                }
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_FIND_MAP_DAU_DINH) {
                        switch (select) {
                            case 0:
                                Boss boss = BossManager.gI().getBossById(BossID.MAP_DAU_DINH);
                                if (boss != null && !boss.isDie()) {
                                    if (player.inventory.gold >= COST_FIND_BOSS) {
                                        Zone z = MapService.gI().getMapCanJoin(player, boss.zone.map.mapId, boss.zone.zoneId);
                                        if (z != null && z.getNumOfPlayers() < z.maxPlayer) {
                                            player.inventory.gold -= COST_FIND_BOSS;
                                            ChangeMapService.gI().changeMap(player, boss.zone, boss.location.x, boss.location.y);
                                            Service.getInstance().sendMoney(player);
                                        } else {
                                            Service.getInstance().sendThongBao(player, "Khu vực đang full.");
                                        }
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Không đủ vàng, còn thiếu "
                                                + Util.numberToMoney(COST_FIND_BOSS - player.inventory.gold) + " vàng");
                                    }
                                    break;
                                }
                                Service.getInstance().sendThongBao(player, "Chết rồi ba...");
                                break;
                            case 1:
                                if (player.getSession().player.nPoint.power >= 80000000000L) {
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 109, -1, 295);
                                } else {
                                    this.npcChat(player, "Bạn chưa đủ 80 tỷ sức mạnh để vào");
                                }
                                break;
                            case 2:
                                if (player.getSession().player.nPoint.power >= 2000000L) {
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 68, -1, -90);
                                } else {
                                    this.npcChat(player, "Bạn chưa đủ 2 triệu sức mạnh để đi đến đây.");
                                    break;
                                }
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_FIND_RAMBO) {
                        switch (select) {
                            case 0:
                                Boss boss = BossManager.gI().getBossById(BossID.RAMBO);
                                if (boss != null && !boss.isDie()) {
                                    if (player.inventory.gold >= COST_FIND_BOSS) {
                                        Zone z = MapService.gI().getMapCanJoin(player, boss.zone.map.mapId, boss.zone.zoneId);
                                        if (z != null && z.getNumOfPlayers() < z.maxPlayer) {
                                            player.inventory.gold -= COST_FIND_BOSS;
                                            ChangeMapService.gI().changeMap(player, boss.zone, boss.location.x, boss.location.y);
                                            Service.getInstance().sendMoney(player);
                                        } else {
                                            Service.getInstance().sendThongBao(player, "Khu vực đang full.");
                                        }
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Không đủ vàng, còn thiếu "
                                                + Util.numberToMoney(COST_FIND_BOSS - player.inventory.gold) + " vàng");
                                    }
                                    break;
                                }
                                Service.getInstance().sendThongBao(player, "Chết rồi ba...");
                                break;
                            case 1:
                                if (player.getSession().player.nPoint.power >= 80000000000L) {
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 109, -1, 295);
                                } else {
                                    this.npcChat(player, "Bạn chưa đủ 80 tỷ sức mạnh để vào");
                                }
                                break;
                            case 2:
                                if (player.getSession().player.nPoint.power >= 2000000L) {
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 68, -1, -90);
                                } else {
                                    this.npcChat(player, "Bạn chưa đủ 2 triệu sức mạnh để đi đến đây.");
                                    break;
                                }
                        }
                    }
                }
                if (this.mapId == 68) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 19, -1, 1100);
                                break;
                        }
                    }
                }
            }
        };
    }

    public static Npc santa(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Xin chào, ta có một số vật phẩm đặt biệt cậu có muốn xem không?",
                            "Cửa hàng", "Cửa hàng\nNew", "Tiệm\nHớt tóc");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5 || this.mapId == 13 || this.mapId == 20) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ShopServiceNew.gI().opendShop(player, "SANTA", false);
                                    break;
//                                case 1:
//                                     this.npcChat(player, "Ôi tôi xin lỗi, cửa hàng của tôi chưa kịp bày bán sản phẩm.");
//                                    break;
                                case 1:
                                    ShopServiceNew.gI().opendShop(player, "SANTA_EVENT", true);
                                    break;
                                case 2:
                                    ShopServiceNew.gI().opendShop(player, "SANTA_HEAD", true);
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc uron(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player pl) {
                if (canOpenNpc(pl)) {
                    ShopServiceNew.gI().opendShop(pl, "URON", false);
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {

                }
            }
        };
    }

    public static Npc ghidanh(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            String[] menuselect = new String[]{};

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.map.mapId == 52) {
                        if (DaiHoiManager.gI().openDHVT && (System.currentTimeMillis() <= DaiHoiManager.gI().tOpenDHVT)) {
                            String nameDH = DaiHoiManager.gI().nameRoundDHVT();
                            this.createOtherMenu(player, ConstNpc.MENU_DHVT, "Hiện đang có giải đấu " + nameDH + " bạn có muốn đăng ký không?", new String[]{"Giải\n" + nameDH + "\n(" + DaiHoiManager.gI().costRoundDHVT() + ")", "Từ chối", "Đại Hội\nVõ Thuật\nLần thứ\n23"});
                        } else {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU, "Đã hết hạn đăng ký thi đấu, xin vui lòng chờ đến giải sau", new String[]{"Thông tin\bChi tiết", "OK", "Đại Hội\nVõ Thuật\nLần thứ\n23"});
                        }
                    } else if (this.mapId == 129) {
                        int goldchallenge = player.golddaihoi;
                        if (player.levelruonggo == 0) {
                            menuselect = new String[]{"Thi đấu\n" + Util.numberToMoney(goldchallenge) + " vàng", "Về\nĐại Hội\nVõ Thuật"};
                        } else {
                            menuselect = new String[]{"Thi đấu\n" + Util.numberToMoney(goldchallenge) + " vàng", "Nhận thưởng\nRương cấp\n" + player.levelruonggo, "Về\nĐại Hội\nVõ Thuật"};
                        }
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Đại hội võ thuật lần thứ 23\nDiễn ra bất kể ngày đêm,ngày nghỉ ngày lễ\nPhần thưởng vô cùng quý giá\nNhanh chóng tham gia nào", menuselect, "Từ chối");
                    } else {
                        super.openBaseMenu(player);
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.map.mapId == 52) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    Service.getInstance().sendThongBaoFromAdmin(player, "Lịch thi đấu trong ngày\bGiải Nhi đồng: 8,13,18h\bGiải Siêu cấp 1: 9,14,19h\bGiải Siêu cấp 2: 10,15,20h\bGiải Siêu cấp 3: 11,16,21h\bGiải Ngoại hạng: 12,17,22,23h\nGiải thưởng khi thắng mỗi vòng\bGiải Nhi đồng: 2 ngọc\bGiải Siêu cấp 1: 4 ngọc\bGiải Siêu cấp 2: 6 ngọc\bGiải Siêu cấp 3: 8 ngọc\bGiải Ngoại hạng: 10.000 vàng\bVô địch: 5 viên đá nâng cấp\nVui lòng đến đúng giờ để đăng ký thi đấu");
                                    break;
                                case 1:
                                    Service.getInstance().sendThongBaoFromAdmin(player, "Nhớ Đến Đúng Giờ nhé");
                                    break;
                                case 2:
                                    ChangeMapService.gI().changeMapNonSpaceship(player, 129, player.location.x, 360);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_DHVT) {
                            switch (select) {
                                case 0:
//                                    if (DaiHoiService.gI().canRegisDHVT(player.nPoint.power)) {
                                    if (DaiHoiManager.gI().lstIDPlayers.size() < 256) {
                                        if (DaiHoiManager.gI().typeDHVT == (byte) 5 && player.inventory.gold >= 10000) {
                                            if (DaiHoiManager.gI().isAssignDHVT(player.id)) {
                                                Service.getInstance().sendThongBao(player, "Bạn đã đăng ký tham gia đại hội võ thuật rồi");
                                            } else {
                                                player.inventory.gold -= 10000;
                                                Service.getInstance().sendMoney(player);
                                                Service.getInstance().sendThongBao(player, "Bạn đã đăng ký thành công, nhớ có mặt tại đây trước giờ thi đấu");
                                                DaiHoiManager.gI().lstIDPlayers.add(player.id);
                                            }
                                        } else if (DaiHoiManager.gI().typeDHVT > (byte) 0 && DaiHoiManager.gI().typeDHVT < (byte) 5 && player.inventory.gem >= (int) (2 * DaiHoiManager.gI().typeDHVT)) {
                                            if (DaiHoiManager.gI().isAssignDHVT(player.id)) {
                                                Service.getInstance().sendThongBao(player, "Bạn đã đăng ký tham gia đại hội võ thuật rồi");
                                            } else {
                                                player.inventory.gem -= (int) (2 * DaiHoiManager.gI().typeDHVT);
                                                Service.getInstance().sendMoney(player);
                                                Service.getInstance().sendThongBao(player, "Bạn đã đăng ký thành công, nhớ có mặt tại đây trước giờ thi đấu");
                                                DaiHoiManager.gI().lstIDPlayers.add(player.id);
                                            }
                                        } else {
                                            Service.getInstance().sendThongBao(player, "Không đủ vàng ngọc để đăng ký thi đấu");
                                        }
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Hiện tại đã đạt tới số lượng người đăng ký tối đa, xin hãy chờ đến giải sau");
                                    }

//                                    } else {
//                                        Service.getInstance().sendThongBao(player, "Bạn không đủ điều kiện tham gia giải này, hãy quay lại vào giải phù hợp");
//                                    }
                                    break;
                                case 1:
                                    break;
                                case 2:
                                    ChangeMapService.gI().changeMapNonSpaceship(player, 129, player.location.x, 360);
                                    break;
                            }
                        }
                    } else if (this.mapId == 129) {
                        int golddaihoi = player.golddaihoi;
                        if (player.levelruonggo == 0) {
                            switch (select) {
                                case 0:
                                    if (InventoryServiceNew.gI().finditemRuongGo(player)) {
                                        if (player.inventory.gold >= golddaihoi) {
                                            DaiHoi23Service.gI().startChallenge(player);
                                            player.inventory.gold -= (golddaihoi);
                                            PlayerService.gI().sendInfoHpMpMoney(player);
                                            player.golddaihoi += 2000000;
                                        } else {
                                            Service.getInstance().sendThongBao(player, "Không đủ vàng, còn thiếu " + Util.numberToMoney(golddaihoi - player.inventory.gold) + " vàng");
                                        }
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Hãy mở rương báu vật trước");
                                    }
                                    break;
                                case 1:
                                    ChangeMapService.gI().changeMapNonSpaceship(player, 52, player.location.x, 336);
                                    break;
                            }
                        } else {
                            switch (select) {
                                case 0:
                                    if (InventoryServiceNew.gI().finditemRuongGo(player)) {
                                        if (player.inventory.gold >= golddaihoi) {
                                            DaiHoi23Service.gI().startChallenge(player);
                                            player.inventory.gold -= (golddaihoi);
                                            PlayerService.gI().sendInfoHpMpMoney(player);
                                            player.golddaihoi += 2000000;
                                        } else {
                                            Service.getInstance().sendThongBao(player, "Không đủ vàng, còn thiếu " + Util.numberToMoney(golddaihoi - player.inventory.gold) + " vàng");
                                        }
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Hãy mở rương báu vật trước");
                                    }
                                    break;
                                case 1:
                                    if (!player.checkruonggo) {
                                        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                                            Item it = ItemService.gI().createNewItem((short) 570);
                                            it.itemOptions.add(new Item.ItemOption(72, player.levelruonggo));
                                            it.itemOptions.add(new Item.ItemOption(30, 0));
                                            it.createTime = System.currentTimeMillis();
                                            InventoryServiceNew.gI().addItemBag(player, it);
                                            InventoryServiceNew.gI().sendItemBags(player);

                                            player.checkruonggo = true;
                                            player.levelruonggo = 0;
                                            Service.getInstance().sendThongBao(player, "Bạn nhận được rương gỗ");
                                        } else {
                                            this.npcChat(player, "Hành trang đã đầy");
                                        }
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Mỗi ngày chỉ có thể nhận rương báu 1 lần");
                                    }
                                    break;
                                case 2:
                                    ChangeMapService.gI().changeMapNonSpaceship(player, 52, player.location.x, 336);
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc baHatMit(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Ngươi tìm ta có việc gì?",
                                "Ép sao\ntrang bị", "Pha lê\nhóa\ntrang bị", "Võ đài bà hạt mít", "Đổi Đồ Kích Hoạt\nVIP");
                    } else if (this.mapId == 121) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Ngươi tìm ta có việc gì?",
                                "Về đảo\nrùa");

                    } else if (this.mapId == 112) {
                        if (player.fightbahatmit.pointbahatmit >= 5) {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                    "Ngươi tìm ta có việc gì?",
                                    "Nhận thưởng", "Đóng");
                        } else {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                    "Ngươi tìm ta có việc gì?",
                                    "Quay về đảo kame", "Thi Đấu cùng các đệ tử của bà ?\n phí 500tr vàng", "Đóng");
                        }
                    } else {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Ngươi tìm ta có việc gì?",
                                "Cửa hàng\nBùa", "Nâng cấp\nVật phẩm",
                                "Nâng cấp\nBông tai\nPorata", "Mở chỉ số\nBông tai\nPorata",
                                "Nhập\nNgọc Rồng");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.EP_SAO_TRANG_BI);
                                    break;
                                case 1:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.PHA_LE_HOA_TRANG_BI);
                                    break;
                                case 2:
                                    if (player.pet != null) {
                                        player.pet.changeStatus(Pet.GOHOME);
                                    }
                                    ChangeMapService.gI().changeMapBaHatMit(player, 112, -1, 217, 408);
                                    break;
                                case 3:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_KICH_HOAT);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                            switch (player.combineNew.typeCombine) {
                                case CombineServiceNew.EP_SAO_TRANG_BI:
                                case CombineServiceNew.PHA_LE_HOA_TRANG_BI:
                                case CombineServiceNew.CHUYEN_HOA_TRANG_BI:
                                case CombineServiceNew.NANG_CAP_KICH_HOAT:
                                    if (select == 0) {
                                        CombineServiceNew.gI().startCombine(player);
                                    }
                                    break;
                            }
                        }
                    } else if (this.mapId == 112) {
                        if (player.fightbahatmit.pointbahatmit >= 5) {
                            if (player.iDMark.isBaseMenu()) {
                                switch (select) {
                                    case 0:
                                        player.fightbahatmit.clear();

                                        break;
                                }
                            }
                        } else {
                            if (player.iDMark.isBaseMenu()) {
                                switch (select) {
                                    case 0:
                                        ChangeMapService.gI().changeMapBySpaceShip(player, 5, -1, 1156);
                                        break;
                                    case 1:
                                        if (player.inventory.gold >= 500000000) {
                                            if (Util.canDoWithTime(timebahatmit, 180000)) {
                                                PlayerService.gI().changeAndSendTypePK(player, ConstPlayer.PK_PVP);
                                                ChangeMapService.gI().changeMapInYard(player, 112, -1, 365);
                                                try {
                                                    new BossBahatmit(player.zone, 584, 336);
                                                } catch (Exception ex) {
                                                    java.util.logging.Logger.getLogger(NpcFactory.class.getName()).log(Level.SEVERE, null, ex);
                                                }
                                                if (player.pet != null) {
                                                    player.pet.changeStatus(Pet.GOHOME);
                                                }
                                                player.pkbahatmit = player;
                                                player.inventory.gold -= 500000000;
                                                Service.getInstance().sendMoney(player);
                                                List<Player> playersMap = player.zone.getNotBosses();
                                                for (Player pl : playersMap) {
                                                    if (!player.equals(pl) && !pl.isBoss && !pl.isDie()) {
                                                        timebahatmit = System.currentTimeMillis();
                                                    }
                                                }
                                            } else {
                                                Service.getInstance().sendThongBaoOK(player, "Vui Lòng chờ hết ván đấu");
                                            }
                                        } else {
                                            Service.getInstance().sendThongBaoOK(player, "Không đủ 500tr vàng");
                                        }
                                        break;
                                }
                            }
                        }
                    } else if (this.mapId == 42 || this.mapId == 43 || this.mapId == 44 || this.mapId == 84) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0: //shop bùa
                                    createOtherMenu(player, ConstNpc.MENU_OPTION_SHOP_BUA,
                                            "Bùa của ta rất lợi hại, nhìn ngươi yếu đuối thế này, chắc muốn mua bùa để "
                                                    + "mạnh mẽ à, mua không ta bán cho, xài rồi lại thích cho mà xem.",
                                            "Bùa\n1 giờ", "Bùa\n8 giờ", "Bùa\n1 tháng", "Đóng");
                                    break;
                                case 1:

                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_VAT_PHAM);
                                    break;
                                case 2: //nâng cấp bông tai
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_BONG_TAI);
                                    break;
                                case 3: //làm phép nhập đá
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.MO_CHI_SO_BONG_TAI);
                                    break;
                                case 4:

                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NHAP_NGOC_RONG);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_SHOP_BUA) {
                            switch (select) {
                                case 0:
                                    ShopServiceNew.gI().opendShop(player, "BUA_1H", true);
                                    break;
                                case 1:
                                    ShopServiceNew.gI().opendShop(player, "BUA_8H", true);
                                    break;
                                case 2:
                                    ShopServiceNew.gI().opendShop(player, "BUA_1M", true);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                            switch (player.combineNew.typeCombine) {
                                case CombineServiceNew.NANG_CAP_VAT_PHAM:
                                case CombineServiceNew.NANG_CAP_BONG_TAI:
                                case CombineServiceNew.MO_CHI_SO_BONG_TAI:
                                case CombineServiceNew.NHAP_NGOC_RONG:

                                    if (select == 0) {
                                        CombineServiceNew.gI().startCombine(player);
                                    }
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc ruongDo(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    InventoryServiceNew.gI().sendItemBox(player);
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {

                }
            }
        };
    }

    public static Npc duongtank(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (mapId == 0) {
                        this.createOtherMenu(player, 0, "A mi khò khò, thí chủ hãy giúp giải cứu đệ tử của bần tăng đang bị phong ấn tại ngũ hành sơn.", "Đồng ý", "Từ chối");
                    }
                    if (mapId == 122) {
                        this.createOtherMenu(player, 0, "Thí chủ muốn quay về làng Aru?", "Đồng ý", "Từ chối");

                    }
                    if (mapId == 124) {
                        this.createOtherMenu(player, 0, "A mi khò khò, ở Ngũ hành sơn có lũ khỉ đã ăn trộm Hồng Đào\b Thí chủ có thể giúp ta lấy lại Hồng Đào từ chúng\bTa sẽ đổi 1 ít đồ để đổi lấy Hồng Đào.", "Cửa hàng\nHồng Đào", "Về\nLàng Aru", "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (select) {
                        case 0:
                            if (mapId == 0) {
                                if (player.nPoint.power < 150000000 || player.nPoint.power >= 40000000000L) {
                                    this.npcChat(player, "Sức mạnh thí chủ không phù hợp để qua Ngũ Hành Sơn!");
                                    return;
                                }
                                ChangeMapService.gI().changeMapBySpaceShip(player, 122, -1, 96);
                            }
                            if (mapId == 122) {
                                ChangeMapService.gI().changeMapBySpaceShip(player, 0, -1, 936);
                            }
                            if (mapId == 124) {
                                if (select == 0) {
                                    ShopServiceNew.gI().opendShop(player, "TAYDUKY", true);
                                    break;
                                }
                                if (select == 1) {
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 0, -1, 936);
                                }
                            }
                            break;
                    }
                }
            }
        };
    }

    public static Npc dauThan(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    player.magicTree.openMenuTree();
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    TaskService.gI().checkDoneTaskConfirmMenuNpc(player, this, (byte) select);
                    switch (player.iDMark.getIndexMenu()) {
                        case ConstNpc.MAGIC_TREE_NON_UPGRADE_LEFT_PEA:
                            if (select == 0) {
                                player.magicTree.harvestPea();
                            } else if (select == 1) {
                                if (player.magicTree.level == 10) {
                                    player.magicTree.fastRespawnPea();
                                } else {
                                    player.magicTree.showConfirmUpgradeMagicTree();
                                }
                            } else if (select == 2) {
                                player.magicTree.fastRespawnPea();
                            }
                            break;
                        case ConstNpc.MAGIC_TREE_NON_UPGRADE_FULL_PEA:
                            if (select == 0) {
                                player.magicTree.harvestPea();
                            } else if (select == 1) {
                                player.magicTree.showConfirmUpgradeMagicTree();
                            }
                            break;
                        case ConstNpc.MAGIC_TREE_CONFIRM_UPGRADE:
                            if (select == 0) {
                                player.magicTree.upgradeMagicTree();
                            }
                            break;
                        case ConstNpc.MAGIC_TREE_UPGRADE:
                            if (select == 0) {
                                player.magicTree.fastUpgradeMagicTree();
                            } else if (select == 1) {
                                player.magicTree.showConfirmUnuppgradeMagicTree();
                            }
                            break;
                        case ConstNpc.MAGIC_TREE_CONFIRM_UNUPGRADE:
                            if (select == 0) {
                                player.magicTree.unupgradeMagicTree();
                            }
                            break;
                    }
                }
            }
        };
    }

    public static Npc calick(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            private final byte COUNT_CHANGE = 50;
            private int count;

            private void changeMap() {
                if (this.mapId != 102) {
                    count++;
                    if (this.count >= COUNT_CHANGE) {
                        count = 0;
                        this.map.npcs.remove(this);
                        Map map = MapService.gI().getMapForCalich();
                        this.mapId = map.mapId;
                        this.cx = Util.nextInt(100, map.mapWidth - 100);
                        this.cy = map.yPhysicInTop(this.cx, 0);
                        this.map = map;
                        this.map.npcs.add(this);
                    }
                }
            }

            @Override
            public void openBaseMenu(Player player) {
                player.iDMark.setIndexMenu(ConstNpc.BASE_MENU);
                if (TaskService.gI().getIdTask(player) < ConstTask.TASK_20_0) {
                    Service.getInstance().hideWaitDialog(player);
                    Service.getInstance().sendThongBao(player, "Không thể thực hiện");
                    return;
                }
                if (this.mapId != player.zone.map.mapId) {
                    Service.getInstance().sendThongBao(player, "Calích đã rời khỏi map!");
                    Service.getInstance().hideWaitDialog(player);
                    return;
                }

                if (this.mapId == 102) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Chào chú, cháu có thể giúp gì?",
                            "Kể\nChuyện", "Quay về\nQuá khứ");
                } else {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Chào chú, cháu có thể giúp gì?", "Kể\nChuyện", "Đi đến\nTương lai", "Từ chối");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (this.mapId == 102) {
                    if (player.iDMark.isBaseMenu()) {
                        if (select == 0) {
                            //kể chuyện
                            NpcService.gI().createTutorial(player, this.avartar, ConstNpc.CALICK_KE_CHUYEN);
                        } else if (select == 1) {
                            //về quá khứ
                            ChangeMapService.gI().goToQuaKhu(player);
                        }
                    }
                } else if (player.iDMark.isBaseMenu()) {
                    if (select == 0) {
                        //kể chuyện
                        NpcService.gI().createTutorial(player, this.avartar, ConstNpc.CALICK_KE_CHUYEN);
                    } else if (select == 1) {
                        //đến tương lai
//                                    changeMap();
                        if (TaskService.gI().getIdTask(player) >= ConstTask.TASK_20_0) {
                            ChangeMapService.gI().goToTuongLai(player);
                        }
                    } else {
                        Service.getInstance().sendThongBao(player, "Không thể thực hiện");
                    }
                }
            }
        };
    }

    public static Npc jaco(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 24 || this.mapId == 25 || this.mapId == 26) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Gô Tên, Calich và Monaka đang gặp chuyện ở hành tinh Potaufeu \n Hãy đến đó ngay", "Đến \nPotaufeu");
                    } else if (this.mapId == 139) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Người muốn trở về?", "Quay về", "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 24 || this.mapId == 25 || this.mapId == 26) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {
                                //đến potaufeu
                                ChangeMapService.gI().goToPotaufeu(player);
                            }
                        }
                    } else if (this.mapId == 139) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                //về trạm vũ trụ
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 24 + player.gender, -1, -1);
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc npclytieunuong54(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                createOtherMenu(player, 0, "Trò chơi Chọn ai đây đang được diễn ra, nếu bạn tin tưởng mình đang tràn đầy may mắn thì có thể tham gia thử", "Thể lệ", "Chọn\nThỏi vàng");
            }

            @Override
            public void confirmMenu(Player pl, int select) {
                if (canOpenNpc(pl)) {
                    String time = ((ChonAiDay.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) + " giây";
                    if (pl.iDMark.getIndexMenu() == 0) {
                        if (select == 0) {
                            createOtherMenu(pl, ConstNpc.IGNORE_MENU, "Thời gian giữa các giải là 5 phút\nKhi hết giờ, hệ thống sẽ ngẫu nhiên chọn ra 1 người may mắn.\nLưu ý: Số thỏi vàng nhận được sẽ bị nhà cái lụm đi 5%!Trong quá trình diễn ra khi đặt cược nếu thoát game mọi phần đặt đều sẽ bị hủy", "Ok");
                        } else if (select == 1) {
                            createOtherMenu(pl, 1, "Tổng giải thường: " + ChonAiDay.gI().goldNormar + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(0) + "%\nTổng giải VIP: " + ChonAiDay.gI().goldVip + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(1) + "%\nSố thỏi vàng đặt thường: " + pl.goldNormar + "\nSố thỏi vàng đặt VIP: " + pl.goldVIP + "\n Thời gian còn lại: " + time, "Cập nhập", "Thường\n20 thỏi\nvàng", "VIP\n200 thỏi\nvàng", "Đóng");
                        }
                    } else if (pl.iDMark.getIndexMenu() == 1) {
                        if (((ChonAiDay.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) > 0) {
                            switch (select) {
                                case 0:
                                    createOtherMenu(pl, 1, "Tổng giải thường: " + ChonAiDay.gI().goldNormar + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(0) + "%\nTổng giải VIP: " + ChonAiDay.gI().goldVip + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(1) + "%\nSố thỏi vàng đặt thường: " + pl.goldNormar + "\nSố thỏi vàng đặt VIP: " + pl.goldVIP + "\n Thời gian còn lại: " + time, "Cập nhập", "Thường\n20 thỏi\nvàng", "VIP\n200 thỏi\nvàng", "Đóng");
                                    break;
                                case 1: {
                                    try {
                                        if (InventoryServiceNew.gI().findItemBag(pl, 457).isNotNullItem() && InventoryServiceNew.gI().findItemBag(pl, 457).quantity >= 20) {
                                            InventoryServiceNew.gI().subQuantityItemsBag(pl, InventoryServiceNew.gI().findItemBag(pl, 457), 20);
                                            InventoryServiceNew.gI().sendItemBags(pl);
                                            pl.goldNormar += 20;
                                            ChonAiDay.gI().goldNormar += 20;
                                            ChonAiDay.gI().addPlayerNormar(pl);
                                            createOtherMenu(pl, 1, "Tổng giải thường: " + ChonAiDay.gI().goldNormar + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(0) + "%\nTổng giải VIP: " + ChonAiDay.gI().goldVip + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(1) + "%\nSố thỏi vàng đặt thường: " + pl.goldNormar + "\nSố thỏi vàng đặt VIP: " + pl.goldVIP + "\n Thời gian còn lại: " + time, "Cập nhập", "Thường\n20 thỏi\nvàng", "VIP\n200 thỏi\nvàng", "Đóng");
                                        } else {
                                            Service.getInstance().sendThongBao(pl, "Bạn không đủ thỏi vàng");
                                        }
                                    } catch (Exception ex) {
                                        java.util.logging.Logger.getLogger(NpcFactory.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                                break;

                                case 2: {
                                    try {
                                        if (InventoryServiceNew.gI().findItemBag(pl, 457).isNotNullItem() && InventoryServiceNew.gI().findItemBag(pl, 457).quantity >= 200) {
                                            InventoryServiceNew.gI().subQuantityItemsBag(pl, InventoryServiceNew.gI().findItemBag(pl, 457), 200);
                                            InventoryServiceNew.gI().sendItemBags(pl);
                                            pl.goldVIP += 200;
                                            ChonAiDay.gI().goldVip += 200;
                                            ChonAiDay.gI().addPlayerVIP(pl);
                                            createOtherMenu(pl, 1, "Tổng giải thường: " + ChonAiDay.gI().goldNormar + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(0) + "%\nTổng giải VIP: " + ChonAiDay.gI().goldVip + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(1) + "%\nSố thỏi vàng đặt thường: " + pl.goldNormar + "\nSố thỏi vàng đặt VIP: " + pl.goldVIP + "\n Thời gian còn lại: " + time, "Cập nhập", "Thường\n20 thỏi\nvàng", "VIP\n200 thỏi\nvàng", "Đóng");
                                        } else {
                                            Service.getInstance().sendThongBao(pl, "Bạn không đủ thỏi vàng");
                                        }
                                    } catch (Exception ex) {
                                    }
                                }
                                break;

                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc thuongDe(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 45) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Con muốn làm gì nào", "Đến Kaio", "Quay số\nmay mắn");
                    }
                    if (this.mapId == 0) {
                        this.createOtherMenu(player, 0,
                                "Con muốn gì nào?\nCon đang còn : " + player.pointPvp + " điểm PvP Point", "Đến DHVT", "Đổi Cải trang sự kiên", "Top PVP");
                    }
                    if (this.mapId == 129) {
                        this.createOtherMenu(player, 0,
                                "Con muốn gì nào?", "Quay ve");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 0) {
                        if (player.iDMark.getIndexMenu() == 0) { // 
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 129, -1, 354);
                                    Service.getInstance().changeFlag(player, Util.nextInt(8));
                                    break; // qua dhvt
                                case 1:  // 
                                    this.createOtherMenu(player, 1,
                                            "Bạn có muốn đổi 500 điểm PVP lấy \n|6|Cải trang Mèo Kid Lân với tất cả chỉ số là 30%\n ", "Ok", "Tu choi");
                                    // bat menu doi item
                                    break;

                                case 2:  // 
                                    Service.getInstance().sendThongBaoOK(player, TopService.getTopPvp());
                                    // mo top pvp
                                    break;

                            }
                        }
                        if (player.iDMark.getIndexMenu() == 1) { // action doi item
                            switch (select) {
                                case 0: // trade
                                    if (player.pointPvp >= 500) {
                                        player.pointPvp -= 500;
                                        Item item = ItemService.gI().createNewItem((short) (1104));
                                        item.itemOptions.add(new Item.ItemOption(49, 30));
                                        item.itemOptions.add(new Item.ItemOption(77, 30));
                                        item.itemOptions.add(new Item.ItemOption(103, 30));
                                        item.itemOptions.add(new Item.ItemOption(207, 0));
//                                      
                                        InventoryServiceNew.gI().addItemBag(player, item);
                                        Service.getInstance().sendThongBao(player, "Chúc Mừng Bạn Đổi Cải Trang Thành Công !");
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Không đủ điểm bạn còn " + (500 - player.pointPvp) + " Điểm nữa");
                                    }
                                    break;
                            }
                        }
                    }
                    if (this.mapId == 129) {
                        switch (select) {
                            case 0: // quay ve
                                ChangeMapService.gI().changeMapBySpaceShip(player, 0, -1, 354);
                                break;
                        }
                    }
                    if (this.mapId == 45) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 48, -1, 354);
                                    break;
                                case 1:
                                    this.createOtherMenu(player, ConstNpc.MENU_CHOOSE_LUCKY_ROUND,
                                            "Con muốn làm gì nào?", "Quay bằng\nvàng",
                                            "Rương phụ\n("
                                                    + (player.inventory.itemsBoxCrackBall.size()
                                                    - InventoryServiceNew.gI().getCountEmptyListItem(player.inventory.itemsBoxCrackBall))
                                                    + " món)",
                                            "Xóa hết\ntrong rương", "Đóng");
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_CHOOSE_LUCKY_ROUND) {
                            switch (select) {
                                case 0:
                                    LuckyRound.gI().openCrackBallUI(player, LuckyRound.USING_GOLD);
                                    break;
                                case 1:
                                    ShopServiceNew.gI().opendShop(player, "ITEMS_LUCKY_ROUND", true);
                                    break;
                                case 2:
                                    NpcService.gI().createMenuConMeo(player,
                                            ConstNpc.CONFIRM_REMOVE_ALL_ITEM_LUCKY_ROUND, this.avartar,
                                            "Con có chắc muốn xóa hết vật phẩm trong rương phụ? Sau khi xóa "
                                                    + "sẽ không thể khôi phục!",
                                            "Đồng ý", "Hủy bỏ");
                                    break;
                            }
                        }
                    }

                }
            }
        };
    }

    public static Npc thanVuTru(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 48) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Con muốn làm gì nào", "Di chuyển");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 48) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    this.createOtherMenu(player, ConstNpc.MENU_DI_CHUYEN,
                                            "Con muốn đi đâu?", "Về\nthần điện", "Thánh địa\nKaio", "Con\nđường\nrắn độc", "Từ chối");
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_DI_CHUYEN) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 45, -1, 354);
                                    break;
                                case 1:
                                    ChangeMapService.gI().changeMap(player, 50, -1, 318, 336);
                                    break;
                                case 2:
                                    //con đường rắn độc
                                    break;
                            }
                        }
                    }
                }
            }

        };
    }

    public static Npc kibit(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 50) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                "Đến\nKaio", "Từ chối");
                    }
                    if (this.mapId == 114) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 50) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMap(player, 48, -1, 354, 240);
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc osin(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 50) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                "Đến\nKaio", "Đến\nhành tinh\nBill", "Từ chối");
                    } else if (this.mapId == 154) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                "Về thánh địa", "Đến\nhành tinh\nngục tù", "Từ chối");
                    } else if (this.mapId == 155) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                "Quay về", "Từ chối");
                    } else if (this.mapId == 52) {
                        try {
                            MapMaBu.gI().setTimeJoinMapMaBu();
                            if (this.mapId == 52) {
                                long now = System.currentTimeMillis();
                                if (now > MapMaBu.TIME_OPEN_MABU && now < MapMaBu.TIME_CLOSE_MABU) {
                                    this.createOtherMenu(player, ConstNpc.MENU_OPEN_MMB, "Đại chiến Ma Bư đã mở, "
                                                    + "ngươi có muốn tham gia không?",
                                            "Hướng dẫn\nthêm", "Tham gia", "Từ chối");
                                } else {
                                    this.createOtherMenu(player, ConstNpc.MENU_NOT_OPEN_MMB,
                                            "Ta có thể giúp gì cho ngươi?", "Hướng dẫn", "Từ chối");
                                }

                            }
                        } catch (Exception ex) {
                            Logger.error("Lỗi mở menu osin");
                        }

                    } else if (this.mapId >= 114 && this.mapId < 120 && this.mapId != 116) {
                        if (player.fightMabu.pointMabu >= player.fightMabu.POINT_MAX) {
                            this.createOtherMenu(player, ConstNpc.GO_UPSTAIRS_MENU, "Ta có thể giúp gì cho ngươi ?",
                                    "Lên Tầng!", "Quay về", "Từ chối");
                        } else {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                    "Quay về", "Từ chối");
                        }
                    } else if (this.mapId == 120) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                "Quay về", "Từ chối");
                    } else {
                        super.openBaseMenu(player);
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 50) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMap(player, 48, -1, 354, 240);
                                    break;
                                case 1:
                                    ChangeMapService.gI().changeMap(player, 154, -1, 200, 312);
                                    break;
                            }
                        }
                    } else if (this.mapId == 154) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMap(player, 50, -1, 318, 336);
                                    break;
                                case 1:
                                    ChangeMapService.gI().changeMap(player, 155, -1, 111, 792);
                                    break;
                            }
                        }
                    } else if (this.mapId == 155) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {
                                ChangeMapService.gI().changeMap(player, 154, -1, 200, 312);
                            }
                        }
                    } else if (this.mapId == 52) {
                        switch (player.iDMark.getIndexMenu()) {
                            case ConstNpc.MENU_REWARD_MMB:
                                break;
                            case ConstNpc.MENU_OPEN_MMB:
                                if (select == 0) {
                                    NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_MAP_MA_BU);
                                } else if (select == 1) {
//                                    if (!player.getSession().actived) {
//                                        Service.getInstance().sendThongBao(player, "Vui lòng kích hoạt tài khoản để sử dụng chức năng này");
//                                    } else
                                    ChangeMapService.gI().changeMap(player, 114, -1, 318, 336);
                                }
                                break;
                            case ConstNpc.MENU_NOT_OPEN_BDW:
                                if (select == 0) {
                                    NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_MAP_MA_BU);
                                }
                                break;
                        }
                    } else if (this.mapId >= 114 && this.mapId < 120 && this.mapId != 116) {
                        if (player.iDMark.getIndexMenu() == ConstNpc.GO_UPSTAIRS_MENU) {
                            if (select == 0) {
                                player.fightMabu.clear();
                                ChangeMapService.gI().changeMap(player, this.map.mapIdNextMabu((short) this.mapId), -1, this.cx, this.cy);
                            } else if (select == 1) {
                                ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, 0, -1);
                            }
                        } else {
                            if (select == 0) {
                                ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, 0, -1);
                            }
                        }
                    } else if (this.mapId == 120) {
                        if (player.iDMark.getIndexMenu() == ConstNpc.BASE_MENU) {
                            if (select == 0) {
                                ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, 0, -1);
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc linhCanh(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (player.clan == null) {
                        this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Chỉ tiếp các bang hội, miễn tiếp khách vãng lai", "Đóng");
                        return;
                    }
                    if (player.clan.getMembers().size() < DoanhTrai.N_PLAYER_CLAN) {
                        this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Bang hội phải có ít nhất 5 thành viên mới có thể mở", "Đóng");
                        return;
                    }
                    if (player.clan.doanhTrai != null) {
                        createOtherMenu(player, ConstNpc.MENU_JOIN_DOANH_TRAI,
                                "Bang hội của ngươi đang đánh trại độc nhãn\n"
                                        + "Thời gian còn lại là "
                                        + TimeUtil.getSecondLeft(player.clan.doanhTrai.getLastTimeOpen(), DoanhTrai.TIME_DOANH_TRAI / 1000)
                                        + ". Ngươi có muốn tham gia không?",
                                "Tham gia", "Không", "Hướng\ndẫn\nthêm");
                        return;
                    }
                    int nPlSameClan = 0;
                    for (Player pl : player.zone.getPlayers()) {
                        if (!pl.equals(player) && pl.clan != null
                                && pl.clan.equals(player.clan) && pl.location.x >= 1285
                                && pl.location.x <= 1645) {
                            nPlSameClan++;
                        }
                    }
                    if (nPlSameClan > DoanhTrai.N_PLAYER_MAP) {
                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Ngươi phải có ít nhất " + DoanhTrai.N_PLAYER_MAP + " đồng đội cùng bang đứng gần mới có thể\nvào\n"
                                        + "tuy nhiên ta khuyên ngươi nên đi cùng với 3-4 người để khỏi chết.\n"
                                        + "Hahaha.", "OK", "Hướng\ndẫn\nthêm");
                        return;
                    }
//                    if (player.clanMember.getNumDateFromJoinTimeToToday() < 1) {
//                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
//                                "Doanh trại chỉ cho phép những người ở trong bang trên 1 ngày. Hẹn ngươi quay lại vào lúc khác",
//                                "OK", "Hướng\ndẫn\nthêm");
//                        return;
//                    }
//                    if (player.clan.haveGoneDoanhTrai) {
//                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
//                                "Bang hội của ngươi đã đi trại lúc " + TimeUtil.formatTime(player.clan.lastTimeOpenDoanhTrai, "HH:mm:ss") + " hôm nay. Người mở\n"
//                                + "(" + player.clan.playerOpenDoanhTrai + "). Hẹn ngươi quay lại vào ngày mai", "OK", "Hướng\ndẫn\nthêm");
//                        return;
//                    }
                    createOtherMenu(player, ConstNpc.MENU_JOIN_DOANH_TRAI,
                            "Hôm nay bang hội của ngươi chưa vào trại lần nào. Ngươi có muốn vào\n"
                                    + "không?\nĐể vào, ta khuyên ngươi nên có 3-4 người cùng bang đi cùng",
                            "Vào\n(miễn phí)", "Không", "Hướng\ndẫn\nthêm");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (player.iDMark.getIndexMenu()) {
                        case ConstNpc.MENU_JOIN_DOANH_TRAI:
                            if (select == 0) {
                                DoanhTraiService.gI().joinDoanhTrai(player);
                            } else if (select == 2) {
                                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_DOANH_TRAI);
                            }
                            break;
                        case ConstNpc.IGNORE_MENU:
                            if (select == 1) {
                                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_DOANH_TRAI);
                            }
                            break;
                    }
                }
            }
        };
    }

    public static Npc duahau(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (player.duahau != null) {
                        player.duahau.sendduahau();
                        if (player.duahau.getSecondDone() != 0) {
                            this.createOtherMenu(player, ConstNpc.NOT_NHAN_DUA, "Dưa ơi dưa à dưa mau lớn để ta thu.",
                                    "Dưa chưa chín chờ đi", "Đóng");
                        } else {
                            this.createOtherMenu(player, ConstNpc.CAN_THU_HOACH_DUA, "Dưa ơi dưa à dưa mau lớn để ta thu", "Thu Hoạch", "Đóng");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (player.iDMark.getIndexMenu()) {
                        case ConstNpc.NOT_NHAN_DUA:
                            Service.getInstance().sendThongBao(player, "Chờ đi tham gì chời");
                            break;
                        case ConstNpc.CAN_THU_HOACH_DUA:
                            switch (select) {
                                case 0:
                                    Item duahau = ItemService.gI().createNewItem((short) 569, 1);
                                    InventoryServiceNew.gI().addItemBag(player, duahau);
                                    Service.getInstance().sendThongBao(player, "Bạn đã nhận được dưa hấu");
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    DuaHau.createduahau(player);
                                    break;
                            }
                            break;
                    }
                }
            }
        };
    }

    public static Npc quaTrung(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            private final int COST_AP_TRUNG_NHANH = 1000000000;

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    player.mabuEgg.sendMabuEgg();
                    if (player.mabuEgg.getSecondDone() != 0) {
                        this.createOtherMenu(player, ConstNpc.CAN_NOT_OPEN_EGG, "Bư bư bư...",
                                "Hủy bỏ\ntrứng", "Ấp nhanh\n" + Util.numberToMoney(COST_AP_TRUNG_NHANH) + " vàng", "Đóng");
                    } else {
                        this.createOtherMenu(player, ConstNpc.CAN_OPEN_EGG, "Bư bư bư...", "Nở", "Hủy bỏ\ntrứng", "Đóng");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (player.iDMark.getIndexMenu()) {
                        case ConstNpc.CAN_NOT_OPEN_EGG:
                            if (select == 0) {
                                this.createOtherMenu(player, ConstNpc.CONFIRM_DESTROY_EGG,
                                        "Bạn có chắc chắn muốn hủy bỏ trứng Mabư?", "Đồng ý", "Từ chối");
                            } else if (select == 1) {
                                if (player.inventory.gold >= COST_AP_TRUNG_NHANH) {
                                    player.inventory.gold -= COST_AP_TRUNG_NHANH;
                                    player.mabuEgg.timeDone = 0;
                                    Service.getInstance().sendMoney(player);
                                    player.mabuEgg.sendMabuEgg();
                                } else {
                                    Service.getInstance().sendThongBao(player,
                                            "Bạn không đủ vàng để thực hiện, còn thiếu "
                                                    + Util.numberToMoney((COST_AP_TRUNG_NHANH - player.inventory.gold)) + " vàng");
                                }
                            }
                            break;
                        case ConstNpc.CAN_OPEN_EGG:
                            switch (select) {
                                case 0:
                                    this.createOtherMenu(player, ConstNpc.CONFIRM_OPEN_EGG,
                                            "Bạn có chắc chắn cho trứng nở?\n"
                                                    + "Đệ tử của bạn sẽ được thay thế bằng đệ Mabư",
                                            "Đệ mabư\nTrái Đất", "Đệ mabư\nNamếc", "Đệ mabư\nXayda", "Từ chối");
                                    break;
                                case 1:
                                    this.createOtherMenu(player, ConstNpc.CONFIRM_DESTROY_EGG,
                                            "Bạn có chắc chắn muốn hủy bỏ trứng Mabư?", "Đồng ý", "Từ chối");
                                    break;
                            }
                            break;
                        case ConstNpc.CONFIRM_OPEN_EGG:
                            switch (select) {
                                case 0:
                                    player.mabuEgg.openEgg(ConstPlayer.TRAI_DAT);
                                    break;
                                case 1:
                                    player.mabuEgg.openEgg(ConstPlayer.NAMEC);
                                    break;
                                case 2:
                                    player.mabuEgg.openEgg(ConstPlayer.XAYDA);
                                    break;
                                default:
                                    break;
                            }
                            break;
                        case ConstNpc.CONFIRM_DESTROY_EGG:
                            if (select == 0) {
                                player.mabuEgg.destroyEgg();
                            }
                            break;
                    }
                }
            }
        };
    }

    public static Npc quocVuong(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                this.createOtherMenu(player, ConstNpc.BASE_MENU,
                        "Con muốn nâng giới hạn sức mạnh cho bản thân hay đệ tử?",
                        "Bản thân", "Đệ tử", "Từ chối");
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                if (player.nPoint.limitPower < NPoint.MAX_LIMIT) {
                                    this.createOtherMenu(player, ConstNpc.OPEN_POWER_MYSEFT,
                                            "Ta sẽ truền năng lượng giúp con mở giới hạn sức mạnh của bản thân lên "
                                                    + Util.numberToMoney(player.nPoint.getPowerNextLimit()),
                                            "Nâng\ngiới hạn\nsức mạnh",
                                            "Nâng ngay\n" + Util.numberToMoney(OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER) + " vàng", "Đóng");
                                } else {
                                    this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                            "Sức mạnh của con đã đạt tới giới hạn",
                                            "Đóng");
                                }
                                break;
                            case 1:
                                if (player.pet != null) {
                                    if (player.pet.nPoint.limitPower < NPoint.MAX_LIMIT) {
                                        this.createOtherMenu(player, ConstNpc.OPEN_POWER_PET,
                                                "Ta sẽ truền năng lượng giúp con mở giới hạn sức mạnh của đệ tử lên "
                                                        + Util.numberToMoney(player.pet.nPoint.getPowerNextLimit()),
                                                "Nâng ngay\n" + Util.numberToMoney(OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER) + " vàng", "Đóng");
                                    } else {
                                        this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                                "Sức mạnh của đệ con đã đạt tới giới hạn",
                                                "Đóng");
                                    }
                                } else {
                                    Service.getInstance().sendThongBao(player, "Không thể thực hiện");
                                }
                                //giới hạn đệ tử
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.OPEN_POWER_MYSEFT) {
                        switch (select) {
                            case 0:
                                OpenPowerService.gI().openPowerBasic(player);
                                break;
                            case 1:
                                if (player.inventory.gold >= OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER) {
                                    if (OpenPowerService.gI().openPowerSpeed(player)) {
                                        player.inventory.gold -= OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER;
                                        Service.getInstance().sendMoney(player);
                                    }
                                } else {
                                    Service.getInstance().sendThongBao(player,
                                            "Bạn không đủ vàng để mở, còn thiếu "
                                                    + Util.numberToMoney((OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER - player.inventory.gold)) + " vàng");
                                }
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.OPEN_POWER_PET) {
                        if (select == 0) {
                            if (player.inventory.gold >= OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER) {
                                if (OpenPowerService.gI().openPowerSpeed(player.pet)) {
                                    player.inventory.gold -= OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER;
                                    Service.getInstance().sendMoney(player);
                                }
                            } else {
                                Service.getInstance().sendThongBao(player,
                                        "Bạn không đủ vàng để mở, còn thiếu "
                                                + Util.numberToMoney((OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER - player.inventory.gold)) + " vàng");
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc bulmaTL(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 102) {
                        if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU, "Cậu bé muốn mua gì nào?", "Cửa hàng", "Đóng");
                        }
                    } else if (this.mapId == 104) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Kính chào Ngài Linh thú sư!", "Cửa hàng", "Đóng");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 102) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {
                                ShopServiceNew.gI().opendShop(player, "BUNMA_FUTURE", true);
                            }
                        }
                    } else if (this.mapId == 104) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {
                                ShopServiceNew.gI().opendShop(player, "BUNMA_LINHTHU", true);
                            }
                        }
                    }
                }
            }
        };
    }

    //8/38/3
    public static Npc bunma8_3(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Em mún tặng chị hoa hồng hả :3", "Tặng hoa", "Xem số hoa đã tặng", "Đổi thưởng", "Đóng");
                }
            }


            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (Manager.SUKIEN == 1) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    Item hoahong = InventoryServiceNew.gI().findItemBag(player, 610);
                                    if (hoahong != null) {
                                        player.diemtanghoa8_3 += hoahong.quantity;
                                        Service.getInstance().sendThongBao(player, "Tặng thành công " + hoahong.quantity + " bông hoa hồng");
                                        InventoryServiceNew.gI().subQuantityItem(player.inventory.itemsBag, hoahong, hoahong.quantity);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                    } else {
                                        Service.getInstance().sendThongBaoOK(player, "Bạn không có hoa hồng để tặng");
                                    }
                                    break;
                                case 1:
                                    Service.getInstance().sendThongBao(player, "|7|Bạn đã tặng " + player.diemtanghoa8_3 + " bông hoa");
                                    break;
                                case 2:
                                    NpcService.gI().createMenuConMeo(player, ConstNpc.MENUSUKIEN8_3, 4116, "Em mún đổi thưởng hả một lần đổi \nphải hi sinh 99 bông hoa đó nha", "20% sức đánh trong 15p", "20% hp trong 15p", "20% ki trong 15p", "Ngẫu nhiên pet bunma dễ thương\n có tỷ lệ ra vĩnh viễn");
                                    break;
                            }
                        }
                    } else {
                        Service.getInstance().sendThongBaoOK(player, "Sự kiện đã kết thúc");
                    }
                }
            }
        };
    }


    public static Npc rongOmega(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    BlackBallWar.gI().setTime();
                    if (this.mapId == 24 || this.mapId == 25 || this.mapId == 26) {
                        try {
                            long now = System.currentTimeMillis();
                            if (now > BlackBallWar.TIME_OPEN && now < BlackBallWar.TIME_CLOSE) {
                                this.createOtherMenu(player, ConstNpc.MENU_OPEN_BDW, "Đường đến với ngọc rồng sao đen đã mở, "
                                                + "ngươi có muốn tham gia không?",
                                        "Hướng dẫn\nthêm", "Tham gia", "Từ chối");
                            } else {
                                String[] optionRewards = new String[7];
                                int index = 0;
                                for (int i = 0; i < 7; i++) {
                                    if (player.rewardBlackBall.timeOutOfDateReward[i] > System.currentTimeMillis()) {
                                        String quantily = player.rewardBlackBall.quantilyBlackBall[i] > 1 ? "x" + player.rewardBlackBall.quantilyBlackBall[i] + " " : "";
                                        optionRewards[index] = quantily + (i + 1) + " sao";
                                        index++;
                                    }
                                }
                                if (index != 0) {
                                    String[] options = new String[index + 1];
                                    for (int i = 0; i < index; i++) {
                                        options[i] = optionRewards[i];
                                    }
                                    options[options.length - 1] = "Từ chối";
                                    this.createOtherMenu(player, ConstNpc.MENU_REWARD_BDW, "Ngươi có một vài phần thưởng ngọc "
                                                    + "rồng sao đen đây!",
                                            options);
                                } else {
                                    this.createOtherMenu(player, ConstNpc.MENU_NOT_OPEN_BDW,
                                            "Ta có thể giúp gì cho ngươi?", "Hướng dẫn", "Từ chối");
                                }
                            }
                        } catch (Exception ex) {
                            Logger.error("Lỗi mở menu rồng Omega");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (player.iDMark.getIndexMenu()) {
                        case ConstNpc.MENU_REWARD_BDW:
                            player.rewardBlackBall.getRewardSelect((byte) select);
                            break;
                        case ConstNpc.MENU_OPEN_BDW:
                            if (select == 0) {
                                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_BLACK_BALL_WAR);
                            } else if (select == 1) {
//                                if (!player.getSession().actived) {
//                                    Service.getInstance().sendThongBao(player, "Vui lòng kích hoạt tài khoản để sử dụng chức năng này");
//
//                                } else
                                player.iDMark.setTypeChangeMap(ConstMap.CHANGE_BLACK_BALL);
                                ChangeMapService.gI().openChangeMapTab(player);
                            }
                            break;
                        case ConstNpc.MENU_NOT_OPEN_BDW:
                            if (select == 0) {
                                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_BLACK_BALL_WAR);
                            }
                            break;
                    }
                }
            }

        };
    }

    public static Npc rong1_to_7s(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isHoldBlackBall()) {
                        this.createOtherMenu(player, ConstNpc.MENU_PHU_HP, "Ta có thể giúp gì cho ngươi?", "Phù hộ", "Từ chối");
                    } else {
                        if (BossManager.gI().existBossOnPlayer(player)
                                || player.zone.items.stream().anyMatch(itemMap -> ItemMapService.gI().isBlackBall(itemMap.itemTemplate.id))
                                || player.zone.getPlayers().stream().anyMatch(p -> p.iDMark.isHoldBlackBall())) {
                            this.createOtherMenu(player, ConstNpc.MENU_OPTION_GO_HOME, "Ta có thể giúp gì cho ngươi?", "Về nhà", "Từ chối");
                        } else {
                            this.createOtherMenu(player, ConstNpc.MENU_OPTION_GO_HOME, "Ta có thể giúp gì cho ngươi?", "Về nhà", "Từ chối", "Gọi BOSS");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.getIndexMenu() == ConstNpc.MENU_PHU_HP) {
                        if (select == 0) {
                            this.createOtherMenu(player, ConstNpc.MENU_OPTION_PHU_HP,
                                    "Ta sẽ giúp ngươi tăng HP lên mức kinh hoàng, ngươi chọn đi",
                                    "x3 HP\n" + Util.numberToMoney(BlackBallWar.COST_X3) + " vàng",
                                    "x5 HP\n" + Util.numberToMoney(BlackBallWar.COST_X5) + " vàng",
                                    "x7 HP\n" + Util.numberToMoney(BlackBallWar.COST_X7) + " vàng",
                                    "Từ chối"
                            );
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_GO_HOME) {
                        if (select == 0) {
                            ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, -1, 250);
                        } else if (select == 2) {
                            BossManager.gI().callBoss(player, mapId);
                        } else if (select == 1) {
                            this.npcChat(player, "Để ta xem ngươi trụ được bao lâu");
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_PHU_HP) {
                        if (player.effectSkin.xHPKI > 1) {
                            Service.getInstance().sendThongBao(player, "Bạn đã được phù hộ rồi!");
                            return;
                        }
                        switch (select) {
                            case 0:
                                BlackBallWar.gI().xHPKI(player, BlackBallWar.X3);
                                break;
                            case 1:
                                BlackBallWar.gI().xHPKI(player, BlackBallWar.X5);
                                break;
                            case 2:
                                BlackBallWar.gI().xHPKI(player, BlackBallWar.X7);
                                break;
                            case 3:
                                this.npcChat(player, "Để ta xem ngươi trụ được bao lâu");
                                break;
                        }
                    }
                }
            }
        };
    }

    public static Npc bill(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Đói bụng quá.. ngươi mang cho ta 99 phần đồ ăn,\nta sẽ cho một món đồ Hủy Diệt.\n Nếu tâm trạng ta vui ngươi có thể nhận được trang bị\ntăng đến 15%!",
                            "OK", "Đóng");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (this.mapId) {
                        case 48:
                            switch (player.iDMark.getIndexMenu()) {
                                case ConstNpc.BASE_MENU:
                                    if (select == 0) {
                                        Item pudding = InventoryServiceNew.gI().findItemBag(player, 663);
                                        Item xucxich = InventoryServiceNew.gI().findItemBag(player, 664);
                                        Item kemdau = InventoryServiceNew.gI().findItemBag(player, 665);
                                        Item mily = InventoryServiceNew.gI().findItemBag(player, 666);
                                        Item sushi = InventoryServiceNew.gI().findItemBag(player, 667);
                                        if (pudding != null && pudding.quantity >= 99 ||
                                                xucxich != null && xucxich.quantity >= 99 ||
                                                kemdau != null && kemdau.quantity >= 99 ||
                                                mily != null && mily.quantity >= 99 ||
                                                sushi != null && sushi.quantity >= 99) {
                                            ShopServiceNew.gI().opendShop(player, "HUY_DIET", true);
                                            break;
                                        } else {
                                            this.npcChat(player, "Còn không mau đem x99 thức ăn đến cho ta !!");
                                            break;
                                        }
                                    }
                            }
                            break;
                    }
                }
            }
        };
    }

    public static Npc whis(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (this.mapId == 154) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Thử đánh với ta xem nào.\nNgươi còn 1 lượt cơ mà.!",
                            "Nói chuyện", "Học \nTuyệt kỹ", "Hướng dẫn");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu() && this.mapId == 154) {
                        switch (select) {
                            case 0:
                                this.createOtherMenu(player, 5, "Ta sẽ giúp ngươi chế tạo trang bị thiên sứ", "Cửa hàng", "Chế tạo", "Đóng");
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == 5) {
                        switch (select) {
                            case 0:
                                ShopServiceNew.gI().opendShop(player, "THIEN_SU", false);
                                break;
                            case 1:
                                CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.CHE_TAO_TRANG_BI_TS);
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_DAP_DO) {
                    }
                }
            }
        };
    }

    public static Npc noibanh(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Bạn muốn nấu bánh hả \n Bánh tét cần x99 (thịt heo,lá dong,thúng nếp,thúng đậu xanh)+300tr vàng\n Bánh chưng cần x99 (thịt heo,lá dong,thúng nếp,thúng đậu xanh)+500tr vàng", "Nấu bánh tét", "Nấu bánh chưng", "Đóng");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                if (Manager.SUKIEN == 0) {
                                    if (player.inventory.gold >= 300000000L) {
                                        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                                            Item thitheo = InventoryServiceNew.gI().findnlsktet(player);
                                            Item thungnep = InventoryServiceNew.gI().findnlsktet1(player);
                                            Item dauxanh = InventoryServiceNew.gI().findnlsktet2(player);
                                            Item ladong = InventoryServiceNew.gI().findnlsktet3(player);
                                            if (thitheo != null && thungnep != null && dauxanh != null && ladong != null) {
                                                Item banhtet = ItemService.gI().createNewItem((short) 752, 1);

                                                // - Số item sự kiện có trong rương
                                                InventoryServiceNew.gI().subQuantityItemsBag(player, thitheo, 99);
                                                InventoryServiceNew.gI().subQuantityItemsBag(player, thungnep, 99);
                                                InventoryServiceNew.gI().subQuantityItemsBag(player, dauxanh, 99);
                                                InventoryServiceNew.gI().subQuantityItemsBag(player, ladong, 99);

                                                banhtet.itemOptions.add(new Item.ItemOption(74, 0));
                                                player.inventory.gold -= 300000000L;
                                                InventoryServiceNew.gI().addItemBag(player, banhtet);
                                                InventoryServiceNew.gI().sendItemBags(player);
                                                Service.getInstance().sendThongBao(player, "Nấu bánh thành công");
                                            } else {
                                                Service.getInstance().sendThongBao(player, "Không đủ nguyên liệu");
                                            }
                                        } else {
                                            Service.getInstance().sendThongBao(player, "Hành trang đầy.");
                                        }
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Không đủ 300tr vàng");
                                    }
                                } else {
                                    Service.getInstance().sendThongBao(player, "Sự kiện đã kết thúc");
                                }
                                break;
                            case 1:
                                if (Manager.SUKIEN == 0) {
                                    if (player.inventory.gold >= 500000000L) {
                                        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                                            Item thitheo = InventoryServiceNew.gI().findnlsktet(player);
                                            Item thungnep = InventoryServiceNew.gI().findnlsktet1(player);
                                            Item dauxanh = InventoryServiceNew.gI().findnlsktet2(player);
                                            Item ladong = InventoryServiceNew.gI().findnlsktet3(player);
                                            if (thitheo != null && thungnep != null && dauxanh != null && ladong != null) {
                                                Item banhchung = ItemService.gI().createNewItem((short) 753, 1);

                                                // - Số item sự kiện có trong rương
                                                InventoryServiceNew.gI().subQuantityItemsBag(player, thitheo, 99);
                                                InventoryServiceNew.gI().subQuantityItemsBag(player, thungnep, 99);
                                                InventoryServiceNew.gI().subQuantityItemsBag(player, dauxanh, 99);
                                                InventoryServiceNew.gI().subQuantityItemsBag(player, ladong, 99);

                                                banhchung.itemOptions.add(new Item.ItemOption(74, 0));
                                                player.inventory.gold -= 500000000L;
                                                InventoryServiceNew.gI().addItemBag(player, banhchung);
                                                InventoryServiceNew.gI().sendItemBags(player);
                                                Service.getInstance().sendThongBao(player, "Nấu bánh thành công");
                                            } else {
                                                Service.getInstance().sendThongBao(player, "Không đủ nguyên liệu");
                                            }
                                        } else {
                                            Service.getInstance().sendThongBao(player, "Hành trang đầy.");
                                        }
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Không đủ 500tr vàng");
                                    }
                                } else {
                                    Service.getInstance().sendThongBao(player, "Sự kiện đã kết thúc");
                                }
                                break;
                        }
                    }
                }
            }
        };
    }

    public static Npc hungvuong(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Giỗ tổ hùng vương là 1 ngày lễ của việt nam ta con muốn gì nào:" +
                            "\n|7| Để Gộp Tre Trăm Đốt Cần 1K khung tre + 300tr vàng" +
                            "\n|3| Cần 50 điểm cho 1 lần đổi", "Gộp Tre Trăm Đốt", "Đổi điểm đánh sơn tinh", "Đổi điểm đánh thủy tinh", "Đổi dưa hấu", "Xem Số Điểm sự kiện", "Đóng");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                if (Manager.SUKIEN == 2) {
                                    if (player.inventory.gold >= 300000000L) {
                                        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                                            Item khungtre = InventoryServiceNew.gI().findnlskgiotohungvuong(player);
                                            if (khungtre != null && khungtre.quantity >= 1000) {
                                                Item caytretramdot = ItemService.gI().createNewItem((short) 1190, 1);

                                                // - Số item sự kiện có trong rương
                                                InventoryServiceNew.gI().subQuantityItemsBag(player, khungtre, 1000);

                                                khungtre.itemOptions.add(new Item.ItemOption(73, 0));
                                                player.inventory.gold -= 300000000L;
                                                InventoryServiceNew.gI().addItemBag(player, caytretramdot);
                                                InventoryServiceNew.gI().sendItemBags(player);
                                                Service.getInstance().sendThongBao(player, "Khắc nhập khắc nhập");
                                            } else {
                                                Service.getInstance().sendThongBao(player, "Không đủ khung tre");
                                            }
                                        } else {
                                            Service.getInstance().sendThongBao(player, "Hành trang đầy.");
                                        }
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Không đủ 300tr vàng");
                                    }
                                } else {
                                    Service.getInstance().sendThongBao(player, "Sự kiện đã kết thúc");
                                }
                                break;
                            case 1:
                                if (Manager.SUKIEN == 2) {
                                    if (player.diemsontinh >= 50) {
                                        Item sontinh = ItemService.gI().createItemSetKichHoat(421, 1);
                                        sontinh.itemOptions.add(new Item.ItemOption(50, Util.nextInt(10, 50)));
                                        sontinh.itemOptions.add(new Item.ItemOption(77, Util.nextInt(10, 150)));
                                        sontinh.itemOptions.add(new Item.ItemOption(103, Util.nextInt(10, 50)));
                                        if (Util.isTrue(199, 200)) {
                                            sontinh.itemOptions.add(new Item.ItemOption(93, Util.nextInt(1, 15)));
                                        }
                                        InventoryServiceNew.gI().addItemBag(player, sontinh);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        player.diemsontinh -= 50;
                                        Service.getInstance().sendThongBao(player, "\n|7|Chúc mừng bạn nhận được cải trang thành sơn tinh");
                                        break;
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Không đủ 50 điểm");
                                    }
                                } else {
                                    Service.getInstance().sendThongBao(player, "Sự kiện đã kết thúc");
                                }
                                break;
                            case 2:
                                if (Manager.SUKIEN == 2) {
                                    if (player.diemthuytinh >= 50) {
                                        Item thuytinh = ItemService.gI().createItemSetKichHoat(422, 1);
                                        thuytinh.itemOptions.add(new Item.ItemOption(50, Util.nextInt(10, 50)));
                                        thuytinh.itemOptions.add(new Item.ItemOption(77, Util.nextInt(10, 50)));
                                        thuytinh.itemOptions.add(new Item.ItemOption(103, Util.nextInt(10, 150)));
                                        if (Util.isTrue(199, 200)) {
                                            thuytinh.itemOptions.add(new Item.ItemOption(93, Util.nextInt(1, 15)));
                                        }
                                        InventoryServiceNew.gI().addItemBag(player, thuytinh);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        player.diemthuytinh -= 50;
                                        Service.getInstance().sendThongBao(player, "\n|5|Chúc mừng bạn nhận được cải trang thành thủy tinh");
                                        break;
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Không đủ 50 điểm");
                                    }
                                } else {
                                    Service.getInstance().sendThongBao(player, "Sự kiện đã kết thúc");
                                }
                                break;
                            case 3:
                                if (Manager.SUKIEN == 2) {
                                    this.createOtherMenu(player, ConstNpc.MENUSUKIENGIOTO, "Con muốn đổi dưa hấu hả", "1 quả 10 thỏi vàng", "5 quả 70 thỏi vàng", "10 quả 200 thỏi vàng", "15 quả 350 thỏi vàng", "20 quả 500 thỏi vàng", "25 quả 700 thỏi vàng", "Đóng");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Sự kiện đã kết thúc");
                                }
                                break;
                            case 4:
                                if (Manager.SUKIEN == 2) {
                                    Service.getInstance().sendThongBaoOK(player, "Bạn đã tiêu diệt số sơn tinh là :" + player.diemsontinh + "\nBạn đã tiêu diệt số thủy tinh là :" + player.diemthuytinh);
                                } else {
                                    Service.getInstance().sendThongBao(player, "Sự kiện đã kết thúc");
                                }
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENUSUKIENGIOTO) {
                        if (Manager.SUKIEN == 2) {
                            switch (select) {
                                case 0:
                                    if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                                        Item duahau = InventoryServiceNew.gI().findduahaugioto(player);
                                        if (duahau != null && duahau.quantity >= 1) {
                                            Item thoivang = ItemService.gI().createNewItem((short) 457, 10);

                                            // - Số item sự kiện có trong rương
                                            InventoryServiceNew.gI().subQuantityItemsBag(player, duahau, 1);

                                            thoivang.itemOptions.add(new Item.ItemOption(73, 0));
                                            InventoryServiceNew.gI().addItemBag(player, thoivang);
                                            InventoryServiceNew.gI().sendItemBags(player);
                                            Service.getInstance().sendThongBao(player, "Đổi thành công");
                                        } else {
                                            Service.getInstance().sendThongBao(player, "Không đủ dưa");
                                        }
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Hành trang đầy.");
                                    }
                                    break;
                                case 1:
                                    if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                                        Item duahau = InventoryServiceNew.gI().findduahaugioto(player);
                                        if (duahau != null && duahau.quantity >= 5) {
                                            Item thoivang = ItemService.gI().createNewItem((short) 457, 70);

                                            // - Số item sự kiện có trong rương
                                            InventoryServiceNew.gI().subQuantityItemsBag(player, duahau, 5);

                                            thoivang.itemOptions.add(new Item.ItemOption(73, 0));
                                            InventoryServiceNew.gI().addItemBag(player, thoivang);
                                            InventoryServiceNew.gI().sendItemBags(player);
                                            Service.getInstance().sendThongBao(player, "Đổi thành công");
                                        } else {
                                            Service.getInstance().sendThongBao(player, "Không đủ dưa");
                                        }
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Hành trang đầy.");
                                    }
                                    break;
                                case 2:
                                    if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                                        Item duahau = InventoryServiceNew.gI().findduahaugioto(player);
                                        if (duahau != null && duahau.quantity >= 10) {
                                            Item thoivang = ItemService.gI().createNewItem((short) 457, 200);

                                            // - Số item sự kiện có trong rương
                                            InventoryServiceNew.gI().subQuantityItemsBag(player, duahau, 10);

                                            thoivang.itemOptions.add(new Item.ItemOption(73, 0));
                                            InventoryServiceNew.gI().addItemBag(player, thoivang);
                                            InventoryServiceNew.gI().sendItemBags(player);
                                            Service.getInstance().sendThongBao(player, "Đổi thành công");
                                        } else {
                                            Service.getInstance().sendThongBao(player, "Không đủ dưa");
                                        }
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Hành trang đầy.");
                                    }
                                    break;
                                case 3:
                                    if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                                        Item duahau = InventoryServiceNew.gI().findduahaugioto(player);
                                        if (duahau != null && duahau.quantity >= 15) {
                                            Item thoivang = ItemService.gI().createNewItem((short) 457, 350);

                                            // - Số item sự kiện có trong rương
                                            InventoryServiceNew.gI().subQuantityItemsBag(player, duahau, 15);

                                            thoivang.itemOptions.add(new Item.ItemOption(73, 0));
                                            InventoryServiceNew.gI().addItemBag(player, thoivang);
                                            InventoryServiceNew.gI().sendItemBags(player);
                                            Service.getInstance().sendThongBao(player, "Đổi thành công");
                                        } else {
                                            Service.getInstance().sendThongBao(player, "Không đủ dưa");
                                        }
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Hành trang đầy.");
                                    }
                                    break;
                                case 4:
                                    if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                                        Item duahau = InventoryServiceNew.gI().findduahaugioto(player);
                                        if (duahau != null && duahau.quantity >= 20) {
                                            Item thoivang = ItemService.gI().createNewItem((short) 457, 500);

                                            // - Số item sự kiện có trong rương
                                            InventoryServiceNew.gI().subQuantityItemsBag(player, duahau, 20);

                                            thoivang.itemOptions.add(new Item.ItemOption(73, 0));
                                            InventoryServiceNew.gI().addItemBag(player, thoivang);
                                            InventoryServiceNew.gI().sendItemBags(player);
                                            Service.getInstance().sendThongBao(player, "Đổi thành công");
                                        } else {
                                            Service.getInstance().sendThongBao(player, "Không đủ dưa");
                                        }
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Hành trang đầy.");
                                    }
                                    break;
                                case 5:
                                    if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                                        Item duahau = InventoryServiceNew.gI().findduahaugioto(player);
                                        if (duahau != null && duahau.quantity >= 25) {
                                            Item thoivang = ItemService.gI().createNewItem((short) 457, 700);

                                            // - Số item sự kiện có trong rương
                                            InventoryServiceNew.gI().subQuantityItemsBag(player, duahau, 25);

                                            thoivang.itemOptions.add(new Item.ItemOption(73, 0));
                                            InventoryServiceNew.gI().addItemBag(player, thoivang);
                                            InventoryServiceNew.gI().sendItemBags(player);
                                            Service.getInstance().sendThongBao(player, "Đổi thành công");
                                        } else {
                                            Service.getInstance().sendThongBao(player, "Không đủ dưa");
                                        }
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Hành trang đầy.");
                                    }
                                    break;
                            }
                        } else {
                            Service.getInstance().sendThongBaoOK(player, "Sự kiện đã kết thúc");
                        }
                    }
                }
            }
        };
    }

    public static Npc boMong(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 47 || this.mapId == 84) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Xin chào, cậu muốn tôi giúp gì?", "Nhiệm vụ\nhàng ngày", "Nhận ngọc miễn phí", "Từ chối");
                    }
//                    if (this.mapId == 47) {
//                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
//                                "Xin chào, cậu muốn tôi giúp gì?", "Từ chối");
//                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 47 || this.mapId == 84) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    if (player.playerTask.sideTask.template != null) {
                                        String npcSay = "Nhiệm vụ hiện tại: " + player.playerTask.sideTask.getName() + " ("
                                                + player.playerTask.sideTask.getLevel() + ")"
                                                + "\nHiện tại đã hoàn thành: " + player.playerTask.sideTask.count + "/"
                                                + player.playerTask.sideTask.maxCount + " ("
                                                + player.playerTask.sideTask.getPercentProcess() + "%)\nSố nhiệm vụ còn lại trong ngày: "
                                                + player.playerTask.sideTask.leftTask + "/" + ConstTask.MAX_SIDE_TASK;
                                        this.createOtherMenu(player, ConstNpc.MENU_OPTION_PAY_SIDE_TASK,
                                                npcSay, "Trả nhiệm\nvụ", "Hủy nhiệm\nvụ");
                                    } else {
                                        this.createOtherMenu(player, ConstNpc.MENU_OPTION_LEVEL_SIDE_TASK,
                                                "Tôi có vài nhiệm vụ theo cấp bậc, "
                                                        + "sức cậu có thể làm được cái nào?",
                                                "Dễ", "Bình thường", "Khó", "Siêu khó", "Địa ngục", "Từ chối");
                                    }
                                    break;
                                case 1:
                                    List<Archivement> list = player.getArchivement().getList();
                                    try {
                                        Message m = new Message(-76);
                                        DataOutputStream ds = m.writer();
                                        ds.writeByte(0);
                                        ds.writeByte(list.size());

                                        for (Archivement a : list) {
                                            ds.writeUTF(a.getInfo1());
                                            ds.writeUTF(a.getInfo2());
                                            ds.writeShort(a.getMoney());
                                            ds.writeBoolean(a.isFinish());
                                            ds.writeBoolean(a.isRecieve());
                                        }

                                        ds.flush();
                                        player.sendMessage(m);
                                        m.cleanup();

                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_LEVEL_SIDE_TASK) {
                            switch (select) {
                                case 0:
                                case 1:
                                case 2:
                                case 3:
                                case 4:
                                    TaskService.gI().changeSideTask(player, (byte) select);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_PAY_SIDE_TASK) {
                            switch (select) {
                                case 0:
                                    TaskService.gI().paySideTask(player);
                                    break;
                                case 1:
                                    TaskService.gI().removeSideTask(player);
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc karin(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        if (this.mapId == 46) {
                            if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                                this.createOtherMenu(player, ConstNpc.BASE_MENU, "Con hãy bay theo cây Gậy Như Ý trên đỉnh tháp để đến Thần Điện gặp Thượng Đế\nCon rất xứng đáng để làm đệ tự của ông ấy",
                                        "Cửa hàng\nĐậu Thần", "Đóng");
                            }
                        }
                    } else if (this.mapId == 104) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Kính chào Ngài Linh thú sư!", "Cửa hàng", "Đóng");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 46) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {
                                ShopServiceNew.gI().opendShop(player, "KARIN", false);
                            }
                        }
                    } else if (this.mapId == 104) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {
                                ShopServiceNew.gI().opendShop(player, "BUNMA_LINHTHU", true);
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc vados(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Ta Vừa Hắc Mắp Xêm Được Tóp Của Toàn Server\b|7|Người Muốn Xem Tóp Gì?",
                            "Tóp Sức Mạnh", "Tóp Pvp", "Tóp Ngũ Hành Sơn", "Đóng");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (this.mapId) {
                        case 5:
                            switch (player.iDMark.getIndexMenu()) {
                                case ConstNpc.BASE_MENU:
                                    if (select == 0) {
                                        Service.getInstance().sendThongBaoOK(player, TopService.getTopPower());
                                        break;
                                    }
                                    if (select == 1) {
                                        Service.getInstance().sendThongBaoOK(player, TopService.getTopPvp());
                                        break;
                                    }
                                    if (select == 2) {
                                        Service.getInstance().sendThongBaoOK(player, TopService.getTopNHS());
                                        break;
                                    }

                                    break;
                            }
                            break;
                    }
                }
            }
        };
    }

    public static Npc gokuSSJ_1(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 80) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Xin chào, tôi có thể giúp gì cho cậu?", "Tới hành tinh\nYardart", "Từ chối");
                    } else if (this.mapId == 131) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Xin chào, tôi có thể giúp gì cho cậu?", "Quay về", "Từ chối");
                    } else {
                        super.openBaseMenu(player);
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (player.iDMark.getIndexMenu()) {
                        case ConstNpc.BASE_MENU:
                            if (this.mapId == 131) {
                                if (select == 0) {
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 80, -1, 870);
                                }
                            }
                            break;
                    }
                }
            }
        };
    }

    public static Npc gokuSSJ_2(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    try {
                        Item biKiep = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 590);
                        if (biKiep != null) {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU, "Bạn đang có " + biKiep.quantity + " bí kiếp.\n"
                                    + "Hãy kiếm đủ 10000 bí kiếp tôi sẽ dạy bạn cách dịch chuyển tức thời của người Yardart", "Học dịch\nchuyển", "Đóng");
                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();

                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    try {
                        Item biKiep = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 590);
                        if (biKiep != null) {
                            if (biKiep.quantity >= 10000 && InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                                Item yardart = ItemService.gI().createNewItem((short) (player.gender + 592));
                                yardart.itemOptions.add(new Item.ItemOption(47, 400));
                                yardart.itemOptions.add(new Item.ItemOption(108, 10));
                                InventoryServiceNew.gI().addItemBag(player, yardart);
                                InventoryServiceNew.gI().subQuantityItemsBag(player, biKiep, 10000);
                                InventoryServiceNew.gI().sendItemBags(player);
                                Service.getInstance().sendThongBao(player, "Bạn vừa nhận được trang phục tộc Yardart");
                            }
                        }
                    } catch (Exception ex) {

                    }
                }
            }
        };
    }

    public static Npc createNPC(int mapId, int status, int cx, int cy, int tempId) {
        int avatar = Manager.NPC_TEMPLATES.get(tempId).avatar;
        try {
            switch (tempId) {
                case ConstNpc.POTAGE:
                    return poTaGe(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.QUY_LAO_KAME:
                    return quyLaoKame(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.MR_POPO:
                    return popo(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.GIUMA_DAU_BO:
                    return mavuong(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.TRUONG_LAO_GURU:
                    return truongLaoGuru(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.VUA_VEGETA:
                    return vuaVegeta(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.ONG_GOHAN:
                case ConstNpc.ONG_MOORI:
                case ConstNpc.ONG_PARAGUS:
                    return ongGohan_ongMoori_ongParagus(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.BUNMA:
                    return bulmaQK(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.DENDE:
                    return dende(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.APPULE:
                    return appule(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.DR_DRIEF:
                    return drDrief(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.CARGO:
                    return cargo(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.CUI:
                    return cui(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.SANTA:
                    return santa(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.URON:
                    return uron(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.BA_HAT_MIT:
                    return baHatMit(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.GHI_DANH:
                    return ghidanh(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.RUONG_DO:
                    return ruongDo(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.DAU_THAN:
                    return dauThan(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.CALICK:
                    return calick(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.JACO:
                    return jaco(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.THUONG_DE:
                    return thuongDe(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.VADOS:
                    return vados(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.THAN_VU_TRU:
                    return thanVuTru(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.KIBIT:
                    return kibit(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.OSIN:
                    return osin(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.LY_TIEU_NUONG:
                    return npclytieunuong54(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.LINH_CANH:
                    return linhCanh(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.QUA_TRUNG:
                    return quaTrung(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.DUA_HAU:
                    return duahau(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.QUOC_VUONG:
                    return quocVuong(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.BUNMA_TL:
                    return bulmaTL(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.RONG_OMEGA:
                    return rongOmega(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.BUNMA8_3:
                    return bunma8_3(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.RONG_1S:
                case ConstNpc.RONG_2S:
                case ConstNpc.RONG_3S:
                case ConstNpc.RONG_4S:
                case ConstNpc.RONG_5S:
                case ConstNpc.RONG_6S:
                case ConstNpc.RONG_7S:
                    return rong1_to_7s(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.CHOPPER:
                    return chopper(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.FRANKY:
                    return franky(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.NAMI:
                    return nami(mapId, status, cx, cy, tempId, avatar);
//                case ConstNpc.TORI_BOT:
//                    return toribot(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.BILL:
                    return bill(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.WHIS:
                    return whis(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.BO_MONG:
                    return boMong(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.NOI_BANH:
                    return noibanh(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.HUNG_VUONG:
                    return hungvuong(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.CUA_HANG_KY_GUI:
                    return kyGui(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.THAN_MEO_KARIN:
                    return karin(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.GOKU_SSJ:
                    return gokuSSJ_1(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.GOKU_SSJ_:
                    return gokuSSJ_2(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.DUONG_TANG:
                    return duongtank(mapId, status, cx, cy, tempId, avatar);
                default:
                    return new Npc(mapId, status, cx, cy, tempId, avatar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                super.openBaseMenu(player);
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
//                                ShopService.gI().openShopNormal(player, this, ConstNpc.SHOP_BUNMA_TL_0, 0, player.gender);
                            }
                        }
                    };
            }
        } catch (Exception e) {
            Logger.logException(NpcFactory.class, e, "Lỗi load npc");
            return null;
        }
    }

    public static Npc mavuong(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 153) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Xin chào, tôi có thể giúp gì cho cậu?", "Tây thánh địa", "Từ chối");
                    } else if (this.mapId == 156) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Người muốn trở về?", "Quay về", "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 153) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {
                                //đến tay thanh dia
                                ChangeMapService.gI().changeMapBySpaceShip(player, 156, -1, 360);
                            }
                        }
                    } else if (this.mapId == 156) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                //về lanh dia bang hoi
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 153, -1, 432);
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    //BTHeo-mark
    public static void createNpcRongThieng() {
        Npc npc = new Npc(-1, -1, -1, -1, ConstNpc.RONG_THIENG, -1) {
            @Override
            public void confirmMenu(Player player, int select) {
                switch (player.iDMark.getIndexMenu()) {
                    case ConstNpc.IGNORE_MENU:

                        break;
                    case ConstNpc.SHENRON_CONFIRM:
                        if (select == 0) {
                            SummonDragon.gI().confirmWish();
                        } else if (select == 1) {
                            SummonDragon.gI().reOpenShenronWishes(player);
                        }
                        break;
                    case ConstNpc.SHENRON_1_1:
                        if (player.iDMark.getIndexMenu() == ConstNpc.SHENRON_1_1 && select == SummonDragon.SHENRON_1_STAR_WISHES_1.length - 1) {
                            NpcService.gI().createMenuRongThieng(player, ConstNpc.SHENRON_1_2, SummonDragon.SHENRON_SAY, SummonDragon.SHENRON_1_STAR_WISHES_2);
                            break;
                        }
                    case ConstNpc.SHENRON_1_2:
                        if (player.iDMark.getIndexMenu() == ConstNpc.SHENRON_1_2 && select == SummonDragon.SHENRON_1_STAR_WISHES_2.length - 1) {
                            NpcService.gI().createMenuRongThieng(player, ConstNpc.SHENRON_1_1, SummonDragon.SHENRON_SAY, SummonDragon.SHENRON_1_STAR_WISHES_1);
                            break;
                        }
                    default:
                        SummonDragon.gI().showConfirmShenron(player, player.iDMark.getIndexMenu(), (byte) select);
                        break;
                }
            }
        };
    }

    public static void createNpcConMeo() {
        Npc npc = new Npc(-1, -1, -1, -1, ConstNpc.CON_MEO, 351) {
            @Override
            public void confirmMenu(Player player, int select) throws Exception {
                switch (player.iDMark.getIndexMenu()) {
                    case ConstNpc.IGNORE_MENU:

                        break;
                    case ConstNpc.MAKE_MATCH_PVP:
                        if (player.getSession().actived) {
                            if (Maintenance.isRuning) {
                                break;
                            }
                            PVPService.gI().sendInvitePVP(player, (byte) select);
                            break;
                        } else {
                            Service.getInstance().sendThongBao(player, "|5|VUI LÒNG KÍCH HOẠT TÀI KHOẢN TẠI\n|7|NROKURO.COM\n|5|ĐỂ MỞ KHÓA TÍNH NĂNG");
                            break;
                        }
                    case ConstNpc.MAKE_FRIEND:
                        if (select == 0) {
                            Object playerId = PLAYERID_OBJECT.get(player.id);
                            if (playerId != null) {
                                FriendAndEnemyService.gI().acceptMakeFriend(player,
                                        Integer.parseInt(String.valueOf(playerId)));
                            }
                        }
                        break;
                    case ConstNpc.REVENGE:
                        if (select == 0) {
                            PVPService.gI().acceptRevenge(player);
                        }
                        break;
                    case ConstNpc.TUTORIAL_SUMMON_DRAGON:
                        if (select == 0) {
                            NpcService.gI().createTutorial(player, -1, SummonDragon.SUMMON_SHENRON_TUTORIAL);
                        }
                        break;
                    case ConstNpc.SUMMON_SHENRON:
                        if (select == 0) {
                            NpcService.gI().createTutorial(player, -1, SummonDragon.SUMMON_SHENRON_TUTORIAL);
                        } else if (select == 1) {
                            SummonDragon.gI().summonShenron(player);
                        }
                        break;
                    case ConstNpc.MENU_OPTION_USE_ITEM1105:
                        if (select == 0) {
                            IntrinsicService.gI().sattd(player);
                        } else if (select == 1) {
                            IntrinsicService.gI().satnm(player);
                        } else if (select == 2) {
                            IntrinsicService.gI().setxd(player);
                        }
                        break;
                    case ConstNpc.MENU_OPTION_USE_ITEM2000:
                    case ConstNpc.MENU_OPTION_USE_ITEM2001:
                    case ConstNpc.MENU_OPTION_USE_ITEM2002:
                        try {
                            ItemService.gI().OpenSKH(player, player.iDMark.getIndexMenu(), select);
                        } catch (Exception e) {
                            Logger.error("Lỗi mở hộp quà");
                        }
                        break;
                    case ConstNpc.MENU_OPTION_USE_ITEM2003:
                    case ConstNpc.MENU_OPTION_USE_ITEM2004:
                    case ConstNpc.MENU_OPTION_USE_ITEM2005:
                        try {
                            ItemService.gI().OpenDHD(player, player.iDMark.getIndexMenu(), select);
                        } catch (Exception e) {
                            Logger.error("Lỗi mở hộp quà");
                        }
                        break;
                    case ConstNpc.MENU_OPTION_USE_ITEM736:
                        try {
                            ItemService.gI().OpenDHD(player, player.iDMark.getIndexMenu(), select);
                        } catch (Exception e) {
                            Logger.error("Lỗi mở hộp quà");
                        }
                        break;
                    case ConstNpc.INTRINSIC:
                        if (select == 0) {
                            IntrinsicService.gI().showAllIntrinsic(player);
                        } else if (select == 1) {
                            IntrinsicService.gI().showConfirmOpen(player);
                        } else if (select == 2) {
                            IntrinsicService.gI().showConfirmOpenVip(player);
                        }
                        break;
                    case ConstNpc.CONFIRM_OPEN_INTRINSIC:
                        if (select == 0) {
                            IntrinsicService.gI().open(player);
                        }
                        break;
                    case ConstNpc.CONFIRM_OPEN_INTRINSIC_VIP:
                        if (select == 0) {
                            IntrinsicService.gI().openVip(player);
                        }
                        break;
                    case ConstNpc.CONFIRM_LEAVE_CLAN:
                        if (select == 0) {
                            ClanService.gI().leaveClan(player);
                        }
                        break;
                    case ConstNpc.CONFIRM_NHUONG_PC:
                        if (select == 0) {
                            ClanService.gI().phongPc(player, (int) PLAYERID_OBJECT.get(player.id));
                        }
                        break;
                    case ConstNpc.BAN_PLAYER:
                        if (select == 0) {
                            PlayerService.gI().banPlayer((Player) PLAYERID_OBJECT.get(player.id));
                            Service.getInstance().sendThongBao(player, "Ban người chơi " + ((Player) PLAYERID_OBJECT.get(player.id)).name + " thành công");
                        }
                        break;

                    case ConstNpc.BUFF_PET:
                        if (select == 0) {
                            Player pl = (Player) PLAYERID_OBJECT.get(player.id);
                            if (pl.pet == null) {
                                PetService.gI().createNormalPet(pl);
                                Service.getInstance().sendThongBao(player, "Phát đệ tử cho " + ((Player) PLAYERID_OBJECT.get(player.id)).name + " thành công");
                            }
                        }
                        break;
                    case ConstNpc.UP_TOP_ITEM:
                        break;
                    case ConstNpc.MENU_ADMIN:
                        switch (select) {
                            case 0:
                                if (player.isAdmin()) {
                                    System.out.println(player.name);
                                    Maintenance.gI().start(15);
                                    System.out.println(player.name);
                                }
                                break;
                            case 1:
                                Input.gI().createFormFindPlayer(player);
                                break;
//                            case 6:
//                                PlayerService.gI().changeAndSendTypePK(player, ConstPlayer.PK_PVP);
//                                ChangeMapService.gI().changeMapInYard(player, 112, -1, 365);
//                                new BossBahatmit(player.zone,1, 365);
//                                if(player.pet != null) {
//                                    player.pet.changeStatus(Pet.GOHOME);
//                                }
//                                break;
                            case 2:
                                Input.gI().createFormConfigEvent(player);
                                break;
                            case 3:
                                Input.gI().createFormConfigExp(player);
                                break;
                            case 4:
                                Input.gI().createFormSenditem1(player);
                                break;
                            case 5:
                                Input.gI().createFormActiveAccount(player);
                                break;
                        }
                        break;
                    case ConstNpc.menutd:
                        switch (select) {
                            case 0:
                                try {
                                    ItemService.gI().settaiyoken(player);
                                } catch (Exception e) {
                                }
                                break;
                            case 1:
                                try {
                                    ItemService.gI().setgenki(player);
                                } catch (Exception e) {
                                }
                                break;
                            case 2:
                                try {
                                    ItemService.gI().setkamejoko(player);
                                } catch (Exception e) {
                                }
                                break;
                        }
                        break;

                    case ConstNpc.menunm:
                        switch (select) {
                            case 0:
                                try {
                                    ItemService.gI().setgodki(player);
                                } catch (Exception e) {
                                }
                                break;
                            case 1:
                                try {
                                    ItemService.gI().setgoddam(player);
                                } catch (Exception e) {
                                }
                                break;
                            case 2:
                                try {
                                    ItemService.gI().setsummon(player);
                                } catch (Exception e) {
                                }
                                break;
                        }
                        break;

                    case ConstNpc.menuxd:
                        switch (select) {
                            case 0:
                                try {
                                    ItemService.gI().setgodgalick(player);
                                } catch (Exception e) {
                                }
                                break;
                            case 1:
                                try {
                                    ItemService.gI().setmonkey(player);
                                } catch (Exception e) {
                                }
                                break;
                            case 2:
                                try {
                                    ItemService.gI().setgodhp(player);
                                } catch (Exception e) {
                                }
                                break;
                        }
                        break;

                    case ConstNpc.CONFIRM_DISSOLUTION_CLAN:
                        switch (select) {
                            case 0:
                                Clan clan = player.clan;
                                clan.deleteDB(clan.id);
                                Manager.CLANS.remove(clan);
                                player.clan = null;
                                player.clanMember = null;
                                ClanService.gI().sendMyClan(player);
                                ClanService.gI().sendClanId(player);
                                Service.getInstance().sendThongBao(player, "Đã giải tán bang hội.");
                                break;
                        }
                        break;
                    case ConstNpc.CONFIRM_ACTIVE:
                        switch (select) {
                            case 0:
                                if (player.getSession().goldBar >= 20) {
                                    player.getSession().actived = true;
                                    if (PlayerDAO.subGoldBar(player, 20)) {
                                        Service.getInstance().sendThongBao(player, "Đã mở thành viên thành công!");
                                        break;
                                    } else {
                                        this.npcChat(player, "Lỗi vui lòng báo admin...");
                                    }
                                }
//                                Service.getInstance().sendThongBao(player, "Bạn không có vàng\n Vui lòng NROGOD.COM để nạp thỏi vàng");
                                break;
                        }
                        break;
                    case ConstNpc.CONFIRM_REMOVE_ALL_ITEM_LUCKY_ROUND:
                        if (select == 0) {
                            for (int i = 0; i < player.inventory.itemsBoxCrackBall.size(); i++) {
                                player.inventory.itemsBoxCrackBall.set(i, ItemService.gI().createItemNull());
                            }
                            player.inventory.itemsBoxCrackBall.clear();
                            Service.getInstance().sendThongBao(player, "Đã xóa hết vật phẩm trong rương");
                        }
                        break;
                    case ConstNpc.MENU_FIND_PLAYER:
                        Player p = (Player) PLAYERID_OBJECT.get(player.id);
                        if (p != null) {
                            switch (select) {
                                case 0:
                                    if (p.zone != null) {
                                        ChangeMapService.gI().changeMapYardrat(player, p.zone, p.location.x, p.location.y);
                                    }
                                    break;
                                case 1:
                                    if (p.zone != null) {
                                        ChangeMapService.gI().changeMap(p, player.zone, player.location.x, player.location.y);
                                    }
                                    break;
                                case 2:
                                    Input.gI().createFormChangeName(player, p);
                                    break;
                                case 3:
                                    String[] selects = new String[]{"Đồng ý", "Hủy"};
                                    NpcService.gI().createMenuConMeo(player, ConstNpc.BAN_PLAYER, -1,
                                            "Bạn có chắc chắn muốn ban " + p.name, selects, p);
                                    break;
                                case 4:
                                    Service.getInstance().sendThongBao(player, "Kik người chơi " + p.name + " thành công");
                                    Client.gI().getPlayers().remove(p);
                                    Client.gI().kickSession(p.getSession());
                                    break;
                            }
                        }
                        break;
                    case ConstNpc.MENUSUKIEN8_3:
                        if (Manager.SUKIEN == 1) {
                            if (player.diemtanghoa8_3 >= 99) {
                                switch (select) {
                                    case 0:
                                        player.itemTime.lastsd20pt8_3 = System.currentTimeMillis();
                                        player.itemTime.sd20pt8_3 = true;
                                        Service.getInstance().point(player);
                                        ItemTimeService.gI().sendAllItemTime(player);
                                        player.diemtanghoa8_3 -= 99;
                                        break;
                                    case 1:
                                        player.itemTime.lasthp20pt8_3 = System.currentTimeMillis();
                                        player.itemTime.hp20pt8_3 = true;
                                        Service.getInstance().point(player);
                                        ItemTimeService.gI().sendAllItemTime(player);
                                        player.diemtanghoa8_3 -= 99;
                                        break;
                                    case 2:
                                        player.itemTime.lastki20pt8_3 = System.currentTimeMillis();
                                        player.itemTime.ki20pt8_3 = true;
                                        Service.getInstance().point(player);
                                        ItemTimeService.gI().sendAllItemTime(player);
                                        player.diemtanghoa8_3 -= 99;
                                        break;
                                    case 3:
                                        Item petbunma = ItemService.gI().createItemSetKichHoat(1992, 1);
                                        petbunma.itemOptions.add(new Item.ItemOption(50, Util.nextInt(10, 50)));
                                        petbunma.itemOptions.add(new Item.ItemOption(77, Util.nextInt(10, 50)));
                                        petbunma.itemOptions.add(new Item.ItemOption(103, Util.nextInt(10, 50)));
                                        petbunma.itemOptions.add(new Item.ItemOption(95, Util.nextInt(5, 10)));
                                        petbunma.itemOptions.add(new Item.ItemOption(96, Util.nextInt(5, 10)));
                                        if (Util.isTrue(199, 200)) {
                                            petbunma.itemOptions.add(new Item.ItemOption(93, Util.nextInt(1, 15)));
                                        }
                                        InventoryServiceNew.gI().addItemBag(player, petbunma);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        player.diemtanghoa8_3 -= 99;
                                        Service.getInstance().sendThongBao(player, "Chúc mừng bạn nhận được " + petbunma.template.name);
                                        break;
                                }
                            } else {
                                Service.getInstance().sendThongBao(player, "Có đủ 99 bông hoa đâu mà đòi đổi quà chời");
                            }
                        }
                        break;
                    case ConstNpc.RUT_VANG:
                        try {
                            if (select == 0) {
                                NapVangService.ChonGiaTien(10, player);
                            } else if (select == 1) {
                                NapVangService.ChonGiaTien(20, player);
                            } else if (select == 2) {
                                NapVangService.ChonGiaTien(50, player);
                            } else if (select == 3) {
                                NapVangService.ChonGiaTien(100, player);
                            } else if (select == 4) {
                                NapVangService.ChonGiaTien(500, player);

                            } else {

                                break;
                            }
                            break;
                        } catch (Exception e) {
                            break;
                        }
                    case ConstNpc.RUONG_GO:
                        int size = player.textRuongGo.size();
                        if (size > 0) {
                            String menuselect = "OK [" + (size - 1) + "]";
                            if (size == 1) {
                                menuselect = "OK";
                            }
                            NpcService.gI().createMenuConMeo(player, ConstNpc.RUONG_GO, -1, player.textRuongGo.get(size - 1), menuselect);
                            player.textRuongGo.remove(size - 1);
                        }
                        break;
                    case ConstNpc.MENUSUKIENTET:
                        if (Manager.SUKIEN == 0) {
                            switch (select) {
                                case 0:
                                    if (player.inventory.gold >= 300000000L) {
                                        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                                            Item caloc = InventoryServiceNew.gI().findnlsktet4(player);
                                            Item cabaymau = InventoryServiceNew.gI().findnlsktet5(player);
                                            Item cadieuhong = InventoryServiceNew.gI().findnlsktet6(player);
                                            if (caloc != null && cabaymau != null && cadieuhong != null) {
                                                Item xocaxanh = ItemService.gI().createNewItem((short) 1005, 1);

                                                // - Số item sự kiện có trong rương
                                                InventoryServiceNew.gI().subQuantityItemsBag(player, caloc, 99);
                                                InventoryServiceNew.gI().subQuantityItemsBag(player, cabaymau, 99);
                                                InventoryServiceNew.gI().subQuantityItemsBag(player, cadieuhong, 99);

                                                xocaxanh.itemOptions.add(new Item.ItemOption(74, 0));
                                                player.inventory.gold -= 300000000L;
                                                InventoryServiceNew.gI().addItemBag(player, xocaxanh);
                                                InventoryServiceNew.gI().sendItemBags(player);
                                                Service.getInstance().sendThongBao(player, "Đổi thành công");
                                            } else {
                                                Service.getInstance().sendThongBao(player, "Không đủ nguyên liệu");
                                            }
                                        } else {
                                            Service.getInstance().sendThongBao(player, "Hành trang đầy.");
                                        }
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Không đủ 300tr vàng");
                                    }
                                    break;
                                case 1:
                                    if (player.inventory.gold >= 500000000L) {
                                        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                                            Item caloc = InventoryServiceNew.gI().findnlsktet4(player);
                                            Item cabaymau = InventoryServiceNew.gI().findnlsktet5(player);
                                            Item cadieuhong = InventoryServiceNew.gI().findnlsktet6(player);
                                            if (caloc != null && cabaymau != null && cadieuhong != null) {
                                                Item xocavang = ItemService.gI().createNewItem((short) 1006, 1);

                                                // - Số item sự kiện có trong rương
                                                InventoryServiceNew.gI().subQuantityItemsBag(player, caloc, 99);
                                                InventoryServiceNew.gI().subQuantityItemsBag(player, cabaymau, 99);
                                                InventoryServiceNew.gI().subQuantityItemsBag(player, cadieuhong, 99);

                                                xocavang.itemOptions.add(new Item.ItemOption(74, 0));
                                                player.inventory.gold -= 500000000L;
                                                InventoryServiceNew.gI().addItemBag(player, xocavang);
                                                InventoryServiceNew.gI().sendItemBags(player);
                                                Service.getInstance().sendThongBao(player, "Đổi thành công");
                                            } else {
                                                Service.getInstance().sendThongBao(player, "Không đủ nguyên liệu");
                                            }
                                        } else {
                                            Service.getInstance().sendThongBao(player, "Hành trang đầy.");
                                        }
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Không đủ 500tr vàng");
                                    }
                                    break;
                                case 2:
                                    break;

                            }
                        }
                        break;
                    case ConstNpc.MENU_EVENT:
                        switch (select) {
                            case 0:
                                Service.getInstance().sendThongBaoOK(player, "Điểm sự kiện: " + player.inventory.event + " ngon ngon...");
                                break;
                            case 1:
//                                Util.showListTop(player, (byte) 2);
                                break;
                            case 2:
                                Service.getInstance().sendThongBao(player, "Sự kiện đã kết thúc...");
//                                NpcService.gI().createMenuConMeo(player, ConstNpc.MENU_GIAO_BONG, -1, "Người muốn giao bao nhiêu bông...",
//                                        "100 bông", "1000 bông", "10000 bông");
                                break;
                            case 3:
                                Service.getInstance().sendThongBao(player, "Sự kiện đã kết thúc...");
//                                NpcService.gI().createMenuConMeo(player, ConstNpc.CONFIRM_DOI_THUONG_SU_KIEN, -1, "Con có thực sự muốn đổi thưởng?\nPhải giao cho ta 3000 điểm sự kiện đấy... ",
//                                        "Đồng ý", "Từ chối");
                                break;

                        }
                        break;
                    case ConstNpc.MENU_GIAO_BONG:
                        ItemService.gI().giaobong(player, (int) Util.tinhLuyThua(10, select + 2));
                        break;
                    case ConstNpc.CONFIRM_DOI_THUONG_SU_KIEN:
                        if (select == 0) {
                            ItemService.gI().openBoxVip(player);
                        }
                        break;
                    case ConstNpc.CONFIRM_TELE_NAMEC:
                        if (select == 0) {
                            NgocRongNamecService.gI().teleportToNrNamec(player);
                            player.inventory.subGemAndRuby(50);
                            Service.getInstance().sendMoney(player);
                        }
                        break;
                }
            }
        };
    }

}
