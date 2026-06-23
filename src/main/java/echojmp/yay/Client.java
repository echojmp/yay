package echojmp.yay;

import echojmp.yay.Enchants.SmeltFunctionality;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.joml.Vector3f;

public class Client implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(Yay.Smelt_Block_Destroy, (client, handler, buf, responseSender) -> {
			BlockPos pos = buf.readBlockPos();
			ClientWorld world = client.world;

			client.execute(() -> {
				Random rand = Random.create();
				world.playSound(client.player, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, .4F,  .666f + rand.nextFloat() / 1.5f);

				for (int i = 0; i < 3; i++) {
					world.addParticle(ParticleTypes.LARGE_SMOKE,
							pos.getX() + .5f + (rand.nextFloat() - .5f),
							pos.getY() + .5f + (rand.nextFloat() - .5f),
							pos.getZ() + .5f + (rand.nextFloat() - .5f),
							(rand.nextFloat() - .5f) / 20 * 2,
							(rand.nextFloat() - .15f) / 20 * 1,
							(rand.nextFloat() - .5f) / 20 * 2
					);
					world.addParticle(ParticleTypes.SMOKE,
							pos.getX() + .5f + (rand.nextFloat() - .5f),
							pos.getY() + .5f + (rand.nextFloat() - .5f),
							pos.getZ() + .5f + (rand.nextFloat() - .5f),
							(rand.nextFloat() - .5f) / 20 * 2,
							(rand.nextFloat() - .15f) / 20 * 1,
							(rand.nextFloat() - .5f) / 20 * 2
					);
				}
				for (int i = 0; i < rand.nextBetween(1, 3); i++) {
					world.addParticle(ParticleTypes.FLAME,
							pos.getX() + .5f + (rand.nextFloat() - .5f),
							pos.getY() + .5f + (rand.nextFloat() - .5f),
							pos.getZ() + .5f + (rand.nextFloat() - .5f),
							(rand.nextFloat() - .5f) / 20 * 2,
							rand.nextFloat() / 20 * 1,
							(rand.nextFloat() - .5f) / 20 * 2
					);
				}
			});
		});
		ClientPlayNetworking.registerGlobalReceiver(Yay.Smelt_ItemEntity_Cooking_Finished, (client, handler, buf, responseSender) -> {
			Vector3f fPos = buf.readVector3f();
			BlockPos bPos = new BlockPos((int)fPos.x, (int)fPos.y, (int)fPos.z);
			ClientWorld world = client.world;

			client.execute(() -> {
				Random rand = Random.create();
				world.playSound(client.player, bPos, SoundEvents.BLOCK_BLASTFURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, .4F,  .666f + rand.nextFloat() / 1.5f);

				for (int i = 0; i < 5; i++) {
					world.addParticle(ParticleTypes.FLAME,
							fPos.x,
							fPos.y + .25,
							fPos.z,
							(rand.nextFloat() - .5f) / 20 * 6,
							(rand.nextFloat() - .5f) / 20 * 6,
							(rand.nextFloat() - .5f) / 20 * 6
					);
					world.addParticle(ParticleTypes.SMALL_FLAME,
							fPos.x,
							fPos.y + .25,
							fPos.z,
							(rand.nextFloat() - .5f) / 20 * 6,
							(rand.nextFloat() - .5f) / 20 * 6,
							(rand.nextFloat() - .5f) / 20 * 6
					);
				}
			});
		});
		ClientPlayNetworking.registerGlobalReceiver(Yay.Smelt_ItemEntity_Cooking, (client, handler, buf, responseSender) -> {
			Vector3f fPos = buf.readVector3f();
			BlockPos bPos = new BlockPos((int)fPos.x, (int)fPos.y, (int)fPos.z);
			ClientWorld world = client.world;

			client.execute(() -> {
				Random rand = Random.create();
				world.playSound(client.player, bPos, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, .4F,  .666f + rand.nextFloat() / 1.5f);

				world.addParticle(ParticleTypes.SMALL_FLAME,
						fPos.x + (rand.nextFloat() - .5f) / 2,
						fPos.y + .25 + (rand.nextFloat() - .5f) / 2,
						fPos.z + (rand.nextFloat() - .5f) / 2,
						(rand.nextFloat() - .5f) / 20 * .1,
						(rand.nextFloat() - .5f) / 20 * .1,
						(rand.nextFloat() - .5f) / 20 * .1
				);
			});
		});
	}
}
