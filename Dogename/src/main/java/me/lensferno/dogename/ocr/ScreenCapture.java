package me.lensferno.dogename.ocr;

import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.*;

//edited code from https://blog.csdn.net/caoyuan10036/article/details/7199505

public class ScreenCapture {
    // test main
    /*
    public static void main(String[] args) {
        File tempFile = new File( "files"+File.separator+"ocr.png");
        ScreenCapture capture = ScreenCapture.getInstance();
        capture.captureImage();
        JLabel imagebox = new JLabel();
        imagebox.setIcon(capture.getPickedIcon());
        try {
            capture.saveToFile(tempFile);
            System.out.println("Over");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
*/

    public static final String SCREEN_CAPTURE_LOCA="files"+File.separator+"ocrCache.png";

    private static boolean canceled =false;


    public static boolean getScreenCapture(){

        try {
            canceled=false;
            File tempFile = new File(SCREEN_CAPTURE_LOCA);
            ScreenCapture capture = ScreenCapture.getInstance();
            capture.captureImage();
            if(canceled){
                return false;
            }
            JLabel imagebox = new JLabel();
            imagebox.setIcon(capture.getPickedIcon());
            capture.saveToFile(tempFile);
            System.out.println("Over");

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private ScreenCapture() {

        try {
            robot = new Robot();
        } catch (AWTException e) {
            System.err.println("Internal Error: " + e);
            e.printStackTrace();
        }
        JPanel cp = (JPanel) dialog.getContentPane();
        cp.setLayout(new BorderLayout());
        labFullScreenImage.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent evn) {
                if (evn.getButton()==MouseEvent.BUTTON3){
                    canceled=true;
                    System.out.println("captureCancel");

                    dialog.setVisible(false);
                    return;
                }
                isFirstPoint = true;
                pickedImage = fullScreenImage.getSubimage(recX, recY, recW,
                        recH);
                dialog.setVisible(false);

            }
        });


        labFullScreenImage.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent evn) {
                if (isFirstPoint) {
                    x1 = evn.getX();
                    y1 = evn.getY();
                    isFirstPoint = false;
                } else {
                    x2 = evn.getX();
                    y2 = evn.getY();
                    int maxX = Math.max(x1, x2);
                    int maxY = Math.max(y1, y2);
                    int minX = Math.min(x1, x2);
                    int minY = Math.min(y1, y2);
                    recX = minX;
                    recY = minY;
                    recW = maxX - minX;
                    recH = maxY - minY;
                    labFullScreenImage.drawRectangle(recX, recY, recW, recH);
                }
            }

            public void mouseMoved(MouseEvent e) {
                labFullScreenImage.drawCross(e.getX(), e.getY());
                //messageLabel.setLocation(e.getX(), e.getY());
            }
        });

        cp.add(BorderLayout.CENTER, labFullScreenImage);
        //cp.add(messageLabel);
        dialog.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
        dialog.setAlwaysOnTop(true);
        dialog.setMaximumSize(Toolkit.getDefaultToolkit().getScreenSize());
        dialog.setUndecorated(true);
        dialog.setSize(dialog.getMaximumSize());
        dialog.setModal(true);
    }

    // Singleton Pattern
    public static ScreenCapture getInstance() {
        return defaultCapturer;
    }

    /** 捕捉屏幕的一个矫形区域 */
    public void captureImage() {
        fullScreenImage = robot.createScreenCapture(new Rectangle(Toolkit
                .getDefaultToolkit().getScreenSize()));
        ImageIcon icon = new ImageIcon(fullScreenImage);
        labFullScreenImage.setIcon(icon);
        dialog.setVisible(true);
    }

    /** 得到捕捉后的BufferedImage */
    public BufferedImage getPickedImage() {
        return pickedImage;
    }

    /** 得到捕捉后的Icon */
    public ImageIcon getPickedIcon() {
        return new ImageIcon(getPickedImage());
    }

    /**
     * 储存为一个文件,为PNG格式
     *
     * @deprecated replaced by saveAsPNG(File file)
     **/
    @Deprecated
    public void saveToFile(File file) throws IOException {
        ImageIO.write(getPickedImage(), defaultImageFormater, file);
    }


    /** 写入一个OutputStream */
    public void write(OutputStream out) throws IOException {
        ImageIO.write(getPickedImage(), defaultImageFormater, out);
    }

    // singleton design pattern
    private static ScreenCapture defaultCapturer = new ScreenCapture();
    private int x1, y1, x2, y2;
    private int recX, recY, recH, recW; // 截取的图像
    private boolean isFirstPoint = true;
    private BackgroundImage labFullScreenImage = new BackgroundImage();
    //private Label messageLabel = new Label("按任意键可退出。");
    private Robot robot;
    private BufferedImage fullScreenImage;
    private BufferedImage pickedImage;
    private String defaultImageFormater = "png";
    private JDialog dialog = new JDialog();
}

/** 显示图片的Label */
class BackgroundImage extends JLabel {
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawRect(x, y, w, h);
        String area = w + " * " + h;
        g.drawString(area, x + w / 2 - 15, y + h / 2);
        g.drawLine(lineX, 0, lineX, getHeight());

        g.drawLine(0, lineY, getWidth(), lineY);
    }

    public void drawRectangle(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        h = height;
        w = width;
        repaint();
    }

    public void drawCross(int x, int y) {
        lineX = x;
        lineY = y;
        repaint();
    }

    int lineX, lineY;
    int x, y, h, w;
}
