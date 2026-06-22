package echojmp.yay.Enchants;

import net.minecraft.block.Block;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

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
		int fuelTime = echojmp.yay.Utils.getFuelTime(item);
		if (fuelTime == 0) {
			return 0;
		}

		return AbstractFurnaceBlockEntity.DEFAULT_COOK_TIME / fuelTime;
	}
	public static int getCookingTime(ItemStack item) {
		return echojmp.yay.Utils.getFuelTime(item) * item.getCount();
	}

	public static boolean canCook(World world, ItemStack item) {
		return !echojmp.yay.Utils.smelt(world, item).isEmpty();
	}

	// idk how to make this...
	public static void cookDrop(ItemStack item, int ticks) {

	}
}
