package edu.foxprogrammer;

import edu.foxprogrammer.model.Card;
import edu.foxprogrammer.model.Rank;
import edu.foxprogrammer.model.Suit;

import java.util.*;

public class Deck {
    Scanner scanner = new Scanner(System.in);
    List<Card> cards = new ArrayList<>();
    List<Stack<Card>> tableau = new ArrayList<>();
    List<Stack<Card>> foundations = new ArrayList<>();
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

    public void mapStacks() {
        for (int i = 0; i < 7; i++) {
            tableau.add(new Stack<>());
        }

        for (int i = 0; i < 4; i++) {
            foundations.add(new Stack<>());
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

    public void displayDeck() { // TODO wyswietlanie psuje sie po przeniesieniu paru innych kart do kolumny
        int maxHeight = tableau.stream().mapToInt(Stack::size).max().orElse(0);


        for (int i = 0; i < 4; ) {
            if (foundations.get(i).isEmpty()) {
                i++;
                System.out.print("\t [SK " + i + "]");
            } else {
                System.out.print("\t [SK " + formatCard(foundations.get(i).peek()) + "]");
                i++;
            }
        }
        System.out.println();
        System.out.println("  1\t\t 2\t\t 3\t\t 4\t\t 5\t\t 6\t\t 7");

        for (int row = 0; row < maxHeight; row++) {
            for (int col = 0; col < tableau.size(); col++) {
                if (col == 0) {
                    System.out.print(row + 1);
                }
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

    public void displayStock() {
        if (lastStockCardID + 1 < stock.size()) {
            lastStockCardID = lastStockCardID + 1;
            System.out.println("Stos rezerwowy:" + "[" + formatCard(stock.get(lastStockCardID)) + "]");
        } else {
            shuffleCards(stock);
            lastStockCardID = 0;
            System.out.println("Stos rezerwowy:" + "[" + formatCard(stock.get(lastStockCardID)) + "]");
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

    private void checkWhatPlayerWants() { //TODO obsłuż wyjątki komend move
        String[] userInput = scanner.nextLine().split(" ");
        if (userInput.length == 3 && checkCommand(userInput)) {
            moveStackOfCards(userInput);
        } else if (userInput.length == 2 && userInput[0].equalsIgnoreCase("sr")) {
            moveCardFromStockToTableau(userInput);
        } else if (userInput.length == 2 && checkCommand(userInput)) {
            moveCardFromTableau(userInput);
        } else if (userInput.length == 1 && userInput[0].equalsIgnoreCase("sr")) {
            displayDeck();
        } else if (userInput.length == 3 && userInput[1].equalsIgnoreCase("sk") && !userInput[0].equalsIgnoreCase("sr")) {
            moveCardFromTableauToFoundation(userInput);
        } else if (userInput.length == 3 && userInput[0].equalsIgnoreCase("sr") && userInput[1].equalsIgnoreCase("sk")) {
            moveCardFromStockToFoundation(userInput);
        } else {
            System.out.println("Nieznana lub niepoprawna komenda!");
            checkWhatPlayerWants();
        }
    }

    private void moveCardFromTableau(String[] userInput) {
        int from = Integer.parseInt(userInput[0]) - 1;
        int to = Integer.parseInt(userInput[1]) - 1;
        Stack<Card> source = tableau.get(from);
        Stack<Card> dest = tableau.get(to);
        Card destCard = null;
        Card moving = source.peek();

        if(!dest.isEmpty()) {
            destCard = dest.peek();
        }

        while (!checkMove(moving, destCard)) {
            System.out.println("Nieprawidłowy ruch!");
            System.out.println("todo");
            checkWhatPlayerWants();
        }

        source.remove(moving);
        dest.push(moving);
        displayDeck();

    }

    private void moveCardFromStockToTableau(String[] userInput) {
        int to = Integer.parseInt(userInput[1]) - 1;
        Stack<Card> source = stock;
        Stack<Card> dest = tableau.get(to);
        Card moving = source.get(lastStockCardID);
        Card destCard = dest.peek();

        while (!checkMove(moving, destCard)) {
            System.out.println("Nieprawidłowy ruch!");
            System.out.println("some");
            checkWhatPlayerWants();
        }

        source.remove(moving);
        dest.push(moving);
        displayDeck();
    }

    private void moveCardFromStockToFoundation(String[] userInput) {
        int to = Integer.parseInt(userInput[2]) - 1;
        Stack<Card> source = stock;
        Stack<Card> dest = tableau.get(to);

        Card moving = source.get(lastStockCardID);
        Card destCard = null;

        if(dest.isEmpty()) {
            destCard = dest.peek();
        }

        while (!checkMoveToEndStock(moving, destCard)) {
            System.out.println("Nieprawidłowy ruch!");
            System.out.println("sout");
            checkWhatPlayerWants();
        }

        source.remove(moving);
        dest.push(moving);
        displayDeck();
    }

    private void moveCardFromTableauToFoundation(String[] userInput) {
        int from = Integer.parseInt(userInput[0]) - 1;
        int to = Integer.parseInt(userInput[2]) - 1;

        Stack<Card> source = tableau.get(from);
        Stack<Card> dest = tableau.get(to);

        Card moving = source.peek();
        Card destCard = null;

        if(!dest.isEmpty()){
            destCard = dest.peek();
        }

        while (!checkMoveToEndStock(moving, destCard)) {
            System.out.println("Nieprawidłowy ruch!");
            System.out.println("something");
            checkWhatPlayerWants();
        }

        source.remove(moving);
        dest.push(moving);
        displayDeck();
    }

    private void moveStackOfCards(String[] userInput) {
        int from = Integer.parseInt(userInput[0]) - 1;
        int fromRow = Integer.parseInt(userInput[1]) - 1;
        int to = Integer.parseInt(userInput[2]) - 1;
        Stack<Card> source = tableau.get(from);
        Stack<Card> dest = tableau.get(to);
        Card destCard = null;
        Card moving = source.get(fromRow);


        if(!dest.isEmpty()){
            destCard = dest.peek();
        }

        while (!checkMove(moving, destCard)) {
            System.out.println("Nieprawidłowy ruch!");
            System.out.println("method");
            checkWhatPlayerWants();
        }

        for (int i = fromRow; i < source.size(); ) {
            Card card = source.remove(fromRow);
            dest.push(card);
            System.out.println(source.size());
        }

        displayDeck();
    }

    private boolean checkMove(Card moving, Card destCard) {
        if (destCard == null) {
            return moving.getRank().equals(Rank.KING);
        } else {
            return !moving.getColor().equals(destCard.getColor()) && moving.getRank().getId() == destCard.getRank().getId() - 1;
        }
    }

    private boolean checkMoveToEndStock(Card moving, Card destCard) {
        if (destCard == null && moving.getRank().equals(Rank.ACE)) {
            return true;
        } else if (destCard != null) {
            return moving.getSuit().equals(destCard.getSuit()) && moving.getRank().getId() == destCard.getRank().getId() + 1;
        } else return false;
    }

    private boolean checkCommand(String[] userInput) {
        try {
            for (String s : userInput) {
                Integer.parseInt(s);
            }
            return true;
        } catch (NumberFormatException e) {
            return false;
        }

    }
}