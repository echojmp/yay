package echojmp.yay;

import echojmp.yay.Enchants.SmeltFunctionality;
import net.fabricmc.api.ClientModInitializer;

public class Client implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		SmeltFunctionality.init();
	}
}
