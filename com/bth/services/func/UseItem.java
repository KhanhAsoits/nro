package com.bth.services.func;

import com.bth.models.map.NgocRongNamec.NgocRongNamecService;
import com.bth.models.player.Inventory;
import com.bth.models.player.Player;
import com.bth.consts.ConstMap;
import com.bth.models.card.Card;
import com.bth.models.card.RadarCard;
import com.bth.models.card.RadarService;
import com.bth.models.item.Item;
import com.bth.consts.ConstNpc;
import com.bth.consts.ConstPlayer;
import com.bth.models.item.Item.ItemOption;
import com.bth.models.map.Zone;
import com.bth.server.Manager;
import com.bth.services.*;
import com.bth.models.skill.Skill;
import com.bth.utils.TimeUtil;
import com.girlkun.network.io.Message;
import com.bth.utils.SkillUtil;
import com.bth.utils.Util;
import com.bth.server.io.MySession;
import com.bth.utils.Logger;
import lombok.var;

import java.util.Date;
/**
 *
 * @author Heroes x BTH
 * 
 */
public class UseItem {

    private static final int ITEM_BOX_TO_BODY_OR_BAG = 0;
    private static final int ITEM_BAG_TO_BOX = 1;
    private static final int ITEM_BODY_TO_BOX = 3;
    private static final int ITEM_BAG_TO_BODY = 4;
    private static final int ITEM_BODY_TO_BAG = 5;
    private static final int ITEM_BAG_TO_PET_BODY = 6;
    private static final int ITEM_BODY_PET_TO_BAG = 7;

    private static final byte DO_USE_ITEM = 0;
    private static final byte DO_THROW_ITEM = 1;
    private static final byte ACCEPT_THROW_ITEM = 2;
    private static final byte ACCEPT_USE_ITEM = 3;

    public static final int[][][] LIST_ITEM_CLOTHES = {
            // áo , quần , găng ,giày,rada
            //td -> nm -> xd
            {{0, 33, 3, 34, 136, 137, 138, 139, 230, 231, 232, 233, 555}, {6, 35, 9, 36, 140, 141, 142, 143, 242, 243, 244, 245, 556}, {21, 24, 37, 38, 144, 145, 146, 147, 254, 255, 256, 257, 562}, {27, 30, 39, 40, 148, 149, 150, 151, 266, 267, 268, 269, 563}, {12, 57, 58, 59, 184, 185, 186, 187, 278, 279, 280, 281, 561}},
            {{1, 41, 4, 42, 152, 153, 154, 155, 234, 235, 236, 237, 557}, {7, 43, 10, 44, 156, 157, 158, 159, 246, 247, 248, 249, 558}, {22, 46, 25, 45, 160, 161, 162, 163, 258, 259, 260, 261, 564}, {28, 47, 31, 48, 164, 165, 166, 167, 270, 271, 272, 273, 565}, {12, 57, 58, 59, 184, 185, 186, 187, 278, 279, 280, 281, 561}},
            {{2, 49, 5, 50, 168, 169, 170, 171, 238, 239, 240, 241, 559}, {8, 51, 11, 52, 172, 173, 174, 175, 250, 251, 252, 253, 560}, {23, 53, 26, 54, 176, 177, 178, 179, 262, 263, 264, 265, 566}, {29, 55, 32, 56, 180, 181, 182, 183, 274, 275, 276, 277, 567}, {12, 57, 58, 59, 184, 185, 186, 187, 278, 279, 280, 281, 561}}
    };

    private static UseItem instance;

    private UseItem() {

    }

    public static UseItem gI() {
        if (instance == null) {
            instance = new UseItem();
        }
        return instance;
    }

    public void getItem(MySession session, Message msg) {
        Player player = session.player;

        TransactionService.gI().cancelTrade(player);
        try {
            int type = msg.reader().readByte();
            int index = msg.reader().readByte();
            if (index == -1) {
                return;
            }
            switch (type) {
                case ITEM_BOX_TO_BODY_OR_BAG:
                    InventoryServiceNew.gI().itemBoxToBodyOrBag(player, index);
                    TaskService.gI().checkDoneTaskGetItemBox(player);
                    break;
                case ITEM_BAG_TO_BOX:
                    InventoryServiceNew.gI().itemBagToBox(player, index);
                    break;
                case ITEM_BODY_TO_BOX:
                    InventoryServiceNew.gI().itemBodyToBox(player, index);
                    break;
                case ITEM_BAG_TO_BODY:
                    InventoryServiceNew.gI().itemBagToBody(player, index);
                    break;
                case ITEM_BODY_TO_BAG:
                    InventoryServiceNew.gI().itemBodyToBag(player, index);
                    break;
                case ITEM_BAG_TO_PET_BODY:
                    InventoryServiceNew.gI().itemBagToPetBody(player, index);
                    break;
                case ITEM_BODY_PET_TO_BAG:
                    InventoryServiceNew.gI().itemPetBodyToBag(player, index);
                    break;
            }
            player.setClothes.setup();
            if (player.pet != null) {
                player.pet.setClothes.setup();
            }
            player.setClanMember();
            Service.getInstance().point(player);
        } catch (Exception e) {
            Logger.logException(UseItem.class, e);

        }
    }

    public void testItem(Player player, Message _msg) {
        TransactionService.gI().cancelTrade(player);
        Message msg;
        try {
            byte type = _msg.reader().readByte();
            int where = _msg.reader().readByte();
            int index = _msg.reader().readByte();
            System.out.println("type: " + type);
            System.out.println("where: " + where);
            System.out.println("index: " + index);
        } catch (Exception e) {
            Logger.logException(UseItem.class, e);
        }
    }

    public void doItem(Player player, Message _msg) {
        TransactionService.gI().cancelTrade(player);
        Message msg;
        byte type;
        try {
            type = _msg.reader().readByte();
            int where = _msg.reader().readByte();
            int index = _msg.reader().readByte();
//            System.out.println(type + " " + where + " " + index);
            switch (type) {
                case DO_USE_ITEM:
                    if (player != null && player.inventory != null) {
                        if (index != -1) {
                            Item item = player.inventory.itemsBag.get(index);
                            if (item.isNotNullItem()) {
                                if (item.template.type == 7) {
                                    msg = new Message(-43);
                                    msg.writer().writeByte(type);
                                    msg.writer().writeByte(where);
                                    msg.writer().writeByte(index);
                                    msg.writer().writeUTF("Bạn chắc chắn học " + player.inventory.itemsBag.get(index).template.name + "?");
                                    player.sendMessage(msg);
                                } else {
                                    UseItem.gI().useItem(player, item, index);
                                }
                            }
                        } else {
                            this.eatPea(player);
                        }
                    }
                    break;
                case DO_THROW_ITEM:
                    if (!(player.zone.map.mapId == 21 || player.zone.map.mapId == 22 || player.zone.map.mapId == 23)) {
                        Item item = null;
                        if (where == 0) {
                            item = player.inventory.itemsBody.get(index);
                        } else {
                            item = player.inventory.itemsBag.get(index);
                        }
                        msg = new Message(-43);
                        msg.writer().writeByte(type);
                        msg.writer().writeByte(where);
                        msg.writer().writeByte(index);
                        msg.writer().writeUTF("Bạn chắc chắn muốn vứt " + item.template.name + "?");
                        player.sendMessage(msg);
                    } else {
                        Service.getInstance().sendThongBao(player, "Không thể thực hiện");
                    }
                    break;
                case ACCEPT_THROW_ITEM:
                    InventoryServiceNew.gI().throwItem(player, where, index);
                    Service.getInstance().point(player);
                    InventoryServiceNew.gI().sendItemBags(player);
                    break;
                case ACCEPT_USE_ITEM:
                    UseItem.gI().useItem(player, player.inventory.itemsBag.get(index), index);
                    break;
            }
        } catch (Exception e) {
//            Logger.logException(UseItem.class, e);
        }
    }

