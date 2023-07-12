package com.bth.services.func;

import com.bth.consts.ConstNpc;
import com.bth.models.item.Item;
import com.bth.models.item.Item.ItemOption;
import com.bth.models.npc.Npc;
import com.bth.models.npc.NpcManager;
import com.bth.models.player.Player;
import com.bth.server.Manager;
import com.bth.server.ServerNotify;
import com.bth.services.InventoryServiceNew;
import com.bth.services.ItemService;
import com.bth.services.RewardService;
import com.bth.services.Service;
import com.bth.utils.Util;
import com.girlkun.network.io.Message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

/**
 *
 * @author Heroes x BTH
 * 
 */
public class CombineServiceNew {

    private static final byte MAX_STAR_ITEM = 12;
    private static final byte MAX_LEVEL_ITEM = 8;

    private static final byte OPEN_TAB_COMBINE = 0;
    private static final byte REOPEN_TAB_COMBINE = 1;
    private static final byte COMBINE_SUCCESS = 2;
    private static final byte COMBINE_FAIL = 3;
    private static final byte COMBINE_DRAGON_BALL = 5;
    public static final byte OPEN_ITEM = 6;

    public static final int EP_SAO_TRANG_BI = 500;
    public static final int PHA_LE_HOA_TRANG_BI = 501;
    public static final int CHUYEN_HOA_TRANG_BI = 502;
    public static final int NANG_CAP_KICH_HOAT = 504;


    public static final int NANG_CAP_VAT_PHAM = 510;
    public static final int NANG_CAP_BONG_TAI = 511;
    public static final int MO_CHI_SO_BONG_TAI = 519;
    public static final int CHE_TAO_TRANG_BI_TS = 520;
    public static final int NHAP_NGOC_RONG = 513;
    
    public static final int REN_KIEM_Z = 517;

    private static final int GOLD_BONG_TAI = 200_000_000;
    private static final int GOLD_KIEM_Z = 200_000_000;
    private static final int GEM_BONG_TAI = 1_000;
    private static final int GEM_KIEM_Z = 1_000;
    private static final int RATIO_BONG_TAI = 50;
    private static final int RATIO_NANG_CAP = 45;
    private static final int RATIO_KIEM_Z2 = 40;

    private final Npc baHatMit;
    private final Npc tosukaio;
    private final Npc whis;
    


    private static CombineServiceNew i;

    public CombineServiceNew(com.bth.models.npc.Npc whis) {
        this.baHatMit = NpcManager.getNpc(ConstNpc.BA_HAT_MIT);
        this.tosukaio = NpcManager.getNpc(ConstNpc.TO_SU_KAIO);
        this.whis = NpcManager.getNpc(ConstNpc.WHIS);
    }

    public static CombineServiceNew gI() {
        if (i == null) {
            i = new CombineServiceNew(null);
        }
        return i;
    }

    /**
     * Mở tab đập đồ
     *
     * @param player
     * @param type   kiểu đập đồ
     */
    public void openTabCombine(Player player, int type) {
        player.combineNew.setTypeCombine(type);
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(OPEN_TAB_COMBINE);
            msg.writer().writeUTF(getTextInfoTabCombine(type));
            msg.writer().writeUTF(getTextTopTabCombine(type));
            if (player.iDMark.getNpcChose() != null) {
                msg.writer().writeShort(player.iDMark.getNpcChose().tempId);
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    /**
     * Hiển thị thông tin đập đồ
     *
     * @param player
     */
    public void showInfoCombine(Player player, int[] index) {
        player.combineNew.clearItemCombine();
        if (index.length > 0) {
            for (int i = 0; i < index.length; i++) {
                player.combineNew.itemsCombine.add(player.inventory.itemsBag.get(index[i]));
            }
        }
        switch (player.combineNew.typeCombine) {
            case NANG_CAP_KICH_HOAT:
                if (player.combineNew.itemsCombine.size() == 2) {
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && (item.template.id >= 555 && item.template.id <= 567)).count() < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu trang bị nâng cấp", "Đóng");
                        break;
                    } 
                    if (!player.combineNew.itemsCombine.get(1).isDTL()) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thần Link", "Đóng");
                        break;
                    }
                    Item trangbicui = null;
                    Item trangbithanlinh = null;
                    if(player.combineNew.itemsCombine.get(1).isNotNullItem()) {
                        if(player.combineNew.itemsCombine.get(1).isDTL()) {
                            trangbithanlinh = player.combineNew.itemsCombine.get(1);
                        }
                    }
                      for (int j = 0; j <1; j++) {
                if (player.combineNew.itemsCombine.get(j).isNotNullItem()) {
                    if (player.combineNew.itemsCombine.get(j).template.id == 555) {
                        trangbicui = player.combineNew.itemsCombine.get(j);
                    }
                    if (player.combineNew.itemsCombine.get(j).template.id == 556) {
                        trangbicui = player.combineNew.itemsCombine.get(j);
                    }
                    if (player.combineNew.itemsCombine.get(j).template.id == 557) {
                        trangbicui = player.combineNew.itemsCombine.get(j);
                    }
                    if (player.combineNew.itemsCombine.get(j).template.id == 558) {
                        trangbicui = player.combineNew.itemsCombine.get(j);
                    }
                    if (player.combineNew.itemsCombine.get(j).template.id == 559) {
                        trangbicui = player.combineNew.itemsCombine.get(j);
                    }
                    if (player.combineNew.itemsCombine.get(j).template.id == 560) {
                        trangbicui = player.combineNew.itemsCombine.get(j);
                    }
                    if (player.combineNew.itemsCombine.get(j).template.id == 561) {
                        trangbicui = player.combineNew.itemsCombine.get(j);
                    }
                    if (player.combineNew.itemsCombine.get(j).template.id == 562) {
                        trangbicui = player.combineNew.itemsCombine.get(j);
                    }
                    if (player.combineNew.itemsCombine.get(j).template.id == 563) {
                        trangbicui = player.combineNew.itemsCombine.get(j);
                    }
                    if (player.combineNew.itemsCombine.get(j).template.id == 564) {
                        trangbicui = player.combineNew.itemsCombine.get(j);
                    }
                    if (player.combineNew.itemsCombine.get(j).template.id == 565) {
                        trangbicui = player.combineNew.itemsCombine.get(j);
                    }
                    if (player.combineNew.itemsCombine.get(j).template.id == 566) {
                        trangbicui = player.combineNew.itemsCombine.get(j);
                    }
                    if (player.combineNew.itemsCombine.get(j).template.id == 567) {
                        trangbicui = player.combineNew.itemsCombine.get(j);
                    }
                }
            }
                    if (trangbicui != null && trangbithanlinh != null) {
                        player.combineNew.goldCombine = 500000000; // Vàng nâng
                        player.combineNew.ratioCombine = (float) 100;  // Tỉ lệ nâng
                        String npcSay = "|2|Ta sẽ làm phép cho " + trangbicui.template.name + " của ngươi thành\n"
                                + "|2|Trang bị kích hoạt ngẫu nhiên cùng loại với phôi"
                                + "\n|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%"
                                + "\n" + (player.combineNew.goldCombine > player.inventory.gold ? "|7|" : "|1|")
                                + "Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";
                        if (player.combineNew.goldCombine > player.inventory.gold) {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                    npcSay, "Còn thiếu\n" + Util.numberToMoney((player.combineNew.goldCombine - player.inventory.gold)) + " vàng");
                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                                    npcSay, "Nâng cấp\n" + Util.numberToMoney(player.combineNew.goldCombine) + " vàng", "Từ chối");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Trang bị  thần linh của ngươi không tồn tại", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy chọn đủ 1 món thần linh làm phôi và 1 món thần linh làm vật hiến tế !!!", "Đóng");
                }
                break;
            case NANG_CAP_BONG_TAI:
                if (player.combineNew.itemsCombine.size() == 2) {
                    Item bongTai = null;
                    Item manhVo = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (item.template.id == 454) {
                            bongTai = item;
                        } else if (item.template.id == 933) {
                            manhVo = item;
                        }
                    }
                    if (bongTai != null && manhVo != null && manhVo.quantity >= 99) {

                        player.combineNew.goldCombine = GOLD_BONG_TAI;
                        player.combineNew.gemCombine = GEM_BONG_TAI;
                        player.combineNew.ratioCombine = RATIO_BONG_TAI;

                        String npcSay = "Bông tai Porata cấp 2" + "\n|2|";
                        for (Item.ItemOption io : bongTai.itemOptions) {
                            npcSay += io.getOptionString() + "\n";
                        }
                        npcSay += "|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%" + "\n";
                        if (player.combineNew.goldCombine <= player.inventory.gold) {
                            npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";
                            baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                    "Nâng cấp\ncần " + player.combineNew.gemCombine + " ngọc");
                        } else {
                            npcSay += "Còn thiếu " + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.gold) + " vàng";
                            baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 1 Bông tai Porata cấp 1 và X99 Mảnh vỡ bông tai", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần 1 Bông tai Porata cấp 1 và X99 Mảnh vỡ bông tai", "Đóng");
                }
                break;
            case MO_CHI_SO_BONG_TAI:
                if (player.combineNew.itemsCombine.size() == 3) {
                    Item bongTai = null;
                    Item manhHon = null;
                    Item daXanhLam = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (item.template.id == 921) {
                            bongTai = item;
                        } else if (item.template.id == 934) {
                            manhHon = item;
                        } else if (item.template.id == 935) {
                            daXanhLam = item;
                        }
                    }
                    if (bongTai != null && manhHon != null && daXanhLam != null && manhHon.quantity >= 99) {

                        player.combineNew.goldCombine = GOLD_BONG_TAI;
                        player.combineNew.gemCombine = GEM_BONG_TAI;
                        player.combineNew.ratioCombine = RATIO_NANG_CAP;

                        String npcSay = "Bông tai Porata cấp 2" + "\n|2|";
                        for (Item.ItemOption io : bongTai.itemOptions) {
                            npcSay += io.getOptionString() + "\n";
                        }
                        npcSay += "|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%" + "\n";
                        if (player.combineNew.goldCombine <= player.inventory.gold) {
                            npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";
                            baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                    "Nâng cấp\ncần " + player.combineNew.gemCombine + " ngọc");
                        } else {
                            npcSay += "Còn thiếu " + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.gold) + " vàng";
                            baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 1 Bông tai Porata cấp 2, X99 Mảnh hồn bông tai và 1 Đá xanh lam", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần 1 Bông tai Porata cấp 2, X99 Mảnh hồn bông tai và 1 Đá xanh lam", "Đóng");
                }
                break;
                case REN_KIEM_Z:
    if (player.combineNew.itemsCombine.size() == 2) {
        Item manhKiemZ = null;
        Item quangKiemZ = null;
        for (Item item : player.combineNew.itemsCombine) {
            if (item.template.id == 865 || item.template.id == 1200) {
                manhKiemZ = item;
            } else if (item.template.id == 1201) {
                quangKiemZ = item;
            }
        }
        if (manhKiemZ != null && quangKiemZ != null && quangKiemZ.quantity >= 99) {
            player.combineNew.goldCombine = GOLD_KIEM_Z;
            player.combineNew.gemCombine = GEM_KIEM_Z;
            player.combineNew.ratioCombine = RATIO_KIEM_Z2;
            String npcSay = "Kiếm Z cấp 1" + "\n|2|";
            for (Item.ItemOption io : manhKiemZ.itemOptions) {
                npcSay += io.getOptionString() + "\n";
            }
            npcSay += "|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%" + "\n";
            if (player.combineNew.goldCombine <= player.inventory.gold) {
                npcSay += "|1|Rèn Kiếm Z " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";
                tosukaio.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                        "Rèn Kiếm Z\ncần " + player.combineNew.gemCombine + " Ngọc xanh");
            } else {
                npcSay += "Còn thiếu " + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.gold) + " vàng";
                tosukaio.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
            }
        } else if (manhKiemZ == null || quangKiemZ == null) {
            this.tosukaio.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                    "Cần 1 Kiếm Z và X99 Quặng Kiếm Z", "Đóng");
        } else {
            this.tosukaio.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                    "Số lượng quặng Kiếm Z không đủ", "Đóng");
        }
    } else {
        this.tosukaio.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                "Cần 1 Kiếm Z và X99 Quặng Kiếm Z", "Đóng");
    }
    break;

            case EP_SAO_TRANG_BI:
                if (player.combineNew.itemsCombine.size() == 2) {
                    Item trangBi = null;
                    Item daPhaLe = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (isTrangBiPhaLeHoa(item)) {
                            trangBi = item;
                        } else if (isDaPhaLe(item)) {
                            daPhaLe = item;
                        }
                    }
                    int star = 0; //sao pha lê đã ép
                    int starEmpty = 0; //lỗ sao pha lê
                    if (trangBi != null && daPhaLe != null) {
                        for (Item.ItemOption io : trangBi.itemOptions) {
                            if (io.optionTemplate.id == 102) {
                                star = io.param;
                            } else if (io.optionTemplate.id == 107) {
                                starEmpty = io.param;
                            }
                        }
                        if (star < starEmpty) {
                            player.combineNew.gemCombine = getGemEpSao(star);
                            String npcSay = trangBi.template.name + "\n|2|";
                            for (Item.ItemOption io : trangBi.itemOptions) {
                                if (io.optionTemplate.id != 102) {
                                    npcSay += io.getOptionString() + "\n";
                                }
                            }
                            if (daPhaLe.template.type == 30) {
                                for (Item.ItemOption io : daPhaLe.itemOptions) {
                                    npcSay += "|7|" + io.getOptionString() + "\n";
                                }
                            } else {
                                npcSay += "|7|" + ItemService.gI().getItemOptionTemplate(getOptionDaPhaLe(daPhaLe)).name.replaceAll("#", getParamDaPhaLe(daPhaLe) + "") + "\n";
                            }
                            npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.gemCombine) + " ngọc";
                            baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                    "Nâng cấp\ncần " + player.combineNew.gemCombine + " ngọc");

                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                    "Cần 1 trang bị có lỗ sao pha lê và 1 loại đá pha lê để ép vào", "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 1 trang bị có lỗ sao pha lê và 1 loại đá pha lê để ép vào", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần 1 trang bị có lỗ sao pha lê và 1 loại đá pha lê để ép vào", "Đóng");
                }
                break;
            case PHA_LE_HOA_TRANG_BI:
                if (player.combineNew.itemsCombine.size() == 1) {
                    Item item = player.combineNew.itemsCombine.get(0);
                    if (isTrangBiPhaLeHoa(item)) {
                        int star = 0;
                        for (Item.ItemOption io : item.itemOptions) {
                            if (io.optionTemplate.id == 107) {
                                star = io.param;
                                break;
                            }
                        }
                        if (star < MAX_STAR_ITEM) {
                            player.combineNew.goldCombine = getGoldPhaLeHoa(star);
                            player.combineNew.gemCombine = getGemPhaLeHoa(star);
                            player.combineNew.ratioCombine = getRatioPhaLeHoa(star);

                            String npcSay = item.template.name + "\n|2|";
                            for (Item.ItemOption io : item.itemOptions) {
                                if (io.optionTemplate.id != 102) {
                                    npcSay += io.getOptionString() + "\n";
                                }
                            }
                            npcSay += "|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%" + "\n";
                            if (player.combineNew.goldCombine <= player.inventory.gold) {
                                npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";
                                baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                        "Nâng cấp\ncần " + player.combineNew.gemCombine + " ngọc");
                            } else {
                                npcSay += "Còn thiếu " + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.gold) + " vàng";
                                baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                            }

                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Vật phẩm đã đạt tối đa sao pha lê", "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Vật phẩm này không thể đục lỗ", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy hãy chọn 1 vật phẩm để pha lê hóa", "Đóng");
                }
                break;
            case NHAP_NGOC_RONG:
                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                    if (player.combineNew.itemsCombine.size() == 1) {
                        Item item = player.combineNew.itemsCombine.get(0);
                        if (item != null && item.isNotNullItem() && (item.template.id > 14 && item.template.id <= 20) && item.quantity >= 7) {
                            String npcSay = "|2|Con có muốn biến 7 " + item.template.name + " thành\n"
                                    + "1 viên " + ItemService.gI().getTemplate((short) (item.template.id - 1)).name + "\n"
                                    + "|7|Cần 7 " + item.template.name;
                            this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay, "Làm phép", "Từ chối");
                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cần 7 viên ngọc rồng 2 sao trở lên", "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cần 7 viên ngọc rồng 2 sao trở lên", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hành trang cần ít nhất 1 chỗ trống", "Đóng");
                }
                break;
                
            case CHE_TAO_TRANG_BI_TS:
                 if (player.combineNew.itemsCombine.size() == 0) {
                    this.whis.createOtherMenu(player, ConstNpc.IGNORE_MENU, "CHIEN_COC_CHEN", "Yes");
                    return;
                }
                  if (player.combineNew.itemsCombine.size() >= 2 &&  player.combineNew.itemsCombine.size() < 5) {
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() &&  item.isCongThucVip()).count() < 1) {
                        this.whis.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu Công thức Vip", "Đóng");
                        return;
                    }
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isManhTS() && item.quantity >= 999).count() < 1) {
                        this.whis.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu Mảnh đồ thiên sứ", "Đóng");
                        return;
                    }
