package com.mygdx.game;

import java.util.ArrayList;
import java.util.List;

public class ButtonList {
    List<BigButton> buttonList;
    int numberButton = 0;

    ButtonList(BigButton... args) {
        buttonList = new ArrayList<>();
        for (BigButton button : args) {
            add(button);
        }
    }

    public void add(BigButton button) {
        buttonList.add(button);
        numberButton++;
    }

    public void add(BigButton... args) {
        for (BigButton button : args) {
            buttonList.add(button);
        }
    }

    public boolean isIn(BigButton button) {
        for (BigButton comparator : buttonList) {
            if (comparator.getId().equals(button.getID())) {
                return true;
            }
        }
        return false;
    }

    public BigButton getButton(String id) {
        for (BigButton comparator : buttonList) {
            if (comparator.getId().equals(id)) {
                return comparator;
            }
        }
        return null;
    }

    public void show() {
        for (BigButton button : buttonList) {
            System.out.println(button.getID());
        }
    }

}
