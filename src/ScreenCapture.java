import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

public class ScreenCapture {

    private Rectangle captureRect;
    private BufferedImage capturedImage;
    public boolean captureSelected = false;

    public void setRectangle() throws Exception{
        Robot r = new Robot();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        BufferedImage screen = r.createScreenCapture(new Rectangle(screenSize));

        BufferedImage screenCopy = new BufferedImage(screen.getWidth(),screen.getHeight(),screen.getType());
        JLabel screenLabel = new JLabel(new ImageIcon(screenCopy));
        JScrollPane screenScroll = new JScrollPane(screenLabel);

        screenScroll.setPreferredSize(new Dimension(
                screen.getWidth(),
                screen.getHeight()
        ));

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(screenScroll,BorderLayout.CENTER);
        repaint(screen,screenCopy);
        screenLabel.repaint();

        JFrame frame = new JFrame();
        frame.getContentPane().add(screenLabel);
        frame.setVisible(true);
        frame.setAlwaysOnTop(true);
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        screenLabel.addMouseMotionListener(new MouseMotionListener() {
            Point start = new Point();

            @Override
            public void mouseDragged(MouseEvent e) {
                Point end = e.getPoint();

                captureRect = new Rectangle(start,new Dimension(end.x-start.x,end.y-start.y));
                repaint(screen,screenCopy);
                screenLabel.repaint();
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                start = e.getPoint();
            }

        });

        screenLabel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                frame.dispatchEvent(new WindowEvent(frame,WindowEvent.WINDOW_CLOSING));
                captureSelected = true;
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }

    public boolean takeScreenshot() throws Exception {
        if(captureRect!= null) {
            Robot r = new Robot();
            capturedImage = r.createScreenCapture(captureRect);
            return true;
        } else return false;
    }

    public BufferedImage getCapturedImage(){
        return this.capturedImage;
    }

    private void repaint(BufferedImage orig, BufferedImage copy) {
        Graphics2D g = copy.createGraphics();
        g.drawImage(orig,0,0, null);
        if (captureRect!=null) {
            g.setColor(Color.RED);
            g.draw(captureRect);
            g.setColor(new Color(255,255,255,150));
            g.fill(captureRect);
        }
        g.dispose();
    }
}
