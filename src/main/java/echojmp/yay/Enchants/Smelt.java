package echojmp.yay.Enchants;

import echojmp.yay.Items.ModItems;
import echojmp.yay.Utils;
import echojmp.yay.Yay;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.*;
import net.minecraft.world.World;

import java.util.Random;

/*
 Enchantment that auto smelts broken blocks' drops using off-hand item as fuel when possible (when sneaking also maybe?)
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
	@Override
	public boolean isTreasure() {
		return true;
	}

	// utility
	public static int getFuelEfficiency(ItemStack item) {
		int fuelTime = echojmp.yay.Utils.getFuelTime(item.getItem());
		if (fuelTime == 0) {
			return 0;
		}

		return fuelTime / AbstractFurnaceBlockEntity.DEFAULT_COOK_TIME;
	}
	public static int getCookingTime(World world, ItemStack item, int fuel, int level) {
		return Utils.getCookTime(world, item) / fuel / (level*level) * 4;
	}

	public static boolean canCook(World world, ItemStack item) {
		return !echojmp.yay.Utils.smelt(world, item).isEmpty();
	}

	//
	private static void smeltGroundItem(World world, ItemEntity item, boolean isOvercooked) {
		ItemStack result = Utils.smelt(world, item.getStack());
		if (isOvercooked) {
			Item newResult = result.getItem();

			if (result.isOf(Items.CHARCOAL)) {
				newResult = ModItems.MOLTEN_CHARCOAL;
			}

			if (!result.isOf(newResult)) {
				int count = result.getCount();
				result = new ItemStack(newResult);
				result.setCount(count);

				// TODO: give achievement
			}
		}

		item.setStack(result);
		item.resetPickupDelay();

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
	public static void cookDrop(World world, ItemEntity drop, int ticks) {
		drop.setPickupDelayInfinite();
		if (ticks > 0) {
			Utils.schedule(ticks, server -> {
				smeltGroundItem(world, drop, false);
			});

			// FX
			Random random = new Random();
			Utils.repeat(ticks, server -> {
				if (random.nextFloat() < .15) {
					Vec3d pos = drop.getPos();

					PacketByteBuf buf = PacketByteBufs.create();
					buf.writeVector3f(pos.toVector3f());

					for (ServerPlayerEntity sPlr : PlayerLookup.tracking((ServerWorld) world, new BlockPos((int)pos.getX(), (int)pos.getY(), (int)pos.getZ()))) {
						ServerPlayNetworking.send(sPlr, Yay.Smelt_ItemEntity_Cooking, buf);
					}
				}
			});
		} else {
			smeltGroundItem(world, drop, true);
		}
	}
}
