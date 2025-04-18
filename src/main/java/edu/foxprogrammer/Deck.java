package edu.foxprogrammer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Deck {
    List<Card> cards = new ArrayList<>();
    Scanner scanner = new  Scanner(System.in);
    List<Card> deck = new ArrayList<>();

    public List<Card> generateCards() {
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                cards.add(new Card(suit, rank, 0, 0));
            }
        }

        Collections.shuffle(cards);

        return cards;
    }

    public void mapDeckToList() {
        int index = 0;
        List<Card> toRemove = new ArrayList<>();

        for (int row = 0; row < 7; row++) {
            for (int col = 0; col < 7; col++) {
                    Card card = cards.get(index);
                    deck.add(card);
                    toRemove.add(card);
                    deck.get(index).setRevealed(false);
                    index++;
            }
        }
        cards.removeAll(toRemove);
    }

    public void displayDeck() {
        int index = 0;

        for (int row = 0; row < 7; row++) {
            for (int col = 0; col < 7; col++) {
                if (col > row) {
                    System.out.print("\t");
                } else if (col == row || deck.get(index).isRevealed()) {
                    Card card = deck.get(index);
                    card.setRow(row+1);
                    card.setRevealed(true);
                    System.out.print(index+1 + "[" +formatCard(card) + "]" + "\t");
                    index++;
                } else {
                    Card card = deck.get(index);
                    card.setRow(row);
                    System.out.print(index + 1 + "[XX]\t");
                    index++;
                }
            }
            System.out.println();
        }
        checkWhatPlayerWant();
    }



    private String formatCard(Card card) {
        String rank = switch (card.getRank()) {
            case ACE -> "A";
            case KING -> "K";
            case QUEEN -> "Q";
            case JACK -> "J";
            default -> String.valueOf(card.getRank().getId());
        };

        String suit = switch (card.getSuit()) {
            case HEARTS -> "♥";
            case DIAMONDS -> "♦";
            case CLUBS -> "♣";
            case SPADES -> "♠";
        };

        return rank + suit;
    }

    private void checkWhatPlayerWant() {
        System.out.println("Podaj numer karty którą chcesz przenieść");
        int cardNum = scanner.nextInt();
        while (!deck.get(--cardNum).isRevealed()) {
            System.out.println("Nie możesz wybrać nie odkrytej karty!");
            System.out.println("Podaj numer karty którą chcesz przenieść.");
            cardNum = scanner.nextInt();
        }
        System.out.println("Podaj numer karty nad którą chcesz przenieść wybraną kartę " + ".");

        int placeNum = scanner.nextInt();
        while (!deck.get(--placeNum).isRevealed()) {
            System.out.println("Nie możesz wybrać nie odkrytej karty!");
            System.out.println("Podaj numer karty na którą chcesz przenieść wybraną karte.");
            placeNum = scanner.nextInt();
        }

        moveCard(cardNum, placeNum);

    }

    private void moveCard(int cardNum, int placeNum) {
        Card movedCard = deck.remove(cardNum);
        deck.add(placeNum, movedCard);

        displayDeck();
    }

}
