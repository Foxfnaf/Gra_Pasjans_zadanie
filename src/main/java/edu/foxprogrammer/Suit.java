package edu.foxprogrammer;

public enum Suit {
    HEARTS(1), DIAMONDS(2), CLUBS(3), SPADES(4);

    private final int suitIndicator;

    Suit(int suitIndicator){
        this.suitIndicator = suitIndicator;
    }
}
