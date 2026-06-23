package echojmp.yay.Enchants;

import com.google.common.collect.Maps;
import echojmp.yay.Utils;
import echojmp.yay.Yay;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.tick.TickScheduler;
import org.apache.logging.log4j.core.jmx.Server;

import java.util.List;
import java.util.Map;

public class SmeltFunctionality {
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

        PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, entity) -> {
            int smeltLevel = EnchantmentHelper.getLevel(Yay.ENCHANTMENT_SMELT, player.getMainHandStack());

            if (smeltLevel > 0) {
                // Logic
                Utils.schedule(1, server -> {
                    List<ItemEntity> drops = world.getEntitiesByClass(ItemEntity.class,
                            new Box(pos),
                            drop -> {return true;}
                    );
                    boolean canCook = false;
                    for (ItemEntity drop : drops) {
                        if (Smelt.canCook(world, drop.getStack())) {
                            canCook = true;
                            break;
                        };
                    }
                    if (!canCook) return;

                    ItemStack offHandItem = player.getOffHandStack();
                    int fuelEff = Smelt.getFuelEfficiency(offHandItem);

                    Map<ItemEntity, Integer> fuelMap = Maps.newLinkedHashMap();
                    while (fuelEff > 0) {
                        for (ItemEntity drop : drops) {
                            if (!Smelt.canCook(world, drop.getStack())) continue;

                            int maxRemove = Math.min(drop.getStack().getCount(), fuelEff);
                            fuelMap.put(drop, fuelMap.getOrDefault(drop, 0) + maxRemove);
                            fuelEff -= maxRemove;
                            if (fuelEff == 0) {
                                if (offHandItem.isOf(Items.LAVA_BUCKET)) {
                                    ItemStack bucket = new ItemStack(Items.BUCKET);
                                    bucket.setCount(offHandItem.getCount());
                                    player.setStackInHand(Hand.OFF_HAND, bucket);
                                } else {
                                    offHandItem.decrement(1);
                                }

                                break;
                            }
                        }
                    }

                    for (Map.Entry<ItemEntity, Integer> entry : fuelMap.entrySet()) {
                        int fuel = entry.getValue();
                        ItemEntity drop = entry.getKey();

                        int cookTime = Smelt.getCookingTime(world, drop.getStack(), fuel, smeltLevel);
                        Smelt.cookDrop(world, drop, cookTime);
                    }

                    // FX
                    if (!fuelMap.isEmpty()) {
                        PacketByteBuf buf = PacketByteBufs.create();
                        buf.writeBlockPos(pos);
                        for (ServerPlayerEntity sPlr : PlayerLookup.tracking((ServerWorld) world, pos)) {
                            ServerPlayNetworking.send(sPlr, Yay.Smelt_Block_Destroy, buf);
                        }
                    }
                });
            }
        });
    }
}
