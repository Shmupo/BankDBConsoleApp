package com.cogent.app_state;

import com.cogent.Bank;

public class StateMachine {
    public State currentState;

    public StateMachine() {
        currentState = new MainMenuState();
    }

    public void setState(State state) {
        Bank.logEvent("CHANGE STATE : " + state);
        currentState = state;
    }

    public void update() {
        if (currentState != null) {
            currentState.execute();
        }
    }
}
