package edu.foxprogrammer;

public class Card {
    private final Suit suit;
    private final Rank rank;
    private boolean isRevealed;
    private int row;

    public Card(Suit suit, Rank rank, int column, int row){
        this.suit = suit;
        this.rank = rank;
        this.row = row;
    }

    public Suit getSuit(){
        return suit;
    }

    public Rank getRank(){
        return rank;
    }

    public boolean isRevealed() {
        return isRevealed;
    }

    public void setRevealed(boolean revealed) {
        isRevealed = revealed;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }
}