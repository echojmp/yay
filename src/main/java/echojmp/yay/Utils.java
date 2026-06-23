package echojmp.yay;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.apache.logging.log4j.core.jmx.Server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public class Utils {
    //private static final Map<Item, Integer> fuelMap = AbstractFurnaceBlockEntity.createFuelTimeMap(); // wait what? wdym buggy?

    // same thing as in the thing
    public static int getFuelTime(Item fuel) {
        return AbstractFurnaceBlockEntity.createFuelTimeMap().getOrDefault(fuel, 0);
    }

    // returns a new ItemStack that the given ItemStack would become if fully smelted in a furnace, does not run asynchronously ig
    private static final Inventory smelt_sample_inv = new SimpleInventory(1);
    public static ItemStack smelt(World world, ItemStack item) {
        RecipeManager recipeManager = world.getRecipeManager();

        smelt_sample_inv.setStack(0, item);
        Optional<SmeltingRecipe> recipe = recipeManager.getFirstMatch(RecipeType.SMELTING, smelt_sample_inv, world);

        if (recipe.isPresent()) {
            ItemStack smeltResult = recipe.get().getOutput(world.getRegistryManager());
            smeltResult.setCount(item.getCount());
            return smeltResult;
        }

        return ItemStack.EMPTY;
    }
    public static float getSmeltXP(World world, ItemStack item) {
        RecipeManager recipeManager = world.getRecipeManager();

        smelt_sample_inv.setStack(0, item);
        Optional<SmeltingRecipe> recipe = recipeManager.getFirstMatch(RecipeType.SMELTING, smelt_sample_inv, world);

        return recipe.map(smeltingRecipe -> smeltingRecipe.getExperience() * item.getCount()).orElse(0F);
    }
    public static int getCookTime(World world, ItemStack item) {
        RecipeManager recipeManager = world.getRecipeManager();

        smelt_sample_inv.setStack(0, item);
        Optional<SmeltingRecipe> recipe = recipeManager.getFirstMatch(RecipeType.SMELTING, smelt_sample_inv, world);

        return recipe.map(AbstractCookingRecipe::getCookTime).orElse(200);
    }

    // I couldn't find how minecraft does it
    private static Map<Consumer<MinecraftServer>, Integer> scheduled = new HashMap<>();
    public static void schedule(int ticks, Consumer<MinecraftServer> func) {
        scheduled.put(func, ticks);
    }

    // lazy but eh
    private static Map<Consumer<MinecraftServer>, Integer> repeated = new HashMap<>();
    public static void repeat(int ticks, Consumer<MinecraftServer> func) {
        repeated.put(func, ticks);
    }

    // init
    public static void initServer() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            // scheduled
            for (Map.Entry<Consumer<MinecraftServer>, Integer> entry : new ArrayList<>(scheduled.entrySet())) {
                int ticksRemaining = entry.getValue() - 1;
                entry.setValue(ticksRemaining);
                if (ticksRemaining <= 0) {
                    scheduled.remove(entry.getKey());
                    entry.getKey().accept(server);
                }
            }

            // repeated
            for (Map.Entry<Consumer<MinecraftServer>, Integer> entry : new ArrayList<>(repeated.entrySet())) {
                int ticksRemaining = entry.getValue() - 1;
                entry.setValue(ticksRemaining);
                if (ticksRemaining <= 0) {
                    repeated.remove(entry.getKey());
                }
                entry.getKey().accept(server);
            }
        });
    }
}
