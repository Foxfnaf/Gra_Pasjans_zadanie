package edu.foxprogrammer;

import edu.foxprogrammer.model.Card;
import edu.foxprogrammer.model.Rank;
import edu.foxprogrammer.model.Suit;

import java.util.*;

public class Deck {
    Scanner scanner = new Scanner(System.in);
    List<Card> cards = new  ArrayList<>();
    List<Stack<Card>> tableau = new ArrayList<>();
    Stack<Card> stock = new Stack<>();

    public void generateCards() {
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                cards.add(new Card(suit, rank));
            }
        }
        shuffleCards();
    }

    public void mapCardsToTableau() {
        for (int i = 0; i < 7; i++) {
            tableau.add(new Stack<>());
        }

        for(int i = 0; i < 7; i++) {
            for(int j = 0; j<=i; j++) {
                tableau.get(i).push(cards.remove(0));
            }
        }
    }

    private void mapCardsToStock() {
        stock.addAll(cards);
    }

    private void shuffleCards() {
        Collections.shuffle(cards);
    }

    public void displayDeck() {

        System.out.println("1 \t\t 2 \t\t 3 \t\t 4 \t\t 5 \t\t 6 \t\t 7 \t\t");

        for (int col = 0; col < tableau.size(); col++) {
            for (int row = 0; row < tableau.get(col).size(); row++) {
                Card card = tableau.get(col).get(row);
                if (col == tableau.get(row).size() - 1 || card.isRevealed()) {
                    card.setRevealed(true);
                    System.out.print("[" + formatCard(card) + "]\t");
                } else {
                    System.out.print("[XX]\t");
                }
            }
            System.out.println();
        }

        moveCard();
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

    private void moveCard() {
        String[] userInput = scanner.nextLine().split(" ");
        int from = Integer.parseInt(userInput[0]) - 1;
        int to = Integer.parseInt(userInput[1]) - 1;

        if (from < 0 || from >= tableau.size() || to < 0 || to >= tableau.size()) {
            System.out.println("Nieprawidłowy numer stosu.");
            return;
        }

        Stack<Card> source = tableau.get(from);
        Stack<Card> dest = tableau.get(to);

        if (source.isEmpty()) {
            System.out.println("Stos źródłowy jest pusty.");
            return;
        }

        Card moving = source.pop();
        System.out.println("Przenoszona karta: " + moving.getRank() + " " + moving.getSuit());

        dest.push(moving);
        Stack<Card> S = tableau.getFirst();
        System.out.println(S);
        displayDeck();
    }


}
