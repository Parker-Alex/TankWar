package com.game;

import java.awt.*;

import com.smart.TankClient;

public class Wall {
	private int x, y, w, h;
	
	public TankClient tc;

	public Wall(int x, int y, int w, int h, TankClient tc) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.tc = tc;
	}
	
	public void draw(Graphics g) {
		Color c = g.getColor();
		g.setColor(Color.BLACK);
		g.fillRect(x, y, w, h);
		g.setColor(c);
	}
	
	public Rectangle getRec() {
		return new Rectangle(x, y, w, h);
	}

}
