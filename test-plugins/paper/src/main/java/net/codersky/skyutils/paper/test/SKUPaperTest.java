package net.codersky.skyutils.paper.test;

import net.codersky.skyutils.paper.PaperUtils;
import net.codersky.skyutils.paper.test.cmd.SkyMessageCmd;
import org.bukkit.plugin.java.JavaPlugin;

public class SKUPaperTest extends JavaPlugin {

	private final PaperUtils<SKUPaperTest> utils = new PaperUtils<>(this);

	@Override
	public void onEnable() {
		System.out.println("Enabling SkyUtils!");
		utils.registerCommands(new SkyMessageCmd(utils));
	}
}
