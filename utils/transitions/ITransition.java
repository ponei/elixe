package elixe.utils.transitions;

public interface ITransition {
	public boolean makeStep(float time);

	public void makeTransition();
}
