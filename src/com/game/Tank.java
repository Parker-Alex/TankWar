package com.game;

import java.awt.*;
import java.awt.event.*;
import java.util.Random;

import com.smart.TankClient;

/**
 * 
 * @author lijiawei
 *
 */
public class Tank {
	
	public static final int X_STEP = 10;
	public static final int Y_STEP = 10;
	
	public static final int WIDTH = 30;
	public static final int HEIGHT = 30;
	
	private boolean L = false, U = false, D = false, R = false;
	public enum Direction {L,LU,U,RU,R,RD,D,LD,STOP}
	
	private static Random r = new Random();	
//	坦克移动方向
	private Direction dir = Direction.STOP;
//	坦克炮筒方向
	private Direction tubeDir = Direction.R;	
//	区分敌我坦克标识
	private boolean good;
//	添加坦克标识	
	private boolean adding = true;
//	判断坦克是否存活
	private boolean living = true;
	
//	定义地方坦克移动的步数
	private int step = r.nextInt(12)+3;
	
	private int x, y, oldx, oldy, hp = 100;

	TankClient tc;
	private BloodSlot b = new BloodSlot();
	
	public Tank() {
	}

	public Tank(int x, int y, boolean good) {
		this.x = x;
		this.y = y;
		this.oldx = x;
		this.oldy = y;
		this.good = good;
	}
	
	public Tank(int x, int y, boolean good,Direction dir, TankClient tc) {
		this(x, y, good);
		this.dir = dir;
		this.tc = tc;
	}

	public void drawTank(Graphics g) {
		if (!living) {
			tc.tankList.remove(this);
			return;
		}
		Color c = g.getColor();
		if (good) {
			g.setColor(Color.RED);
			b.draw(g);
		}
		if (!good)
			g.setColor(Color.BLUE);
		g.fillOval(x, y, WIDTH, HEIGHT);
		g.setColor(c);
		
		switch(tubeDir) {
		case L:
			g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x, y + Tank.HEIGHT/2);
			break;
		case LU:
			g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x, y);
			break;
		case U:
			g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x + Tank.WIDTH/2, y);
			break;
		case RU:
			g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x + Tank.WIDTH, y);
			break;
		case R:
			g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x + Tank.WIDTH, y + Tank.HEIGHT/2);
			break;
		case RD:
			g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x + Tank.WIDTH, y + Tank.HEIGHT);
			break;
		case D:
			g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x + Tank.WIDTH/2, y + Tank.HEIGHT);
			break;
		case LD:
			g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x, y + Tank.HEIGHT);
			break;
		}
		
		moveTank();
	}
	
	public void moveTank() {
//		记录移动前的位置
		this.oldx = x;
		this.oldy = y;
		
		switch(dir) {
		case L:
			x-=X_STEP;
			break;
		case LU:
			x-=X_STEP;
			y-=Y_STEP;
			break;
		case U:
			y-=Y_STEP;
			break;
		case RU:
			x+=X_STEP;
			y-=Y_STEP;
			break;
		case R:
			x+=X_STEP;
			break;
		case RD:
			x+=X_STEP;
			y+=Y_STEP;
			break;
		case D:
			y+=Y_STEP;
			break;
		case LD:
			x-=X_STEP;
			y+=Y_STEP;
			break;
		case STOP:
			break;
		}
		
		if (this.dir != Direction.STOP) {
			this.tubeDir = this.dir;
		}
		
		if (x < 0) x=0;
		if (y < 30) y=30;
		if (x + Tank.WIDTH > TankClient.SCREEN_WIDTH) x = TankClient.SCREEN_WIDTH - Tank.WIDTH;
		if (y + Tank.HEIGHT > TankClient.SCREEN_HEIGHT) y = TankClient.SCREEN_HEIGHT - Tank.HEIGHT; 
		
		if (!good) {
			Direction[] dirs = Direction.values();
			if (step == 0) {
				step = r.nextInt(12) + 3;
				int i = r.nextInt(dirs.length);
				dir = dirs[i];
			}
			step--;
			
			if (r.nextInt(40) > 35) this.shot();
		}
	}
	
	private void stay() {
		x = this.oldx;
		y = this.oldy;
	}
	
	public void keyPress(KeyEvent e) {
		int key = e.getKeyCode();
		switch(key) {
		case KeyEvent.VK_F:
			rebirth();
			break;
		case KeyEvent.VK_S:
			superShot();
			break;
		case KeyEvent.VK_A:
			addTanks();
			break;
		case KeyEvent.VK_D:
			removesAll();
			break;
		case KeyEvent.VK_X:
			shot();
			break;
		case KeyEvent.VK_LEFT:
			L = true;
			break;
		case KeyEvent.VK_RIGHT:
			R = true;
			break;
		case KeyEvent.VK_UP:
			U = true;
			break;
		case KeyEvent.VK_DOWN:
			D = true;
			break;
		}
		nowDirection();
	}
	
	private void nowDirection() {
		if (L && !U && !R && !D)  dir = Direction.L;
		else if (!L && U && !R && !D)  dir = Direction.U;
		else if (!L && !U && R && !D)  dir = Direction.R;
		else if (!L && !U && !R && D)  dir = Direction.D;
		else if (L && U && !R && !D)  dir = Direction.LU;
		else if (L && !U && !R && D)  dir = Direction.LD;
		else if (!L && U && R && !D)  dir = Direction.RU;
		else if (!L && !U && R && D)  dir = Direction.RD;
		else if (!L && !U && !R && !D)  dir = Direction.STOP;
	}
	
	public void keyRelease(KeyEvent e) {
		int key = e.getKeyCode();
		switch(key) {
		case KeyEvent.VK_LEFT:
			L = false;
			break;
		case KeyEvent.VK_RIGHT:
			R = false;
			break;
		case KeyEvent.VK_UP:
			U = false;
			break;
		case KeyEvent.VK_DOWN:
			D = false;
			break;
		}
		nowDirection();
	}
	
	public Bullet shot() {
		if (!living) return null;
		int x = this.x + Tank.WIDTH/2 - Bullet.WIDTH/2;
		int y = this.y + Tank.HEIGHT/2 - Bullet.HEIGHT/2;
		Bullet b = new Bullet(x, y, good, tubeDir, this.tc);
		tc.bulletList.add(b);
		return b;
	}
	
	public Bullet shot(Direction dir) {
		if (!living) return null;
		int x = this.x + Tank.WIDTH/2 - Bullet.WIDTH/2;
		int y = this.y + Tank.HEIGHT/2 - Bullet.HEIGHT/2;
		Bullet b = new Bullet(x, y, good, dir, this.tc);
		tc.bulletList.add(b);
		return b;
	}
	
	public void superShot() {
		Direction[] dirs = Direction.values();
		for (int i=0; i<8; i++) {
			shot(dirs[i]);
		}
	}
	
	public boolean collidesWall(Wall w) {
		if (this.living && this.getRec().intersects(w.getRec())) {
			stay();
			return true;
		}
		return false;
	}
	
