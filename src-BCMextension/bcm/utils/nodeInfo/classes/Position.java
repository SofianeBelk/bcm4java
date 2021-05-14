package bcm.utils.nodeInfo.classes;

import java.util.concurrent.locks.ReentrantLock;

import bcm.utils.nodeInfo.interfaces.PositionI;

public class Position implements PositionI{
	
	private int x,y;
	private ReentrantLock lock = new ReentrantLock();
	
	// Those are for tests, auto incremented after creation of 1 position
	public static int xInc = 0;
	public static int yInc = 0;
	
	public Position() {
		lock.lock();
		this.x = Position.xInc;
		this.y = Position.yInc;
		
		if(Position.yInc < Position.xInc) {
			Position.yInc += 1;
		}
		else {
			Position.xInc +=1;
		}
		lock.unlock();
	}
	
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
	
	public String toString() {
		return "position : ("+this.x+","+this.y+")";
	}
}