    private void useItem(Player pl, Item item, int indexBag) {
        if (item.template.strRequire <= pl.nPoint.power) {
            switch (item.template.type) {
                case 7: //sách học, nâng skill
                    learnSkill(pl, item);
                    break;
                case 33:
                    UseCard(pl,item);
                    break;
                case 6: //đậu thần
                    this.eatPea(pl);
                    break;
                case 12: //ngọc rồng các loại
                    controllerCallRongThan(pl, item);
                    break;
                case 23: //thú cưỡi mới
                case 24: //thú cưỡi cũ
                    InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                    break;
                case 11: //item bag
                    InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                    Service.getInstance().sendFlagBag(pl);
                    break;
                case 72: {
                    InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                    Service.getInstance().sendPetFollow(pl, (short) (item.template.iconID - 1));
                    break;
                }
                default:
                    switch (item.template.id) {
                        case 992:
                            pl.type = 1;
                            pl.maxTime = 5;
                            Service.getInstance().Transport(pl);
                            break;
                        case 570:
                            openWoodChest(pl,item);
                            break;
                        case 1991:
                            pl.gdvip++;
                            Service.getInstance().sendThongBaoOK(pl, "Bạn vừa tăng 1 lượt giao dịch vip <3");
                            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                            break;
                        case 361:
                            if (pl.idNRNM != -1) {
                                Service.getInstance().sendThongBao(pl, "Không thể thực hiện");
                                return;
                            }
                            pl.idGo = (short) Util.nextInt(0, 6);
                            NpcService.gI().createMenuConMeo(pl, ConstNpc.CONFIRM_TELE_NAMEC, -1, "1 Sao (" + NgocRongNamecService.gI().getDis(pl, 0, (short) 353) + " m)\n2 Sao (" + NgocRongNamecService.gI().getDis(pl, 1, (short) 354) + " m)\n3 Sao (" + NgocRongNamecService.gI().getDis(pl, 2, (short) 355) + " m)\n4 Sao (" + NgocRongNamecService.gI().getDis(pl, 3, (short) 356) + " m)\n5 Sao (" + NgocRongNamecService.gI().getDis(pl, 4, (short) 357) + " m)\n6 Sao (" + NgocRongNamecService.gI().getDis(pl, 5, (short) 358) + " m)\n7 Sao (" + NgocRongNamecService.gI().getDis(pl, 6, (short) 359) + " m)", "Đến ngay\nViên " + (pl.idGo + 1) + " Sao\n50 ngọc", "Kết thức");
                            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                            InventoryServiceNew.gI().sendItemBags(pl);
                            break;
                        case 457:
                            pl.inventory.gold += 500000000;
                            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                            Service.getInstance().sendMoney(pl);
                            InventoryServiceNew.gI().sendItemBags(pl);
                            break;
                        case 892: //Thần chết cute
                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                            PetService.Pet2(pl, 882, 883, 884);
                            Service.getInstance().point(pl);
                            break;
                        case 893: //Thần chết cute
                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                            PetService.Pet2(pl, 885, 886, 887);
                            Service.getInstance().point(pl);
                            break;
                        case 909: //Thần chết cute
                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                            PetService.Pet2(pl, 897, 898, 899);
                            Service.getInstance().point(pl);
                            break;
                        case 942:
                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                            PetService.Pet2(pl, 966, 967, 968);
                            Service.getInstance().point(pl);
                            break;
                        case 943:
                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                            PetService.Pet2(pl, 969, 970, 971);
                            Service.getInstance().point(pl);
                            break;
                        case 944:
                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                            PetService.Pet2(pl, 972, 973, 974);
                            Service.getInstance().point(pl);
                            break;
                        case 967:
                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                            PetService.Pet2(pl, 1050, 1051, 1052);
                            Service.getInstance().point(pl);
                            break;
                        case 1407: // Con cún vàng
                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                            PetService.Pet2(pl, 663, 664, 665);
                            Service.getInstance().point(pl);
                            break;
                        case 1408: // Cua đỏ
                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                            PetService.Pet2(pl, 1074, 1075, 1076);
                            Service.getInstance().point(pl);
                            break;
                        case 1410: // Bí ma vương
                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                            PetService.Pet2(pl, 1155, 1156, 1157);
                            Service.getInstance().point(pl);
                            break;
                        case 1411: // Mèo đuôi vàng đen
                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                            PetService.Pet2(pl, 1183, 1184, 1185);
                            Service.getInstance().point(pl);
                            break;
                        case 1412: // Mèo đuôi vàng trắng
                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                            PetService.Pet2(pl, 1201, 1202, 1203);
                            Service.getInstance().point(pl);
                            break;
                        case 1413: // Gà 9 cựa
                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                            PetService.Pet2(pl, 1239, 1240, 1241);
                            Service.getInstance().point(pl);
                            break;
                        case 1414: // Ngựa 9 hồng mao
                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                            PetService.Pet2(pl, 1242, 1243, 1244);
                            Service.getInstance().point(pl);
                            break;
                        case 1415: // Voi 9 ngà
                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                            PetService.Pet2(pl, 1245, 1246, 1247);
                            Service.getInstance().point(pl);
                            break;
                        case 1416: // Pet Minions
                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                            PetService.Pet2(pl, 1254, 1255, 1256);
                            Service.getInstance().point(pl);
                            break;
                        case 293:
                            openGoiDau1(pl, item);
                            break;
                        case 294:
                            openGoiDau2(pl, item);
                            break;
                        case 295:
                            openGoiDau3(pl, item);
                            break;
                        case 296:
                            openGoiDau4(pl, item);
                            break;
                        case 297:
                            openGoiDau5(pl, item);
                            break;
                        case 298:
                            openGoiDau6(pl, item);
                            break;
                        case 299:
                            openGoiDau7(pl, item);
                            break;
                        case 596:
                            openGoiDau8(pl, item);
                            break;
                        case 597:
                            openGoiDau9(pl, item);
                            break;
                        case 211: //nho tím
                        case 212: //nho xanh
                            eatGrapes(pl, item);
                            break;
                        case 1105://hop qua skh, item 2002 xd
                            UseItem.gI().Hopts(pl, item);
                            break;
                        case 342:
                        case 343:
                        case 344:
                        case 345:
                            if (pl.zone.items.stream().filter(it -> it != null && it.itemTemplate.type == 22).count() < 5) {
                                Service.getInstance().DropVeTinh(pl, item, pl.zone, pl.location.x, pl.location.y);
                                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                            } else {
                                Service.getInstance().sendThongBao(pl, "Đặt ít thôi con");
                            }
                            break;
                        case 1006:
                            if(Manager.SUKIEN == 0) {
                                ItemService.gI().openxocavang(pl, item);
                            }else{
                                Service.getInstance().sendThongBaoOK(pl,"Sự kiện đã kết thúc");
                            }
                            break;
                        case 1005:
                            if(Manager.SUKIEN == 0) {
                                ItemService.gI().openxocaxanh(pl,item);
                            }else{
                                Service.getInstance().sendThongBaoOK(pl,"Sự kiện đã kết thúc");
                            }
                            break;
                        case 1190:
                            ItemService.gI().OpenSkHungVuong(pl,item);
                            break;
                        case 1098:
                            ItemService.gI().Openquatanthu(pl,item);
                            break;
                        case 380: //cskb
                            openCSKB(pl, item);
                            break;
                        case 381: //cuồng nộ
                        case 382: //bổ huyết
                        case 383: //bổ khí
                        case 384: //giáp xên
                        case 385: //ẩn danh
                        case 379: //máy dò capsule

                        case 663: //bánh pudding
                        case 664: //xúc xíc
                        case 665: //kem dâu
                        case 666: //mì ly
                        case 667: //sushi
                        case 1099:
                        case 1100:
                        case 1101:
                        case 1102:
                        case 1103:
                            useItemTime(pl, item);
                            break;
                        case 521: //tdlt
                            useTDLT(pl, item);
                            break;
                        case 454: //bông tai
                            UseItem.gI().usePorata(pl);
                            break;
                        case 921: //bông tai
                            UseItem.gI().usePorata2(pl);
                            break;
                        case 193: //gói 10 viên capsule
                            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                        case 194: //capsule đặc biệt
                            openCapsuleUI(pl);
                            break;
                        case 401: //đổi đệ tử
                            changePet(pl, item);
                            break;

                        case 1108: //đổi đệ tử
                            changePetBerus(pl, item);
                            break;
                        case 402: //sách nâng chiêu 1 đệ tử
                        case 403: //sách nâng chiêu 2 đệ tử
                        case 404: //sách nâng chiêu 3 đệ tử
                        case 759: //sách nâng chiêu 4 đệ tử
                            upSkillPet(pl, item);
                            break;
                        case 2000://hop qua skh, item 2000 td
                        case 2001://hop qua skh, item 2001 nm
                        case 2002://hop qua skh, item 2002 xd
                            UseItem.gI().ItemSKH(pl, item);
                            break;

                        case 2003://hop qua skh, item 2003 td
                        case 2004://hop qua skh, item 2004 nm
                        case 2005://hop qua skh, item 2005 xd
                            UseItem.gI().ItemDHD(pl, item);
                            break;
                        case 736:
                            ItemService.gI().OpenItem736(pl, item);
                            break;
                        case 987:
                            Service.getInstance().sendThongBao(pl, "Bảo vệ trang bị không bị rớt cấp"); //đá bảo vệ
                            break;
                        case 2006:
                            Input.gI().createFormChangeNameByItem(pl);
                            break;
                            
                            case 1128:
                            openDaBaoVe(pl, item);
                            break;
                        case 1129:
                            openSPL(pl, item);
                            break;
                        case 1130:
                            openDaNangCap(pl, item);
                            break;
                        case 1131:
                            openManhTS(pl, item);
                            break;
                        case 2027:
                            if (InventoryServiceNew.gI().getCountEmptyBag(pl) == 0) {
                                Service.getInstance().sendThongBao(pl, "Hành trang không đủ chỗ trống");
                            } else {
                                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                                short[] tinhlinh = new short[]{1999,1998,1995};
                                Item itemtinhlinh = ItemService.gI().createNewItem(tinhlinh[Util.nextInt(tinhlinh.length - 1)]);
                                itemtinhlinh.itemOptions.add(new Item.ItemOption(50, Util.nextInt(5,10)));
                                itemtinhlinh.itemOptions.add(new Item.ItemOption(77,  Util.nextInt(1,5)));
                                itemtinhlinh.itemOptions.add(new Item.ItemOption(103,  Util.nextInt(1,5)));
                                itemtinhlinh.itemOptions.add(new Item.ItemOption(95,  Util.nextInt(1,3)));
                                itemtinhlinh.itemOptions.add(new Item.ItemOption(96,  Util.nextInt(1,3)));
                                InventoryServiceNew.gI().addItemBag(pl, itemtinhlinh);
                                InventoryServiceNew.gI().sendItemBags(pl);
                                Service.getInstance().sendThongBao(pl, "Chúc mừng bạn nhận được tinh linh " + itemtinhlinh.template.name);
                            }
                            break;
                        case 2028: {
                            if (InventoryServiceNew.gI().getCountEmptyBag(pl) == 0) {
                                Service.getInstance().sendThongBao(pl, "Hành trang không đủ chỗ trống");
                            } else {
                                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                                short[] linhthu = new short[]{2013,2014,2015,2019,2020,2021,2022,2023,2024,2025,2026,2033,2034};
                                Item itemlinhthu = ItemService.gI().createNewItem(linhthu[Util.nextInt(linhthu.length - 1)]);
                                itemlinhthu.itemOptions.add(new Item.ItemOption(50, Util.nextInt(5,10)));
                                itemlinhthu.itemOptions.add(new Item.ItemOption(77,  Util.nextInt(1,5)));
                                itemlinhthu.itemOptions.add(new Item.ItemOption(103,  Util.nextInt(1,5)));
                                itemlinhthu.itemOptions.add(new Item.ItemOption(95,  Util.nextInt(1,3)));
                                itemlinhthu.itemOptions.add(new Item.ItemOption(96,  Util.nextInt(1,3)));
                                InventoryServiceNew.gI().addItemBag(pl, itemlinhthu);
                                InventoryServiceNew.gI().sendItemBags(pl);
                                Service.getInstance().sendThongBao(pl, "Chúc mừng bạn nhận được Linh thú " + itemlinhthu.template.name);
                            }
                            break;
                        }
                    }
                    break;
            }
            InventoryServiceNew.gI().sendItemBags(pl);
        } else {
            Service.getInstance().sendThongBaoOK(pl, "Sức mạnh không đủ yêu cầu");
        }
    }

