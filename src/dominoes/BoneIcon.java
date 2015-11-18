package dominoes;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Ben Griffiths and Matt Wildman
 * MSc Computer Science 
 * 31/03/2013
 * BoneIcone
 * @extends ImageIcon
 */
@SuppressWarnings("serial")
public class BoneIcon extends ImageIcon {
	
	//private static final String FILE_PATH = "res/images/";
	private static final String FILE_NAME = "halfBone";
	private static final String FILE_EXTENSION = ".png";
	private static final String UNKNOWN_BONE_FILE = FILE_NAME + "0" + FILE_EXTENSION;
	@Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private BufferedImage leftHalfImage;
	@Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private BufferedImage rightHalfImage;
	@Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private BufferedImage boneImage; // the combination of leftHalfImage and rightHalfImage to produce a complete bone
	@Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private String leftHalfFilePath;
	@Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private String rightHalfFilePath;
	@Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private boolean isHorizontal = true; // by default, a bone icon lies horizontally
	
	/**
	 * Default constructor
	 * Creates an icon to represent an unknown ('face-down') bone
	 */
	public BoneIcon()	{
		super();
		setLeftHalfFilePath(UNKNOWN_BONE_FILE);
		setRightHalfFilePath(UNKNOWN_BONE_FILE);
		loadAndSetImage();
		negative(getBoneImage());
	}
	
	/**
	 * Constructor
	 * Creates an icon to represent a specific known bone
	 * @param bone - the bone to be displayed in the icon's image
	 */
	public BoneIcon(Bone bone)	{
		super();
		setLeftHalfFilePath(FILE_NAME + bone.left() + FILE_EXTENSION);
		setRightHalfFilePath(FILE_NAME + bone.right() + FILE_EXTENSION);
		loadAndSetImage();
	}
	
	/**
	 * rotate
	 * Rotate the image of this bone icon through 90 degrees, clockwise
	 */
	public void rotate()	{
		int width = getBoneImage().getWidth();
        int height = getBoneImage().getHeight();
        // switch height and width around to get dimensions of rotated image
        BufferedImage rotatedImage = new BufferedImage(height, width, getBoneImage().getColorModel().getTransparency());
        for(int x = 0; x < width; x++)  
            for(int y = 0; y < height; y++ )  
                rotatedImage.setRGB(height - 1 - y, x, getBoneImage().getRGB(x, y));
        this.setImage(rotatedImage);
        this.switchIsHorizontal(); // this icon now lies vertically
	}
	
	/**
	 * rotate180
	 * Rotates a bone icon through 180 degrees
	 */
	public void rotate180()	{
		String temp = getLeftHalfFilePath();
		setLeftHalfFilePath(getRightHalfFilePath());
		setRightHalfFilePath(temp);
		loadAndSetImage();
	}
	
	/**
	 * rotateImage180
	 * @param image - the image to be rotated through 180 degrees
	 * @return the rotated image
	 */
	private BufferedImage rotateImage180(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();
		BufferedImage rotatedImage = new BufferedImage(width, height, image.getColorModel().getTransparency());
        Graphics2D g = rotatedImage.createGraphics();
        g.drawImage(image, 0, 0, width, height, width, height, 0, 0, null);
        return rotatedImage;
	}
	
	/**
	 * negative
	 * @param image the image to be colour-inverted
	 * @return a 'negative' of the image
	 */
	private BufferedImage negative(BufferedImage image) {
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int RGBvalues = image.getRGB(x, y); // gets RGB values at this pixel
                Color colour = new Color(RGBvalues, true);
                // invert RGB values such that (255, 255, 255) --> (0, 0, 0)
                colour = new Color(Math.abs(colour.getRed() - 255), Math.abs(colour.getGreen() - 255), 
                				   Math.abs(colour.getBlue() - 255));
                image.setRGB(x, y, colour.getRGB()); // change the colour of this pixel
            }
        }
        return image;
    }
	
	/**
	 * loadAndSetImage
	 * Reads in images from files and then sets the icon to use the image created by combining the two loaded images.
	 */
	private void loadAndSetImage() {
		ClassLoader classLoader = this.getClass().getClassLoader();
		URL leftHalfURL = classLoader.getResource(getLeftHalfFilePath());
		URL rightHalfURL = classLoader.getResource(getRightHalfFilePath());
		try	{
			setLeftHalfImage(ImageIO.read(leftHalfURL));
			setRightHalfImage(ImageIO.read(rightHalfURL));
		}
		catch(IOException e) {
			System.err.println("Error loading images " + leftHalfURL.toString() + ", " + rightHalfURL.toString());
		}
		setRightHalfImage(rotateImage180(getRightHalfImage()));
		setBoneImage(combine(getLeftHalfImage(), getRightHalfImage()));
		this.setImage(getBoneImage());
	}
	
	/**
	 * combine
	 * Stitches two images together into one image that is the same height and twice as long.
	 * Assumes both images have equal dimensions
	 * @param leftHalf - a BufferedImage that will form the left half of the combined image
	 * @param rightHalf - a BufferedImage that will form the right half of the combined image
	 * @return an image composed of leftHalf and rightHalf stitched together
	 */
	private BufferedImage combine(BufferedImage leftHalf, BufferedImage rightHalf) {
		int newWidth = leftHalf.getWidth() * 2;
		int newHeight = leftHalf.getHeight();
		BufferedImage combinedImage = new BufferedImage(newWidth, newHeight, leftHalf.getColorModel().getTransparency());
		Graphics2D g = combinedImage.createGraphics();
		g.drawImage(leftHalf,0,0,null); // draw the leftHalf onto the combined image, starting at point (0,0)
		g.drawImage(rightHalf,leftHalf.getWidth(),0,null); // add the rightHalf to the combined image
		g.dispose();
		return combinedImage;
	}
	
	/**
	 * switchIsHorizontal
	 * Sets isHorizontal to !isHorizontal
	 */
	private void switchIsHorizontal() {
		this.setHorizontal(!this.isHorizontal());
	}
}