//                    if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDaNangCap()).count() < 1 || player.combineNew.itemsCombine.size() == 4 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDaNangCap()).count() < 1) {
//                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu Đá nâng cấp", "Đóng");
//                        return;
//                    }
//                    if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDaMayMan()).count() < 1 || player.combineNew.itemsCombine.size() == 4 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDaMayMan()).count() < 1) {
//                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu Đá may mắn", "Đóng");
//                        return;
//                    }
                    Item mTS = null, daNC = null, daMM = null;
                        for (Item item : player.combineNew.itemsCombine) {
                            if (item.isNotNullItem()) {
                                if (item.isManhTS()) {
                                mTS = item;
                            } else if (item.isDaNangCap()) {
                                daNC = item;
                            } else if (item.isDaMayMan()) {
                                daMM = item;
                            }
                        }
                    }
                    int tilemacdinh = 35;    
                    int tilenew = tilemacdinh;
//                    if (daNC != null) {
//                        tilenew += (daNC.template.id - 1073) * 10;                     
//                    }

                    String npcSay = "|2|Chế tạo " + player.combineNew.itemsCombine.stream().filter(Item::isManhTS).findFirst().get().typeNameManh() + " Thiên sứ " 
                            + player.combineNew.itemsCombine.stream().filter(Item::isCongThucVip).findFirst().get().typeHanhTinh() + "\n"
                            + "|7|Mảnh ghép " +  mTS.quantity + "/999\n";
                    if (daNC != null) {
                        npcSay += "|2|Đá nâng cấp " + player.combineNew.itemsCombine.stream().filter(Item::isDaNangCap).findFirst().get().typeDanangcap() 
                                  + " (+" + (daNC.template.id - 1073) + "0% tỉ lệ thành công)\n";
                    }
                    if (daMM != null) {
                        npcSay += "|2|Đá may mắn " + player.combineNew.itemsCombine.stream().filter(Item::isDaMayMan).findFirst().get().typeDaMayman()
                                  + " (+" + (daMM.template.id - 1078) + "0% tỉ lệ tối đa các chỉ số)\n";
                    }
                    if (daNC != null) {
                        tilenew += (daNC.template.id - 1073) * 10;
                        npcSay += "|2|Tỉ lệ thành công: " + tilenew + "%\n";
                    } else {
                        npcSay += "|2|Tỉ lệ thành công: " + tilemacdinh + "%\n";
                    }
                    npcSay += "|7|Phí nâng cấp: 500 triệu vàng";
                    if (player.inventory.gold < 500000000) {
                        this.whis.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Bạn không đủ vàng", "Đóng");
                        return;
                    }
                    this.whis.createOtherMenu(player, ConstNpc.MENU_DAP_DO,
                            npcSay, "Nâng cấp\n500 Tr vàng", "Từ chối");
                } else {
                    if (player.combineNew.itemsCombine.size() > 4) {
                        this.whis.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Nguyên liệu không phù hợp", "Đóng");
                        return;
                    }
                    this.whis.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Không đủ nguyên liệu, mời quay lại sau", "Đóng");
                }
                break;    
                
            case NANG_CAP_VAT_PHAM:
                if (player.combineNew.itemsCombine.size() >= 2 && player.combineNew.itemsCombine.size() < 4) {
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.type < 5).count() < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đồ nâng cấp", "Đóng");
                        break;
                    }
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.type == 14).count() < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đá nâng cấp", "Đóng");
                        break;
                    }
                    if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.id == 987).count() < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đồ nâng cấp", "Đóng");
                        break;
                    }
                    Item itemDo = null;
                    Item itemDNC = null;
                    Item itemDBV = null;
                    for (int j = 0; j < player.combineNew.itemsCombine.size(); j++) {
                        if (player.combineNew.itemsCombine.get(j).isNotNullItem()) {
                            if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.get(j).template.id == 987) {
                                itemDBV = player.combineNew.itemsCombine.get(j);
                                continue;
                            }
                            if (player.combineNew.itemsCombine.get(j).template.type < 5) {
                                itemDo = player.combineNew.itemsCombine.get(j);
                            } else {
                                itemDNC = player.combineNew.itemsCombine.get(j);
                            }
                        }
                    }
                    if (isCoupleItemNangCapCheck(itemDo, itemDNC)) {
                        int level = 0;
                        for (Item.ItemOption io : itemDo.itemOptions) {
                            if (io.optionTemplate.id == 72) {
                                level = io.param;
                                break;
                            }
                        }
                        if (level < MAX_LEVEL_ITEM) {
                            player.combineNew.goldCombine = getGoldNangCapDo(level);
                            player.combineNew.ratioCombine = (float) getTileNangCapDo(level);
                            player.combineNew.countDaNangCap = getCountDaNangCapDo(level);
                            player.combineNew.countDaBaoVe = (short) getCountDaBaoVe(level);
                            String npcSay = "|2|Hiện tại " + itemDo.template.name + " (+" + level + ")\n|0|";
                            for (Item.ItemOption io : itemDo.itemOptions) {
                                if (io.optionTemplate.id != 72) {
                                    npcSay += io.getOptionString() + "\n";
                                }
                            }
                            String option = null;
                            int param = 0;
                            for (Item.ItemOption io : itemDo.itemOptions) {
                                if (io.optionTemplate.id == 47
                                        || io.optionTemplate.id == 6
                                        || io.optionTemplate.id == 0
                                        || io.optionTemplate.id == 7
                                        || io.optionTemplate.id == 14
                                        || io.optionTemplate.id == 22
                                        || io.optionTemplate.id == 23) {
                                    option = io.optionTemplate.name;
                                    param = io.param + (io.param * 10 / 100);
                                    break;
                                }
                            }
                            npcSay += "|2|Sau khi nâng cấp (+" + (level + 1) + ")\n|7|"
                                    + option.replaceAll("#", String.valueOf(param))
                                    + "\n|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%\n"
                                    + (player.combineNew.countDaNangCap > itemDNC.quantity ? "|7|" : "|1|")
                                    + "Cần " + player.combineNew.countDaNangCap + " " + itemDNC.template.name
                                    + "\n" + (player.combineNew.goldCombine > player.inventory.gold ? "|7|" : "|1|")
                                    + "Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";

                            String daNPC = player.combineNew.itemsCombine.size() == 3 && itemDBV != null ? String.format("\nCần tốn %s đá bảo vệ", player.combineNew.countDaBaoVe) : "";
                            if ((level == 2 || level == 4 || level == 6) && !(player.combineNew.itemsCombine.size() == 3 && itemDBV != null)) {
                                npcSay += "\nNếu thất bại sẽ rớt xuống (+" + (level - 1) + ")";
                            }
                            if (player.combineNew.countDaNangCap > itemDNC.quantity) {
                                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                        npcSay, "Còn thiếu\n" + (player.combineNew.countDaNangCap - itemDNC.quantity) + " " + itemDNC.template.name);
                            } else if (player.combineNew.goldCombine > player.inventory.gold) {
                                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                        npcSay, "Còn thiếu\n" + Util.numberToMoney((player.combineNew.goldCombine - player.inventory.gold)) + " vàng");
                            } else if (player.combineNew.itemsCombine.size() == 3 && Objects.nonNull(itemDBV) && itemDBV.quantity < player.combineNew.countDaBaoVe) {
                                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                        npcSay, "Còn thiếu\n" + (player.combineNew.countDaBaoVe - itemDBV.quantity) + " đá bảo vệ");
                            } else {
                                this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                                        npcSay, "Nâng cấp\n" + Util.numberToMoney(player.combineNew.goldCombine) + " vàng" + daNPC, "Từ chối");
                            }
                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Trang bị của ngươi đã đạt cấp tối đa", "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy chọn 1 trang bị và 1 loại đá nâng cấp", "Đóng");
                    }
                } else {
                    if (player.combineNew.itemsCombine.size() > 3) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cất đi con ta không thèm", "Đóng");
                        break;
                    }
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy chọn 1 trang bị và 1 loại đá nâng cấp", "Đóng");
                }
                break;
        }
    }

    /**
     * Bắt đầu đập đồ - điều hướng từng loại đập đồ
     *
     * @param player
     */
    public void startCombine(Player player) {
        switch (player.combineNew.typeCombine) {
            case NANG_CAP_KICH_HOAT:
                nangCapDoKichHoat(player);
                break;
            case EP_SAO_TRANG_BI:
                epSaoTrangBi(player);
                break;
            case PHA_LE_HOA_TRANG_BI:
                phaLeHoaTrangBi(player);
                break;
            case CHUYEN_HOA_TRANG_BI:
                break;
//            case NANG_KICH_HOAT_VIP:
//                nangKichHoatVip(player);
//                break;
            case NHAP_NGOC_RONG:
                nhapNgocRong(player);
                break;
            case NANG_CAP_VAT_PHAM:
                nangCapVatPham(player);
                break;
            case NANG_CAP_BONG_TAI:
                nangCapBongTai(player);
                break;
            case MO_CHI_SO_BONG_TAI:
                moChiSoBongTai(player);
            case REN_KIEM_Z:
                renKiemZ(player);
                break;
            case CHE_TAO_TRANG_BI_TS:
                openCreateItemAngel(player);
                break; 
        }

        player.iDMark.setIndexMenu(ConstNpc.IGNORE_MENU);
        player.combineNew.clearParamCombine();
        player.combineNew.lastTimeCombine = System.currentTimeMillis();

    }

  private void nangCapDoKichHoat(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {
                 if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && (item.template.id >= 555 && item.template.id <= 567)).count() < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu trang bị nâng cấp", "Đóng");
                   return;
                    } 
                    if (!player.combineNew.itemsCombine.get(1).isDTL()) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thần Link", "Đóng");
                 return;
                    }
            Item trangbicui = null;
            Item trangbithanlinh = null;
            if(player.combineNew.itemsCombine.get(1).isNotNullItem()){
            if(player.combineNew.itemsCombine.get(1).isDTL()) {
                trangbithanlinh = player.combineNew.itemsCombine.get(1);
            }
            }
            for (int j = 0; j <1; j++) {
                if (player.combineNew.itemsCombine.get(j).isNotNullItem()) {
                    if (player.combineNew.itemsCombine.get(j).template.id == 555) {
                        trangbicui = player.combineNew.itemsCombine.get(j);
                    }
                    if (player.combineNew.itemsCombine.get(j).template.id == 556) {
                        trangbicui = player.combineNew.itemsCombine.get(j);
                    }
                    if (player.combineNew.itemsCombine.get(j).template.id == 557) {
                        trangbicui = player.combineNew.itemsCombine.get(j);
                    }
                    if (player.combineNew.itemsCombine.get(j).template.id == 558) {
                        trangbicui = player.combineNew.itemsCombine.get(j);
                    }
                    if (player.combineNew.itemsCombine.get(j).template.id == 559) {
                        trangbicui = player.combineNew.itemsCombine.get(j);
                    }
                    if (player.combineNew.itemsCombine.get(j).template.id == 560) {
                        trangbicui = player.combineNew.itemsCombine.get(j);
                    }
                    if (player.combineNew.itemsCombine.get(j).template.id == 561) {
                        trangbicui = player.combineNew.itemsCombine.get(j);
                    }
                    if (player.combineNew.itemsCombine.get(j).template.id == 562) {
                        trangbicui = player.combineNew.itemsCombine.get(j);
                    }
                    if (player.combineNew.itemsCombine.get(j).template.id == 563) {
                        trangbicui = player.combineNew.itemsCombine.get(j);
                    }
                    if (player.combineNew.itemsCombine.get(j).template.id == 564) {
                        trangbicui = player.combineNew.itemsCombine.get(j);
                    }
                    if (player.combineNew.itemsCombine.get(j).template.id == 565) {
                        trangbicui = player.combineNew.itemsCombine.get(j);
                    }
                    if (player.combineNew.itemsCombine.get(j).template.id == 566) {
                        trangbicui = player.combineNew.itemsCombine.get(j);
                    }
                    if (player.combineNew.itemsCombine.get(j).template.id == 567) {
                        trangbicui = player.combineNew.itemsCombine.get(j);
                    }
                }
            }
            int gold = player.combineNew.goldCombine;
            if (player.inventory.gold < gold) {
                Service.getInstance().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            }
            if (trangbicui != null && trangbithanlinh != null) {
                player.inventory.gold -= gold;
                if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
                    if (trangbicui.template.id == 555) {
                        Item dkh = ItemService.gI().createNewItem((short) 0);
                        if (Util.isTrue(35, 100)) {
                            dkh.itemOptions.add(new Item.ItemOption(47, 5));
                            dkh.itemOptions.add(new Item.ItemOption(127, 1));
                            dkh.itemOptions.add(new Item.ItemOption(139, 1));
                            dkh.itemOptions.add(new Item.ItemOption(30, 1));
                            InventoryServiceNew.gI().addItemBag(player, dkh);
                        }
                        else if (Util.isTrue(35, 100)) {
                            dkh.itemOptions.add(new Item.ItemOption(47, 5));
                            dkh.itemOptions.add(new Item.ItemOption(128, 1));
                            dkh.itemOptions.add(new Item.ItemOption(140, 1));
                            dkh.itemOptions.add(new Item.ItemOption(30, 1));
                            InventoryServiceNew.gI().addItemBag(player, dkh);
                        }
                        else {
                            dkh.itemOptions.add(new Item.ItemOption(47, 5));
                            dkh.itemOptions.add(new Item.ItemOption(129, 1));
                            dkh.itemOptions.add(new Item.ItemOption(141, 1));
                            dkh.itemOptions.add(new Item.ItemOption(30, 1));
                            InventoryServiceNew.gI().addItemBag(player, dkh);
                        }
                    }
                    if (trangbicui.template.id == 556) {
                        Item dkh = ItemService.gI().createNewItem((short) 6);
                        if (Util.isTrue(35, 100)) {
                            dkh.itemOptions.add(new Item.ItemOption(6, 20));
                            dkh.itemOptions.add(new Item.ItemOption(127, 1));
                            dkh.itemOptions.add(new Item.ItemOption(139, 1));
                            dkh.itemOptions.add(new Item.ItemOption(30, 1));
                            InventoryServiceNew.gI().addItemBag(player, dkh);
                        }
                        else if (Util.isTrue(35, 100)) {
                            dkh.itemOptions.add(new Item.ItemOption(6, 20));
                            dkh.itemOptions.add(new Item.ItemOption(128, 1));
                            dkh.itemOptions.add(new Item.ItemOption(140, 1));
                            dkh.itemOptions.add(new Item.ItemOption(30, 1));
                            InventoryServiceNew.gI().addItemBag(player, dkh);
                        }
                        else {
                            dkh.itemOptions.add(new Item.ItemOption(6, 20));
                            dkh.itemOptions.add(new Item.ItemOption(129, 1));
                            dkh.itemOptions.add(new Item.ItemOption(141, 1));
                            dkh.itemOptions.add(new Item.ItemOption(30, 1));
                            InventoryServiceNew.gI().addItemBag(player, dkh);
                        }
                    }
                    if (trangbicui.template.id == 557) {
                        Item dkh = ItemService.gI().createNewItem((short) 1);
                        if (Util.isTrue(35, 100)) {
                            dkh.itemOptions.add(new Item.ItemOption(47, 5));
                            dkh.itemOptions.add(new Item.ItemOption(130, 1));
                            dkh.itemOptions.add(new Item.ItemOption(142, 1));
                            dkh.itemOptions.add(new Item.ItemOption(30, 1));
                            InventoryServiceNew.gI().addItemBag(player, dkh);
                        }
                        else if (Util.isTrue(35, 100)) {
                            dkh.itemOptions.add(new Item.ItemOption(47, 5));
                            dkh.itemOptions.add(new Item.ItemOption(132, 1));
                            dkh.itemOptions.add(new Item.ItemOption(144, 1));
                            dkh.itemOptions.add(new Item.ItemOption(30, 1));
                            InventoryServiceNew.gI().addItemBag(player, dkh);
                        }
                        else {
                            dkh.itemOptions.add(new Item.ItemOption(47, 5));
                            dkh.itemOptions.add(new Item.ItemOption(131, 1));
                            dkh.itemOptions.add(new Item.ItemOption(143, 1));
                            dkh.itemOptions.add(new Item.ItemOption(30, 1));
                            InventoryServiceNew.gI().addItemBag(player, dkh);
                        }
                    }
                    if (trangbicui.template.id == 558) {
                        Item dkh = ItemService.gI().createNewItem((short) 7);
                        if (Util.isTrue(35, 100)) {
                            dkh.itemOptions.add(new Item.ItemOption(6, 20));
                            dkh.itemOptions.add(new Item.ItemOption(130, 1));
                            dkh.itemOptions.add(new Item.ItemOption(142, 1));
                            dkh.itemOptions.add(new Item.ItemOption(30, 1));
                            InventoryServiceNew.gI().addItemBag(player, dkh);
                        }
                        else if (Util.isTrue(35, 100)) {
                            dkh.itemOptions.add(new Item.ItemOption(6, 20));
                            dkh.itemOptions.add(new Item.ItemOption(132, 1));
                            dkh.itemOptions.add(new Item.ItemOption(144, 1));
                            dkh.itemOptions.add(new Item.ItemOption(30, 1));
                            InventoryServiceNew.gI().addItemBag(player, dkh);
                        }
                        else {
                            dkh.itemOptions.add(new Item.ItemOption(6, 20));
                            dkh.itemOptions.add(new Item.ItemOption(131, 1));
                            dkh.itemOptions.add(new Item.ItemOption(143, 1));
                            dkh.itemOptions.add(new Item.ItemOption(30, 1));
                            InventoryServiceNew.gI().addItemBag(player, dkh);
                        }
                    }
                    if (trangbicui.template.id == 559) {
                        Item dkh = ItemService.gI().createNewItem((short) 2);
                        if (Util.isTrue(35, 100)) {
                            dkh.itemOptions.add(new Item.ItemOption(47, 5));
                            dkh.itemOptions.add(new Item.ItemOption(133, 1));
                            dkh.itemOptions.add(new Item.ItemOption(136, 1));
                            dkh.itemOptions.add(new Item.ItemOption(30, 1));
                            InventoryServiceNew.gI().addItemBag(player, dkh);
                        }
                        else if (Util.isTrue(35, 100)) {
                            dkh.itemOptions.add(new Item.ItemOption(47, 5));
                            dkh.itemOptions.add(new Item.ItemOption(134, 1));
                            dkh.itemOptions.add(new Item.ItemOption(137, 1));
                            dkh.itemOptions.add(new Item.ItemOption(30, 1));
                            InventoryServiceNew.gI().addItemBag(player, dkh);
                        }
                        else {
                            dkh.itemOptions.add(new Item.ItemOption(47, 5));
                            dkh.itemOptions.add(new Item.ItemOption(135, 1));
                            dkh.itemOptions.add(new Item.ItemOption(138, 1));
                            dkh.itemOptions.add(new Item.ItemOption(30, 1));
                            InventoryServiceNew.gI().addItemBag(player, dkh);
                        }
                    }
                    if (trangbicui.template.id == 560) {
                        Item dkh = ItemService.gI().createNewItem((short) 8);
                        if (Util.isTrue(35, 100)) {
                            dkh.itemOptions.add(new Item.ItemOption(6, 20));
                            dkh.itemOptions.add(new Item.ItemOption(133, 1));
                            dkh.itemOptions.add(new Item.ItemOption(136, 1));
                            dkh.itemOptions.add(new Item.ItemOption(30, 1));
                            InventoryServiceNew.gI().addItemBag(player, dkh);
                        }
                        else if (Util.isTrue(35, 100)) {
                            dkh.itemOptions.add(new Item.ItemOption(6, 20));
                            dkh.itemOptions.add(new Item.ItemOption(134, 1));
                            dkh.itemOptions.add(new Item.ItemOption(137, 1));
                            dkh.itemOptions.add(new Item.ItemOption(30, 1));
                            InventoryServiceNew.gI().addItemBag(player, dkh);
                        }
                        else {
                            dkh.itemOptions.add(new Item.ItemOption(6, 20));
                            dkh.itemOptions.add(new Item.ItemOption(135, 1));
                            dkh.itemOptions.add(new Item.ItemOption(138, 1));
                            dkh.itemOptions.add(new Item.ItemOption(30, 1));
                            InventoryServiceNew.gI().addItemBag(player, dkh);
                        }
                        
                    }
                    if (trangbicui.template.id == 561) {
                        Item dkh = ItemService.gI().createNewItem((short) 12);
                        if (player.gender == 0) {
                            if (Util.isTrue(35, 100)) {
                                dkh.itemOptions.add(new Item.ItemOption(14, 1));
                                dkh.itemOptions.add(new Item.ItemOption(127, 1));
                                dkh.itemOptions.add(new Item.ItemOption(139, 1));
                                dkh.itemOptions.add(new Item.ItemOption(30, 1));
                                InventoryServiceNew.gI().addItemBag(player, dkh);
                            } else if (Util.isTrue(35, 100)) {
                                dkh.itemOptions.add(new Item.ItemOption(14, 1));
                                dkh.itemOptions.add(new Item.ItemOption(128, 1));
                                dkh.itemOptions.add(new Item.ItemOption(140, 1));
                                dkh.itemOptions.add(new Item.ItemOption(30, 1));
                                InventoryServiceNew.gI().addItemBag(player, dkh);
                            } else {
                                dkh.itemOptions.add(new Item.ItemOption(14, 1));
                                dkh.itemOptions.add(new Item.ItemOption(129, 1));
                                dkh.itemOptions.add(new Item.ItemOption(141, 1));
                                dkh.itemOptions.add(new Item.ItemOption(30, 1));
                                InventoryServiceNew.gI().addItemBag(player, dkh);
                            }
                        }
                        if (player.gender == 1) {
                            if (Util.isTrue(35, 100)) {
                                dkh.itemOptions.add(new Item.ItemOption(14, 1));
                                dkh.itemOptions.add(new Item.ItemOption(130, 1));
                                dkh.itemOptions.add(new Item.ItemOption(142, 1));
                                dkh.itemOptions.add(new Item.ItemOption(30, 1));
                                InventoryServiceNew.gI().addItemBag(player, dkh);
                            } else if (Util.isTrue(35, 100)) {
                                dkh.itemOptions.add(new Item.ItemOption(14, 1));
                                dkh.itemOptions.add(new Item.ItemOption(132, 1));
                                dkh.itemOptions.add(new Item.ItemOption(144, 1));
                                dkh.itemOptions.add(new Item.ItemOption(30, 1));
                                InventoryServiceNew.gI().addItemBag(player, dkh);
                            } else {
                                dkh.itemOptions.add(new Item.ItemOption(14, 1));
                                dkh.itemOptions.add(new Item.ItemOption(131, 1));
                                dkh.itemOptions.add(new Item.ItemOption(143, 1));
                                dkh.itemOptions.add(new Item.ItemOption(30, 1));
                                InventoryServiceNew.gI().addItemBag(player, dkh);
                            }
                        }
                        if (player.gender == 2) {
                            if (Util.isTrue(35, 100)) {
                                dkh.itemOptions.add(new Item.ItemOption(14, 1));
                                dkh.itemOptions.add(new Item.ItemOption(133, 1));
                                dkh.itemOptions.add(new Item.ItemOption(136, 1));
                                dkh.itemOptions.add(new Item.ItemOption(30, 1));
                                InventoryServiceNew.gI().addItemBag(player, dkh);
                            } else if (Util.isTrue(35, 100)) {
                                dkh.itemOptions.add(new Item.ItemOption(14, 1));
                                dkh.itemOptions.add(new Item.ItemOption(134, 1));
                                dkh.itemOptions.add(new Item.ItemOption(137, 1));
                                dkh.itemOptions.add(new Item.ItemOption(30, 1));
                                InventoryServiceNew.gI().addItemBag(player, dkh);
                            } else {
                                dkh.itemOptions.add(new Item.ItemOption(14, 1));
                                dkh.itemOptions.add(new Item.ItemOption(135, 1));
                                dkh.itemOptions.add(new Item.ItemOption(138, 1));
                                dkh.itemOptions.add(new Item.ItemOption(30, 1));
                                InventoryServiceNew.gI().addItemBag(player, dkh);
                            }
                        }
                    }
                    if (trangbicui.template.id == 562) {
                        Item dkh = ItemService.gI().createNewItem((short) 21);
                        if (Util.isTrue(35, 100)) {
                            dkh.itemOptions.add(new Item.ItemOption(0, 5));
                            dkh.itemOptions.add(new Item.ItemOption(127, 1));
                            dkh.itemOptions.add(new Item.ItemOption(139, 1));
                            dkh.itemOptions.add(new Item.ItemOption(30, 1));
                            InventoryServiceNew.gI().addItemBag(player, dkh);
                        }
                        else if (Util.isTrue(35, 100)) {
                            dkh.itemOptions.add(new Item.ItemOption(0, 5));
                            dkh.itemOptions.add(new Item.ItemOption(128, 1));
                            dkh.itemOptions.add(new Item.ItemOption(140, 1));
                            dkh.itemOptions.add(new Item.ItemOption(30, 1));
                            InventoryServiceNew.gI().addItemBag(player, dkh);
                        }
                        else {
                            dkh.itemOptions.add(new Item.ItemOption(0, 5));
                            dkh.itemOptions.add(new Item.ItemOption(129, 1));
                            dkh.itemOptions.add(new Item.ItemOption(141, 1));
                            dkh.itemOptions.add(new Item.ItemOption(30, 1));
                            InventoryServiceNew.gI().addItemBag(player, dkh);
                        }
                    }
                    if (trangbicui.template.id == 563) {
                        Item dkh = ItemService.gI().createNewItem((short) 27);
                        if (Util.isTrue(35, 100)) {
                            dkh.itemOptions.add(new Item.ItemOption(7, 15));
                            dkh.itemOptions.add(new Item.ItemOption(127, 1));
                            dkh.itemOptions.add(new Item.ItemOption(139, 1));
                            dkh.itemOptions.add(new Item.ItemOption(30, 1));
                            InventoryServiceNew.gI().addItemBag(player, dkh);
                        }
                        else if (Util.isTrue(35, 100)) {
                            dkh.itemOptions.add(new Item.ItemOption(7, 15));
                            dkh.itemOptions.add(new Item.ItemOption(128, 1));
                            dkh.itemOptions.add(new Item.ItemOption(140, 1));
                            dkh.itemOptions.add(new Item.ItemOption(30, 1));
                            InventoryServiceNew.gI().addItemBag(player, dkh);
                        }
                        else {
                            dkh.itemOptions.add(new Item.ItemOption(7, 15));
                            dkh.itemOptions.add(new Item.ItemOption(129, 1));
                            dkh.itemOptions.add(new Item.ItemOption(141, 1));
                            dkh.itemOptions.add(new Item.ItemOption(30, 1));
                            InventoryServiceNew.gI().addItemBag(player, dkh);
                        }
                    }
                    if (trangbicui.template.id == 564) {
                        Item dkh = ItemService.gI().createNewItem((short) 22);
                        if (Util.isTrue(35, 100)) {
                            dkh.itemOptions.add(new Item.ItemOption(0, 5));
                            dkh.itemOptions.add(new Item.ItemOption(130, 1));
                            dkh.itemOptions.add(new Item.ItemOption(142, 1));
                            dkh.itemOptions.add(new Item.ItemOption(30, 1));
                            InventoryServiceNew.gI().addItemBag(player, dkh);
                        } else if (Util.isTrue(35, 100)) {
                            dkh.itemOptions.add(new Item.ItemOption(0, 5));
                            dkh.itemOptions.add(new Item.ItemOption(132, 1));
                            dkh.itemOptions.add(new Item.ItemOption(144, 1));
                            dkh.itemOptions.add(new Item.ItemOption(30, 1));
                            InventoryServiceNew.gI().addItemBag(player, dkh);
                        } else {
                            dkh.itemOptions.add(new Item.ItemOption(0, 5));
                            dkh.itemOptions.add(new Item.ItemOption(131, 1));
                            dkh.itemOptions.add(new Item.ItemOption(143, 1));
                            dkh.itemOptions.add(new Item.ItemOption(30, 1));
                            InventoryServiceNew.gI().addItemBag(player, dkh);
                        }
                    }
                    if (trangbicui.template.id == 565) {
                        Item dkh = ItemService.gI().createNewItem((short) 28);
                        if (Util.isTrue(35, 100)) {
                            dkh.itemOptions.add(new Item.ItemOption(7, 15));
                            dkh.itemOptions.add(new Item.ItemOption(130, 1));
                            dkh.itemOptions.add(new Item.ItemOption(142, 1));
                            dkh.itemOptions.add(new Item.ItemOption(30, 1));
                            InventoryServiceNew.gI().addItemBag(player, dkh);
                        } else if (Util.isTrue(35, 100)) {
                            dkh.itemOptions.add(new Item.ItemOption(7, 15));
                            dkh.itemOptions.add(new Item.ItemOption(132, 1));
                            dkh.itemOptions.add(new Item.ItemOption(144, 1));
                            dkh.itemOptions.add(new Item.ItemOption(30, 1));
                            InventoryServiceNew.gI().addItemBag(player, dkh);
                        } else {
                            dkh.itemOptions.add(new Item.ItemOption(7, 15));
                            dkh.itemOptions.add(new Item.ItemOption(131, 1));
                            dkh.itemOptions.add(new Item.ItemOption(143, 1));
                            dkh.itemOptions.add(new Item.ItemOption(30, 1));
                            InventoryServiceNew.gI().addItemBag(player, dkh);
                        }
                    }
                    if (trangbicui.template.id == 566) {
                        Item dkh = ItemService.gI().createNewItem((short) 23);
                        if (Util.isTrue(35, 100)) {
                            dkh.itemOptions.add(new Item.ItemOption(0, 5));
                            dkh.itemOptions.add(new Item.ItemOption(133, 1));
                            dkh.itemOptions.add(new Item.ItemOption(136, 1));
                            dkh.itemOptions.add(new Item.ItemOption(30, 1));
                            InventoryServiceNew.gI().addItemBag(player, dkh);
                        } else if (Util.isTrue(35, 100)) {
                            dkh.itemOptions.add(new Item.ItemOption(0, 5));
                            dkh.itemOptions.add(new Item.ItemOption(134, 1));
                            dkh.itemOptions.add(new Item.ItemOption(137, 1));
                            dkh.itemOptions.add(new Item.ItemOption(30, 1));
                            InventoryServiceNew.gI().addItemBag(player, dkh);
                        } else {
                            dkh.itemOptions.add(new Item.ItemOption(0, 5));
                            dkh.itemOptions.add(new Item.ItemOption(135, 1));
                            dkh.itemOptions.add(new Item.ItemOption(138, 1));
                            dkh.itemOptions.add(new Item.ItemOption(30, 1));
                            InventoryServiceNew.gI().addItemBag(player, dkh);
                        }
                    }
                    if (trangbicui.template.id == 567) {
                        Item dkh = ItemService.gI().createNewItem((short) 29);
                        if (Util.isTrue(35, 100)) {
                            dkh.itemOptions.add(new Item.ItemOption(7, 15));
                            dkh.itemOptions.add(new Item.ItemOption(133, 1));
                            dkh.itemOptions.add(new Item.ItemOption(136, 1));
                            dkh.itemOptions.add(new Item.ItemOption(30, 1));
                            InventoryServiceNew.gI().addItemBag(player, dkh);
                        } else if (Util.isTrue(35, 100)) {
                            dkh.itemOptions.add(new Item.ItemOption(7, 15));
                            dkh.itemOptions.add(new Item.ItemOption(134, 1));
                            dkh.itemOptions.add(new Item.ItemOption(137, 1));
                            dkh.itemOptions.add(new Item.ItemOption(30, 1));
                            InventoryServiceNew.gI().addItemBag(player, dkh);
                        } else {
                            dkh.itemOptions.add(new Item.ItemOption(7, 15));
                            dkh.itemOptions.add(new Item.ItemOption(135, 1));
                            dkh.itemOptions.add(new Item.ItemOption(138, 1));
                            dkh.itemOptions.add(new Item.ItemOption(30, 1));
                            InventoryServiceNew.gI().addItemBag(player, dkh);
                        }
                        
                    }
                    InventoryServiceNew.gI().subQuantityItemsBag(player, trangbithanlinh, 1);
                    InventoryServiceNew.gI().subQuantityItemsBag(player, trangbicui, 1);
                    InventoryServiceNew.gI().sendItemBags(player);
                    Service.getInstance().sendMoney(player);
                    reOpenItemCombine(player);
                    sendEffectSuccessCombine(player);
                }
            }
        }
    }
    private void doiKiemz(Player player) {
        if (player.combineNew.itemsCombine.size() == 3) {
            Item keo = null, luoiKiem = null, chuoiKiem = null;
            for (Item it : player.combineNew.itemsCombine) {
                if (it.template.id == 2015) {
                    keo = it;
                } else if (it.template.id == 2016) {
                    chuoiKiem = it;
                } else if (it.template.id == 2017) {
                    luoiKiem = it;
                }
            }
            if (keo != null && keo.quantity >= 99 && luoiKiem != null && chuoiKiem != null) {
                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                    sendEffectSuccessCombine(player);
                    Item item = ItemService.gI().createNewItem((short) 2018);
                    item.itemOptions.add(new Item.ItemOption(50, Util.nextInt(9, 15)));
                    item.itemOptions.add(new Item.ItemOption(77, Util.nextInt(8, 15)));
                    item.itemOptions.add(new Item.ItemOption(103, Util.nextInt(8, 15)));
                    if (Util.isTrue(80, 100)) {
                        item.itemOptions.add(new Item.ItemOption(93, Util.nextInt(1, 15)));
                    }
                    InventoryServiceNew.gI().addItemBag(player, item);

                    InventoryServiceNew.gI().subQuantityItemsBag(player, keo, 99);
                    InventoryServiceNew.gI().subQuantityItemsBag(player, luoiKiem, 1);
                    InventoryServiceNew.gI().subQuantityItemsBag(player, chuoiKiem, 1);

                    InventoryServiceNew.gI().sendItemBags(player);
                    Service.getInstance().sendMoney(player);
                    reOpenItemCombine(player);
                }
            }
        }
    }

    private void doiChuoiKiem(Player player) {
        if (player.combineNew.itemsCombine.size() == 1) {
            Item manhNhua = player.combineNew.itemsCombine.get(0);
            if (manhNhua.template.id == 2014 && manhNhua.quantity >= 99) {
                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                    sendEffectSuccessCombine(player);
                    Item item = ItemService.gI().createNewItem((short) 2016);
                    InventoryServiceNew.gI().addItemBag(player, item);

                    InventoryServiceNew.gI().subQuantityItemsBag(player, manhNhua, 99);

                    InventoryServiceNew.gI().sendItemBags(player);
                    Service.getInstance().sendMoney(player);
                    reOpenItemCombine(player);
                }
            }
        }
    }

    private void doiLuoiKiem(Player player) {
        if (player.combineNew.itemsCombine.size() == 1) {
            Item manhSat = player.combineNew.itemsCombine.get(0);
            if (manhSat.template.id == 2013 && manhSat.quantity >= 99) {
                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                    sendEffectSuccessCombine(player);
                    Item item = ItemService.gI().createNewItem((short) 2017);
                    InventoryServiceNew.gI().addItemBag(player, item);
                    InventoryServiceNew.gI().subQuantityItemsBag(player, manhSat, 99);

                    InventoryServiceNew.gI().sendItemBags(player);
                    Service.getInstance().sendMoney(player);
                    reOpenItemCombine(player);
                }
            }
        }
    }

    private void nangCapBongTai(Player player) {
    if (player.combineNew.itemsCombine.size() == 2) {
        int gold = player.combineNew.goldCombine;
        if (player.inventory.gold < gold) {
            Service.getInstance().sendThongBao(player, "Không đủ vàng để thực hiện");
            return;
        }
        
        int gem = player.combineNew.gemCombine;
        if (player.inventory.gem < gem) {
            Service.getInstance().sendThongBao(player, "Không đủ ngọc để thực hiện");
            return;
        }
        
        Item bongTai = null;
        Item manhVo = null;
        for (Item item : player.combineNew.itemsCombine) {
            if (item.template.id == 454) {
                bongTai = item;
            } else if (item.template.id == 933) {
                manhVo = item;
            }
        }
        
        if (bongTai != null && manhVo != null && manhVo.quantity >= 99) {
            Item findItemBag = InventoryServiceNew.gI().findItemBag(player, 921); //Khóa btc2
            if (findItemBag != null) {
                Service.getInstance().sendThongBao(player, "Ngươi đã có bông tai Porata cấp 2 trong hàng trang rồi, không thể nâng cấp nữa.");
                return;
            }
            player.inventory.gold -= gold;
            player.inventory.gem -= gem;
            InventoryServiceNew.gI().subQuantityItemsBag(player, manhVo, 99);
            if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
                bongTai.template = ItemService.gI().getTemplate(921);
                bongTai.itemOptions.add(new Item.ItemOption(72, 2));
                sendEffectSuccessCombine(player);
            } else {
                sendEffectFailCombine(player);
            }
            InventoryServiceNew.gI().sendItemBags(player);
            Service.getInstance().sendMoney(player);
            reOpenItemCombine(player);
        }
    }
}


    private void moChiSoBongTai(Player player) {
        if (player.combineNew.itemsCombine.size() == 3) {
            int gold = player.combineNew.goldCombine;
            if (player.inventory.gold < gold) {
                Service.getInstance().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            }
            int gem = player.combineNew.gemCombine;
            if (player.inventory.gem < gem) {
                Service.getInstance().sendThongBao(player, "Không đủ ngọc để thực hiện");
                return;
            }
            Item linhthu = null;
            Item thangtinhthach = null;
            Item thucan = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.template.id == 921) {
                    linhthu = item;
                } else if (item.template.id == 934) {
                    thangtinhthach = item;
                } else if (item.template.id == 935) {
                    thucan = item;
                }
            }
            if (linhthu != null && thangtinhthach != null && thangtinhthach.quantity >= 99) {
                player.inventory.gold -= gold;
                player.inventory.gem -= gem;
                InventoryServiceNew.gI().subQuantityItemsBag(player, thangtinhthach, 99);
                InventoryServiceNew.gI().subQuantityItemsBag(player, thucan, 1);
                if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
                    linhthu.itemOptions.clear();
                    linhthu.itemOptions.add(new Item.ItemOption(72, 2));
                    int rdUp = Util.nextInt(0, 7);
                    if (rdUp == 0) {
                        linhthu.itemOptions.add(new Item.ItemOption(50, Util.nextInt(5, 25)));
                    } else if (rdUp == 1) {
                        linhthu.itemOptions.add(new Item.ItemOption(77, Util.nextInt(5, 25)));
                    } else if (rdUp == 2) {
                        linhthu.itemOptions.add(new Item.ItemOption(103, Util.nextInt(5, 25)));
                    } else if (rdUp == 3) {
                        linhthu.itemOptions.add(new Item.ItemOption(108, Util.nextInt(5, 25)));
                    } else if (rdUp == 4) {
                        linhthu.itemOptions.add(new Item.ItemOption(94, Util.nextInt(5, 15)));
                    } else if (rdUp == 5) {
                        linhthu.itemOptions.add(new Item.ItemOption(14, Util.nextInt(5, 15)));
                    } else if (rdUp == 6) {
                        linhthu.itemOptions.add(new Item.ItemOption(80, Util.nextInt(5, 25)));
                    } else if (rdUp == 7) {
                        linhthu.itemOptions.add(new Item.ItemOption(81, Util.nextInt(5, 25)));
                    }
                    sendEffectSuccessCombine(player);
                } else {
                    sendEffectFailCombine(player);
                }
                InventoryServiceNew.gI().sendItemBags(player);
                Service.getInstance().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }
    
    private void renKiemZ(Player player) {
    if (player.combineNew.itemsCombine.size() == 2) {
        int gold = player.combineNew.goldCombine;
        if (player.inventory.gold < gold) {
            Service.getInstance().sendThongBao(player, "Không đủ vàng để thực hiện");
            return;
        }
        
        int gem = player.combineNew.gemCombine;
        if (player.inventory.gem < gem) {
            Service.getInstance().sendThongBao(player, "Không đủ ngọc để thực hiện");
            return;
        }
        
        Item manhKiemZ = null;
        Item quangKiemZ = null;
        for (Item item : player.combineNew.itemsCombine) {
            if (item.template.id == 865 || item.template.id == 1200) {
                manhKiemZ = item;
            } else if (item.template.id == 1201) {
                quangKiemZ = item;
            }
        }
        
        if (manhKiemZ != null && quangKiemZ != null && quangKiemZ.quantity >= 99) {
             //Item findItemBag = InventoryServiceNew.gI().findItemBag(player, 1200); //Nguyên liệu
            //if (findItemBag != null) {
                //Service.gI().sendThongBao(player, "Con đã có Kiếm Z trong hành trang rồi, không thể rèn nữa.");
                //return;
            //}
            player.inventory.gold -= gold;
            player.inventory.gem -= gem;
            InventoryServiceNew.gI().subQuantityItemsBag(player, quangKiemZ, 99);
            if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
    manhKiemZ.template = ItemService.gI().getTemplate(1200);
    manhKiemZ.itemOptions.clear();
    Random rand = new Random();
int ratioCombine = rand.nextInt(60) + 1;
int level = 0;
if (ratioCombine <= 40) {
    level = 1 + rand.nextInt(4);
} else if (ratioCombine <= 70) {
    level = 5 + rand.nextInt(4);
} else if (ratioCombine <= 90) {
    level = 9 + rand.nextInt(4);
} else if (ratioCombine <= 95) {
    level = 13 + rand.nextInt(3);
} else {
    level = 16;
}
manhKiemZ.itemOptions.add(new Item.ItemOption(0, level * 200 + 10000));
manhKiemZ.itemOptions.add(new Item.ItemOption(49, level * 1 + 20));
manhKiemZ.itemOptions.add(new Item.ItemOption(14, level));
manhKiemZ.itemOptions.add(new Item.ItemOption(97, level));
manhKiemZ.itemOptions.add(new Item.ItemOption(30, 0));
manhKiemZ.itemOptions.add(new Item.ItemOption(72, level));
sendEffectSuccessCombine(player);
            } else {
                sendEffectFailCombine(player);
            }
            InventoryServiceNew.gI().sendItemBags(player);
            Service.getInstance().sendMoney(player);
            reOpenItemCombine(player);
        }
    }
}
    
    public void openCreateItemAngel(Player player) {
        // Công thức vip + x999 Mảnh thiên sứ + đá nâng cấp + đá may mắn
        if (player.combineNew.itemsCombine.size() < 2 || player.combineNew.itemsCombine.size() > 4) {
            Service.getInstance().sendThongBao(player, "Thiếu vật phẩm, vui lòng thêm vào");
            return;
        }
        if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isCongThucVip()).count() != 1) {
            Service.getInstance().sendThongBao(player, "Thiếu Công thức Vip");
            return;
        }
        if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isManhTS() && item.quantity >= 999).count() != 1) {
            Service.getInstance().sendThongBao(player, "Thiếu Mảnh thiên sứ");
            return;
        }
