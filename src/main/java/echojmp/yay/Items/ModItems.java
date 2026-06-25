package echojmp.yay.Items;

import echojmp.yay.Yay;
import net.fabricmc.fabric.api.item.v1.FabricItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItems {
    public static final ItemGroup ModGroup = Registry.register(Registries.ITEM_GROUP,
            new Identifier(Yay.MOD_ID, "molten_charcoal"),
            FabricItemGroup.builder().displayName(Text.translatable("itemgroup"))
                    .icon(() -> new ItemStack(ModItems.MOLTEN_CHARCOAL)).entries((displayContext, entries) -> {
                        entries.add(ModItems.MOLTEN_CHARCOAL);
                        entries.add(ModItems.MOLTEN_COAL);
                        entries.add(ModItems.MOLTEN_IRON_INGOT);
                        entries.add(ModItems.MOLTEN_GOLD_INGOT);
                        entries.add(ModItems.MOLTEN_COPPER_INGOT);
                        entries.add(ModItems.MOLTEN_DIAMOND);
                        entries.add(ModItems.MOLTEN_EMERALD);
                        entries.add(ModItems.MOLTEN_LAPIS_LAZULI);
                    }).build());

    public static final Item MOLTEN_CHARCOAL = registerItem("molten_charcoal", new Item(new FabricItemSettings()));
    public static final Item MOLTEN_COAL = registerItem("molten_coal", new Item(new FabricItemSettings()));
    public static final Item MOLTEN_IRON_INGOT = registerItem("molten_iron_ingot", new Item(new FabricItemSettings()));
    public static final Item MOLTEN_GOLD_INGOT = registerItem("molten_gold_ingot", new Item(new FabricItemSettings()));
    public static final Item MOLTEN_COPPER_INGOT = registerItem("molten_copper_ingot", new Item(new FabricItemSettings()));
    public static final Item MOLTEN_DIAMOND = registerItem("molten_diamond", new Item(new FabricItemSettings()));
    public static final Item MOLTEN_EMERALD = registerItem("molten_emerald", new Item(new FabricItemSettings()));
    public static final Item MOLTEN_LAPIS_LAZULI = registerItem("molten_lapis_lazuli", new Item(new FabricItemSettings()));

    public static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(Yay.MOD_ID, name), item);
    }

    public static void init() {}
}
