package echojmp.yay.Enchants;

import com.google.common.collect.Maps;
import echojmp.yay.Yay;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

public class SmeltFuncs {
    public static void init() {
        /*AttackBlockCallback.EVENT.register((player, level, hand, pos, direction) -> {
            BlockState state = level.getBlockState(pos);

            // check if enchant would work
            if (!player.isSpectator() && EnchantmentHelper.getLevel(new Smelt(), player.getStackInHand(hand)) > 0) { // has enchant
                if (Smelt.) { // is enchant usable on block

                }
            }

            return InteractionResult.PASS;
        });*/
        Yay.LOGGER.info("init SmeltFuncs");

        PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, entity) -> {
            int smeltLevel = EnchantmentHelper.getLevel(Yay.ENCHANTMENT_SMELT, player.getMainHandStack());

            if (smeltLevel > 0) {
                // this needs to run on the client I think
                Yay.LOGGER.info("playing sound");
                world.playSound(player, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 1.0F, 1.0F);

                Yay.LOGGER.info("showing particles");
                Random rand = Random.create();
                for (int i = 0; i < 8; i++) {
                    world.addParticle(ParticleTypes.ASH,
                            pos.getX() + (rand.nextFloat() - .5f),
                            pos.getY() + (rand.nextFloat() - .5f),
                            pos.getZ() + (rand.nextFloat() - .5f),
                            0.0D + (rand.nextFloat() - .5f) / 20 * 4,
                            0.0D + (rand.nextFloat() - .15f) / 20 * 2,
                            0.0D + (rand.nextFloat() - .5f) / 20 * 4
                    );
                    world.addParticle(ParticleTypes.SMALL_FLAME,
                            pos.getX() + (rand.nextFloat() - .5f),
                            pos.getY() + (rand.nextFloat() - .5f),
                            pos.getZ() + (rand.nextFloat() - .5f),
                            0.0D + (rand.nextFloat() - .5f) / 20 * 4,
                            0.0D + (rand.nextFloat() - .15f) / 20 * 2,
                            0.0D + (rand.nextFloat() - .5f) / 20 * 4
                    );
                }


                Yay.LOGGER.info("drops");
                List<ItemEntity> drops = world.getEntitiesByClass(ItemEntity.class,
                        new Box(pos),
                        drop -> {return true;}
                );

                // aaaaaaa
                Yay.LOGGER.info("found " + drops.size() + " drops");

                int fuelEff = Smelt.getFuelEfficiency(player.getOffHandStack());
                Map<ItemEntity, Integer> fuelMap = Maps.newLinkedHashMap();
                while (fuelEff > 0) {
                    for (ItemEntity drop : drops) {
                        fuelMap.put(drop, fuelMap.getOrDefault(drop, 0) + 1);
                        fuelEff -= 1;
                        if (fuelEff == 0) {
                            break;
                        }
                    }
                }

                for (Map.Entry<ItemEntity, Integer> entry : fuelMap.entrySet()) {
                    int fuel = entry.getValue();
                    ItemEntity drop = entry.getKey();
                    Yay.LOGGER.info("got " + fuel + " fuel for " + drop);
                }
            }
        });
    }
}
