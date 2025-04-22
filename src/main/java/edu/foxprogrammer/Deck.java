package edu.foxprogrammer;

import edu.foxprogrammer.model.Card;
import edu.foxprogrammer.model.Rank;
import edu.foxprogrammer.model.Suit;

import java.util.*;

public class Deck {
    Scanner scanner = new Scanner(System.in);
    List<Card> cards = new ArrayList<>();
    List<Stack<Card>> tableau = new ArrayList<>();
    Stack<Card> stock = new Stack<>();
    int lastStockCardID = -1;

    public void generateCards() {

        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                if (suit.getSuitIndicator() == 2) {
                    cards.add(new Card(suit, rank, "black"));
                } else cards.add(new Card(suit, rank, "red"));
            }
        }
        shuffleCards(cards);
    }

    public void mapCardsToTableau() {
        for (int i = 0; i < 7; i++) {
            tableau.add(new Stack<>());
        }

        for (int i = 0; i < 7; i++) {
            int cardsInStack = 7 - i;
            for (int j = 0; j < cardsInStack; j++) {
                tableau.get(i).push(cards.removeFirst());
            }
        }
    }


    public void mapCardsToStock() {
        stock.addAll(cards);
    }

    private void shuffleCards(List<Card> cards) {
        Collections.shuffle(cards);
    }

    public void displayDeck() {
        int maxHeight = tableau.stream().mapToInt(Stack::size).max().orElse(0);


        System.out.println("  1\t\t 2\t\t 3\t\t 4\t\t 5\t\t 6\t\t 7");

        for (int row = 0; row < maxHeight; row++) {
            for (int col = 0; col < tableau.size(); col++) {
                Stack<Card> column = tableau.get(col);

                if (row < column.size()) {
                    Card card = column.get(row);
                    if (row == column.size() - 1 || card.isRevealed()) {
                        card.setRevealed(true);
                        System.out.print("[" + formatCard(card) + "]\t");
                    } else {
                        System.out.print("[XX]\t");
                    }
                } else {
                    System.out.print("\t");
                }
            }
            System.out.println();
        }

        displayStock();
    }

    private void displayStock() {
        if (lastStockCardID + 1 < stock.size()) {
            lastStockCardID = lastStockCardID + 1;
            System.out.println("Stos rezerwowy:" + "[" + formatCard(stock.get(lastStockCardID)) + "]");
        } else {
            shuffleCards(stock);
            lastStockCardID = -1;
        }
        checkWhatPlayerWants();
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

    private void checkWhatPlayerWants() {
        String[] userInput = scanner.nextLine().split(" ");
        if(userInput[0].equalsIgnoreCase("sr") && userInput.length != 1){
            moveCardFromStock(userInput);
        } else if (checkCommand(userInput)) {
            moveCardFromTableau(userInput);
        }if (userInput[0].equalsIgnoreCase("sr") && userInput.length == 1){
            displayStock();
        } else {
            System.out.println("Nieznana lub niepoprawna komenda!");
            checkWhatPlayerWants();
        }
    }

    private void moveCardFromTableau(String[] userInput){
        int from = Integer.parseInt(userInput[0]) - 1;
        int to = Integer.parseInt(userInput[1]) - 1;
        Stack<Card> source = tableau.get(from);
        Stack<Card> dest = tableau.get(to);

        Card moving = source.peek();
        Card destCard = dest.peek();

        while (!checkMove(moving, destCard)) {
            System.out.println("Nieprawidłowy ruch!");
            checkWhatPlayerWants();
        }

        dest.push(moving);
        displayDeck();

    }

    private void moveCardFromStock(String[] userInput) {
        int to = Integer.parseInt(userInput[1]) - 1;
        Stack<Card> source = stock;
        Stack<Card> dest = tableau.get(to);
        Card moving = source.get(lastStockCardID);
        Card destCard = dest.peek();

        while (!checkMove(moving, destCard)) {
            System.out.println("Nieprawidłowy ruch!");
            checkWhatPlayerWants();
        }

        dest.push(moving);
        displayDeck();
    }

    private boolean checkMove(Card moving, Card destCard) {
        return !moving.getColor().equals(destCard.getColor()) && moving.getRank().getId() == destCard.getRank().getId() - 1;
    }

    private boolean checkCommand(String[] userInput) {
        try {
            for (int i = 0; i < userInput.length; i ++) {
                int number = Integer.parseInt(userInput[i]);
            }
            return true;
        } catch (NumberFormatException e) {
            return false;
        }

    }


}
