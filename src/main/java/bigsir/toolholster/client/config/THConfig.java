package bigsir.toolholster.client.config;

import bigsir.toolholster.client.config.setup.Config;
import bigsir.toolholster.client.config.setup.IModConfig;
import bigsir.toolholster.core.data.ItemConfig;
import net.minecraft.core.item.Item;
import net.minecraft.core.util.collection.NamespaceID;

import java.util.*;

public class THConfig implements IModConfig {
	public boolean mirrorRender = false;
	public ConfigData[] holsteredItems = new ConfigData[]{
		new ConfigData("/^minecraft:item/tool_.+/").withFull3D(),
		new ConfigData("minecraft:item/paintbrush").withFull3D(),
		new ConfigData("-minecraft:item/tool_compass"),
		new ConfigData("-minecraft:item/tool_clock"),
		new ConfigData("-minecraft:item/tool_calendar"),
	};
	private static final byte[] HOLSTERED_ITEM = new byte[16384];
	private static final int HOLSTER_MASK = 0b1;
	private static final int MASK_3D = 0b10;
	public static final Config<THConfig> configWrapper = new Config<>(new THConfig());
	public static THConfig instance;
	public static final Map<UUID, byte[]> serverData = new HashMap<>();
	private static final List<ItemConfig> configList = new ArrayList<>();

	@Override
	public void init() {
		instance = configWrapper.getConfig();

		for(ConfigData data : holsteredItems){
			String string = data.namespaceId;

			if(string.startsWith("/") && string.endsWith("/")){
				String regex = string.substring(1, string.length()-1);

				for (Map.Entry<NamespaceID, Item> entry : Item.itemsMap.entrySet()) {
					String fullID = entry.getKey().namespace() + ":" + entry.getKey().value();
					if(fullID.matches(regex)){
						int offsetId = entry.getValue().id-16384;
						int byteData = 1 | (data.full3d ? MASK_3D : 0);
						HOLSTERED_ITEM[offsetId] = (byte) byteData;

						ItemConfig config = new ItemConfig(entry.getValue().id)
							.setFull3D(data.full3d)
							.setMirrored(false)
							.setSide(data.side); //FIXME
						configList.add(config);
					}
				}

			}else if(string.startsWith("-")){
				String[] split = string.substring(1).split(":");
				Item item = Item.itemsMap.get(NamespaceID.getPermanent(split[0], split[1]));
				HOLSTERED_ITEM[item.id-16384] = 0;

			}else{
				String[] split = string.split(":");
				Item item = Item.itemsMap.get(NamespaceID.getPermanent(split[0], split[1]));
				int offsetId = item.id-16384;
				int byteData = 1 | (data.full3d ? MASK_3D : 0);
				HOLSTERED_ITEM[offsetId] = (byte) byteData;

				ItemConfig config = new ItemConfig(item.id)
					.setFull3D(data.full3d)
					.setMirrored(false)
					.setSide(data.side); //FIXME
				configList.add(config);
			}
		}

		//Array no longer used so it can be nulled
		//holsteredItems = null;
	}

	public static List<ItemConfig> getConfigList() {
		return configList;
	}

	public static boolean isHolstered(int id, UUID uuid){
		byte[] data = serverData.getOrDefault(uuid, HOLSTERED_ITEM);
		if(data == null) return false;

		id -= 16384;
		return id >= 0 && id < data.length && (data[id] & HOLSTER_MASK) != 0;
	}

	public static boolean isFull3D(int id, UUID uuid){
		byte[] data = serverData.getOrDefault(uuid, HOLSTERED_ITEM);
		if(data == null) return false;

		id -= 16384;
		return id >= 0 && id < data.length && (data[id] & MASK_3D) != 0;
	}

	public static byte[] getHolsteredItem() {
		return HOLSTERED_ITEM;
	}

	public static class ConfigData {
		public String namespaceId;
		boolean full3d = false;
		boolean side = false;

		public ConfigData(String namespaceId){
			this.namespaceId = namespaceId;
		}

		public ConfigData withFull3D(){
			this.full3d = true;
			return this;
		}
	}
}
