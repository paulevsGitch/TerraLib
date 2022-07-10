package paulevs.terralib.map;

import paulevs.terralib.biome.TerraBiome;
import paulevs.terralib.map.picker.BiomePicker;

import java.util.Arrays;
import java.util.Random;

public class BiomeChunk {
	private static final short SIDE = 32;
	private static final byte SIDE_PRE = 4;
	private static final short SIZE = SIDE * SIDE;
	private static final short MAX_SIDE = SIZE - SIDE;
	private static final byte SCALE_PRE = SIDE / SIDE_PRE;
	private static final byte SIZE_PRE = SIDE_PRE * SIDE_PRE;
	private static final byte SIDE_MASK = SIDE - 1;
	private static final byte SIDE_PRE_MASK = SIDE_PRE - 1;
	private static final byte SIDE_OFFSET = (byte) Math.round(Math.log(SIDE) / Math.log(2));
	private static final byte SIDE_PRE_OFFSET = (byte) Math.round(Math.log(SIDE_PRE) / Math.log(2));
	private static final short[][] NEIGHBOURS;
	
	private final TerraBiome[] biomes = new TerraBiome[SIZE];
	
	public BiomeChunk(Random random, BiomePicker picker, int cx, int cz) {
		TerraBiome[][] buffers = new TerraBiome[2][SIZE];
		
		for (TerraBiome[] buffer: buffers) {
			Arrays.fill(buffer, null);
		}
		
		int sx = cx << SIDE_OFFSET;
		int sz = cz << SIDE_OFFSET;
		for (byte index = 0; index < SIZE_PRE; index++) {
			byte px = (byte) (index >> SIDE_PRE_OFFSET);
			byte pz = (byte) (index & SIDE_PRE_MASK);
			px = (byte) (px * SCALE_PRE + random.nextInt(SCALE_PRE));
			pz = (byte) (pz * SCALE_PRE + random.nextInt(SCALE_PRE));
			circle(buffers[0], getIndex(px, pz), picker.getBiome(sx + px, sz + pz, random), null);
		}
		
		boolean hasEmptyCells = true;
		byte bufferIndex = 0;
		while (hasEmptyCells) {
			TerraBiome[] inBuffer = buffers[bufferIndex];
			bufferIndex = (byte) ((bufferIndex + 1) & 1);
			TerraBiome[] outBuffer = buffers[bufferIndex];
			hasEmptyCells = false;
			
			for (short index = SIDE; index < MAX_SIDE; index++) {
				byte z = (byte) (index & SIDE_MASK);
				if (z == 0 || z == SIDE_MASK) {
					continue;
				}
				if (inBuffer[index] != null) {
					outBuffer[index] = inBuffer[index];
					short[] neighbours = getNeighbours(index & SIDE_MASK);
					short indexSide = (short) (index + neighbours[random.nextInt(6)]);
					if (indexSide >= 0 && indexSide < SIZE && outBuffer[indexSide] == null) {
						outBuffer[indexSide] = inBuffer[index];
					}
				}
				else {
					hasEmptyCells = true;
				}
			}
		}
		
		TerraBiome[] outBuffer = buffers[bufferIndex];
		byte preN = (byte) (SIDE_MASK - 2);
		for (byte index = 0; index < SIDE; index++) {
			outBuffer[getIndex(index, (byte) 0)] = outBuffer[getIndex(index, (byte) 2)];
			outBuffer[getIndex((byte) 0, index)] = outBuffer[getIndex((byte) 2, index)];
			outBuffer[getIndex(index, SIDE_MASK)] = outBuffer[getIndex(index, preN)];
			outBuffer[getIndex(SIDE_MASK, index)] = outBuffer[getIndex(preN, index)];
		}
		
		for (short index = 0; index < SIZE; index++) {
			if (outBuffer[index] == null) {
				fixNull(outBuffer, index);
			}
			else if (random.nextInt(4) == 0) {
				circle(outBuffer, index, outBuffer[index].getSubBiome(random), outBuffer[index]);
			}
		}
		
		System.arraycopy(outBuffer, 0, this.biomes, 0, SIZE);
	}
	
	private void circle(TerraBiome[] buffer, short center, TerraBiome biome, TerraBiome mask) {
		if (buffer[center] == mask) {
			buffer[center] = biome;
		}
		short[] neighbours = getNeighbours(center & SIDE_MASK);
		for (short i: neighbours) {
			short index = (short) (center + i);
			if (index >= 0 && index < SIZE && buffer[index] == mask) {
				buffer[index] = biome;
			}
		}
	}
	
	private void fixNull(TerraBiome[] buffer, short center) {
		short[] neighbours = getNeighbours(center & SIDE_MASK);
		for (short i: neighbours) {
			short index = (short) (center + i);
			if (index >= 0 && index < SIZE && buffer[index] != null) {
				buffer[center] = buffer[index];
				return;
			}
		}
	}
	
	private static byte wrap(int value) {
		return (byte) (value & SIDE_MASK);
	}
	
	private short getIndex(byte x, byte z) {
		return (short) ((short) x << SIDE_OFFSET | z);
	}
	
	public TerraBiome getBiome(int x, int z) {
		return biomes[getIndex(wrap(x), wrap(z))];
	}
	
	public void setBiome(int x, int z, TerraBiome biome) {
		biomes[getIndex(wrap(x), wrap(z))] = biome;
	}
	
	public int getSide() {
		return SIDE;
	}
	
	public static int scaleCoordinate(int value) {
		return value >> SIDE_OFFSET;
	}
	
	public static boolean isBorder(int value) {
		return wrap(value) == SIDE_MASK;
	}
	
	private short[] getNeighbours(int z) {
		return NEIGHBOURS[z & 1];
	}
	
	public static float scaleMap(float size) {
		return size / (SIDE >> 2);
	}
	
	static {
		NEIGHBOURS = new short[2][6];
		
		NEIGHBOURS[0][0] = 1;
		NEIGHBOURS[0][1] = -1;
		NEIGHBOURS[0][2] = SIDE;
		NEIGHBOURS[0][3] = -SIDE;
		NEIGHBOURS[0][4] = SIDE + 1;
		NEIGHBOURS[0][5] = SIDE - 1;
		
		NEIGHBOURS[1][0] = 1;
		NEIGHBOURS[1][1] = -1;
		NEIGHBOURS[1][2] = SIDE;
		NEIGHBOURS[1][3] = -SIDE;
		NEIGHBOURS[1][4] = -SIDE + 1;
		NEIGHBOURS[1][5] = -SIDE - 1;
	}
}
