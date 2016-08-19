package gameInterface;

import helpers.Dir;
import helpers.Point;
import worlds.ObjectLayer;
import worlds.World;

public class IEList<T extends IEListItem> extends InterfaceElement {
	private Point nextPosition;
	private ObjectLayer<T> items;
	
	public IEList(Point firstPoint) {
		nextPosition = firstPoint;
		items = new ObjectLayer<T>();
	}
	
	@Override
	public void setWorld(World world) {
		super.setWorld(world);
		items.applyToAll(i -> i.setWorld(world));
	}
	
	@Override
	public void mousePressed(int button, Point clickPoint) {
		items.applyToAll(i -> i.mousePressed(button, clickPoint));
	}

	@Override
	public void onClick(int button) {
		// TODO Auto-generated method stub
		
	}
	
	public void add(T go) {
		items.add(go);
		
		go.setPosition(nextPosition);
		go.setWorld(getWorld());
		nextPosition = nextPosition.move(Dir.SOUTH, go.getHeight());
	}
	
	@Override
	protected void draw() {
		items.render();
	}
}
