package com.zennyel.dragonflight.command;

import com.zennyel.dragonflight.DragonFlightPlugin;
import com.zennyel.dragonflight.utils.ItemStackUtils;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GiveEyeCommand extends AbstractCommand{

    private DragonFlightPlugin dragonFlightPlugin;


    public GiveEyeCommand(DragonFlightPlugin dragonFlightPlugin) {
        this.dragonFlightPlugin = dragonFlightPlugin;
    }

    @Override
    public void handleCommands(CommandSender sender, Command command, String s, String[] args) {
        handleItemGive(sender, args);
    }

    public void handleItemGive(CommandSender sender, String[] args){
        if(!(sender instanceof Player)){
            return;
        }
        Player player = (Player) sender;

        if(args.length != 1){
            player.sendMessage("§cWrong usage, use: /giveeye <quantity>.");
            return;
        }

        int i = Integer.parseInt(args[0]);

        String displayName = dragonFlightPlugin.getConfig().getString("EnderEyeItem.displayName");
        String lore = dragonFlightPlugin.getConfig().getString("EnderEyeItem.lore");

        ItemStack enderEye = ItemStackUtils.createItemStack(Material.ENDER_EYE, displayName, lore);

        for(int v = 0; v < i; v++) {
            player.getInventory().addItem(enderEye);
        }

        player.sendMessage("§5EnderEyes gived to " + player.getName());
    }


}
