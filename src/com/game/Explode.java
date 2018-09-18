package com.game;

import java.awt.*;

import com.smart.TankClient;

public class Explode {
	
	private int x, y, index = 0;
	
	int[] diameter = {8, 16, 32, 40, 32, 16, 10};
	
	private boolean living = true;
	
	private TankClient tc;
	
	public void draw(Graphics g) {
		
		if (!living) {
			tc.explodeList.remove(this);
			return;
		}
		
		if (index == diameter.length) {
			living = false;
			index = 0;
			return;
		}
		Color c = g.getColor();
		g.setColor(Color.ORANGE);
		g.fillOval(x, y, diameter[index], diameter[index]);
		g.setColor(c);
		index++;
	}
	
	public Explode(int x, int y, TankClient tc) {
		this.x = x;
		this.y = y;
		this.tc = tc;
	}

}
