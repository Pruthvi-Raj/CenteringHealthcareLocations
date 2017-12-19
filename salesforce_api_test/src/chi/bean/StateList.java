package chi.bean;

import java.util.ArrayList;

public class StateList {
	
	ArrayList<State> statesList;
	
	
	public StateList(){
		this.statesList=new ArrayList<State>();
	}
	
	
	public ArrayList<State> getStatesList() {
		return statesList;
	}

	public void setStatesList(ArrayList<State> statesList) {
		this.statesList = statesList;
	}
	
	public State addState(){
		State state = new State();
		statesList.add(state);
		return state;
	}
}
