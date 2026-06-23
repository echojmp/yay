package echojmp.yay.Enchants;

import echojmp.yay.Utils;
import echojmp.yay.Yay;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import org.apache.logging.log4j.core.jmx.Server;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

/*
 Enchantment that auto smelts broken blocks' drops (when sneaking maybe?)

 - fuel_duration (ticks) = getFuelTime(off-hand item stack)
 - fuel_efficiency (items that a fuel can smelt) = smelt_duration / fuel_duration
 - cooking_time (ticks per drop) = smelt_duration / level * 2 / fuel_efficiency

 conditions:
  • fuel_efficiency >= 1
  • drop must be smeltable

 extra:
  • I also want it to change the breaking texture if the enchant is there (nvm cuz now it's on drops :(, can still add it tho ig)
  • should add smelt xp to xp

 * details:
  - items aren't pick-up able while cooking
  - as soon as cooking starts, the off-hand item's count goes down by 1 (1 item is used)
  - also particles on smelt :D
*/

public class Smelt extends Enchantment {
	public Smelt() {
		super(Enchantment.Rarity.UNCOMMON, EnchantmentTarget.DIGGER, new EquipmentSlot[] {EquipmentSlot.MAINHAND});
	}

	@Override
	public int getMinPower(int level) {
		return level * 3;
	}
	@Override
	public int getMaxLevel() {
		return 3;
	}

	// utility
	public static int getFuelEfficiency(ItemStack item) {
		int fuelTime = echojmp.yay.Utils.getFuelTime(item.getItem());
		if (fuelTime == 0) {
			return 0;
		}

		return fuelTime / AbstractFurnaceBlockEntity.DEFAULT_COOK_TIME;
	}
	public static int getCookingTime(ItemStack item, int fuel, int level) {
		return AbstractFurnaceBlockEntity.DEFAULT_COOK_TIME * item.getCount() / fuel / level * 2;
	}

	public static boolean canCook(World world, ItemStack item) {
		return !echojmp.yay.Utils.smelt(world, item).isEmpty();
	}

	//
	private static void smeltGroundItem(World world, ItemEntity item) {
		item.resetPickupDelay();
		item.setStack(Utils.smelt(world, item.getStack()));

		// xp
		float experience = Utils.getSmeltXP(world, item.getStack());
		int i = MathHelper.floor(experience);
		float f = MathHelper.fractionalPart(experience);
		if (f != 0.0F && Math.random() < f) {
			i++;
		}

		ExperienceOrbEntity.spawn((ServerWorld) world, item.getPos(), i);

		// FX
		Vec3d pos = item.getPos();

		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeVector3f(pos.toVector3f());

		for (ServerPlayerEntity sPlr : PlayerLookup.tracking((ServerWorld) world, new BlockPos((int)pos.getX(), (int)pos.getY(), (int)pos.getZ()))) {
			ServerPlayNetworking.send(sPlr, Yay.Smelt_ItemEntity_Cooking_Finished, buf);
		}
	}
	public static void cookDrop(World world, ItemEntity item, int ticks) {
		Yay.LOGGER.info("smelting "+item.toString()+" for "+ticks+" ticks");
		item.setPickupDelayInfinite();
		if (ticks > 0) {
			Utils.schedule(ticks, server -> {
				smeltGroundItem(world, item);
			});

			// FX
			Random random = new Random();
			Utils.repeat(ticks, server -> {
				if (random.nextFloat() < .1) {
					Vec3d pos = item.getPos();

					PacketByteBuf buf = PacketByteBufs.create();
					buf.writeVector3f(pos.toVector3f());

					for (ServerPlayerEntity sPlr : PlayerLookup.tracking((ServerWorld) world, new BlockPos((int)pos.getX(), (int)pos.getY(), (int)pos.getZ()))) {
						ServerPlayNetworking.send(sPlr, Yay.Smelt_ItemEntity_Cooking, buf);
					}
				}
			});
		} else {
			smeltGroundItem(world, item);
		}
	}
}