//	消除坦克重合
	public boolean collidesTanks(java.util.List<Tank> tankList) {
		for (int i=0; i<tankList.size(); i++) {
			Tank t = tankList.get(i);
			if (this.living && t.isLiving() && 
					this.getRec().intersects(t.getRec()) && this != t) {
				this.stay();
				t.stay();
				return true;
			}
		}
		return false;
	}
	
//	增加坦克
	public void addTanks() {
		if (tc.tankList.size() >= 20) return;
		adding = true;
		Random r = new Random();
		for (int i = 0; i < 5; i++) {
			int randW = r.nextInt(800);
			int randH = r.nextInt(600);
			tc.tankList.add(new Tank(randW, randH, false, Tank.Direction.D, tc));
		}
	}
	
//	移除坦克和子弹
	public void removesAll() {
		adding = false;
		tc.tankList.removeAll(tc.tankList);
		tc.bulletList.removeAll(tc.bulletList);
	}
	
	public void rebirth() {
		this.setHp(100);
		this.living = true;
	}
	
	private class BloodSlot {
		public void draw(Graphics g) {
			Color c = g.getColor();
			g.setColor(Color.YELLOW);
			g.drawRect(x, y-10, WIDTH, 10);
			int w = WIDTH * hp/100;
			g.fillRect(x, y-10, w, 10);
			g.setColor(c);
		}
	}
	
	public boolean eatHp(HpPackage hp) {
		if(this.living && hp.isLiving() && this.getRec().intersects(hp.getRec()) && this.hp != 100) {
			hp.setLiving(false);
			this.hp = 100;
			return true;
		}
		return false;
	}
	
	public Rectangle getRec() {
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}
	
	public boolean isLiving() {
		return living;
	}

	public void setLiving(boolean living) {
		this.living = living;
	}

	public boolean isGood() {
		return good;
	}

	public void setGood(boolean good) {
		this.good = good;
	}

	public boolean isAdding() {
		return adding;
	}

	public void setAdding(boolean adding) {
		this.adding = adding;
	}

	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}
	
	
}
