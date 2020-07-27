package me.bukkit.hidden1nin.RewardCodes;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;





public class Main extends JavaPlugin implements Listener{
	
	public static File RewardConfig;
	static FileConfiguration islandRewardsConfig;
	@SuppressWarnings("deprecation")
	public static ItemStack RED_STAINED_GLASS_PANE = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.RED.getData());
	@SuppressWarnings("deprecation")
	public static ItemStack LIGHT_BLUE_STAINED_GLASS_PANE= new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.LIGHT_BLUE.getData());
	@SuppressWarnings("deprecation")
	public static ItemStack BLACK_STAINED_GLASS_PANE= new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.BLACK.getData());
	public static void openRewards(Player player) {
		Inventory EasySkyCustomRecipes = Bukkit.createInventory(null,45,ChatColor.BLUE+""+ChatColor.BOLD+"Codes");
		createKeyPad(EasySkyCustomRecipes, "X X X X");
		player.openInventory(EasySkyCustomRecipes);
	}
	@EventHandler
	public void onInventory(InventoryClickEvent event) {
		if (event.getCurrentItem() != null) {
	ItemStack clicked = event.getCurrentItem(); // The item that was clicked
	if (event.getView().getTitle().equalsIgnoreCase(ChatColor.BLUE+""+ChatColor.BOLD+"Codes")&& clicked != null&& event.getClickedInventory() instanceof PlayerInventory == false) {
		event.setCancelled(true);
		Player player = (Player) event.getWhoClicked();
		
		String code = event.getClickedInventory().getItem(24).getItemMeta().getDisplayName();
		if(clicked.getItemMeta().getDisplayName().contains("X")||clicked.getItemMeta().getDisplayName().contains(" ")) {
			return;
		}
		if(code.contains("X")) {
			code =ChatColor.stripColor(code).replaceFirst("X",ChatColor.stripColor(clicked.getItemMeta().getDisplayName()));
			Main.createDisplay(RED_STAINED_GLASS_PANE, event.getClickedInventory(), 24, ChatColor.RED+""+ChatColor.BOLD+code, "");
		}
			if(code.contains("X")) {
				return;
		}else {
			code =code.replace(" ", "");
			for(int i =0;Main.islandRewardsConfig.contains("Rewards."+i);i++) {
				if(ChatColor.stripColor(code).equalsIgnoreCase(Main.islandRewardsConfig.getString("Rewards."+i+".Code"))) {
					if(!Main.islandRewardsConfig.getStringList("Rewards."+i+".Users").contains(player.getName())&&Main.islandRewardsConfig.getStringList("Rewards."+i+".Users").size()<Main.islandRewardsConfig.getInt("Rewards."+i+".MaxUsers")&&Main.islandRewardsConfig.getBoolean("Rewards."+i+".Enabled")) {
					for(int reward = 0; reward<Main.islandRewardsConfig.getStringList("Rewards."+i+".Items").size();reward++) {
						
						
						String ItemRewards=Main.islandRewardsConfig.getStringList("Rewards."+i+".Items").get(reward);
						if(ItemRewards.contains("none")) {}else {
						ItemStack rewards = new ItemStack(Material.getMaterial(ItemRewards.split(":")[0].toUpperCase()),Integer.parseInt(ItemRewards.split(":")[1]));
						player.getInventory().addItem(rewards);
						}
						if(reward ==Main.islandRewardsConfig.getStringList("Rewards."+i+".Items").size()-1&&!Main.islandRewardsConfig.getStringList("Rewards."+i+".Commands").contains("none")) {
							for(String Command:Main.islandRewardsConfig.getStringList("Rewards."+i+".Commands")) {
								if(!Command.equalsIgnoreCase("none")) {
							Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),Command.replace("%player%", player.getName()));
								}
						
							}}
					
					
					}
					List<String> users = Main.islandRewardsConfig.getStringList("Rewards."+i+".Users");
					users.add(player.getName());
					Main.islandRewardsConfig.set("Rewards."+i+".Users", users);
					Main.saveRewards();
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.islandRewardsConfig.getString("Rewards."+i+".Message")));
					player.closeInventory();
					return;
					}else {
					player.closeInventory();
					player.sendMessage(ChatColor.RED+"This code has already been used!");
					return;
					
				}
				}
			}
			player.closeInventory();
			player.sendMessage(ChatColor.RED+"This code is invalid!");
			
		}
	}
		}}
	public boolean onCommand(CommandSender sender, Command cmd, String Island, String[] args) {
		if (cmd.getName().equalsIgnoreCase("code") && sender instanceof Player||cmd.getName().equalsIgnoreCase("codes") && sender instanceof Player) {
			Player player = (Player) sender;
			if(args.length == 0) {
				if(Main.islandRewardsConfig.getBoolean("Rewards.Enabled")) {
				openRewards(player );
				}else {
					player.sendMessage("Codes are disabled!");
				}
			}else {
				if(player.hasPermission("permission.EasySkies.Code")&&args[0].equalsIgnoreCase("list")) {
					player.sendMessage(ChatColor.WHITE+""+ChatColor.BOLD+"Codes");
					for(int i =0;Main.islandRewardsConfig.contains("Rewards."+i);i++) {
					player.sendMessage("   "+ChatColor.AQUA+Main.islandRewardsConfig.getString("Rewards."+i+".Code"));
					}
				}
			}
		}
		return true;
	}
	public static void saveRewards(){
		try{
			islandRewardsConfig.options().copyDefaults(true);
			islandRewardsConfig.save(RewardConfig);
		 
		}catch(Exception e){
		e.printStackTrace();
		}
		}
	public static void createKeyPad(Inventory inv,String code) {
		if(inv.getSize()>=45) {
			
			Main.borderDisplay(RED_STAINED_GLASS_PANE, inv, " ", "");
			Main.createDisplay(RED_STAINED_GLASS_PANE, inv, 13," ", "");
			Main.createDisplay(RED_STAINED_GLASS_PANE, inv, 22," ", "");
			Main.createDisplay(RED_STAINED_GLASS_PANE, inv, 31," ", "");
			Main.createDisplay(RED_STAINED_GLASS_PANE, inv, 24, ChatColor.RED+""+ChatColor.BOLD+code, "");
			
			Main.createDisplay(LIGHT_BLUE_STAINED_GLASS_PANE, inv, 10, ChatColor.WHITE+""+ChatColor.BOLD+"1", "");
			Main.createDisplay(LIGHT_BLUE_STAINED_GLASS_PANE, inv, 11, ChatColor.WHITE+""+ChatColor.BOLD+"2", "");
			Main.createDisplay(LIGHT_BLUE_STAINED_GLASS_PANE, inv, 12, ChatColor.WHITE+""+ChatColor.BOLD+"3", "");
			
			Main.createDisplay(LIGHT_BLUE_STAINED_GLASS_PANE, inv, 19, ChatColor.WHITE+""+ChatColor.BOLD+"4", "");
			Main.createDisplay(LIGHT_BLUE_STAINED_GLASS_PANE, inv, 20, ChatColor.WHITE+""+ChatColor.BOLD+"5", "");
			Main.createDisplay(LIGHT_BLUE_STAINED_GLASS_PANE, inv, 21, ChatColor.WHITE+""+ChatColor.BOLD+"6", "");
			
			Main.createDisplay(LIGHT_BLUE_STAINED_GLASS_PANE, inv, 28, ChatColor.WHITE+""+ChatColor.BOLD+"7", "");
			Main.createDisplay(LIGHT_BLUE_STAINED_GLASS_PANE, inv, 29, ChatColor.WHITE+""+ChatColor.BOLD+"8", "");
			Main.createDisplay(LIGHT_BLUE_STAINED_GLASS_PANE, inv, 30, ChatColor.WHITE+""+ChatColor.BOLD+"9", "");
			
		}
		Main.fillDisplay(BLACK_STAINED_GLASS_PANE, inv, " ", "");
		}
	
	
	public static void fillDisplay(ItemStack item, Inventory inv, String name, String lore) {
		for(int i = 0;inv.getSize()>i;i++) {
			if(inv.getItem(i)==null) {
			createDisplay(item,inv,i,name,lore);
			}
		}
	}
	public static void lineDisplay(ItemStack item, Inventory inv,int line, String name, String lore) {
		if(line>5||line*9-1>inv.getSize()) {return;}
		for(int i = 0;9>i;i++) {
			
			createDisplay(item,inv,i+line*9,name,lore);
		}
	}
	public static void borderDisplay(ItemStack item, Inventory inv, String name, String lore) {
		for(int i = 0;inv.getSize()>i;i+=9) {
			lineDisplay(item, inv, 0,name,lore);
			lineDisplay(item, inv, (inv.getSize()/9)-1,name,lore);
			if(inv.getItem(i)==null) {
			createDisplay(item,inv,i,name,lore);
			}
			if(inv.getItem(i+8)==null) {
				createDisplay(item,inv,i+8,name,lore);
				}
		}
	}
	public static ItemStack createDisplay(ItemStack item, Inventory inv, int Slot, String name, String lore) {
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		ArrayList<String> Lore = new ArrayList<String>();
		Lore.add(lore);
		meta.setLore(Lore);
		//meta.addEnchant(enchant, 1, true);
		item.setItemMeta(meta);
		if(Slot > -1) {
		inv.setItem(Slot, item);
		}else {
			if(Slot==-1) {
			inv.addItem(item);
			}else {
				if(Slot == -2) {
					
					//do nothing
				}
			}
		}
		return item;
		 
		}
	