//        if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDaNangCap()).count() != 1 || player.combineNew.itemsCombine.size() == 4 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDaNangCap()).count() != 1) {
//            Service.getInstance().sendThongBao(player, "Thiếu Đá nâng cấp");
//            return;
//        }
//        if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDaMayMan()).count() != 1 || player.combineNew.itemsCombine.size() == 4 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDaMayMan()).count() != 1) {
//            Service.getInstance().sendThongBao(player, "Thiếu Đá may mắn");
//            return;
//        }
        Item mTS = null, daNC = null, daMM = null, CtVip = null;
        for (Item item : player.combineNew.itemsCombine) {
                if (item.isNotNullItem()) {
                    if (item.isManhTS()) {
                        mTS = item;
                    } else if (item.isDaNangCap()) {
                        daNC = item;
                    } else if (item.isDaMayMan()) {
                        daMM = item;
                    } else if (item.isCongThucVip()) {
                        CtVip = item;
                    }
                }
            }
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0 ) {//check chỗ trống hành trang
            if (player.inventory.gold < 500000000) {
                Service.getInstance().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            }
                    player.inventory.gold -= 500000000;
                    
                    int tilemacdinh = 35;
                    int tileLucky = 20;
                    if (daNC != null) {
                        tilemacdinh += (daNC.template.id - 1073)*10;
                    } else {
                        tilemacdinh = tilemacdinh;
                    }
                    if (daMM != null) {
                        tileLucky += tileLucky*(daMM.template.id - 1078)*10/100;
                    } else {
                        tileLucky = tileLucky;
                    }
                    if (Util.nextInt(0, 100) < tilemacdinh) {
                        Item itemCtVip = player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isCongThucVip()).findFirst().get();
                        if (daNC != null) {
                        Item itemDaNangC = player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDaNangCap()).findFirst().get();
                        }
                        if (daMM != null) {
                        Item itemDaMayM = player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDaMayMan()).findFirst().get();
                        }
                        Item itemManh = player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isManhTS() && item.quantity >= 999).findFirst().get();
                        
                        tilemacdinh = Util.nextInt(0, 50);
                        if (tilemacdinh == 49) { tilemacdinh = 20;}
                        else if (tilemacdinh == 48 || tilemacdinh == 47) { tilemacdinh = 19;}
                        else if (tilemacdinh == 46 || tilemacdinh == 45) { tilemacdinh = 18;}
                        else if (tilemacdinh == 44 || tilemacdinh == 43) { tilemacdinh = 17;}
                        else if (tilemacdinh == 42 || tilemacdinh == 41) { tilemacdinh = 16;}
                        else if (tilemacdinh == 40 || tilemacdinh == 39) { tilemacdinh = 15;}
                        else if (tilemacdinh == 38 || tilemacdinh == 37) { tilemacdinh = 14;}
                        else if (tilemacdinh == 36 || tilemacdinh == 35) { tilemacdinh = 13;}
                        else if (tilemacdinh == 34 || tilemacdinh == 33) { tilemacdinh = 12;}
                        else if (tilemacdinh == 32 || tilemacdinh == 31) { tilemacdinh = 11;}
                        else if (tilemacdinh == 30 || tilemacdinh == 29) { tilemacdinh = 10;}
                        else if (tilemacdinh <= 28 || tilemacdinh >= 26) { tilemacdinh = 9;}
                        else if (tilemacdinh <= 25 || tilemacdinh >= 23) { tilemacdinh = 8;}
                        else if (tilemacdinh <= 22 || tilemacdinh >= 20) { tilemacdinh = 7;}
                        else if (tilemacdinh <= 19 || tilemacdinh >= 17) { tilemacdinh = 6;}
                        else if (tilemacdinh <= 16 || tilemacdinh >= 14) { tilemacdinh = 5;}
                        else if (tilemacdinh <= 13 || tilemacdinh >= 11) { tilemacdinh = 4;}
                        else if (tilemacdinh <= 10 || tilemacdinh >= 8) { tilemacdinh = 3;}
                        else if (tilemacdinh <= 7 || tilemacdinh >= 5) { tilemacdinh = 2;}
                        else if (tilemacdinh <= 4 || tilemacdinh >= 2) { tilemacdinh = 1;}
                        else if (tilemacdinh <= 1) { tilemacdinh = 0;}
                        short[][] itemIds = {{1048, 1051, 1054, 1057, 1060}, {1049, 1052, 1055, 1058, 1061}, {1050, 1053, 1056, 1059, 1062}}; // thứ tự td - 0,nm - 1, xd - 2

                        Item itemTS = ItemService.gI().DoThienSu(itemIds[itemCtVip.template.gender > 2 ? player.gender : itemCtVip.template.gender][itemManh.typeIdManh()], itemCtVip.template.gender);
                        
                        tilemacdinh += 10;
                        
                        if (tilemacdinh > 0) {
                            for(byte i = 0; i < itemTS.itemOptions.size(); i++) {
                            if(itemTS.itemOptions.get(i).optionTemplate.id != 21 && itemTS.itemOptions.get(i).optionTemplate.id != 30) {
                                itemTS.itemOptions.get(i).param += (itemTS.itemOptions.get(i).param*tilemacdinh/100);
                            }
                        }
                    }
                        tilemacdinh = Util.nextInt(0, 100);
                        
                        if (tilemacdinh <= tileLucky) {
                        if (tilemacdinh >= (tileLucky - 3)) {
                            tileLucky = 3;
                        } else if (tilemacdinh <= (tileLucky - 4) && tilemacdinh >= (tileLucky - 10)) {
                            tileLucky = 2;
                        } else { tileLucky = 1; }
                        itemTS.itemOptions.add(new Item.ItemOption(15, tileLucky));
                        ArrayList<Integer> listOptionBonus = new ArrayList<>();
                        listOptionBonus.add(50); 
                        listOptionBonus.add(77); 
                        listOptionBonus.add(103); 
                        listOptionBonus.add(98);
                        listOptionBonus.add(99);
                        for (int i = 0; i < tileLucky; i++) {
                            tilemacdinh = Util.nextInt(0, listOptionBonus.size());
                            itemTS.itemOptions.add(new ItemOption(listOptionBonus.get(tilemacdinh), Util.nextInt(1, 5)));
                            listOptionBonus.remove(tilemacdinh);
                        }
                    }
                        
                        InventoryServiceNew.gI().addItemBag(player, itemTS);
                        sendEffectSuccessCombine(player);
                    } else {
                        sendEffectFailCombine(player);
                    }
                    if (mTS != null && daMM != null && daNC != null && CtVip != null ) {
                        InventoryServiceNew.gI().subQuantityItemsBag(player, CtVip, 1);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, daNC, 1);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, mTS, 999);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, daMM, 1);
                    } else if (CtVip != null && mTS != null) {
                        InventoryServiceNew.gI().subQuantityItemsBag(player, CtVip, 1);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, mTS, 999);
                    } else if (CtVip != null && mTS != null && daNC != null) {
                        InventoryServiceNew.gI().subQuantityItemsBag(player, CtVip, 1);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, mTS, 999);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, daNC, 1);
                    } else if (CtVip != null && mTS != null && daMM != null) {
                        InventoryServiceNew.gI().subQuantityItemsBag(player, CtVip, 1);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, mTS, 999);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, daMM, 1);
                    }
                    
                    InventoryServiceNew.gI().sendItemBags(player);
                    Service.getInstance().sendMoney(player);
                    reOpenItemCombine(player);
                } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
        }
    }


    private void epSaoTrangBi(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {
            int gem = player.combineNew.gemCombine;
            if (player.inventory.gem < gem) {
                Service.getInstance().sendThongBao(player, "Không đủ ngọc để thực hiện");
                return;
            }
            Item trangBi = null;
            Item daPhaLe = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (isTrangBiPhaLeHoa(item)) {
                    trangBi = item;
                } else if (isDaPhaLe(item)) {
                    daPhaLe = item;
                }
            }
            int star = 0; //sao pha lê đã ép
            int starEmpty = 0; //lỗ sao pha lê
            if (trangBi != null && daPhaLe != null) {
                Item.ItemOption optionStar = null;
                for (Item.ItemOption io : trangBi.itemOptions) {
                    if (io.optionTemplate.id == 102) {
                        star = io.param;
                        optionStar = io;
                    } else if (io.optionTemplate.id == 107) {
                        starEmpty = io.param;
                    }
                }
                if (star < starEmpty) {
                    player.inventory.gem -= gem;
                    int optionId = getOptionDaPhaLe(daPhaLe);
                    int param = getParamDaPhaLe(daPhaLe);
                    Item.ItemOption option = null;
                    for (Item.ItemOption io : trangBi.itemOptions) {
                        if (io.optionTemplate.id == optionId) {
                            option = io;
                            break;
                        }
                    }
                    if (option != null) {
                        option.param += param;
                    } else {
                        trangBi.itemOptions.add(new Item.ItemOption(optionId, param));
                    }
                    if (optionStar != null) {
                        optionStar.param++;
                    } else {
                        trangBi.itemOptions.add(new Item.ItemOption(102, 1));
                    }

                    InventoryServiceNew.gI().subQuantityItemsBag(player, daPhaLe, 1);
                    sendEffectSuccessCombine(player);
                }
                InventoryServiceNew.gI().sendItemBags(player);
                Service.getInstance().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }

    private void phaLeHoaTrangBi(Player player) {
        if (!player.combineNew.itemsCombine.isEmpty()) {
            int gold = player.combineNew.goldCombine;
            int gem = player.combineNew.gemCombine;
            if (player.inventory.gold < gold) {
                Service.getInstance().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            } else if (player.inventory.gem < gem) {
                Service.getInstance().sendThongBao(player, "Không đủ ngọc để thực hiện");
                return;
            }
            Item item = player.combineNew.itemsCombine.get(0);
            if (isTrangBiPhaLeHoa(item)) {
                int star = 0;
                Item.ItemOption optionStar = null;
                for (Item.ItemOption io : item.itemOptions) {
                    if (io.optionTemplate.id == 107) {
                        star = io.param;
                        optionStar = io;
                        break;
                    }
                }
                if (star < MAX_STAR_ITEM) {
                    player.inventory.gold -= gold;
                    player.inventory.gem -= gem;
                    byte ratio = (optionStar != null && optionStar.param > 4) ? (byte) 2 : 1;
                    if (Util.isTrue(player.combineNew.ratioCombine, 100 * ratio)) {
                        if (optionStar == null) {
                            item.itemOptions.add(new Item.ItemOption(107, 1));
                        } else {
                            optionStar.param++;
                        }
                        sendEffectSuccessCombine(player);
                        if (optionStar != null && optionStar.param >= 7) {
                            ServerNotify.gI().notify("Chúc mừng " + player.name + " vừa pha lê hóa "
                                    + "thành công " + item.template.name + " lên " + optionStar.param + " sao pha lê");
                        }
                    } else {
                        sendEffectFailCombine(player);
                    }
                }
                InventoryServiceNew.gI().sendItemBags(player);
                Service.getInstance().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }
    private void nangKichHoatVip(Player player) {
        if (!player.combineNew.itemsCombine.isEmpty()) {
            int gold = player.combineNew.goldCombine;
            if (player.inventory.gold < gold) {
                Service.getInstance().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            }
            Item item = player.combineNew.itemsCombine.get(0);
            if (isTrangBiHakai(item)) {
                int idItemKichHoatTD[][] = {{0, 33, 3, 34, 136, 137, 138, 139, 230, 231, 232, 233},
                        {6, 35, 9, 36, 140, 141, 142, 143, 242, 243, 244, 245},
                        {21, 24, 37, 38, 144, 145, 146, 147, 254, 255, 256, 257},
                        {27, 30, 39, 40, 148, 149, 150, 151, 266, 267, 268, 269},
                        {12, 57, 58, 59, 184, 185, 186, 187, 278, 279, 280, 281}};
                int idItemKichHoatXD[][] = {{2, 49, 5, 50, 168, 169, 170, 171, 238, 239, 240, 241},
                        {8, 51, 11, 52, 172, 173, 174, 175, 250, 251, 252, 253},
                        {23, 53, 26, 54, 176, 177, 178, 179, 262, 263, 264, 265},
                        {29, 55, 32, 56, 180, 181, 182, 183, 274, 275, 276, 277},
                        {12, 57, 58, 59, 184, 185, 186, 187, 278, 279, 280, 281}};
                int idItemKichHoatNM[][] = {{1, 41, 4, 42, 152, 153, 154, 155, 234, 235, 236, 237},
                        {7, 43, 10, 44, 156, 157, 158, 159, 246, 247, 248, 249},
                        {22, 46, 25, 45, 160, 161, 162, 163, 258, 259, 260, 261},
                        {28, 47, 31, 48, 164, 165, 166, 167, 270, 271, 272, 273},
                        {12, 57, 58, 59, 184, 185, 186, 187, 278, 279, 280, 281}};

                int optionItemKichHoat[][] = {{127, 128, 129, 139, 140, 141}, {130, 131, 132, 142, 143, 144}, {133, 134, 135, 136, 137, 138}};

                int  type = item.template.type;
                int   gender = item.template.gender;
                int random = Util.nextInt(0, 3);
                int option1 = optionItemKichHoat[gender][random];
                int option2 = optionItemKichHoat[gender][random + 3];
                Item itemKichHoat = null;
                if (gender == 0) {
                    Item _item = ItemService.gI().createNewItem((short) idItemKichHoatTD[type][Util.nextInt(0, 12)]);
                    itemKichHoat = new Item(_item);
                    itemKichHoat.itemOptions.addAll(ItemService.gI().getListOptionItemShop((short) idItemKichHoatTD[type][Util.nextInt(0, 12)]));
                }
                if (gender == 1) {
                    Item _item = ItemService.gI().createNewItem((short) idItemKichHoatNM[type][Util.nextInt(0, 12)]);
                    itemKichHoat = new Item(_item);
                    itemKichHoat.itemOptions.addAll(ItemService.gI().getListOptionItemShop((short) idItemKichHoatNM[type][Util.nextInt(0, 12)]));
                }
                if (gender == 2) {
                    Item _item = ItemService.gI().createNewItem((short) idItemKichHoatXD[type][Util.nextInt(0, 12)]);
                    itemKichHoat = new Item(_item);
                    itemKichHoat.itemOptions.addAll(ItemService.gI().getListOptionItemShop((short) idItemKichHoatXD[type][Util.nextInt(0, 12)]));
                }

                itemKichHoat.itemOptions.add(new ItemOption(option1, 0));
                itemKichHoat.itemOptions.add(new ItemOption(option2, 0));
                itemKichHoat.itemOptions.add(new ItemOption(30, 0));

                InventoryServiceNew.gI().addItemBag(player, itemKichHoat);
                player.inventory.gold -= 500000000;
                sendEffectCombineDB(player, item.template.iconID);
                InventoryServiceNew.gI().removeItemBag(player, item);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.getInstance().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }
    private void nhapNgocRong(Player player) {
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            if (!player.combineNew.itemsCombine.isEmpty()) {
                Item item = player.combineNew.itemsCombine.get(0);
                if (item != null && item.isNotNullItem() && (item.template.id > 14 && item.template.id <= 20) && item.quantity >= 7) {
                    Item nr = ItemService.gI().createNewItem((short) (item.template.id - 1));
                    InventoryServiceNew.gI().addItemBag(player, nr);
                    InventoryServiceNew.gI().subQuantityItemsBag(player, item, 7);
                    InventoryServiceNew.gI().sendItemBags(player);
                    reOpenItemCombine(player);
                    sendEffectCombineDB(player, item.template.iconID);
                }
            }
        }
    }

 
    private void nangCapVatPham(Player player) {
        if (player.combineNew.itemsCombine.size() >= 2 && player.combineNew.itemsCombine.size() < 4) {
            if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.type < 5).count() != 1) {
                return;
            }
            if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.type == 14).count() != 1) {
                return;
            }
            if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.id == 987).count() != 1) {
                return;//admin
            }
            Item itemDo = null;
            Item itemDNC = null;
            Item itemDBV = null;
            for (int j = 0; j < player.combineNew.itemsCombine.size(); j++) {
                if (player.combineNew.itemsCombine.get(j).isNotNullItem()) {
                    if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.get(j).template.id == 987) {
                        itemDBV = player.combineNew.itemsCombine.get(j);
                        continue;
                    }
                    if (player.combineNew.itemsCombine.get(j).template.type < 5) {
                        itemDo = player.combineNew.itemsCombine.get(j);
                    } else {
                        itemDNC = player.combineNew.itemsCombine.get(j);
                    }
                }
            }
            if (isCoupleItemNangCapCheck(itemDo, itemDNC)) {
                int countDaNangCap = player.combineNew.countDaNangCap;
                int gold = player.combineNew.goldCombine;
                short countDaBaoVe = player.combineNew.countDaBaoVe;
                if (player.inventory.gold < gold) {
                    Service.getInstance().sendThongBao(player, "Không đủ vàng để thực hiện");
                    return;
                }

                if (itemDNC.quantity < countDaNangCap) return;
                if (player.combineNew.itemsCombine.size() == 3) {
                    if (Objects.isNull(itemDBV)) return;
                    if (itemDBV.quantity < countDaBaoVe) return;
                }

                int level = 0;
                Item.ItemOption optionLevel = null;
                for (Item.ItemOption io : itemDo.itemOptions) {
                    if (io.optionTemplate.id == 72) {
                        level = io.param;
                        optionLevel = io;
                        break;
                    }
                }
                if (level < MAX_LEVEL_ITEM) {
                    player.inventory.gold -= gold;
                    Item.ItemOption option = null;
                    Item.ItemOption option2 = null;
                    for (Item.ItemOption io : itemDo.itemOptions) {
                        if (io.optionTemplate.id == 47
                                || io.optionTemplate.id == 6
                                || io.optionTemplate.id == 0
                                || io.optionTemplate.id == 7
                                || io.optionTemplate.id == 14
                                || io.optionTemplate.id == 22
                                || io.optionTemplate.id == 23) {
                            option = io;
                        } else if (io.optionTemplate.id == 27
                                || io.optionTemplate.id == 28) {
                            option2 = io;
                        }
                    }
                    if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
                        option.param += (option.param * 10 / 100);
                        if (option2 != null) {
                            option2.param += (option2.param * 10 / 100);
                        }
                        if (optionLevel == null) {
                            itemDo.itemOptions.add(new Item.ItemOption(72, 1));
                        } else {
                            optionLevel.param++;
                        }
                        if (optionLevel != null && optionLevel.param >= 5) {
                            ServerNotify.gI().notify("Chúc mừng " + player.name + " vừa nâng cấp "
                                    + "thành công trang bị lên +" + optionLevel.param);
                        }
                        sendEffectSuccessCombine(player);
                    } else {
                        if ((level == 2 || level == 4 || level == 6) && (player.combineNew.itemsCombine.size() != 3)) {
                            option.param -= (option.param * 10 / 100);
                            if (option2 != null) {
                                option2.param -= (option2.param * 10 / 100);
                            }
                            optionLevel.param--;
                        }
                        sendEffectFailCombine(player);
                    }
                    if (player.combineNew.itemsCombine.size() == 3)
                        InventoryServiceNew.gI().subQuantityItemsBag(player, itemDBV, countDaBaoVe);
                    InventoryServiceNew.gI().subQuantityItemsBag(player, itemDNC, player.combineNew.countDaNangCap);
                    InventoryServiceNew.gI().sendItemBags(player);
                    Service.getInstance().sendMoney(player);
                    reOpenItemCombine(player);
                }
            }
        }
    }

    //--------------------------------------------------------------------------

    /**r
     * Hiệu ứng mở item
     *
     * @param player
     */
    public void sendEffectOpenItem(Player player, short icon1, short icon2) {
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(OPEN_ITEM);
            msg.writer().writeShort(icon1);
            msg.writer().writeShort(icon2);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    /**
     * Hiệu ứng đập đồ thành công
     *
     * @param player
     */
    private void sendEffectSuccessCombine(Player player) {
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(COMBINE_SUCCESS);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    /**
     * Hiệu ứng đập đồ thất bại
     *
     * @param player
     */
    private void sendEffectFailCombine(Player player) {
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(COMBINE_FAIL);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    /**
     * Gửi lại danh sách đồ trong tab combine
     *
     * @param player
     */
    private void reOpenItemCombine(Player player) {
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(REOPEN_TAB_COMBINE);
            msg.writer().writeByte(player.combineNew.itemsCombine.size());
            for (Item it : player.combineNew.itemsCombine) {
                for (int j = 0; j < player.inventory.itemsBag.size(); j++) {
                    if (it == player.inventory.itemsBag.get(j)) {
                        msg.writer().writeByte(j);
                    }
                }
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    /**
     * Hiệu ứng ghép ngọc rồng
     *
     * @param player
     * @param icon
     */
    private void sendEffectCombineDB(Player player, short icon) {
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(COMBINE_DRAGON_BALL);
            msg.writer().writeShort(icon);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    //--------------------------------------------------------------------------Ratio, cost combine
    private int getGoldPhaLeHoa(int star) {
        switch (star) {
            case 0:
                return 5000000;
            case 1:
                return 20000000;
            case 2:
                return 35000000;
            case 3:
                return 50000000;
            case 4:
                return 75000000;
            case 5:
                return 90000000;
            case 6:
                return 115000000;
            case 7:
                return 150000000;
            case 8:
                return 200000000;
            case 9:
                return 300000000;
            case 10:
                return 450000000;
            case 11:
                return 500000000;
            case 12:
                return 650000000;
        }
        return 0;
    }

    private float getRatioPhaLeHoa(int star) { //tile dap do chi hat mit
        switch (star) {
            case 0:
                return 50f;
            case 1:
                return 40f;
            case 2:
                return 30f;
            case 3:
                return 20f;
            case 4:
                return 10f;
            case 5:
                return 5f;
            case 6:
                return 2f;
            case 7:
                return 1f;
            case 8:
                return 0.9f;    
            case 9:
                return 0.7f;    
            case 10:
                return 0.5f;
            case 11:
                return 0.3f;
             case 12:
                return 0.1f;    
        }
        
        
        return 0;
    }

    private int getGemPhaLeHoa(int star) {
        switch (star) {
            case 0:
                return 10;
            case 1:
                return 20;
            case 2:
                return 30;
            case 3:
                return 40;
            case 4:
                return 50;
            case 5:
                return 60;
            case 6:
                return 70;
            case 7:
                return 80;
            case 8:
                return 100;    
            case 9:
                return 100;    
            case 10:
                return 200;
            case 11:
                return 200;
             case 12:
                return 500;      
        }
        return 0;
    }

    private int getGemEpSao(int star) {
        switch (star) {
            case 0:
                return 1;
            case 1:
                return 2;
            case 2:
                return 5;
            case 3:
                return 10;
            case 4:
                return 25;
            case 5:
                return 50;
            case 6:
                return 100;
        }
        return 0;
    }

    private double getTileNangCapDo(int level) {
        switch (level) {
            case 0:
                return 80;
            case 1:
                return 50;
            case 2:
                return 20;
            case 3:
                return 10;
            case 4:
                return 7;
            case 5:
                return 5;
            case 6:
                return 1;
            case 7:
                return 1;
            case 8:
                return 5;
            case 9:
                return 1;
            case 10:
                return 0.3;
            case 11:
                return 1; 
            case 12:
                return 1;     
        }
        return 0;
    }

    private int getCountDaNangCapDo(int level) {
        switch (level) {
            case 0:
                return 3;
            case 1:
                return 7;
            case 2:
                return 11;
            case 3:
                return 17;
            case 4:
                return 23;
            case 5:
                return 35;
            case 6:
                return 50;
            case 7:
                return 70;
        }
        return 0;
    }

    private int getCountDaBaoVe(int level) {
        return level + 1;
    }

    private int getGoldNangCapDo(int level) {
        switch (level) {
            case 0:
                return 10000;
            case 1:
                return 70000;
            case 2:
                return 300000;
            case 3:
                return 1500000;
            case 4:
                return 7000000;
            case 5:
                return 23000000;
            case 6:
                return 100000000;
            case 7:
                return 250000000;
        }
        return 0;
    }

    //--------------------------------------------------------------------------check
    private boolean isCoupleItemNangCap(Item item1, Item item2) {
        Item trangBi = null;
        Item daNangCap = null;
        if (item1 != null && item1.isNotNullItem()) {
            if (item1.template.type < 5) {
                trangBi = item1;
            } else if (item1.template.type == 14) {
                daNangCap = item1;
            }
        }
        if (item2 != null && item2.isNotNullItem()) {
            if (item2.template.type < 5) {
                trangBi = item2;
            } else if (item2.template.type == 14) {
                daNangCap = item2;
            }
        }
        if (trangBi != null && daNangCap != null) {
            if (trangBi.template.type == 0 && daNangCap.template.id == 223) {
                return true;
            } else if (trangBi.template.type == 1 && daNangCap.template.id == 222) {
                return true;
            } else if (trangBi.template.type == 2 && daNangCap.template.id == 224) {
                return true;
            } else if (trangBi.template.type == 3 && daNangCap.template.id == 221) {
                return true;
            } else if (trangBi.template.type == 4 && daNangCap.template.id == 220) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean isCoupleItemNangCapCheck(Item trangBi, Item daNangCap) {
        if (trangBi != null && daNangCap != null) {
            if (trangBi.template.type == 0 && daNangCap.template.id == 223) {
                return true;
            } else if (trangBi.template.type == 1 && daNangCap.template.id == 222) {
                return true;
            } else if (trangBi.template.type == 2 && daNangCap.template.id == 224) {
                return true;
            } else if (trangBi.template.type == 3 && daNangCap.template.id == 221) {
                return true;
            } else if (trangBi.template.type == 4 && daNangCap.template.id == 220) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }


    private boolean isDaPhaLe(Item item) {
        return item != null && (item.template.type == 30 || (item.template.id >= 14 && item.template.id <= 20) || (item.template.id >= 441 && item.template.id <= 447) || (item.template.id >= 964 && item.template.id <= 965));
    }

    private boolean isTrangBiPhaLeHoa(Item item) {
        if (item != null && item.isNotNullItem()) {
            if (item.template.type < 5 ) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
    public boolean isTrangBiHakai(Item item) {
        if (item != null && item.isNotNullItem()) {
            if (item.template.id >= 555 && item.template.id <= 567 ||  item.template.id == 2053 ||  item.template.id == 2054) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
    private int getParamDaPhaLe(Item daPhaLe) {
        if (daPhaLe.template.type == 30) {
            return daPhaLe.itemOptions.get(0).param;
        }
        switch (daPhaLe.template.id) {
            case 20:
                return 5;
            case 19:
                return 5;
            case 18:
                return 5;
            case 17:
                return 5;
            case 16:
                return 3;
            case 15:
                return 2;
            case 14:
                return 5;
            case 441:
                return 5;
            case 442:
                return 5;
            case 443:
                return 5;
            case 444:
                return 5;
            case 445:
                return 5;
            case 446:
                return 5;
            case 447:
                return 5;
            case 964:
                return 10;
            case 965:
                return 10;
            default:
                return -1;
        }
    }
    private int getOptionDaPhaLe(Item daPhaLe) {
        if (daPhaLe.template.type == 30) {
            return daPhaLe.itemOptions.get(0).optionTemplate.id;
        }
        switch (daPhaLe.template.id) {
            case 20:
                return 77; // hp
            case 19:
                return 103; // ki
            case 18:
                return 80; // hp 30s
            case 17:
                return 81; // mp 30s
            case 16:
                return 50; // sức đánh
            case 15:
                return 94; // giáp %
            case 14:
                return 108; // né đòn
            case 441:
                return 95; // hút hp
            case 442:
                return 96; // hút ki
            case 443:
                return 97; // phả sát thương
            case 444:
                return 98; // xuyên giáp chưởng
            case 445:
                return 99; // xuyên giáp đấm
            case 446:
                return 100; // vàng rơi từ quái
            case 447:
                return 19; // tấn công % khi đánh quái
            case 964:
                return 14; // chí mạng
            case 965: 
                return 50; // sức đánh
            default:
                return -1;
        }
    }

    /**
     * Trả về id item c0
     *
     * @param gender
     * @param type
     * @return
     */
    private int getTempIdItemC0(int gender, int type) {
        if (type == 4) {
            return 12;
        }
        switch (gender) {
            case 0:
                switch (type) {
                    case 0:
                        return 0;
                    case 1:
                        return 6;
                    case 2:
                        return 21;
                    case 3:
                        return 27;
                }
                break;
            case 1:
                switch (type) {
                    case 0:
                        return 1;
                    case 1:
                        return 7;
                    case 2:
                        return 22;
                    case 3:
                        return 28;
                }
                break;
            case 2:
                switch (type) {
                    case 0:
                        return 2;
                    case 1:
                        return 8;
                    case 2:
                        return 23;
                    case 3:
                        return 29;
                }
                break;
        }
        return -1;
    }

    //Trả về tên đồ c0
    private String getNameItemC0(int gender, int type) {
        if (type == 4) {
            return "Rada cấp 1";
        }
        switch (gender) {
            case 0:
                switch (type) {
                    case 0:
                        return "Áo vải 3 lỗ";
                    case 1:
                        return "Quần vải đen";
                    case 2:
                        return "Găng thun đen";
                    case 3:
                        return "Giầy nhựa";
                }
                break;
            case 1:
                switch (type) {
                    case 0:
                        return "Áo sợi len";
                    case 1:
                        return "Quần sợi len";
                    case 2:
                        return "Găng sợi len";
                    case 3:
                        return "Giầy sợi len";
                }
                break;
            case 2:
                switch (type) {
                    case 0:
                        return "Áo vải thô";
                    case 1:
                        return "Quần vải thô";
                    case 2:
                        return "Găng vải thô";
                    case 3:
                        return "Giầy vải thô";
                }
                break;
        }
        return "";
    }

    //--------------------------------------------------------------------------Text tab combine
    private String getTextTopTabCombine(int type) {
        switch (type) {
            case NANG_CAP_KICH_HOAT:
                return "Ta sẽ nâng cấp \n  trang bị thần linh của ngươi\n thành trang bị kích hoạt!";
            case EP_SAO_TRANG_BI:
                return "Ta sẽ phù phép\ncho trang bị của ngươi\ntrở lên mạnh mẽ";
            case PHA_LE_HOA_TRANG_BI:
                return "Ta sẽ phù phép\ncho trang bị của ngươi\ntrở thành trang bị pha lê";
//            case NANG_KICH_HOAT_VIP:
//                return "Ta sẽ phù phép\ncho trang bị thần linh của ngươi\ntrở thành trang bị cực phẩm có chỉ số kích hoạt";
            case NHAP_NGOC_RONG:
                return "Ta sẽ phù phép\ncho 7 viên Ngọc Rồng\nthành 1 viên Ngọc Rồng cấp cao";
            case NANG_CAP_VAT_PHAM:
                return "Ta sẽ phù phép cho trang bị của ngươi trở lên mạnh mẽ";
            case NANG_CAP_BONG_TAI:
                return "Ta sẽ phù phép\ncho bông tai Porata của ngươi\nthành cấp 2";
            case MO_CHI_SO_BONG_TAI:
                return "Ta sẽ phù phép\ncho bông tai Porata cấp 2 của ngươi\ncó 1 chỉ số ngẫu nhiên";
            case REN_KIEM_Z:
                return "Ta sẽ rèn\ncho con thanh\nKiếm Z này";
            case CHE_TAO_TRANG_BI_TS:
                return "Chế tạo\ntrang bị thiên sứ";
            default:
                return "";
        }
    }

    private String getTextInfoTabCombine(int type) {
        switch (type) {
            case NANG_CAP_KICH_HOAT:
                return "Vào hành trang\nChọn 1 trang bị thần linh làm phôi \n + 1 món thần linh làm vật hiến tế\nSau đó chọn 'Làm phép'\nĐồ kích hoạt sau khi nâng cấp sẽ cùng loại với phôi";
            case EP_SAO_TRANG_BI:
                return "Chọn trang bị\n(Áo, quần, găng, giày hoặc rađa) có ô đặt sao pha lê\nChọn loại sao pha lê\n"
                        + "Sau đó chọn 'Nâng cấp'";
            case PHA_LE_HOA_TRANG_BI:
                return "Chọn trang bị\n(Áo, quần, găng, giày hoặc rađa)\nSau đó chọn 'Nâng cấp'";
//            case NANG_KICH_HOAT_VIP:
//                return "Chọn trang bị thần linh cho vào đây\nSau đó chọn 'Nâng cấp'";
            case NHAP_NGOC_RONG:
                return "Vào hành trang\nChọn 7 viên ngọc cùng sao\nSau đó chọn 'Làm phép'";
            case NANG_CAP_VAT_PHAM:
                return "vào hành trang\nChọn trang bị\n(Áo, quần, găng, giày hoặc rađa)\nChọn loại đá để nâng cấp\n"
                        + "Sau đó chọn 'Nâng cấp'";
            case CHE_TAO_TRANG_BI_TS:
                return "Cần 1 công thức vip\n"
                        + "Mảnh trang bị tương ứng\n"
                        + "1 đá nâng cấp (tùy chọn)"
                        + "1 đá may mắn (tùy chọn)"
                        + "Sau đó chọn 'Nâng cấp'";
            case NANG_CAP_BONG_TAI:
                return "Vào hành trang\nChọn bông tai Porata\nChọn mảnh bông tai để nâng cấp, số lượng\n99 cái\nSau đó chọn 'Nâng cấp'";
            case MO_CHI_SO_BONG_TAI:
                return "Vào hành trang\nChọn bông tai Porata\nChọn mảnh hồn bông tai số lượng 99 cái\nvà đá xanh lam để nâng cấp\nSau đó chọn 'Nâng cấp'";
            case REN_KIEM_Z:
                return "VChọn Kiếm Z\nChọn Quặng Z, số lượng\n99 cái\nSau đó chọn 'Rèn Kiếm'\n Ngẫu nhiên Kiếm Z cấp 1 đến cấp 16"; 
            default:
                return "";
        }
    }
}
