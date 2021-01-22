package elixe.events;

public class OnGetCollisionBorderEvent {
private float borderSize;

public OnGetCollisionBorderEvent(float borderSize) {
	super();
	this.borderSize = borderSize;
}

public float getBorderSize() {
	return borderSize;
}

public void setBorderSize(float borderSize) {
	this.borderSize = borderSize;
}



}
