/*
 *#####################################
 *#     Written by Dalton Nofs        #
 *#####################################
*/

package mob_inv_pkg;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;
//import java.util.Map;
import java.util.UUID;

//import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
//import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class mob_inv_main extends JavaPlugin
{
	//Fired when plugin is enabled
	@Override
    public void onEnable() 
    {
		getServer().getPluginManager().registerEvents(new mob_inv_listner(), this);
		load_arrays();
    }
    // Fired when plugin is disabled
    @Override
    public void onDisable() 
    {
    	store_invs();
    	HandlerList.unregisterAll(this);
    }
    
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
    	if(cmd.getName().equalsIgnoreCase("mobinvhelp"))
    	{
    		sender.sendMessage((getDataFolder() + " saved_invs"));
    		//Bukkit.broadcastMessage(mob_inv_listner.mob_invs.get(0).toString());
    		//Bukkit.broadcastMessage(Integer.toString(mob_inv_listner.mob_invs.size()));
    		load_arrays();
    	}
    	else{}
    	return true;
    }
    
	public void load_arrays()
    {
    	try
	    {
	        File load_from = new File(getDataFolder(), "saved_inv.txt");
    		String path = getDataFolder() + "/saved_inv.txt";
	        if (!load_from.exists()){}
	        else
	        {
	        	FileReader fr = new FileReader(path);
				BufferedReader text_reader = new BufferedReader(fr);
        		//seperates id, and blocks from one another which are sep by " "
				String temp1 = "";
				while((temp1 = text_reader.readLine())!=null)
        		{
	        		String temp2 = new String(temp1);
	        		String[] temp_split = temp2.split(" ");
	        		int o = 0;
	        		//Inventory temp_inv = Bukkit.createInventory(null, get_inv_size(UUID.fromString(temp_split[0])));
	        		ArrayList<ItemStack> temp_list= new ArrayList<ItemStack>();
	        		for(String temp_split_str : temp_split) //Separates block info sep by "," (divides into 2 parts)
	        		{
	        			if(o != 0)//(only falls in on second go so it dosnt try to split the uuid)
	        			{
		        			//adds uuid to uuid array
		        			//splits splits into specific block data using ","
		        			String[] temp_split_str_split = temp_split_str.split(";");
		        			//now to create an itemstack and assign stored values
		        			ItemStack temp_stack = new ItemStack(Material.getMaterial(temp_split_str_split[0]));
		        			temp_stack.setAmount(Integer.parseInt(temp_split_str_split[1]));
		        			temp_stack.setDurability(Short.parseShort(temp_split_str_split[2]));
		        			//Bukkit.broadcastMessage(String.valueOf(temp_split_str_split.length));
		        			//Bukkit.broadcastMessage(String.valueOf(temp_split_str_split[1] + " " + temp_split_str_split[2] + " " + temp_split_str_split[3]));
		        			//Bukkit.broadcastMessage(temp_split_str_split[3].toString());
		        			if(Integer.parseInt(temp_split_str_split[3]) == 1)
		        			{
		        				//Bukkit.broadcastMessage(String.valueOf(temp_split_str_split[4]));
		        				String[] temp_enchant_string = temp_split_str_split[4].split(":");
			        			for(String enchant_split : temp_enchant_string)
			        			{
			        				String[] enchant_split_split = enchant_split.split(",");
			        				temp_stack.addEnchantment(Enchantment.getByName(enchant_split_split[0]), Integer.parseInt(enchant_split_split[1]));
			        				//Bukkit.broadcastMessage(enchant_split_split[0] + " " + enchant_split_split[1]);
			        			}
		        			}
		        			else{}
		        			//Bukkit.broadcastMessage("adding temp_stack");
		        			temp_list.add(temp_stack);
	        			}
	        			else{o++;}
	        		}
	        		//mob_inv_listner.mob_invs.put(UUID.fromString(temp_split[0]),temp_insv);
	        		mob_inv_listner.from_file.put(UUID.fromString(temp_split[0]), temp_list); //works because each new line is a different mob
	        	}
	        	text_reader.close();
	        }
	    }
	    catch (IOException e)
	    {
	
	        e.printStackTrace();
	
	    }
    }
    
    public int get_number_lines()
    {
    	int number_of_lines = 0;
    	try
	    {
    		String path = getDataFolder() + "/saved_inv.txt";
        	FileReader fr = new FileReader(path);
        	BufferedReader text_reader = new BufferedReader(fr);
        	@SuppressWarnings("unused")
			String count_lines;
        	while((count_lines = text_reader.readLine()) != null)
        	{
        		number_of_lines++;
        	}
        	text_reader.close();
	    } 
	    catch (IOException e)
	    {
	
	        e.printStackTrace();
	
	    }
    	return number_of_lines;
    }

	public void inv_to_file(String msg)
	{
	
	    try
	    {
	        File data_folder = getDataFolder();
	        if(!data_folder.exists())
	        {
	        	data_folder.mkdir();
	        }
	
	        File save_to = new File(getDataFolder(), "saved_inv.txt");
	        if (save_to.exists())
	        {
	        	save_to.delete();
	        }
	        else{}
	        save_to.createNewFile();
	
	
	        FileWriter fw = new FileWriter(save_to, true);
	
	        PrintWriter pw = new PrintWriter(fw);
	
	        pw.println(msg);
	
	        pw.flush();
	
	        pw.close();
	
	    } 
	    catch (IOException e)
	    {
	
	        e.printStackTrace();
	
	    }
	}
	
	public void store_invs()
	{
		String temp_string = "";
    	for(UUID key : mob_inv_listner.mob_invs.keySet())
    	{
    		temp_string += key.toString() + " ";
    		for(ItemStack j : mob_inv_listner.mob_invs.get(key))
    		{
    			if(j!= null)
    			{
    				/*String temp_ench = "";
    				Map<Enchantment, Integer> temp_map = j.getEnchantments();
    				for(Enchantment k : temp_map.keySet())
    				{
    					temp_ench += k.toString() + " ";
    				}*/
    				String enchant_string = "";
    				Map<Enchantment, Integer> temp_enchant_map = j.getEnchantments();
    				boolean first_time = true;
    				String enchant_bool = "0";
    				if(temp_enchant_map.isEmpty())
    				{
    					//Bukkit.broadcastMessage("EMPTY");
    					//dont do this if empty
    				}
    				else
    				{
	    				for(Enchantment temp_enchantment : temp_enchant_map.keySet())
	    				{
	    					if(first_time == true)
	    					{
	    						enchant_string = enchant_string + temp_enchantment.getName() + "," + j.getEnchantmentLevel(Enchantment.getByName(temp_enchantment.getName()));
	    						first_time = false;
	    					}
	    					else
	    					{
	    						enchant_string = enchant_string + ":" + temp_enchantment.getName() + "," + j.getEnchantmentLevel(Enchantment.getByName(temp_enchantment.getName()));
	    					}
	    				}
	    				enchant_bool = "1";
    				}
    				temp_string += j.getType().toString() + ";" + j.getAmount() + ";" + j.getDurability() + ";" + enchant_bool + ";" + enchant_string + " ";// + j.getEnchantments(). + " ";	
    			}
    			else{}
    		}
    		temp_string += "\n";
    	}
    	for(UUID key : mob_inv_listner.from_file.keySet())
    	{
    		if(mob_inv_listner.mob_invs.containsKey(key))
    		{
    			
    		}
    		else
    		{
    			temp_string += key.toString() + " ";
        		for(ItemStack j : mob_inv_listner.from_file.get(key))
        		{
        			if(j!= null)
        			{
        				temp_string += j.getType().toString() + ";" + j.getAmount() + ";" + j.getDurability() + ";" + j.getEnchantments() + " ";	
        			}
        			else{}
        		}
        		temp_string += "\n";
    		}
    	}
    	inv_to_file(temp_string);
	}
}
