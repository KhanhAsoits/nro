package com.bth.services.func;

import com.bth.models.player.Player;
import com.bth.server.Manager;
import com.girlkun.database.GirlkunDB;
import com.bth.consts.ConstNpc;
import com.bth.jdbc.daos.PlayerDAO;
import com.bth.models.item.Item;
import com.bth.models.map.Zone;
import com.bth.models.npc.Npc;
import com.bth.models.npc.NpcManager;
import com.girlkun.network.io.Message;
import com.girlkun.network.session.ISession;
import com.bth.server.Client;
import com.bth.server.ServerNotify;
import com.bth.services.Service;
import com.bth.services.GiftService;
import com.bth.services.InventoryServiceNew;
import com.bth.services.ItemService;
import com.bth.services.NpcService;
import com.girlkun.result.GirlkunResultSet;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Heroes x BTH
 * 
 */
public class Input {

    public static String LOAI_THE;
    public static String MENH_GIA;
    private static final Map<Integer, Object> PLAYER_ID_OBJECT = new HashMap<Integer, Object>();

    public static final int CHANGE_PASSWORD = 500;
    public static final int GIFT_CODE = 501;
    public static final int FIND_PLAYER = 502;
    public static final int CHANGE_NAME = 503;
    public static final int CHOOSE_LEVEL_BDKB = 504;
    public static final int NAP_THE = 505;
    public static final int CHANGE_NAME_BY_ITEM = 506;
    public static final int GIVE_IT = 507;
    public static final int SEND_ITEM_OP = 512;
    public static final int CHOOSE_LEVEL_KHIGA = 517;
    private static final int CONFIG_EVENT = 514;
    private static final int CONFIG_EXP = 515;
    private static final int ACTIVE = 516;
    private static final int CHATSERVER = 518;

    public static final int QUY_DOI_COIN = 519;
    public static final int QUY_DOI_NGOC_XANH = 520;
    public static final int QUY_DOI_NGOC_HONG = 521;
    
    public static final int QUY_DOI_HRZ = 513;

    public static final byte NUMERIC = 0;
    public static final byte ANY = 1;
    public static final byte PASSWORD = 2;

    private static Input intance;

    private Input() {

    }

    public static Input gI() {
        if (intance == null) {
            intance = new Input();
        }
        return intance;
    }

