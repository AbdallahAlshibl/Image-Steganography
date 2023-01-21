import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.regex.*;
import javax.imageio.ImageIO;

public class IS_encode {

	// ---------------------------------------------------------------------------------------
	// INPUTTEXTFILE -> the text file that need to encode in image..
	static final String INPUTTEXTFILE = "C:\\Users\\Abdallah\\Downloads\\COE_test.txt";

	// INPUTIMAGE -> the input image that will encode the text within ...
	static String INPUTIMAGE = "C:\\Users\\Abdallah\\Downloads\\one.png";

	// OUTPUTIMAGE -> the output image that has the encode text -> the image that
	// used in the decode class ...
	static final String OUTPUTIMAGE = "C:\\Users\\Abdallah\\Downloads\\outputImage.png";
	// ---------------------------------------------------------------------------------------

	public static void main(String[] args) throws Exception {

		// first check the image extension ... acceptable extensions are [jpeg/jpg/png]
		if (imageFile(INPUTIMAGE)) {
			// convert image extension from png to jpg if needed ...
			if (pngImage(INPUTIMAGE)) {
				ConvertPngToJpg(INPUTIMAGE);
			}
			String messageContent = (readMessageFile());
			int[] bits = getMessageBits(messageContent);
			System.out.println("msg in file " + messageContent);
			for (int i = 0; i < bits.length; i++)
				System.out.print(bits[i]);
			System.out.println();
			BufferedImage image = readImageFile(INPUTIMAGE);
			hideTheMessage(bits, image);
		} else {
			System.out.println("Invalid image extension, please check the acceptable extensions. ");
		}

	}

	// Function to validate image file extension .
	public static boolean imageFile(String str) {
		// Regex to check valid image file extension.
		String regex = "([^\\s]+(\\.(?i)(jpe?g|png))$)";

		// Compile the ReGex
		Pattern p = Pattern.compile(regex);

		// If the string is empty
		// return false
		if (str == null) {
			return false;
		}

		// Pattern class contains matcher() method
		// to find matching between given string
		// and regular expression.
		Matcher m = p.matcher(str);

		// Return if the string
		// matched the ReGex
		return m.matches();
	}

	// Function to validate (png) image file extension .
	public static boolean pngImage(String str) {
		// Regex to check valid image file extension.
		String regex = "([^\\s]+(\\.(?i)(png))$)";

		// Compile the ReGex
		Pattern p = Pattern.compile(regex);

		// If the string is empty
		// return false
		if (str == null) {
			return false;
		}

		// Pattern class contains matcher() method
		// to find matching between given string
		// and regular expression.
		Matcher m = p.matcher(str);

		// Return if the string
		// matched the ReGex
		return m.matches();
	}

	// function to convert image from png to jpg ...
	public static void ConvertPngToJpg(String inputImage) throws IOException {
		Path source = Paths.get(inputImage);
		Path target = Paths.get("C:\\Users\\Abdallah\\Downloads\\new.jpg");

		BufferedImage originalImage = ImageIO.read(source.toFile());

		// jpg needs BufferedImage.TYPE_INT_RGB
		// png needs BufferedImage.TYPE_INT_ARGB

		// create a blank, RGB, same width and height
		BufferedImage newBufferedImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(),
				BufferedImage.TYPE_INT_RGB);

		// draw a white background and puts the originalImage on it.
		newBufferedImage.createGraphics().drawImage(originalImage, 0, 0, Color.WHITE, null);

		// save an image
		ImageIO.write(newBufferedImage, "jpg", target.toFile());
		String str = source.toString().replace("png", "jpg");
		str = target.toString().replace("\\","\\\\");
		INPUTIMAGE = str;

	}

	// function to read the text file ...
	public static String readMessageFile() throws FileNotFoundException {
		String messageContent = "";
		File file = new File(INPUTTEXTFILE);
		Scanner scan = new Scanner(file);
		while (scan.hasNextLine()) {
			String next = scan.nextLine();
			messageContent += next;
			if (scan.hasNextLine()) {
				messageContent += "\n";
			}
		}
		scan.close();
		return messageContent;
	}

	// function to get the bits of the character ...
	public static int[] getMessageBits(String msg) {
		int j = 0;
		int[] messageBits = new int[msg.length() * 8];
		for (int i = 0; i < msg.length(); i++) {
			int x = msg.charAt(i);
			String x_s = Integer.toBinaryString(x);
			while (x_s.length() != 8) {
				x_s = '0' + x_s;
			}
			System.out.println("dec value for " + x + " is " + x_s);

			for (int i1 = 0; i1 < 8; i1++) {
				messageBits[j] = Integer.parseInt(String.valueOf(x_s.charAt(i1)));
				j++;
			}
			;
		}

		return messageBits;
	}

	// function to read the image file ...
	public static BufferedImage readImageFile(String INPUTIMAGE) {
		BufferedImage image = null;
		File p = new File(INPUTIMAGE);
		try {
			image = ImageIO.read(p);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return image;
	}

	// function to hide the text within the output image ...
	public static void hideTheMessage(int[] bits, BufferedImage image) throws Exception {
		File f = new File(OUTPUTIMAGE);
		BufferedImage outputImage = null;
		int bit_l = bits.length / 8;
		int[] bl_msg = new int[8];
		System.out.println("bit lent " + bit_l);
		String bl_s = Integer.toBinaryString(bit_l);
		while (bl_s.length() != 8) {
			bl_s = '0' + bl_s;
		}
		for (int i1 = 0; i1 < 8; i1++) {
			bl_msg[i1] = Integer.parseInt(String.valueOf(bl_s.charAt(i1)));
		}
		;
		int j = 0;
		int b = 0;
		int currentBitEntry = 8;

		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				if (x == 0 && y < 8) {
					int currentPixel = image.getRGB(x, y);
					int ori = currentPixel;
					int red = currentPixel >> 16;
					red = red & 255;
					int green = currentPixel >> 8;
					green = green & 255;
					int blue = currentPixel;
					blue = blue & 255;
					String x_s = Integer.toBinaryString(blue);
					String sten_s = x_s.substring(0, x_s.length() - 1);
					sten_s = sten_s + Integer.toString(bl_msg[b]);

					// j++;
					int temp = Integer.parseInt(sten_s, 2);
					int s_pixel = Integer.parseInt(sten_s, 2);
					int a = 255;
					int rgb = (a << 24) | (red << 16) | (green << 8) | s_pixel;
					image.setRGB(x, y, rgb);
					ImageIO.write(image, "png", f);
					b++;

				} else if (currentBitEntry < bits.length + 8) {

					int currentPixel = image.getRGB(x, y);
					int ori = currentPixel;
					int red = currentPixel >> 16;
					red = red & 255;
					int green = currentPixel >> 8;
					green = green & 255;
					int blue = currentPixel;
					blue = blue & 255;
					String x_s = Integer.toBinaryString(blue);
					String sten_s = x_s.substring(0, x_s.length() - 1);
					sten_s = sten_s + Integer.toString(bits[j]);
					j++;
					int temp = Integer.parseInt(sten_s, 2);
					int s_pixel = Integer.parseInt(sten_s, 2);

					int a = 255;
					int rgb = (a << 24) | (red << 16) | (green << 8) | s_pixel;
					image.setRGB(x, y, rgb);
					ImageIO.write(image, "png", f);

					currentBitEntry++;
				}
			}
		}
	}
}