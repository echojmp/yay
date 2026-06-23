package echojmp.yay;

import echojmp.yay.Enchants.Smelt;
import echojmp.yay.Enchants.SmeltFunctionality;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

import static net.minecraft.server.command.CommandManager.literal;

public class Yay implements ModInitializer {
	public static final String MOD_ID = "yay";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	// Enchants
	public static final Enchantment ENCHANTMENT_SMELT = Registry.register(Registries.ENCHANTMENT, new Identifier(MOD_ID, "smelt"), new Smelt());

	// Packets
		// Smelt
	public static final Identifier Smelt_Block_Destroy = Identifier.of(MOD_ID, "smelt_block_destroy");
	public static final Identifier Smelt_ItemEntity_Cooking = Identifier.of(MOD_ID, "smelt_itementity_cooking");
	public static final Identifier Smelt_ItemEntity_Cooking_Finished = Identifier.of(MOD_ID, "smelt_itementity_cooking_finished");

	// Init
	@Override
	public void onInitialize() {
		// Commands
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("fueleff")
			.requires(source -> source.hasPermissionLevel(2))
			.executes(context -> {
				ServerPlayerEntity player = context.getSource().getPlayer();
				ItemStack mainItem = player.getMainHandStack();

				context.getSource().sendFeedback(() -> Text.literal("Got fuel efficiency of "+mainItem.getItem().toString()+": "+Smelt.getFuelEfficiency(mainItem)), false);

				return 1;
			}))
		);

		// Inits
		Utils.initServer();
		SmeltFunctionality.init();
	}

	public static Identifier id(String path) {
		return new Identifier(MOD_ID, path);
	}
}
