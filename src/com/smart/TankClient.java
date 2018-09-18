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

//	 ���Ʒ���
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
	
//	�����ƶ���˸����
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
	
//  �������ڷ���
	public void lauchFrame() {
		this.setTitle("TankWar");
		this.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
		this.setLocation(300, 100);
//		���һ�������࣬ʵ�ִ��ڿɹر�
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
//				0��ʾ�����ر�
				System.exit(0);
			}
		});
//		���ô��ڴ�С���ɱ�
		this.setResizable(false);
//		this.setBackground(Color.WHITE);
//		��Ӱ���������
		this.addKeyListener(new TankMove());
		setVisible(true);
		new Thread(new PaintThread()).run();;
	}
	
//	main����
	public static void main(String[] args) {
		TankClient tc = new TankClient();
		tc.lauchFrame();
	}

//	�̷߳���
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

//	�ҷ�̹�˲�������
	private class TankMove extends KeyAdapter {
		
		public void keyReleased(KeyEvent e) {
			myTank.keyRelease(e);
		}

		public void keyPressed(KeyEvent e) {
			myTank.keyPress(e);
		}	
	}
	
}
