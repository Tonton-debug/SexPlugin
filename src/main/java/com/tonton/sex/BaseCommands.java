package com.tonton.sex;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import net.md_5.bungee.api.ChatColor;

public class BaseCommands implements CommandExecutor {
	private boolean HasMinDistance(Player pl,Player pl2) {
		return pl.getLocation().distance(pl2.getLocation())<5;
	}
	private boolean OneDimention(Player pl,Player pl2) {
		return pl.getWorld()==pl2.getWorld();
	}
	private long WorldTimeToGlobal() {
		return System.currentTimeMillis()/1000;
	}
	private int TimeToSex(Player pl) {
		if(!pl.hasMetadata("timeSex")) 
			return 0;
		long time=pl.getMetadata("timeSex").get(0).asLong();
		long result=120-(WorldTimeToGlobal()-time);
		if(result<0)
			return 0;
		return (int)result;
	}
	private Player GetBySexPlayer(Player pl) {
		if(!pl.hasMetadata("LastSender"))
			return null;
		return Bukkit.getPlayer(pl.getMetadata("LastSender").get(0).asString());
	}
	private boolean CanSexByTime(Player pl) {
		if(pl.hasMetadata("timeSex")) {
			if(TimeToSex(pl)!=0) {
				return false;
			}
		}
		return true;
	}
	 public  String capitalizeFirst(String str) {
	        if (str == null || str.isEmpty()) {
	            return str; 
	        }
	       
	        if (str.length() == 1) {
	            return str.toUpperCase();
	        }
	        
	        return str.substring(0, 1).toUpperCase() + str.substring(1);
	    }
	private boolean CanSex(Player pl,Player pl2) {
		boolean one= OneDimention(pl,pl2)&&HasMinDistance(pl,pl2);
		return one;
	}
	private void KillByLove(Player pl,Player pl2) {
		Random random = new Random();
		if(random.nextInt(0,5)==2) {
			pl.setHealth(0);
			Bukkit.getServer().broadcastMessage(pl.getName()+" НЕ ВЫДЕРЖАЛ ОТКАЗА ОТ ИГРОКА "+pl2.getName()+" И УМЕР ОТ СЕРДЕЧНОГО ПРИСТУПА");
		}
	}
	private void SummonRandomEntity(Player pl,Player pl2) {
		Random random = new Random();
		if(random.nextInt(0,4)==2) {
		try {
		
		EntityType[] entityTypes = EntityType.values();
		int randomIndex = random.nextInt(entityTypes.length);
		EntityType randomEntityType = entityTypes[randomIndex];
		Entity entity=pl.getWorld().spawnEntity(pl.getLocation(), randomEntityType);
		entity.setCustomName(capitalizeFirst(pl.getName())+ capitalizeFirst(pl2.getName())+random.nextInt(0,1000));
		 Bukkit.getServer().broadcastMessage(pl.getName()+" И "+pl2.getName()+" ПОТРАХАЛИСЬ И У НИХ РОДИЛСЯ "+randomEntityType);
		} catch(Exception e) {
			
		}
		}
	}
	private void SummonPatricle(Player pl,Particle particle) {
		pl.getWorld().spawnParticle(particle, pl.getLocation(), 5, 0.5, 0.5, 0.5, 0.1);
	}
	private void GenerateZombieMelon(Block melon) {
		melon.setType(Material.AIR);
		LivingEntity livingEntity=(LivingEntity) melon.getWorld().spawnEntity(melon.getLocation(), EntityType.VILLAGER);
				  livingEntity.getEquipment().setHelmet(new ItemStack(Material.LEGACY_MELON_BLOCK));
	       livingEntity.setCustomName("ДРУГГ");
	       livingEntity.setMaxHealth(100);
	       livingEntity.setHealth(100);
	       livingEntity.getEquipment().setItemInHand(new ItemStack(Material.MELON));
	}
	private void GenerateRandomMelonEvent(Player pl,Block melon) {
		Random random=new Random();
		int randomValue=random.nextInt(4);
		switch(randomValue){
				case 0:
					GenerateZombieMelon(melon);
					pl.sendMessage("ИЗ ТЫКВЫ ВЫЛЕЗ ДРУГ");
					break;
				case 1:
					melon.setType(Material.AIR);
					pl.getInventory().addItem(new ItemStack(Material.MELON,64));
					pl.sendMessage("ТЫКВА РОДИЛА МНОГО МАЛЕНЬКИХ ТЫКОВОК И УМЕРЛА");
					break;
				case 2:
					melon.setType(Material.AIR);
					pl.getWorld().createExplosion(pl.getLocation(), 2);
					pl.sendMessage("ВО ВРЕМЯ СОИТИЯ АРБУЗ ВЗОРВАЛСЯ");
					break;
				case 3:
					World melonWorld=Bukkit.getWorld("melon_world");
					World baseWorld=Bukkit.getWorld("world");
					if(melonWorld==null||baseWorld==null)
						pl.sendMessage("ВЫ ТРАХНУЛИ ТЫКВУ, НО НИЧЕГО НЕ ПРОИЗОШЛО");
					if(pl.getWorld()==baseWorld) {
						pl.teleport(new Location(melonWorld,0,melonWorld.getHighestBlockYAt(0, 0)+1,0));
						Bukkit.broadcastMessage(ChatColor.RED+pl.getName()+" ТРАХНУЛ АРБУЗ И ИСЧЕЗ БЕЗ СЛЕДА");
					}else  {
						pl.teleport(new Location(baseWorld,0,baseWorld.getHighestBlockYAt(0, 0)+1,0));
						pl.getInventory().clear();
						pl.sendMessage("Во время секса тыква съела все ваши вещи...");
					}
					break;
		}
	}
	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		// TODO Auto-generated method stub
		if(!(arg0 instanceof  Player)) {
			return true;
		}
		Player player=(Player)arg0;
		System.out.println("AAA:"+arg2+" a:"+arg3.length);
		if(arg2.equals("sex")&&arg3.length>0) {
			switch(arg3[0]) {
			case "help":
				arg0.sendMessage("/sex [имя игрока] - предложить секс игроку");
				arg0.sendMessage("/sex melon - трахнуть арбуз под вами");
				arg0.sendMessage("/sex when - узнать когда можно потрахаться");
				arg0.sendMessage("/sex accept - согласиться на секс");
				arg0.sendMessage("/sex cancel - отказатся");
				break;
			case "accept":
				if(!player.hasMetadata("sender")) {
					arg0.sendMessage("Вас никто не хочет трахать :(");
					break;
				}
				String sender=player.getMetadata("sender").get(0).asString();
				if(sender=="") {
					arg0.sendMessage("Вас никто не хочет трахать :(");
					break;
				}
				Player senderPl=Bukkit.getPlayer(sender);
				if(senderPl==null) {
					arg0.sendMessage("ГДЕ ТРАХАЛЬЩИК");
					player.setMetadata("sender", new FixedMetadataValue(Main.MainPlugin,""));
					break;
				}
					if(!HasMinDistance(senderPl,player)||!OneDimention(senderPl,player)) {
						arg0.sendMessage(senderPl.getName()+" слишком далеко :(");
						player.setMetadata("sender", new FixedMetadataValue(Main.MainPlugin,""));
						break;
					}
					player.setMetadata("sender", new FixedMetadataValue(Main.MainPlugin,""));
					senderPl.setMetadata("timeSex", new FixedMetadataValue(Main.MainPlugin,WorldTimeToGlobal()));
					senderPl.sendMessage("ВЫ ПОТРАХАЛИСЬ С ИГРОКОМ "+senderPl.getName());
					arg0.sendMessage("ВЫ ПОТРАХАЛИСЬ С ИГРОКОМ "+senderPl.getName());
					SummonPatricle(senderPl,Particle.HAPPY_VILLAGER);
					SummonPatricle(player,Particle.HAPPY_VILLAGER);
					SummonRandomEntity(player,senderPl);
				break;
			case "melon":
				if(!CanSexByTime(player)) {
					arg0.sendMessage("ВАШЕ СЕМЯ ЕЩЁ НЕ ОХЛАДИЛОСЬ. ПОДОЖДИТЕ "+TimeToSex(player)+" СЕКУНД");
					break;
				}
				Block melon=player.getWorld().getBlockAt(player.getLocation().add(0,-1,0));
				System.out.println("MELL:"+melon.getType());
				if(melon.getType()!=Material.MELON){
					arg0.sendMessage("ПОД ВАМИ НЕТУ АРБУЗА. КОГО ТРАХАТЬ???");
					
					break;
				}
				player.setMetadata("timeSex", new FixedMetadataValue(Main.MainPlugin,WorldTimeToGlobal()));
				GenerateRandomMelonEvent(player,melon);
			//	player.setMetadata("timeSex", new FixedMetadataValue(Main.MainPlugin,WorldTimeToGlobal()));
				break;
			case "cancel":
				if(!player.hasMetadata("sender"))
					break;
				String sender2=player.getMetadata("sender").get(0).asString();
				if(sender2=="") 
					break;
				
				Player senderPl2=Bukkit.getPlayer(sender2);
				if(senderPl2!=null) {
					senderPl2.sendMessage(player.getName()+" ОТКАЗАЛСЯ ОТ ТРАХАНЬЯ :(");
					arg0.sendMessage("ВЫ ОТКАЗАЛИСЬ ОТ ТРАХАНЬЯ");
					player.setMetadata("sender", new FixedMetadataValue(Main.MainPlugin,""));
					SummonPatricle(senderPl2,Particle.ANGRY_VILLAGER);
					SummonPatricle(player,Particle.ANGRY_VILLAGER);
					KillByLove(senderPl2,player);
				}
				break;
			case "when":
				arg0.sendMessage("СЕМЯ ОСВЕЖИТСЯ ЧЕРЕЗ:"+TimeToSex(player)+" СЕКУНД");
				break;
			default:
				if(arg3.length!=1) {
					arg0.sendMessage("Недосаточно аргументов");
				break;
				
				}
				String playerName=arg3[0];
				if(Bukkit.getPlayer(playerName)==player) {
					arg0.sendMessage("ТЫ КАК СЕБЯ ТРАХАТЬ СОБРАЛСЯ");
					break;
				
				}
				if(Bukkit.getPlayer(playerName)==null) {
					arg0.sendMessage("Не удалось найти игрока");
					break;
				}
				Player pl=Bukkit.getPlayer(playerName);
				if(!CanSex(pl,player)) {
					arg0.sendMessage("СЕКС ЗАПРЕЩЁН.  ИГРОК СЛИШКОМ ДАЛЕКО");
					break;
				}
				if(!CanSexByTime(player)) {
					arg0.sendMessage("ВАШЕ СЕМЯ ЕЩЁ НЕ ОХЛАДИЛОСЬ. ПОДОЖДИТЕ "+TimeToSex(player)+" СЕКУНД");
					break;
				}
				Player otherLast=GetBySexPlayer(player);
				if(otherLast!=null&&otherLast!=pl) {
					otherLast.sendMessage("ИГРОК "+player.getName()+" ПЕРЕДУМАЛ ВАС ТРАХАТЬ :(");
					otherLast.setMetadata("sender", new FixedMetadataValue(Main.MainPlugin,""));
				}
				arg0.sendMessage("ВЫ ПРЕДЛОЖИЛИ СЕКС ИГРОКУ "+pl.getName()+"!!! ЖДЁМ ОТВЕТА");
					pl.setMetadata("sender", new FixedMetadataValue(Main.MainPlugin,player.getName()));
					player.setMetadata("LastSender", new FixedMetadataValue(Main.MainPlugin,pl.getName()));
					pl.sendMessage("ВАС ХОЧЕТ ТРАХНУТЬ "+player.getName()+". НАПИШИТЕ /sex accept ДЛЯ ПРИНЯТИЯ И /sex cancel ДЛЯ ОТТОРЖЕНИЯ");
				break;
			
					
			}
		
	}
		return true;
	}
}