    private void useItemChangeFlagBag(Player player, Item item) {
        switch (item.template.id) {
            case 994: //vỏ ốc
                break;
            case 995: //cây kem
                break;
            case 996: //cá heo
                break;
            case 997: //con diều
                break;
            case 998: //diều rồng
                break;
            case 999: //mèo mun
                if (!player.effectFlagBag.useMeoMun) {
                    player.effectFlagBag.reset();
                    player.effectFlagBag.useMeoMun = !player.effectFlagBag.useMeoMun;
                } else {
                    player.effectFlagBag.reset();
                }
                break;
            case 1000: //xiên cá
                if (!player.effectFlagBag.useXienCa) {
                    player.effectFlagBag.reset();
                    player.effectFlagBag.useXienCa = !player.effectFlagBag.useXienCa;
                } else {
                    player.effectFlagBag.reset();
                }
                break;
            case 1001: //phóng heo
                if (!player.effectFlagBag.usePhongHeo) {
                    player.effectFlagBag.reset();
                    player.effectFlagBag.usePhongHeo = !player.effectFlagBag.usePhongHeo;
                } else {
                    player.effectFlagBag.reset();
                }
                break;
        }
        Service.getInstance().point(player);
        Service.getInstance().sendFlagBag(player);
    }

    private void changePet(Player player, Item item) {
        if (player.pet != null) {
            int gender = player.pet.gender + 1;
            if (gender > 2) {
                gender = 0;
            }
            PetService.gI().changeNormalPet(player, gender);
            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
        } else {
            Service.getInstance().sendThongBao(player, "Không thể thực hiện");
        }
    }

