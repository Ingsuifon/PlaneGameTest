package cn.sxt.game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * 飞机游戏的主窗口
 * @author 影随风
 */
public class MyGameFrame extends JFrame {
    public static int GAME_WIDTH = 500;
    public static int GAME_HEIGHT = 500;
    Image planeImg = GameUtil.getImage("images/plane.png");
    Image bg = GameUtil.getImage("images/bg.jpg");
    Plane plane = new Plane(planeImg, 250, 250);
    Shell[] shells = new Shell[50];
    @Override
    public void paint(Graphics g) {
        g.drawImage(bg, 0, 0, null);
        plane.drawSelf(g);
        for(int i = 0; i < shells.length; i++) {
            shells[i].draw(g);
            boolean touch = shells[i].getRect().intersects(plane.getRect());
            if(touch)
                plane.life = false;
        }
    }

    class PaintThread extends Thread {
        @Override
        public void run() {
            while(true) {
                repaint();
                try {
                    Thread.sleep(40);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class KeyMonitor extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            plane.addDirection(e);
        }

        @Override
        public void keyReleased(KeyEvent e) {
            plane.minusDirection(e);
        }
    }

    public void launchFrame() {
        this.setTitle("飞机游戏");
        this.setSize(GAME_WIDTH, GAME_HEIGHT);
        this.setLocation(300, 300);
        this.setVisible(true);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        new PaintThread().start();
        addKeyListener(new KeyMonitor());  //增加键盘监听

        for(int i = 0; i < shells.length; i++)
            shells[i] = new Shell();
    }

    public static void main(String[] args) {
        MyGameFrame f = new MyGameFrame();
        f.launchFrame();
    }
}
