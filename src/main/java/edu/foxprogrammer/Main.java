package edu.foxprogrammer;

public class Main {
    public static void main(String[] args) {
        Deck deck = new Deck();

        deck.generateCards();

        deck.mapDeckToList();

        deck.displayDeck();
    }
}