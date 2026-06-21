package echojmp.yay;

import echojmp.yay.Enchants.Smelt;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.minecraft.server.command.CommandManager.literal;

public class Yay implements ModInitializer {
	public static final String MOD_ID = "yay";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// enchants
		Registry.register(Registries.ENCHANTMENT, new Identifier(MOD_ID, "smelt"), new Smelt());

		// Commands
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("smelt")
		.executes(context -> {
			ServerPlayerEntity player = context.getSource().getPlayer();
			ItemStack mainItem = player.getMainHandStack();

			ItemStack result = echojmp.yay.Utils.smelt(context.getSource().getWorld(), player.getMainHandStack());
			player.getInventory().setStack(player.getInventory().selectedSlot, result);

			return 1;
		})));
	}

	public static Identifier id(String path) {
		return new Identifier(MOD_ID, path);
	}
}
