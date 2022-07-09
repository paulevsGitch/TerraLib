package paulevs.terralib;

import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ModID;

public class TerraLib {
	public static final ModID MOD_ID = ModID.of("terralib");
	
	public static Identifier id(String name) {
		return MOD_ID.id(name);
	}
}
