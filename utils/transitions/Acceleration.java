package elixe.utils.transitions;

public class Acceleration implements ITransition {


	float startTime, transitionTime;
	
	
	public Acceleration(float startTime, float transitionTime) {
		super();
		this.startTime = startTime;
		this.transitionTime = transitionTime;
	}

	
	public boolean makeStep(float time) {
		// TODO Auto-generated method stub
		return false;
	}

	
	public void makeTransition() {
		// TODO Auto-generated method stub
		
	}

}
