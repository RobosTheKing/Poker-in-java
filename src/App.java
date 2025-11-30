import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;


import javax.swing.Timer;
import javax.swing.JButton;
import javax.swing.JFrame;

import panel.*;
import cardRelated.*;
import players.Dealer;
import players.botStuff.Player;
import javaIsStupid.*;

public class App {
    // suit numbers
    // 0 = clubs
    // 1 = hearts
    // 2 = spades
    // 3 = diamonds

    // value numbers:
    // 0 = two      0000
    // 1 = three    0001
    // 2 = four     0010
    // 3 = five     0011
    // 4 = six      0100
    // 5 = seven    0101
    // 6 = eight    0110
    // 7 = nine     0111
    // 8 = ten      1000
    // 9 = jack     1001
    // 10 = queen   1010
    // 11 = king    1011
    // 12 = ace     1100

    // 00000000001000000000100110011011

    // 000000000000000000000100000000000000000000000000000001000000000


    static boolean isDealing = false;

    public static void main(String[] args) throws Exception {
        Deck deck = new Deck();
        deck.shuffle();
        Player[] players = {new Player(false), new Player(false), new Player(true), new Player(false), new Player(false)};
        Dealer dealer = new Dealer();

        JFrame frame = new JFrame("Poker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setLayout(new BorderLayout());

        CardTable table = new CardTable();

        frame.add(table, BorderLayout.CENTER);

        frame.setSize(800, 450);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setMinimumSize(new Dimension(872, 500));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        Int cashInPot = new Int(0);
        Int highestBet = new Int(0);
        Text potText = new Text("Pot: " + cashInPot.value() + "$" , frame.getWidth() / 2, 30);
        Text rankingText = new Text("", 100, 100, false);
        Text cashText = new Text("Cash in bank: " + players[2].getCash(), frame.getWidth() / 2, frame.getHeight() -500);
        Text highestBetText = new Text("Highest bet: " + highestBet.value(), frame.getWidth() / 2, 80);
        table.add(potText);
        table.add(rankingText);
        table.add(cashText);
        table.add(highestBetText);

        JButton giveCardsAgain = new JButton("Rozdej");
        giveCardsAgain.setBounds(frame.getWidth() - 1000, 10, 400, 100);
        giveCardsAgain.setFont(new Font("Arial", Font.PLAIN, 48));
        giveCardsAgain.addActionListener(l -> {
            if (isDealing) return;
            isDealing = true;
            deck.shuffle();
            table.resetCards();
            for (int i = 0; i < players.length; i++) {
                players[i].resetCards();
            }
            dealer.resetComunutyCards();
            giveAllPlayersCards(deck, frame, table, players, () -> {
                putComunityCards(deck, frame, table, dealer, 400, () -> {isDealing = false;});
            });
        });

        JButton evalCards = new JButton("Vyhodnoť");
        evalCards.setBounds(600, 10, 400, 100);
        evalCards.setFont(new Font("Arial", Font.PLAIN, 48));
        evalCards.addActionListener(l -> {
            if (isDealing) return;
            StringBuilder textForEval = new StringBuilder(500);
            textForEval.append("Ranking:\n");
            long[] evalPlayers = {players[0].evalCards(dealer.getComunityCards()), players[1].evalCards(dealer.getComunityCards()), players[2].evalCards(dealer.getComunityCards()), players[3].evalCards(dealer.getComunityCards()), players[4].evalCards(dealer.getComunityCards())};
            int[] sortedIndexes = sortByIndex(evalPlayers);
            for (int i = 0; i < players.length; i++) {
                textForEval.append("   " + (i + 1) + ". Player " + (sortedIndexes[i] + 1) + " with score: " + evalPlayers[sortedIndexes[i]] + "\n");
            }

            String rankingString = textForEval.toString();
            rankingText.setText(rankingString);
            table.repaint();
        });

        table.add(giveCardsAgain);
        table.add(evalCards);

        // nastavit pozice hráčů
        players[0].setX(frame.getWidth() - 100);
        players[0].setY(frame.getHeight() / 4);

        players[1].setX(frame.getWidth() - 100);
        players[1].setY(frame.getHeight() / 4 * 3);

        players[2].setX(frame.getWidth() / 2); 
        players[2].setY(frame.getHeight() - 100);

        players[3].setX(100);
        players[3].setY(frame.getHeight() / 4 * 3);

        players[4].setX(100);
        players[4].setY(frame.getHeight() / 4);

        players[0].nextState();
        players[1].nextState();
        players[1].nextState();
        players[2].nextState();
        players[2].nextState();
        players[2].nextState();

        /*
        giveAllPlayersCards(deck, frame, table, players, () -> {
            putComunityCards(deck, frame, table, dealer, 400);
        });

        Thread.sleep(10000);

        players[1].play(cashInPot, dealer, 5, highestBet);
        pot.setText("Pot: " + cashInPot.getValue() + "$");
        table.repaint();
        */
    }

    static void giveAllPlayersCards(Deck deck, JFrame frame, CardTable table, Player[] players, Runnable onFinish) {
        final int delay = 400;
        final int[] step = {0};

        Timer timer = new Timer(delay, null);
        timer.addActionListener(e -> {
            int playerNum = step[0] % players.length;
            players[playerNum].addCardObj(new CardObj(deck.draw(), frame.getWidth() / 2, 0, 300, 400, 0));
            table.add(players[playerNum].getCardObj(players[playerNum].getCardNum() - 1));
            int cardNum = players[playerNum].getCardNum();

            if (cardNum == 1) {
                if (playerNum < 2) {
                    table.moveObjTo(players[playerNum].getCoordinates(), players[playerNum].getCardObj(0), delay);
                    table.rotateObj(90, players[playerNum].getCardObj(0), delay, 0, true);
                    // testing
                    table.flipCard(players[playerNum].getCardObj(0), delay, 0);
                } else if (playerNum == 2) {
                    table.moveObjTo(players[playerNum].getCoordinates(), players[playerNum].getCardObj(0), delay);
                    players[playerNum].getCardObj(0).setHumans(true);
                    players[playerNum].getCardObj(0).setPickable(true);
                    table.flipCard(players[playerNum].getCardObj(0), delay, 0);
                } else {
                    table.moveObjTo(players[playerNum].getCoordinates(), players[playerNum].getCardObj(0), delay);
                    table.rotateObj(-90, players[playerNum].getCardObj(0), delay, 0, true);
                    // testing
                    table.flipCard(players[playerNum].getCardObj(0), delay, 0);
                }
            }

            if (cardNum == 2) { //STA4ILO
                if (playerNum < 2) {
                    table.moveObjTo(players[playerNum].getX(), players[playerNum].getY() - 100, players[playerNum].getCardObj(1), delay);
                    table.rotateObj(90 + 5, players[playerNum].getCardObj(1), delay, 0, true);
                    table.moveObjTo(players[playerNum].getX(), players[playerNum].getY() + 100, players[playerNum].getCardObj(0), delay);
                    table.rotateObj(90 - 5, players[playerNum].getCardObj(0), delay, 0, true);
                    // testing
                    table.flipCard(players[playerNum].getCardObj(1), delay, 0);
                } else if (playerNum == 2) {
                    table.moveObjTo(players[2].getX() + 100, players[2].getY(), players[2].getCardObj(1), delay);
                    table.rotateObj(5, players[2].getCardObj(1), delay, 0, true);
                    table.moveObjTo(players[2].getX() - 100, players[2].getY(), players[2].getCardObj(0), delay);
                    table.rotateObj(-5, players[2].getCardObj(0), delay, 0, true);
                    players[2].getCardObj(1).setHumans(true);
                    players[2].getCardObj(1).setPickable(true);
                    table.flipCard(players[2].getCardObj(1), delay, 0);
                    players[2].getCardObj(0).setZPosition(0);
                    players[2].getCardObj(1).setZPosition(1);
                } else {
                    table.moveObjTo(players[playerNum].getX(), players[playerNum].getY() + 100, players[playerNum].getCardObj(1), delay);
                    table.rotateObj(-(90 - 5), players[playerNum].getCardObj(1), delay, 0, true);
                    table.moveObjTo(players[playerNum].getX(), players[playerNum].getY() - 100, players[playerNum].getCardObj(0), delay);
                    table.rotateObj(-(90 + 5), players[playerNum].getCardObj(0), delay, 0, true);
                    // testing
                    table.flipCard(players[playerNum].getCardObj(1), delay, 0);
                }
            }

            step[0]++;
            if (step[0] >= players.length * 2) {
                ((Timer)e.getSource()).stop();
                if (onFinish != null) onFinish.run();
            }
        });

        timer.setInitialDelay(0);
        timer.start();
    }

    static void putComunityCards(Deck deck, JFrame frame, CardTable table, Dealer dealer, int initialDelay, Runnable onFinish) {
        final int delay = 400;
        final int[] step = {0};

        Timer timer = new Timer(delay, null);
        timer.addActionListener(e -> {
            dealer.addCard(new CardObj(deck.draw(), frame.getWidth() / 2, 0, 300, 400, 0));
            table.add(dealer.getCard(step[0]));
            int centerX = frame.getWidth() / 2;
            int centerY = frame.getHeight() / 2;
            table.moveObjTo(centerX + 400 * (step[0] - 2), centerY, dealer.getCard(step[0]), delay);
            // testing
            table.flipCard(dealer.getCard(step[0]), delay, 0);

            step[0]++;
            if (step[0] > 4) {
                ((Timer)e.getSource()).stop();
                if (onFinish != null) onFinish.run();
            }
        });

        timer.setInitialDelay(initialDelay);
        timer.start();
    }

    static int[] sortByIndex(long[] evalPlayers) {
        int[] indexes = new int[evalPlayers.length];
        for (int i = 0; i < evalPlayers.length; i++) {
            indexes[i] = i;
        }

        for (int i = 0; i < indexes.length; i++) {
            for (int j = 0; j < indexes.length - 1 - i; j++) {
                if (evalPlayers[indexes[j]] < evalPlayers[indexes[j + 1]]) {
                    indexes = swap(indexes, j, j + 1);
                }
            }
        }
        return indexes;
    }

    static int[] swap(int[] arr, int i, int j) {
        int card = arr[i];
        arr[i] = arr[j];
        arr[j] = card;
        return arr;
    }

    static long[] swap(long[] arr, int i, int j) {
        long card = arr[i];
        arr[i] = arr[j];
        arr[j] = card;
        return arr;
    }
}