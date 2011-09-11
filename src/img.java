import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;


public class img {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		BufferedImage im = new BufferedImage(64, 64, BufferedImage.TYPE_3BYTE_BGR);
		Graphics gr = im.getGraphics();

		ArrayList<Byte> al = new ArrayList<Byte>();

		byte[] bb = new byte[12290];
		try {
			FileInputStream fis = new FileInputStream("t.txt");
			System.out.println(fis.read(bb));
			System.out.println(new String(bb));
//			byte b;
//			while ((b = (byte) fis.read()) != -1) {
//				System.out.println(b);
//				al.add(b);
//			}
			System.out.println(new File("t.txt").exists());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		 int ix = 0;
//		 byte bb[] = new byte[al.size()];
		 while (ix < al.size()) {
			 bb[ix] = al.get(ix);
			 ix += 1;
		 }
		 int idx = 0;
		 int idxa = 0;
//		 idx += 2;
		 for (int j = 0 ; j < 64 ; j++) 
			 for (int i = 0 ; i < 64 ; i++) {
				 int r=0,g=0,b=0,t;
				 r = bb[idx++];	
				 g = bb[idx++];
				 b = bb[idx++];
				 if (r == -1 && b == -1 && g == -1) {
					 
				 } else
					 System.out.println(r + ", " + g + ", " + b);
//				 if (r < 0 || g < 0 || b < 0)
//					 continue;
				 if (r == -1)
					 r = 255;
				 if (g == -1)
					 g = 255;
				 if (b == -1)
					 b = 255;
//				 if (g < 0 && b < 0 && r == 255) {
//					 r = b;
//					 g = 0;
//					 b = 0;
//				 }
				 if (r < 0)
					 r = 255 - -r;
				 if (g < 0)
					 g = 255 - -g;
				 if (b < 0)
					 b = 255 - -b;
				 
				 gr.setColor(new Color(r, g, b));
				 gr.drawLine(i, j, i, j);
				 
//				 idx++;
			 }
		

		 try {
//			 i = ImageIO.read(b);
			 ImageIO.write(im, "PNG", new File("out.png"));
		 } catch (IOException e) {
		 // TODO Auto-generated catch block
		 e.printStackTrace();
		 }
	}

}
