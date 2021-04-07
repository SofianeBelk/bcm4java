package fr.bcm.utils.nodeInfo.classes;

import fr.bcm.utils.nodeInfo.interfaces.PositionI;

public class Position implements PositionI{
	
	private int x,y;
	
	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}

	@Override
	public double distance(PositionI p) {
		return Math.sqrt(Math.pow((p.getX()) - this.x, 2) + Math.pow((p.getY()) - this.y, 2));
	}
}
