package edu.foxprogrammer.model;

public enum Suit {
    HEARTS(1), DIAMONDS(1), CLUBS(2), SPADES(2);

    private final int suitIndicator;

    Suit(int suitIndicator){
        this.suitIndicator = suitIndicator;
    }

    public int getSuitIndicator() {
        return suitIndicator;
    }
}
