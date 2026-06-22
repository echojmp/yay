package echojmp.yay;

import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.*;
import net.minecraft.world.World;

import java.util.Map;
import java.util.Optional;

public class Utils {
    private static final Map<Item, Integer> fuelMap = AbstractFurnaceBlockEntity.createFuelTimeMap();

    // same thing as in the thing
    public static int getFuelTime(ItemStack fuel) {
        if (fuel.isEmpty()) {
            return 0;
        }

        Item item = fuel.getItem();
        return fuelMap.getOrDefault(item, 0);
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
}
