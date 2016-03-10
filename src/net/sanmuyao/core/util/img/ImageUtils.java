package net.sanmuyao.core.util.img;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * 图片操作工具类
 * @author dqm
 * 2015年11月26日 周四
 */
public class ImageUtils {
	
	/**
	 * 实现图片按角度旋转
	 * @param fileName 文件名，包括全路径
	 * @param degree 旋转角度
	 * @throws IOException
	 * 直接将指定的图片按角度旋转，旋转后直接覆盖原图
	 */
	public static void rotate(String fileName, int degree) throws Exception {
		int index = fileName.lastIndexOf(".");
		if(index<0) {
			throw new Exception("无法定位文件的扩展名，请确认文件名称及路径："+fileName+" 是否正确。");
		}
		String exp = fileName.substring(index+1);
		File file = new File(fileName);
		if(!file.isFile()) {
			throw new Exception("文件："+fileName+" 无效。");
		}
		if(!file.exists()) {
			throw new Exception("文件："+fileName+" 无法访问。");
		}
        BufferedImage src = ImageIO.read(file);  
        BufferedImage des = ImageUtils.rotate(src, degree);  
        ImageIO.write(des, exp, new File(fileName));
	}
	
	/**
	 * 实现图片按角度旋转
	 * @param bufferedimage 目标图像。BufferedImage src = ImageIO.read(new File(path + name));
	 * @param degree 旋转角度
	 * @return
	 * 将传入的图片对象旋转，旋转完后将图片文件返回
	 */
    public static BufferedImage rotate(BufferedImage bufferedimage, int degree) {  
    	int w = bufferedimage.getWidth();
        int h = bufferedimage.getHeight();
        int type = bufferedimage.getColorModel().getTransparency();
        BufferedImage img;
        Graphics2D graphics2d;
        (graphics2d = (img = new BufferedImage(w, h, type))
                .createGraphics()).setRenderingHint(
                RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2d.rotate(Math.toRadians(degree), w / 2, h / 2);
        graphics2d.drawImage(bufferedimage, 0, 0, null);
        graphics2d.dispose();
        return img;
    }  
}