    private void changePetBerus(Player player, Item item) {
        if (player.pet != null) {
            int gender = player.pet.gender;
            PetService.gI().changeBerusPet(player, gender);
            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
        } else {
            Service.getInstance().sendThongBao(player, "Không thể thực hiện");
        }
    }
private void openDaBaoVe(Player player, Item item) {
    if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
        short[] possibleItems = { 987, 987 };
        byte selectedIndex = (byte) Util.nextInt(0, possibleItems.length - 2);
        short[] icon = new short[2];
        icon[0] = item.template.iconID;
        Item newItem = ItemService.gI().createNewItem(possibleItems[selectedIndex]);
        newItem.itemOptions.add(new ItemOption(73, 0));
        newItem.quantity = (short) Util.nextInt(1, 10);
        InventoryServiceNew.gI().addItemBag(player, newItem);
        icon[1] = newItem.template.iconID;

        InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
        InventoryServiceNew.gI().sendItemBags(player);

        CombineServiceNew.gI().sendEffectOpenItem(player, icon[0], icon[1]);
    } else {
        Service.getInstance().sendThongBao(player, "Hàng trang đã đầy");
    }
}

private void openSPL(Player player, Item item) {
    if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
        short[] possibleItems = { 441, 442, 443, 444, 445, 446, 447 };
        byte selectedIndex = (byte) Util.nextInt(0, possibleItems.length - 2);
        short[] icon = new short[2];
        icon[0] = item.template.iconID;
        Item newItem = ItemService.gI().createNewItem(possibleItems[selectedIndex]);
        newItem.itemOptions.add(new ItemOption(73, 0));
        newItem.quantity = (short) Util.nextInt(1, 10);
        InventoryServiceNew.gI().addItemBag(player, newItem);
        icon[1] = newItem.template.iconID;

        InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
        InventoryServiceNew.gI().sendItemBags(player);

        CombineServiceNew.gI().sendEffectOpenItem(player, icon[0], icon[1]);
    } else {
        Service.getInstance().sendThongBao(player, "Hàng trang đã đầy");
    }
}

private void openDaNangCap(Player player, Item item) {
    if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
        short[] possibleItems = { 220, 221, 222, 223, 224 };
        byte selectedIndex = (byte) Util.nextInt(0, possibleItems.length - 2);
        short[] icon = new short[2];
        icon[0] = item.template.iconID;
        Item newItem = ItemService.gI().createNewItem(possibleItems[selectedIndex]);
        newItem.itemOptions.add(new ItemOption(73, 0));
        newItem.quantity = (short) Util.nextInt(1, 10);
        InventoryServiceNew.gI().addItemBag(player, newItem);
        icon[1] = newItem.template.iconID;

        InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
        InventoryServiceNew.gI().sendItemBags(player);

        CombineServiceNew.gI().sendEffectOpenItem(player, icon[0], icon[1]);
    } else {
        Service.getInstance().sendThongBao(player, "Hàng trang đã đầy");
    }
}

private void openManhTS(Player player, Item item) {
    if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
        short[] possibleItems = { 1066, 1067, 1068, 1069, 1070 };
        byte selectedIndex = (byte) Util.nextInt(0, possibleItems.length - 2);
        short[] icon = new short[2];
        icon[0] = item.template.iconID;
        Item newItem = ItemService.gI().createNewItem(possibleItems[selectedIndex]);
        newItem.itemOptions.add(new ItemOption(73, 0));
        newItem.quantity = (short) Util.nextInt(1, 99);
        InventoryServiceNew.gI().addItemBag(player, newItem);
        icon[1] = newItem.template.iconID;

        InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
        InventoryServiceNew.gI().sendItemBags(player);

        CombineServiceNew.gI().sendEffectOpenItem(player, icon[0], icon[1]);
    } else {
        Service.getInstance().sendThongBao(player, "Hàng trang đã đầy");
    }
}

private void openGoiDau1(Player player, Item item) {
    if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
        short[] possibleItems = { 13, 13 };
        byte selectedIndex = (byte) Util.nextInt(0, possibleItems.length - 2);
        short[] icon = new short[2];
        icon[0] = item.template.iconID;
        Item newItem = ItemService.gI().createNewItem(possibleItems[selectedIndex]);
        newItem.quantity = (short) Util.nextInt(99, 99);
        InventoryServiceNew.gI().addItemBag(player, newItem);
        icon[1] = newItem.template.iconID;

        InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
        InventoryServiceNew.gI().sendItemBags(player);

        CombineServiceNew.gI().sendEffectOpenItem(player, icon[0], icon[1]);
    } else {
        Service.getInstance().sendThongBao(player, "Hàng trang đã đầy");
    }
}

