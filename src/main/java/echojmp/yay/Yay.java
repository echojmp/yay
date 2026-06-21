package echojmp.yay;

import echojmp.yay.Enchants.Smelt;
import net.fabricmc.api.ModInitializer;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Yay implements ModInitializer {
	public static final String MOD_ID = "yay";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		Registry.register(Registries.ENCHANTMENT, new Identifier(MOD_ID, "smelt"), new Smelt());
	}

	public static Identifier id(String path) {
		return new Identifier(MOD_ID, path);
	}
}
