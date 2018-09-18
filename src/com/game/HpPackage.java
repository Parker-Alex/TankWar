package com.game;

import java.awt.*;

import com.smart.TankClient;

public class HpPackage {
	private int x, y, w, h, step = 0, time = 0;
	private boolean living = true;
	
	TankClient tc;
	
//	private int[][] pos = {{300, 300}, {400, 300}, {500, 300}, {600, 300}, {700, 300},
//			{700, 400}, {700, 500},{600, 500}, {500, 500}, {500, 400}, {500, 300}, 
//			{500, 200},{400, 200}, {300, 200}, {300, 300}, {300, 400}, {300, 500},
//			{400, 500}, {500, 500}, {500, 400}, {500, 300}, {500, 200}, {600, 200},
//			{700, 200}, {700, 300}, {500, 300}, {300, 300}};
	private int[][] pos = {{300,300},{310,300},{320,300},{330,300},{340,300},{350,300}};
	
	public HpPackage(TankClient tc) {
		this.tc = tc;
		x = pos[0][0];
		y = pos[0][1];
		w = h = 20;
	}
	
	public void draw(Graphics g) {
		livesTime();
		rebirth();
		if(!this.living) return;
		Color c = g.getColor();
		g.setColor(Color.PINK);
		g.fillRect(x, y, w, h);
		g.setColor(c);
		
		move();
	}
	
	public void move() {
		step++;
		if(step == pos.length) {
			step = 0;
		}
		x = pos[step][0];
		y = pos[step][1];
	}
	
	public void rebirth() {
		if(!this.living) {
			time++;
			if(time == 300) {
				this.living = true;
				time = 0;
			}
		}
	}
	
	public void livesTime() {
		if(this.living) {
			time++;
			if(time == 200) {
				time = 0;
				this.living = false;
			}
		}
	}
	
	public Rectangle getRec() {
		return new Rectangle(x, y, w, h);
	}

	public boolean isLiving() {
		return living;
	}

	public void setLiving(boolean living) {
		this.living = living;
	}

	
}
