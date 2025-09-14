package com.tonton.sex;

import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.Material;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;





public class Main extends JavaPlugin {
	public static JavaPlugin MainPlugin;
	 @Override
	    public void onEnable(){
		 MainPlugin=this;
		 getCommand("sex").setExecutor(new BaseCommands());
	    }
	    @Override
	    public void onDisable(){
	    }
	
}
