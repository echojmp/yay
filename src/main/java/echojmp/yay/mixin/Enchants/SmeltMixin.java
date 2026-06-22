package echojmp.yay.mixin.Enchants;

import echojmp.yay.Enchants.Smelt;
import echojmp.yay.Yay;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.sound.Sound;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public class SmeltMixin {
	// particles
	/*@Inject(at = @At("HEAD"), method = "onBreak")
	private void init(World world, BlockPos pos, BlockState state, PlayerEntity player, CallbackInfo ci) {
		Yay.LOGGER.info("onBreak called");
		Yay.LOGGER.info("got onSmelt level", EnchantmentHelper.getLevel(new Smelt(), player.getMainHandStack()));

		if (EnchantmentHelper.getLevel(new Smelt(), player.getMainHandStack()) > 0) {
			world.playSound(player, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 1.0F, 1.0F);

			Random rand = Random.create();
			for (int i = 0; i < 16; i++) {
				world.addParticle(ParticleTypes.ASH,
						pos.getX() + (rand.nextFloat() - .5f),
						pos.getY() + (rand.nextFloat() - .5f),
						pos.getZ() + (rand.nextFloat() - .5f),
						0.0D + (rand.nextFloat() - .5f) / 20 * 4,
						0.0D + (rand.nextFloat() - .15f) / 20 * 2,
						0.0D + (rand.nextFloat() - .5f) / 20 * 4
				);
			}
		}
	}*/
}