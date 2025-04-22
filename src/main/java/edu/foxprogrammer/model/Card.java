package edu.foxprogrammer.model;

public class Card {
    private final Suit suit;
    private final Rank rank;
    private boolean isRevealed;
    private String color;

    public Card(Suit suit, Rank rank, String color) {
        this.suit = suit;
        this.rank = rank;
        this.color = color;
    }

    public Suit getSuit() {
        return suit;
    }

    public Rank getRank() {
        return rank;
    }

    public boolean isRevealed() {
        return isRevealed;
    }

    public void setRevealed(boolean revealed) {
        isRevealed = revealed;
    }

    public String getColor() {
        return color;
    }
}