@Override
public void onEnable() {
	Bukkit.getServer().getPluginManager().registerEvents(this, this);
	RewardConfig = new File(getDataFolder(), "Rewards.yml");
	islandRewardsConfig =YamlConfiguration.loadConfiguration(RewardConfig);
	islandRewardsConfig.options().copyDefaults(true);
	saveRewards();
	if(!Main.islandRewardsConfig.contains("Rewards.Enabled")) {
		Main.islandRewardsConfig.addDefault("Rewards.Enabled", true); 
		Main.saveRewards(); 
	}
	if(!Main.islandRewardsConfig.contains("Rewards.0")) {
		List<String> RewardCommands = new ArrayList<String>();
		RewardCommands.add("eco give %player% 100");
		List<String> RewardItems = new ArrayList<String>();
		RewardItems.add("Dirt:64");
		Main.islandRewardsConfig.addDefault("Rewards.0.Enabled", true); 
		Main.islandRewardsConfig.addDefault("Rewards.0.Code", "1234"); 
		Main.islandRewardsConfig.addDefault("Rewards.0.MaxUsers", 5); 
		Main.islandRewardsConfig.addDefault("Rewards.0.Message", "You claimed 64 dirt and &a100$"); 
		Main.islandRewardsConfig.addDefault("Rewards.0.Commands", RewardCommands); 
		Main.islandRewardsConfig.addDefault("Rewards.0.Items", RewardItems); 
		Main.islandRewardsConfig.addDefault("Rewards.0.Users", new ArrayList<String>());  
		Main.saveRewards();  
		}
}
@Override
public void onDisable() {
	
}
}
