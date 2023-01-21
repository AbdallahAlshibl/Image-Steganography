import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Scanner;
import javax.imageio.ImageIO;

public class IS_decode {
	// ---------------------------------------------------------------------------------------
	// INPUTIMAGE -> the input image that need to be decoded the get the text ...
	static final String INPUTIMAGE = "C:\\Users\\Abdallah\\Downloads\\outputImage.png";

	// OUTPUTTEXTFILE -> the path for the new file that has the decoded text (create new file) ...
	static final String OUTPUTTEXTFILE = "C:\\Users\\Abdallah\\Downloads\\decodedMessage.txt";
	// ---------------------------------------------------------------------------------------

	public static String messageBits = "";
	public static int len = 0;

	public static void main(String[] args) throws Exception {

		BufferedImage imageY = readImageFile(INPUTIMAGE);

		DecodeTheMessage(imageY);
		String msg = "";
		for (int i = 0; i < len * 8; i = i + 8) {

			String sub = messageBits.substring(i, i + 8);

			int m = Integer.parseInt(sub, 2);
			char ch = (char) m;
			System.out.println("m " + m + " c " + ch);
			msg += ch;
		}
		PrintWriter out = new PrintWriter(new FileWriter(OUTPUTTEXTFILE, true), true);
		out.write(msg);
		out.close();
	}

	// function to read the image file ...
	public static BufferedImage readImageFile(String COVERIMAGEFILE) {
		BufferedImage image = null;
		File p = new File(COVERIMAGEFILE);
		try {
			image = ImageIO.read(p);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return image;
	}

	// function to decode the message ...
	public static void DecodeTheMessage(BufferedImage imageY) throws Exception {

		int j = 0;
		int currentBitEntry = 0;
		String bx_msg = "";
		for (int x = 0; x < imageY.getWidth(); x++) {
			for (int y = 0; y < imageY.getHeight(); y++) {
				if (x == 0 && y < 8) {
					int currentPixel = imageY.getRGB(x, y);
					int red = currentPixel >> 16;
					red = red & 255;
					int green = currentPixel >> 8;
					green = green & 255;
					int blue = currentPixel;
					blue = blue & 255;
					String x_s = Integer.toBinaryString(blue);
					bx_msg += x_s.charAt(x_s.length() - 1);
					len = Integer.parseInt(bx_msg, 2);

				} else if (currentBitEntry < len * 8) {
					int currentPixel = imageY.getRGB(x, y);
					int red = currentPixel >> 16;
					red = red & 255;
					int green = currentPixel >> 8;
					green = green & 255;
					int blue = currentPixel;
					blue = blue & 255;
					String x_s = Integer.toBinaryString(blue);
					messageBits += x_s.charAt(x_s.length() - 1);

					currentBitEntry++;
				}
			}
		}
		System.out.println("bin value of msg hided in img is " + messageBits);
	}
}