    public void doInput(Player player, Message msg) {
        try {
            String[] text = new String[msg.reader().readByte()];
            for (int i = 0; i < text.length; i++) {
                text[i] = msg.reader().readUTF();
            }
            switch (player.iDMark.getTypeInput()) {
                case GIVE_IT:
                    String name = text[0];
                    int id = Integer.valueOf(text[1]);
                    int q = Integer.valueOf(text[2]);
                    if (Client.gI().getPlayer(name) != null) {
                        Item item = ItemService.gI().createNewItem(((short) id));
                        item.quantity = q;
                        InventoryServiceNew.gI().addItemBag(Client.gI().getPlayer(name), item);
                        InventoryServiceNew.gI().sendItemBags(Client.gI().getPlayer(name));
                        Service.getInstance().sendThongBao(Client.gI().getPlayer(name), "Nhận " + item.template.name + " từ " + player.name);

                    } else {
                        Service.getInstance().sendThongBao(player, "Không online");
                    }
                    break;
case SEND_ITEM_OP:
    if (player.isAdmin()) {
        int idItemBuff = Integer.parseInt(text[1]);
        String[] options = text[2].split(" ");
        int slItemBuff = Integer.parseInt(text[3]);
        Player admin = player;
        Player target = Client.gI().getPlayer(text[0]);
        if (target != null) {
            String txtBuff = "Bạn vừa nhận được: " + target.name + ", hãy kiểm tra hành trang\n";
            Item itemBuffTemplate = ItemService.gI().createNewItem((short) idItemBuff);
            for (String option : options) {
                String[] optionParts = option.split("-");
                int idOptionBuff = Integer.parseInt(optionParts[0]);
                int slOptionBuff = Integer.parseInt(optionParts[1]);
                itemBuffTemplate.itemOptions.add(new Item.ItemOption(idOptionBuff, slOptionBuff));
            }
            itemBuffTemplate.quantity = slItemBuff;
            InventoryServiceNew.gI().addItemBag(target, itemBuffTemplate);
            InventoryServiceNew.gI().sendItemBags(target);
            txtBuff += "x" + slItemBuff + " " + itemBuffTemplate.template.name + "\n";
            Service.getInstance().sendThongBao(target, txtBuff);
        } else {
            Service.getInstance().sendThongBao(admin, "Không tìm thấy cư dân hoặc cư dân.");
        }
    }
    break;
                case CHANGE_PASSWORD:
                    Service.getInstance().changePassword(player, text[0], text[1], text[2]);
                    break;
                case GIFT_CODE:
                    GiftService.gI().giftCode(player, text[0]);
                    break;
                case ACTIVE:
                    String username = text[0];
                    try {
                        GirlkunResultSet rs = GirlkunDB.executeQuery("SELECT * FROM `account` WHERE username = ?", username);
                        if (rs.first()) {
                            GirlkunDB.executeUpdate("update account set active = 1 where username = ?", username);

                            Service.getInstance().sendThongBao(player,"Mở thành viên thành công");
                            rs.dispose();
                        } else {
                            Service.getInstance().sendThongBaoOK(player, "Tên tài khoản không tồn tại");
                            rs.dispose();
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case CHATSERVER:
                    String thongbao = text[0];
                    try {
                        msg = new Message(93);
                        msg.writer().writeUTF(thongbao);
                        Service.getInstance().sendMessAllPlayer(msg);
                        msg.cleanup();
                    } catch (Exception e) {
                    }
                    break;
                case FIND_PLAYER:
                    Player pl = Client.gI().getPlayer(text[0]);
                    if (pl != null) {
                        NpcService.gI().createMenuConMeo(player, ConstNpc.MENU_FIND_PLAYER, 1139, "Quyền Năng Thiên Sứ?",
                                new String[]{"Dịch chuyển\nđến\n" + pl.name, "Triệu hồi\n" + pl.name + "\nđến đây", "Đổi tên\n" + pl.name + "", "Khóa\n" + pl.name + "", "Đăng xuất\n" + pl.name + ""},
                                pl);
                    } else {
                        Service.getInstance().sendThongBao(player, "Cư dân " + pl.name + " hiện tại không online!!");
                    }
                    break;
                case CHANGE_NAME: {
                    Player plChanged = (Player) PLAYER_ID_OBJECT.get((int) player.id);
                    if (plChanged != null) {
                        if (GirlkunDB.executeQuery("select * from player where name = ?", text[0]).next()) {
                            Service.getInstance().sendThongBao(player, "Tên cư dân đã tồn tại!");
                        } else {
                            plChanged.name = text[0];
                            GirlkunDB.executeUpdate("update player set name = ? where id = ?", plChanged.name, plChanged.id);
                            Service.getInstance().player(plChanged);
                            Service.getInstance().Send_Caitrang(plChanged);
                            Service.getInstance().sendFlagBag(plChanged);
                            Zone zone = plChanged.zone;
                            ChangeMapService.gI().changeMap(plChanged, zone, plChanged.location.x, plChanged.location.y);
                            Service.getInstance().sendThongBao(plChanged, "Đại thiên sứ đã đổi tên của bạn thành: " + player.name + "");
                            Service.getInstance().sendThongBao(player, "Quyền năng của ngài đã áp dụng lên cư dân  ");
                        }
                    }
                }
                break;
                case CHANGE_NAME_BY_ITEM: {
                    if (player != null) {
                        if (GirlkunDB.executeQuery("select * from player where name = ?", text[0]).next()) {
                            Service.getInstance().sendThongBao(player, "Tên nhân vật đã tồn tại");
                            createFormChangeNameByItem(player);
                        } else {
                            Item theDoiTen = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 2006);
                            if (theDoiTen == null) {
                                Service.getInstance().sendThongBao(player, "Không tìm thấy thẻ đổi tên");
                            } else {
                                InventoryServiceNew.gI().subQuantityItemsBag(player, theDoiTen, 1);
                                player.name = text[0];
                                GirlkunDB.executeUpdate("update player set name = ? where id = ?", player.name, player.id);
                                Service.getInstance().player(player);
                                Service.getInstance().Send_Caitrang(player);
                                Service.getInstance().sendFlagBag(player);
                                Zone zone = player.zone;
                                ChangeMapService.gI().changeMap(player, zone, player.location.x, player.location.y);
                                Service.getInstance().sendThongBao(player, "Chúc mừng bạn đã có cái tên mới đẹp đẽ hơn tên ban đầu");
                            }
                        }
                    }
                }
                break;
                case CONFIG_EVENT:
                    byte event = Byte.parseByte(text[0]);
                    Manager.SUKIEN = event;
                    break;
                case CONFIG_EXP:
                    byte exp = Byte.parseByte(text[0]);
                    Manager.RATE_EXP_SERVER = exp;
                    break;
                case CHOOSE_LEVEL_BDKB:
                    int level = Integer.parseInt(text[0]);
                    if (level >= 1 && level <= 110) {
                        Npc npc = NpcManager.getByIdAndMap(ConstNpc.QUY_LAO_KAME, player.zone.map.mapId);
                        if (npc != null) {
                            npc.createOtherMenu(player, ConstNpc.MENU_ACCEPT_GO_TO_BDKB,
                                    "Con có chắc chắn muốn tới bản đồ kho báu cấp độ " + level + "?",
                                    new String[]{"Đồng ý", "Từ chối"}, level);
                        }
                    } else {
                        Service.getInstance().sendThongBao(player, "Không thể thực hiện");
                    }
                
                case NAP_THE:
                    //NapThe.SendCard(player, LOAI_THE, MENH_GIA, text[0], text[1]);
                    break;
                case QUY_DOI_COIN:
                    int ratioGold = 1;
                    int coinGold = 10;
                    int goldTrade = Integer.parseInt(text[0]);
                    if (goldTrade <= 0 || goldTrade >= 500) {
                        Service.getInstance().sendThongBao(player, "Quá giới hạn mỗi lần chỉ được 500");
                    } else if (player.session.vnd >= goldTrade * coinGold) {
                        PlayerDAO.subGoldBar(player, goldTrade * coinGold);
                        Item thoiVang = ItemService.gI().createNewItem((short) 457, goldTrade * 1);// x3
                        InventoryServiceNew.gI().addItemBag(player, thoiVang);
                        InventoryServiceNew.gI().sendItemBags(player);
                        Service.getInstance().sendThongBao(player, "bạn nhận được " + goldTrade * ratioGold
                                + " " + thoiVang.template.name);
                    } else {
                        Service.getInstance().sendThongBao(player, "Số Xu của bạn là " + player.session.vnd + " không đủ để quy "
                                + " đổi " + goldTrade + " thỏi vàng " + " " + "bạn cần thêm" + (player.session.vnd - goldTrade));
                    }
                    break;
                    
                    case QUY_DOI_NGOC_XANH:
                    int ratioGem = 1;
                    int coinGem = 1;
                    int GemTrade = Integer.parseInt(text[0]);
                    if (GemTrade <= 0 || GemTrade >= 100000000) {
                        Service.getInstance().sendThongBao(player, "Quá giới hạn mỗi lần chỉ được 100.000.000 Ngọc xanh");
                    } else if (player.session.vnd >= GemTrade * coinGem) {
                        PlayerDAO.subGoldBar(player, GemTrade * coinGem);
                        Item thoiVang = ItemService.gI().createNewItem((short) 77, GemTrade * 10);// x3
                        InventoryServiceNew.gI().addItemBag(player, thoiVang);
                        InventoryServiceNew.gI().sendItemBags(player);
                        Service.getInstance().sendThongBao(player, "bạn nhận được " + GemTrade * ratioGem
                                + " " + thoiVang.template.name);
                    } else {
                        Service.getInstance().sendThongBao(player, "Số Xu của bạn là " + player.session.vnd + " không đủ để quy "
                                + " đổi " + GemTrade + " thỏi vàng " + " " + "bạn cần thêm" + (player.session.vnd - GemTrade));
                    }
                    break;
                    
                    case QUY_DOI_NGOC_HONG:
                    int ratioHRZ = 1;
                    int coinHRZ = 1;
                    int HRZTrade = Integer.parseInt(text[0]);
                    if (HRZTrade <= 0 || HRZTrade >= 100000000) {
                        Service.getInstance().sendThongBao(player, "Quá giới hạn mỗi lần chỉ được 100.000.000 Ngọc Hồng");
                    } else if (player.session.vnd >= HRZTrade * coinHRZ) {
                        PlayerDAO.subGoldBar(player, HRZTrade * coinHRZ);
                        Item thoiVang = ItemService.gI().createNewItem((short) 861, HRZTrade * 10);//
                        InventoryServiceNew.gI().addItemBag(player, thoiVang);
                        InventoryServiceNew.gI().sendItemBags(player);
                        Service.getInstance().sendThongBao(player, "bạn nhận được " + HRZTrade * ratioHRZ
                                + " " + thoiVang.template.name);
                    } else {
                        Service.getInstance().sendThongBao(player, "Số Xu của bạn là " + player.session.vnd + " không đủ để quy "
                                + " đổi " + HRZTrade + " thỏi vàng " + " " + "bạn cần thêm" + (player.session.vnd - HRZTrade));
                    }
                    break;
            }
        } catch (Exception e) {
        }
    }

    public void createForm(Player pl, int typeInput, String title, SubInput... subInputs) {
        pl.iDMark.setTypeInput(typeInput);
        Message msg;
        try {
            msg = new Message(-125);
            msg.writer().writeUTF(title);
            msg.writer().writeByte(subInputs.length);
            for (SubInput si : subInputs) {
                msg.writer().writeUTF(si.name);
                msg.writer().writeByte(si.typeInput);
            }
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void createForm(ISession session, int typeInput, String title, SubInput... subInputs) {
        Message msg;
        try {
            msg = new Message(-125);
            msg.writer().writeUTF(title);
            msg.writer().writeByte(subInputs.length);
            for (SubInput si : subInputs) {
                msg.writer().writeUTF(si.name);
                msg.writer().writeByte(si.typeInput);
            }
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void createFormChangePassword(Player pl) {
        createForm(pl, CHANGE_PASSWORD, "Quên Mật Khẩu", new SubInput("Nhập mật khẩu đã quên", PASSWORD),
                new SubInput("Mật khẩu mới", PASSWORD),
                new SubInput("Nhập lại mật khẩu mới", PASSWORD));
    }

    public void createFormGiveItem(Player pl) {
        createForm(pl, GIVE_IT, "Tặng vật phẩm", new SubInput("Tên", ANY), new SubInput("Id Item", ANY), new SubInput("Số lượng", ANY));
    }

    public void createFormGiftCode(Player pl) {
        createForm(pl, GIFT_CODE, "Gift code ", new SubInput("Gift-code", ANY));
    }

    public void createFormFindPlayer(Player pl) {
        createForm(pl, FIND_PLAYER, "Tìm kiếm người chơi", new SubInput("Tên người chơi", ANY));
    }
    
    public void createFormChooseLevelKhiGa(Player pl) {
        createForm(pl, CHOOSE_LEVEL_KHIGA, "Chọn cấp độ", new SubInput("Cấp độ (1-110)", NUMERIC));

    }
    public void createFormConfigEvent(Player pl) {
        createForm(pl, CONFIG_EVENT, "Bảng Quản Lý Sự Kiện\n" +
                "0 Event tết, 1 Event 8/3, 2 Event 10/3\n" +
                "Sự kiện đang mở hiện tại: " + Manager.SUKIEN, new SubInput("", ANY));
    }

    public void createFormConfigExp(Player pl) {
        createForm(pl, CONFIG_EXP, "Server Exp" + Manager.RATE_EXP_SERVER, new SubInput("", ANY));
    }

    public void createFormActiveAccount(Player pl) {
        createForm(pl, ACTIVE, "Active account", new SubInput("Tên tài khoản đăng nhập", ANY));
    }
    
    public void createFormchatserver(Player player) {
        createForm(player, CHATSERVER, "Chat Server", new SubInput("Nhập nội dung", ANY));
    }

    public void createFormNapThe(Player pl, String loaiThe, String menhGia) {
        LOAI_THE = loaiThe;
        MENH_GIA = menhGia;
        createForm(pl, NAP_THE, "Nạp thẻ", new SubInput("Số Seri", ANY), new SubInput("Mã thẻ", ANY));
    }
    public void createFormSenditem1(Player pl) {
    createForm(pl, SEND_ITEM_OP, "Quyền năng Heroes Z",
            new SubInput("Name", ANY),
            new SubInput("ID Item", NUMERIC),
            new SubInput("ID Option", ANY),
            new SubInput("Quantity", NUMERIC));
}


    public void createFormQDTV(Player pl) {

        createForm(pl, QUY_DOI_COIN, "Quy đổi thỏi vàng, giới hạn đổi không quá 500 Thỏi vàng."
                + "\n10 Xu = 1 Thỏi vàng", new SubInput("Nhập số lượng muốn đổi", NUMERIC));
    }

    public void createFormQDNX(Player pl) {

        createForm(pl, QUY_DOI_NGOC_XANH, "Quy đổi Xu sang Ngọc Xanh."
                + "\n1 Xu = 10 Ngọc Xanh", new SubInput("Nhập số lượng muốn đổi", NUMERIC));
    }
    
    public void createFormQDHN(Player pl) {

        createForm(pl, QUY_DOI_NGOC_HONG, "Quy đổi Xu sang Ngọc Hồng."
                + "\n1 Xu = 10 Ngọc Hồng", new SubInput("Nhập số lượng muốn đổi", NUMERIC));
    }

    public void createFormChangeName(Player pl, Player plChanged) {
        PLAYER_ID_OBJECT.put((int) pl.id, plChanged);
        createForm(pl, CHANGE_NAME, "Đổi tên " + plChanged.name, new SubInput("Tên mới", ANY));
    }

    public void createFormChangeNameByItem(Player pl) {
        createForm(pl, CHANGE_NAME_BY_ITEM, "Đổi tên " + pl.name, new SubInput("Tên mới", ANY));
    }

    public void createFormChooseLevelBDKB(Player pl) {
        createForm(pl, CHOOSE_LEVEL_BDKB, "Chọn cấp độ", new SubInput("Cấp độ (1-110)", NUMERIC));
    }

    public static class SubInput {

        private String name;
        private byte typeInput;

        public SubInput(String name, byte typeInput) {
            this.name = name;
            this.typeInput = typeInput;
        }
    }

}
