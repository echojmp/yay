package echojmp.yay.Enchants;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;

// Enchantment that auto smelt broken blocks, but makes mining slower depending on level
// at level n, it will add smelt_duration / (2n^2) to mining time
// I also want it to change the breaking texture if the enchant is there
// should only work if you have fuel in your off-hand, which can go to waste

// should add smelt xp to xp
// should add some kind of tag to make sure you've been smelting the block the whole time

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
		return 5;
	}
}