private void openGoiDau2(Player player, Item item) {
    if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
        short[] possibleItems = { 60, 60 };
        byte selectedIndex = (byte) Util.nextInt(0, possibleItems.length - 2);
        short[] icon = new short[2];
        icon[0] = item.template.iconID;
        Item newItem = ItemService.gI().createNewItem(possibleItems[selectedIndex]);
        newItem.quantity = (short) Util.nextInt(99, 99);
        InventoryServiceNew.gI().addItemBag(player, newItem);
        icon[1] = newItem.template.iconID;

        InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
        InventoryServiceNew.gI().sendItemBags(player);

        CombineServiceNew.gI().sendEffectOpenItem(player, icon[0], icon[1]);
    } else {
        Service.getInstance().sendThongBao(player, "Hàng trang đã đầy");
    }
}

private void openGoiDau3(Player player, Item item) {
    if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
        short[] possibleItems = { 61, 61 };
        byte selectedIndex = (byte) Util.nextInt(0, possibleItems.length - 2);
        short[] icon = new short[2];
        icon[0] = item.template.iconID;
        Item newItem = ItemService.gI().createNewItem(possibleItems[selectedIndex]);
        newItem.quantity = (short) Util.nextInt(99, 99);
        InventoryServiceNew.gI().addItemBag(player, newItem);
        icon[1] = newItem.template.iconID;

        InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
        InventoryServiceNew.gI().sendItemBags(player);

        CombineServiceNew.gI().sendEffectOpenItem(player, icon[0], icon[1]);
    } else {
        Service.getInstance().sendThongBao(player, "Hàng trang đã đầy");
    }
}

private void openGoiDau4(Player player, Item item) {
    if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
        short[] possibleItems = { 62, 62 };
        byte selectedIndex = (byte) Util.nextInt(0, possibleItems.length - 2);
        short[] icon = new short[2];
        icon[0] = item.template.iconID;
        Item newItem = ItemService.gI().createNewItem(possibleItems[selectedIndex]);
        newItem.quantity = (short) Util.nextInt(99, 99);
        InventoryServiceNew.gI().addItemBag(player, newItem);
        icon[1] = newItem.template.iconID;

        InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
        InventoryServiceNew.gI().sendItemBags(player);

        CombineServiceNew.gI().sendEffectOpenItem(player, icon[0], icon[1]);
    } else {
        Service.getInstance().sendThongBao(player, "Hàng trang đã đầy");
    }
}

private void openGoiDau5(Player player, Item item) {
    if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
        short[] possibleItems = { 63, 63 };
        byte selectedIndex = (byte) Util.nextInt(0, possibleItems.length - 2);
        short[] icon = new short[2];
        icon[0] = item.template.iconID;
        Item newItem = ItemService.gI().createNewItem(possibleItems[selectedIndex]);
        newItem.quantity = (short) Util.nextInt(99, 99);
        InventoryServiceNew.gI().addItemBag(player, newItem);
        icon[1] = newItem.template.iconID;

        InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
        InventoryServiceNew.gI().sendItemBags(player);

        CombineServiceNew.gI().sendEffectOpenItem(player, icon[0], icon[1]);
    } else {
        Service.getInstance().sendThongBao(player, "Hàng trang đã đầy");
    }
}

private void openGoiDau6(Player player, Item item) {
    if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
        short[] possibleItems = { 64, 64 };
        byte selectedIndex = (byte) Util.nextInt(0, possibleItems.length - 2);
        short[] icon = new short[2];
        icon[0] = item.template.iconID;
        Item newItem = ItemService.gI().createNewItem(possibleItems[selectedIndex]);
        newItem.quantity = (short) Util.nextInt(99, 99);
        InventoryServiceNew.gI().addItemBag(player, newItem);
        icon[1] = newItem.template.iconID;

        InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
        InventoryServiceNew.gI().sendItemBags(player);

        CombineServiceNew.gI().sendEffectOpenItem(player, icon[0], icon[1]);
    } else {
        Service.getInstance().sendThongBao(player, "Hàng trang đã đầy");
    }
}

private void openGoiDau7(Player player, Item item) {
    if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
        short[] possibleItems = { 65, 65 };
        byte selectedIndex = (byte) Util.nextInt(0, possibleItems.length - 2);
        short[] icon = new short[2];
        icon[0] = item.template.iconID;
        Item newItem = ItemService.gI().createNewItem(possibleItems[selectedIndex]);
        newItem.quantity = (short) Util.nextInt(99, 99);
        InventoryServiceNew.gI().addItemBag(player, newItem);
        icon[1] = newItem.template.iconID;

        InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
        InventoryServiceNew.gI().sendItemBags(player);

        CombineServiceNew.gI().sendEffectOpenItem(player, icon[0], icon[1]);
    } else {
        Service.getInstance().sendThongBao(player, "Hàng trang đã đầy");
    }
}

private void openGoiDau8(Player player, Item item) {
    if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
        short[] possibleItems = { 352, 352 };
        byte selectedIndex = (byte) Util.nextInt(0, possibleItems.length - 2);
        short[] icon = new short[2];
        icon[0] = item.template.iconID;
        Item newItem = ItemService.gI().createNewItem(possibleItems[selectedIndex]);
        newItem.quantity = (short) Util.nextInt(99, 99);
        InventoryServiceNew.gI().addItemBag(player, newItem);
        icon[1] = newItem.template.iconID;

        InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
        InventoryServiceNew.gI().sendItemBags(player);

        CombineServiceNew.gI().sendEffectOpenItem(player, icon[0], icon[1]);
    } else {
        Service.getInstance().sendThongBao(player, "Hàng trang đã đầy");
    }
}

