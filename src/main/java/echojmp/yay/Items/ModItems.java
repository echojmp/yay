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
                    }).build());

    public static final Item MOLTEN_CHARCOAL = registerItem("molten_charcoal", new Item(new FabricItemSettings()));

    public static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(Yay.MOD_ID, name), item);
    }

    public static void init() {}
}
