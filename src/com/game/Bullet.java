package com.game;

import java.awt.*;
import java.util.List;

import com.smart.TankClient;

public class Bullet {
	
	public static final int X_STEP = 20;
	public static final int Y_STEP =20;
	
	public static final int WIDTH = 10;
	public static final int HEIGHT = 10;
	
	private boolean living = true;
	
	private int x, y;
	
	private boolean good;
	
	private Tank.Direction dir;
	
	private TankClient tc;

	public Bullet(int x, int y, boolean good, Tank.Direction dir, TankClient tc) {
		this.x = x;
		this.y = y;
		this.good = good;
		this.dir = dir;
		this.tc = tc;
	}
	
	public void draw(Graphics g) {
		if (!living) {
			tc.bulletList.remove(this);
			return;
		}
		Color c = g.getColor();
		if (!this.good) {
			g.setColor(Color.WHITE);
		}else {
			g.setColor(Color.BLACK);
		}
		g.fillOval(x, y, WIDTH, HEIGHT);
		g.setColor(c);
		
		move();
	}
	
	public void move() {
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
		}
		
		if (x < 0 || y < 0 || x > TankClient.SCREEN_WIDTH || y > TankClient.SCREEN_HEIGHT) {
			living = false;
		}
	}

	public Rectangle getRec() {
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}
	
	public boolean attack(Tank t) {
		if (this.getRec().intersects(t.getRec()) && t.isLiving() && this.good != t.isGood()) {
			if (t.isGood()) {
				t.setHp(t.getHp() - 20);
				if (t.getHp() <= 0) t.setLiving(false);
			}else {
				t.setLiving(false);
			}
			this.setLiving(false);
			Explode e = new Explode(x, y, tc);
			tc.explodeList.add(e);
			return true;
		}
		return false;
	}
	
	public boolean attackTanks(List<Tank> tankList) {
		for (int i = 0; i < tankList.size(); i++) {
			if (tankList.get(i).isLiving()) {
				this.attack(tankList.get(i));
			}
		}
		return false;
	}
	
	public boolean collidesWall(Wall w) {
		if (living && this.getRec().intersects(w.getRec())) {
			living = false;
			Explode e = new Explode(x, y, tc);
			tc.explodeList.add(e);
			return true;
		}
		return false;
	}
	
	public boolean isLiving() {
		return living;
	}

	public void setLiving(boolean living) {
		this.living = living;
	}
	
	
}