private void openGoiDau9(Player player, Item item) {
    if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
        short[] possibleItems = { 523, 523 };
        byte selectedIndex = (byte) Util.nextInt(0, possibleItems.length - 2);
        short[] icon = new short[2];
        icon[0] = item.template.iconID;
        Item newItem = ItemService.gI().createNewItem(possibleItems[selectedIndex]);
        newItem.quantity = (short) Util.nextInt(99, 99);
        InventoryServiceNew.gI().addItemBag(player, newItem);
        icon[1] = newItem.template.iconID;

        InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
        InventoryServiceNew.gI().sendItemBags(player);

        CombineServiceNew.gI().sendEffectOpenItem(player, icon[0], icon[1]);
    } else {
        Service.getInstance().sendThongBao(player, "Hàng trang đã đầy");
    }
}
 
    private void openPhieuCaiTrangHaiTac(Player pl, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
            Item ct = ItemService.gI().createNewItem((short) Util.nextInt(618, 626));
            ct.itemOptions.add(new ItemOption(147, 3));
            ct.itemOptions.add(new ItemOption(77, 3));
            ct.itemOptions.add(new ItemOption(103, 3));
            ct.itemOptions.add(new ItemOption(149, 0));
            if (item.template.id == 2006) {
                ct.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
            } else if (item.template.id == 2007) {
                ct.itemOptions.add(new ItemOption(93, Util.nextInt(7, 30)));
            }
            InventoryServiceNew.gI().addItemBag(pl, ct);
            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
            InventoryServiceNew.gI().sendItemBags(pl);
            CombineServiceNew.gI().sendEffectOpenItem(pl, item.template.iconID, ct.template.iconID);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    private void eatGrapes(Player pl, Item item) {
        int percentCurrentStatima = pl.nPoint.stamina * 100 / pl.nPoint.maxStamina;
        if (percentCurrentStatima > 50) {
            Service.getInstance().sendThongBao(pl, "Thể lực vẫn còn trên 50%");
            return;
        } else if (item.template.id == 211) {
            pl.nPoint.stamina = pl.nPoint.maxStamina;
            Service.getInstance().sendThongBao(pl, "Thể lực của bạn đã được hồi phục 100%");
        } else if (item.template.id == 212) {
            pl.nPoint.stamina += (pl.nPoint.maxStamina * 20 / 100);
            Service.getInstance().sendThongBao(pl, "Thể lực của bạn đã được hồi phục 20%");
        }
        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
        InventoryServiceNew.gI().sendItemBags(pl);
        PlayerService.gI().sendCurrentStamina(pl);
    }

    private void openCSKB(Player pl, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
            short[] temp = { 16, 190, 381, 382, 383, 384, 385};
            int[][] gold = {{100000, 200000}};
            byte index = (byte) Util.nextInt(0, temp.length - 1);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            if (index <= 3) {
                pl.inventory.gold += Util.nextInt(gold[0][0], gold[0][1]);
                if (pl.inventory.gold > Inventory.LIMIT_GOLD) {
                    pl.inventory.gold = Inventory.LIMIT_GOLD;
                }
                PlayerService.gI().sendInfoHpMpMoney(pl);
                icon[1] = 930;
            } else {
                Item it = ItemService.gI().createNewItem(temp[index]);
                it.itemOptions.add(new ItemOption(73, 0));
                InventoryServiceNew.gI().addItemBag(pl, it);
                icon[1] = it.template.iconID;
            }
            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
            InventoryServiceNew.gI().sendItemBags(pl);

            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }
    
     private void useItemHopQuaTanThu(Player pl, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
            short[] temp = { 19, 20, 21, 22};
            int[][] gold = {{10000, 50000}};
            byte index = (byte) Util.nextInt(0, temp.length - 1);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            if (index <= 3) {
                pl.inventory.gold += Util.nextInt(gold[0][0], gold[0][1]);
                if (pl.inventory.gold > Inventory.LIMIT_GOLD) {
                    pl.inventory.gold = Inventory.LIMIT_GOLD;
                }
                PlayerService.gI().sendInfoHpMpMoney(pl);
                icon[1] = 930;
            } else {
                Item it = ItemService.gI().createNewItem(temp[index]);
                it.itemOptions.add(new ItemOption(73, 0));
                InventoryServiceNew.gI().addItemBag(pl, it);
                icon[1] = it.template.iconID;
            }
            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
            InventoryServiceNew.gI().sendItemBags(pl);

            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    private void useItemTime(Player pl, Item item) {
        switch (item.template.id) {
            case 382: //bổ huyết
                pl.itemTime.lastTimeBoHuyet = System.currentTimeMillis();
                pl.itemTime.isUseBoHuyet = true;
                break;
            case 383: //bổ khí
                pl.itemTime.lastTimeBoKhi = System.currentTimeMillis();
                pl.itemTime.isUseBoKhi = true;
                break;
            case 384: //giáp xên
                pl.itemTime.lastTimeGiapXen = System.currentTimeMillis();
                pl.itemTime.isUseGiapXen = true;
                break;
            case 381: //cuồng nộ
                pl.itemTime.lastTimeCuongNo = System.currentTimeMillis();
                pl.itemTime.isUseCuongNo = true;
                Service.getInstance().point(pl);
                break;
            case 385: //ẩn danh
                pl.itemTime.lastTimeAnDanh = System.currentTimeMillis();
                pl.itemTime.isUseAnDanh = true;
                break;
            case 379: //máy dò capsule
                pl.itemTime.lastTimeUseMayDo = System.currentTimeMillis();
                pl.itemTime.isUseMayDo = true;
                break;
            case 1099:// cn
                pl.itemTime.lastTimeCuongNo2 = System.currentTimeMillis();
                pl.itemTime.isUseCuongNo2 = true;
                Service.getInstance().point(pl);

                break;
            case 1100:// bo huyet
                pl.itemTime.lastTimeBoHuyet2 = System.currentTimeMillis();
                pl.itemTime.isUseBoHuyet2 = true;
                break;
            case 1101://bo khi
                pl.itemTime.lastTimeBoKhi2 = System.currentTimeMillis();
                pl.itemTime.isUseBoKhi2 = true;
                break;
            case 1102://xbh
                pl.itemTime.lastTimeGiapXen2 = System.currentTimeMillis();
                pl.itemTime.isUseGiapXen2 = true;
                break;
            case 1103://an danh
                pl.itemTime.lastTimeAnDanh2 = System.currentTimeMillis();
                pl.itemTime.isUseAnDanh2 = true;
                break;
            case 663: //bánh pudding
            case 664: //xúc xíc
            case 665: //kem dâu
            case 666: //mì ly
            case 667: //sushi
                pl.itemTime.lastTimeEatMeal = System.currentTimeMillis();
                pl.itemTime.isEatMeal = true;
                ItemTimeService.gI().removeItemTime(pl, pl.itemTime.iconMeal);
                pl.itemTime.iconMeal = item.template.iconID;
                break;
            case 1109: //máy dò đồ
                pl.itemTime.lastTimeUseMayDo2 = System.currentTimeMillis();
                pl.itemTime.isUseMayDo2 = true;
                break;
        }
        Service.getInstance().point(pl);
        ItemTimeService.gI().sendAllItemTime(pl);
        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
        InventoryServiceNew.gI().sendItemBags(pl);
    }

    private void controllerCallRongThan(Player pl, Item item) {
        int tempId = item.template.id;
        if (tempId >= SummonDragon.NGOC_RONG_1_SAO && tempId <= SummonDragon.NGOC_RONG_7_SAO) {
            switch (tempId) {
                case SummonDragon.NGOC_RONG_1_SAO:
//                case SummonDragon.NGOC_RONG_2_SAO:
//                case SummonDragon.NGOC_RONG_3_SAO:
                    SummonDragon.gI().openMenuSummonShenron(pl, (byte) (tempId - 13));
                    break;
                default:
                    NpcService.gI().createMenuConMeo(pl, ConstNpc.TUTORIAL_SUMMON_DRAGON,
                            -1, "Bạn chỉ có thể gọi rồng từ ngọc 3 sao, 2 sao, 1 sao", "Hướng\ndẫn thêm\n(mới)", "OK");
                    break;
            }
        }
    }

    private void learnSkill(Player pl, Item item) {
        Message msg;
        try {
            if (item.template.gender == pl.gender || item.template.gender == 3) {
                String[] subName = item.template.name.split("");
                byte level = Byte.parseByte(subName[subName.length - 1]);
                Skill curSkill = SkillUtil.getSkillByItemID(pl, item.template.id);
                if (curSkill.point == 7) {
                    Service.getInstance().sendThongBao(pl, "Kỹ năng đã đạt tối đa!");
                } else {
                    if (curSkill.point == 0) {
                        if (level == 1) {
                            curSkill = SkillUtil.createSkill(SkillUtil.getTempSkillSkillByItemID(item.template.id), level);
                            SkillUtil.setSkill(pl, curSkill);
                            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                            msg = Service.getInstance().messageSubCommand((byte) 23);
                            msg.writer().writeShort(curSkill.skillId);
                            pl.sendMessage(msg);
                            msg.cleanup();
                        } else {
                            Skill skillNeed = SkillUtil.createSkill(SkillUtil.getTempSkillSkillByItemID(item.template.id), level);
                            Service.getInstance().sendThongBao(pl, "Vui lòng học " + skillNeed.template.name + " cấp " + skillNeed.point + " trước!");
                        }
                    } else {
                        if (curSkill.point + 1 == level) {
                            curSkill = SkillUtil.createSkill(SkillUtil.getTempSkillSkillByItemID(item.template.id), level);
                            //System.out.println(curSkill.template.name + " - " + curSkill.point);
                            SkillUtil.setSkill(pl, curSkill);
                            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                            msg = Service.getInstance().messageSubCommand((byte) 62);
                            msg.writer().writeShort(curSkill.skillId);
                            pl.sendMessage(msg);
                            msg.cleanup();
                        } else {
                            Service.getInstance().sendThongBao(pl, "Vui lòng học " + curSkill.template.name + " cấp " + (curSkill.point + 1) + " trước!");
                        }
                    }
                    InventoryServiceNew.gI().sendItemBags(pl);
                }
            } else {
                Service.getInstance().sendThongBao(pl, "Không thể thực hiện");
            }
        } catch (Exception e) {
            Logger.logException(UseItem.class, e);
        }
    }

    private void useTDLT(Player pl, Item item) {
        if (pl.itemTime.isUseTDLT) {
            ItemTimeService.gI().turnOffTDLT(pl, item);
        } else {
            ItemTimeService.gI().turnOnTDLT(pl, item);
        }
    }
    private void usePorata2(Player pl) {
        if (pl.pet == null || pl.fusion.typeFusion == 4) {
            Service.getInstance().sendThongBao(pl, "Không thể thực hiện");
        } else {
            if (pl.fusion.typeFusion == ConstPlayer.NON_FUSION) {
                pl.pet.fusion2(true);
            } else {
                pl.pet.unFusion();
            }
        }
    }

    private void usePorata(Player pl) {
        if (pl.pet == null || pl.fusion.typeFusion == 4) {
            Service.getInstance().sendThongBao(pl, "Không thể thực hiện");
        } else {
            if (pl.fusion.typeFusion == ConstPlayer.NON_FUSION) {
                pl.pet.fusion(true);
            } else {
                pl.pet.unFusion();
            }
        }
    }

    private void openCapsuleUI(Player pl) {
        pl.iDMark.setTypeChangeMap(ConstMap.CHANGE_CAPSULE);
        ChangeMapService.gI().openChangeMapTab(pl);
    }

    public void choseMapCapsule(Player pl, int index) {
        if (pl != null) {
            int zoneId = -1;
            Zone zoneChose = pl.mapCapsule.get(index);
            //Kiểm tra số lượng người trong khu

            if (zoneChose.getNumOfPlayers() > 25
                    || MapService.gI().isMapDoanhTrai(zoneChose.map.mapId)
                    || MapService.gI().isMapBanDoKhoBau(zoneChose.map.mapId)
                    || MapService.gI().isMapKhiGa(zoneChose.map.mapId)
                    || MapService.gI().isMapMaBu(zoneChose.map.mapId)
                    || MapService.gI().isMapHuyDiet(zoneChose.map.mapId)) {
                Service.getInstance().sendThongBao(pl, "Hiện tại không thể vào được khu!");
                return;
            }
            if (index != 0 || zoneChose.map.mapId == 21
                    || zoneChose.map.mapId == 22
                    || zoneChose.map.mapId == 23) {
                pl.mapBeforeCapsule = pl.zone;
            } else {
                zoneId = pl.mapBeforeCapsule != null ? pl.mapBeforeCapsule.zoneId : -1;
                pl.mapBeforeCapsule = null;
            }
            ChangeMapService.gI().changeMapBySpaceShip(pl, pl.mapCapsule.get(index).map.mapId, zoneId, -1);
        }
    }

    public void UseCard(Player pl, Item item){
        RadarCard radarTemplate = RadarService.gI().RADAR_TEMPLATE.stream().filter(c -> c.Id == item.template.id).findFirst().orElse(null);
        if(radarTemplate == null) return;
        if (radarTemplate.Require != -1){
            RadarCard radarRequireTemplate = RadarService.gI().RADAR_TEMPLATE.stream().filter(r -> r.Id == radarTemplate.Require).findFirst().orElse(null);
            if(radarRequireTemplate == null) return;
            Card cardRequire = pl.Cards.stream().filter(r -> r.Id == radarRequireTemplate.Id).findFirst().orElse(null);
            if (cardRequire == null || cardRequire.Level < radarTemplate.RequireLevel){
                Service.getInstance().sendThongBao(pl,"Bạn cần sưu tầm "+radarRequireTemplate.Name+" ở cấp độ "+radarTemplate.RequireLevel+" mới có thể sử dụng thẻ này");
                return;
            }
        }
        Card card = pl.Cards.stream().filter(r->r.Id == item.template.id).findFirst().orElse(null);
        if (card == null){
            Card newCard = new Card(item.template.id,(byte)1,radarTemplate.Max,(byte)-1,radarTemplate.Options);
            if (pl.Cards.add(newCard)){
                RadarService.gI().RadarSetAmount(pl,newCard.Id, newCard.Amount, newCard.MaxAmount);
                RadarService.gI().RadarSetLevel(pl,newCard.Id, newCard.Level);
                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                InventoryServiceNew.gI().sendItemBags(pl);
            }
        }else{
            if (card.Level >= 2){
                Service.getInstance().sendThongBao(pl,"Thẻ này đã đạt cấp tối đa");
                return;
            }
            card.Amount++;
            if (card.Amount >= card.MaxAmount){
                card.Amount = 0;
                if (card.Level == -1){
                    card.Level = 1;
                }else {
                    card.Level++;
                }
                Service.getInstance().point(pl);
            }
            RadarService.gI().RadarSetAmount(pl,card.Id, card.Amount, card.MaxAmount);
            RadarService.gI().RadarSetLevel(pl,card.Id, card.Level);
            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
            InventoryServiceNew.gI().sendItemBags(pl);
        }
    }

    public void eatPea(Player player) {
        Item pea = null;
        for (Item item : player.inventory.itemsBag) {
            if (item.isNotNullItem() && item.template.type == 6) {
                pea = item;
                break;
            }
        }
        if (pea != null) {
            int hpKiHoiPhuc = 0;
            int lvPea = Integer.parseInt(pea.template.name.substring(13));
            for (Item.ItemOption io : pea.itemOptions) {
                if (io.optionTemplate.id == 2) {
                    hpKiHoiPhuc = io.param * 1000;
                    break;
                }
                if (io.optionTemplate.id == 48) {
                    hpKiHoiPhuc = io.param;
                    break;
                }
            }
            player.nPoint.setHp(player.nPoint.hp + hpKiHoiPhuc);
            player.nPoint.setMp(player.nPoint.mp + hpKiHoiPhuc);
            PlayerService.gI().sendInfoHpMp(player);
            Service.getInstance().sendInfoPlayerEatPea(player);
            if (player.pet != null && player.zone.equals(player.pet.zone) && !player.pet.isDie()) {
                int statima = 100 * lvPea;
                player.pet.nPoint.stamina += statima;
                if (player.pet.nPoint.stamina > player.pet.nPoint.maxStamina) {
                    player.pet.nPoint.stamina = player.pet.nPoint.maxStamina;
                }
                player.pet.nPoint.setHp(player.pet.nPoint.hp + hpKiHoiPhuc);
                player.pet.nPoint.setMp(player.pet.nPoint.mp + hpKiHoiPhuc);
                Service.getInstance().sendInfoPlayerEatPea(player.pet);
                Service.getInstance().chatJustForMe(player, player.pet, "Cảm ơn sư phụ đã cho con đậu thần");
            }

            InventoryServiceNew.gI().subQuantityItemsBag(player, pea, 1);
            InventoryServiceNew.gI().sendItemBags(player);
        }
    }

    private void openWoodChest(Player pl, Item item) {
        int time = (int) TimeUtil.diffDate(new Date(), new Date(item.createTime), TimeUtil.DAY);
//        if (time != 0) {
            Item itemReward = null;
            int param = item.itemOptions.size();
            int gold = 0;
        int[] listItem = {441, 442, 443, 444, 445, 446, 447, 220, 221, 222, 223, 224, 225,16,17,18,457};
        int[] listClothesReward;
        int[] listItemReward;
        String text = "Bạn nhận được\n";
        if (param < 8) {
            gold = 100000 * param;
            listClothesReward = new int[]{randClothes(param)};
            listItemReward = Util.pickNRandInArr(listItem, 3);
        } else if (param < 10) {
            gold = 250000 * param;
            listClothesReward = new int[]{randClothes(param), randClothes(param)};
            listItemReward = Util.pickNRandInArr(listItem, 4);
        } else {
            gold = 500000 * param;
            listClothesReward = new int[]{randClothes(param), randClothes(param), randClothes(param)};
            listItemReward = Util.pickNRandInArr(listItem, 6);
            int ruby = Util.nextInt(1, 5);
            pl.inventory.ruby += ruby;
            pl.textRuongGo.add(text + "|1| " + ruby + " Hồng Ngọc");
        }
        if (param == 11) {
            itemReward = ItemService.gI().createNewItem((short) 457);
            itemReward.quantity = Util.nextInt(1, 30);
            InventoryServiceNew.gI().addItemBag(pl, itemReward);
            pl.textRuongGo.add(text + itemReward.getInfoItem());
        }
        NpcService.gI().createMenuConMeo(pl, ConstNpc.RUONG_GO, -1, "Bạn nhận được\n|1|+" + Util.numberToMoney(gold) + " vàng", "OK [" + pl.textRuongGo.size() + "]");
        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
        pl.inventory.addGold(gold);
        InventoryServiceNew.gI().sendItemBags(pl);
        PlayerService.gI().sendInfoHpMpMoney(pl);
    }

    private int randClothes(int level) {
        return LIST_ITEM_CLOTHES[Util.nextInt(0, 2)][Util.nextInt(0, 4)][level - 1];
    }

    private void upSkillPet(Player pl, Item item) {
        if (pl.pet == null) {
            Service.getInstance().sendThongBao(pl, "Không thể thực hiện");
            return;
        }
        try {
            switch (item.template.id) {
                case 402: //skill 1
                    if (SkillUtil.upSkillPet(pl.pet.playerSkill.skills, 0)) {
                        Service.getInstance().chatJustForMe(pl, pl.pet, "Cảm ơn sư phụ");
                        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                    } else {
                        Service.getInstance().sendThongBao(pl, "Không thể thực hiện");
                    }
                    break;
                case 403: //skill 2
                    if (SkillUtil.upSkillPet(pl.pet.playerSkill.skills, 1)) {
                        Service.getInstance().chatJustForMe(pl, pl.pet, "Cảm ơn sư phụ");
                        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                    } else {
                        Service.getInstance().sendThongBao(pl, "Không thể thực hiện");
                    }
                    break;
                case 404: //skill 3
                    if (SkillUtil.upSkillPet(pl.pet.playerSkill.skills, 2)) {
                        Service.getInstance().chatJustForMe(pl, pl.pet, "Cảm ơn sư phụ");
                        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                    } else {
                        Service.getInstance().sendThongBao(pl, "Không thể thực hiện");
                    }
                    break;
                case 759: //skill 4
                    if (SkillUtil.upSkillPet(pl.pet.playerSkill.skills, 3)) {
                        Service.getInstance().chatJustForMe(pl, pl.pet, "Cảm ơn sư phụ");
                        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                    } else {
                        Service.getInstance().sendThongBao(pl, "Không thể thực hiện");
                    }
                    break;

            }

        } catch (Exception e) {
            Service.getInstance().sendThongBao(pl, "Không thể thực hiện");
        }
    }

    private void ItemSKH(Player pl, Item item) {//hop qua skh
        NpcService.gI().createMenuConMeo(pl, item.template.id, -1, "Hãy chọn một món quà", "Áo", "Quần", "Găng", "Giày", "Rada", "Từ Chối");
    }

    private void ItemDHD(Player pl, Item item) {//hop qua do huy diet
        NpcService.gI().createMenuConMeo(pl, item.template.id, -1, "Hãy chọn một món quà", "Áo", "Quần", "Găng", "Giày", "Rada", "Từ Chối");
    }

    private void Hopts(Player pl, Item item) {//hop qua do huy diet
        NpcService.gI().createMenuConMeo(pl, item.template.id, -1, "Chọn hành tinh của mày đi", "Set trái đất", "Set namec", "Set xayda", "Từ chổi");
    }

}
