package com.smart;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.game.Bullet;
import com.game.Explode;
import com.game.HpPackage;
import com.game.Tank;
import com.game.Wall;

public class TankClient extends Frame{
	private boolean flag = true;
	
	public static final int SCREEN_WIDTH = 800;
	public static final int SCREEN_HEIGHT = 600;
	
	public Tank myTank = new Tank(50, 50, true, Tank.Direction.STOP, this);
	public Wall w1 = new Wall(200, 200, 40, 300, this);
	public Wall w2 = new Wall(400, 100, 300, 30, this);
	public HpPackage hp = new HpPackage(this);
	
	public List<Tank> tankList = new ArrayList<Tank>();
	public List<Explode> explodeList = new ArrayList<Explode>();
	public List<Bullet> bulletList = new ArrayList<Bullet>();
	
	private Image offScreen = null;

//	 绘制方法
	public void paint(Graphics g) {
		g.drawString("bullet count : " + bulletList.size(), 10, 40);
		g.drawString("explode count : " + explodeList.size(), 10, 60);
		g.drawString("enemyTank count : " + tankList.size(), 10, 80);
		g.drawString("hp : " + myTank.getHp(), 10, 100);
		
		if (tankList.size() == 0 && myTank.isAdding()) {
			myTank.addTanks();
		}
		
		for (int i = 0; i < bulletList.size(); i++) {
			Bullet b = bulletList.get(i);
			b.attack(myTank);
			b.attackTanks(tankList);
			b.collidesWall(w1);
			b.collidesWall(w2);
			b.draw(g);
		}
		
		for (int i = 0; i < explodeList.size(); i++) {
			Explode e = explodeList.get(i);
			e.draw(g);
		}
		
		for (int i = 0; i < tankList.size(); i++) {
			Tank t = tankList.get(i);
			t.collidesWall(w1);
			t.collidesWall(w2);
			t.collidesTanks(tankList);
			t.drawTank(g);
		}
	
		myTank.drawTank(g);
		myTank.collidesTanks(tankList);
		myTank.eatHp(hp);
		w1.draw(g);
		w2.draw(g);
		hp.draw(g);
	}
	
//	减少移动闪烁方法
	public void update(Graphics g) {
		if (offScreen == null) {
			offScreen = this.createImage(SCREEN_WIDTH, SCREEN_HEIGHT);
		}
		Graphics offGraphics = offScreen.getGraphics();
		Color c = offGraphics.getColor();
		offGraphics.setColor(Color.LIGHT_GRAY);
		offGraphics.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
		offGraphics.setColor(c);
		paint(offGraphics);	
		g.drawImage(offScreen, 0, 0, null);
	}
	
//  启动窗口方法
	public void lauchFrame() {
		this.setTitle("TankWar");
		this.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
		this.setLocation(300, 100);
//		添加一个匿名类，实现窗口可关闭
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
//				0表示正常关闭
				System.exit(0);
			}
		});
//		设置窗口大小不可变
		this.setResizable(false);
//		this.setBackground(Color.WHITE);
//		添加按键监听器
		this.addKeyListener(new TankMove());
		setVisible(true);
		new Thread(new PaintThread()).run();;
	}
	
//	main方法
	public static void main(String[] args) {
		TankClient tc = new TankClient();
		tc.lauchFrame();
	}

//	线程方法
	private class PaintThread implements Runnable {

		public void run() {
			while(true) {
				repaint();
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
	}

//	我方坦克操作方法
	private class TankMove extends KeyAdapter {
		
		public void keyReleased(KeyEvent e) {
			myTank.keyRelease(e);
		}

		public void keyPressed(KeyEvent e) {
			myTank.keyPress(e);
		}	
	}
	
}
