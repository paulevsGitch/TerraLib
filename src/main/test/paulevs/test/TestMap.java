package paulevs.test;

import net.minecraft.level.gen.BiomeSource;
import paulevs.terralib.biome.OverworldBiomeSource;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class TestMap {
	public static void main(String[] args) {
		BufferedImage img = new BufferedImage(512, 512, BufferedImage.TYPE_INT_ARGB);
		int[] data = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
		
		BiomeSource source = new OverworldBiomeSource(0, 256);
		
		int index = 0;
		for (int x = 0; x < 512; x++) {
			for (int z = 0; z < 512; z++) {
				data[index++] = source.getBiome(x, z).grassColor | 255 << 24;
			}
		}
		
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(new JLabel(new ImageIcon(img)));
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}
