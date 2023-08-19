package com.zennyel.dragonflight.listener;

import com.zennyel.dragonflight.DragonFlightPlugin;
import com.zennyel.dragonflight.manager.EnderEyeManager;
import com.zennyel.dragonflight.utils.ItemStackUtils;
import com.zennyel.dragonflight.utils.StringUtils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class InventoryClickListener implements Listener {

    private DragonFlightPlugin dragonFlightPlugin;

    public InventoryClickListener(DragonFlightPlugin dragonFlightPlugin) {
        this.dragonFlightPlugin = dragonFlightPlugin;
    }

    @EventHandler

    public void onInventoryClick(InventoryClickEvent event) {

        if (event.getCurrentItem() == null || event.getCursor() == null) {
            return;
        }

        if (event.getCursor().getType().equals(Material.AIR) || event.getCurrentItem().getType().equals(Material.AIR)) {
            return;
        }

        if (event.getCursor().getType() != Material.ENCHANTED_BOOK) {
            return;
        }

        Player player = (Player) event.getWhoClicked();

        ItemStack currentItem = event.getCurrentItem();
        ItemStack cursorItem = event.getCursor();

        if (ItemStackUtils.getWeaponOrArmor(currentItem) == null) {
            return;
        }

        if (ItemStackUtils.getNbt(currentItem, "enchant") != null) {
            if (StringUtils.getIntAlgarism(ItemStackUtils.getNbt(cursorItem, "level")) <= StringUtils.getIntAlgarism(ItemStackUtils.getNbt(currentItem, "level"))) {
                return;
            }
        }

        String enchantType = ItemStackUtils.getNbt(cursorItem, "enchant");
        int enchantLevel = StringUtils.getIntAlgarism(ItemStackUtils.getNbt(cursorItem, "level"));

        Enchantment customEnchant = null;

        if (ItemStackUtils.getWeaponOrArmor(currentItem).equalsIgnoreCase("armor")) {
            if (ItemStackUtils.getNbt(cursorItem, "enchant").equalsIgnoreCase("dragonstrike")) {
                customEnchant = dragonFlightPlugin.getArmorEnhanceEnchant();
            } else if (ItemStackUtils.getNbt(cursorItem, "enchant").equalsIgnoreCase("soul")) {
                customEnchant = dragonFlightPlugin.getArmorReduceEnchant();
            }
        } else if (ItemStackUtils.getWeaponOrArmor(currentItem).equalsIgnoreCase("sword")) {
            if (ItemStackUtils.getNbt(cursorItem, "enchant").equalsIgnoreCase("dragonstrike")) {
                customEnchant = dragonFlightPlugin.getSwordEnhanceEnchant();
            } else {
                return;
            }
        } else if (ItemStackUtils.getWeaponOrArmor(currentItem).equalsIgnoreCase("bow")) {
            if (ItemStackUtils.getNbt(cursorItem, "enchant").equalsIgnoreCase("dragonstrike")) {
                customEnchant = dragonFlightPlugin.getBowEnhanceEnchant();
            } else {
                return;
            }
        }

        if (customEnchant != null) {
            ItemStackUtils.addCustomEnchantment(currentItem, customEnchant, enchantLevel, cursorItem);
            ItemStackUtils.addNbt(currentItem, "enchant", customEnchant.getName());
            ItemStackUtils.addNbt(currentItem, "level", String.valueOf(enchantLevel));
            event.setCurrentItem(currentItem);

            event.getView().setCursor(null);

            player.updateInventory();
        }
    }



}





