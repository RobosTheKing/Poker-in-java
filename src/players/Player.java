package players;

import java.util.Arrays;
import java.util.stream.Stream;

import cardRelated.Card;
import cardRelated.CardHand5;
import cardRelated.CardHand7;
import cardRelated.EvaluateHand;
import panel.Coordinates;
import panel.CardObj;
import javaIsStupid.*;

public class Player {
    private CardObj[] cardObjs = new CardObj[2];
    private int cardNum = 0;
    private int cash = 1000;
    private int bet = 0;
    private int state = 0;
    private double x, y;
    private boolean player;

    public Player(boolean player) {
        this.player = player;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Coordinates getCoordinates() {
        return new Coordinates(x, y);
    }

    public int getCash() {
        return cash;
    }

    public void addCash(int cash) {
        this.cash += cash; // spd
    }

    public void resetCards() {
        for (int i = 0; i < cardObjs.length; i++) {
            cardObjs[i] = null;
        }
        cardNum = 0;
    }

    public void addCardObj(CardObj card) {
        this.cardObjs[cardNum] = card;
        cardNum++;
    }

    public int getCardNum() {
        return cardNum;
    }

    public CardObj getCardObj(int i) {
        return cardObjs[i];
    }

    public long evalCards(Dealer dealer) {
        return evalCards(dealer.getComunityCards());
    }

    public long evalCards(CardObj[] comunityCards) {
        CardObj[] combinedCards = Stream.concat(Arrays.stream(comunityCards), Arrays.stream(cardObjs)).toArray(CardObj[]::new);
        long cards = 0;
        
        for (CardObj cardObj : combinedCards) {
            cards = setBitTo1(cards, cardObj.getRepresentingCard().getCard());
        }

        return EvaluateHand.evaluate7(cards);
    }

    private long setBitTo1(long value, int index) {
        return value | (1 << index);
    }

    public boolean isPlayer() {
        return player;
    }

    public void setPlayer(boolean player) {
        this.player = player;
    }

    public int state() {
        return state;
    }

    public void nextState() {
        if (state == 3) state = 0;
        else state++;
    }

    public void play(Int cashInPot, Dealer dealer, int smallBlindAmount, Int highestBet) {
        if (player) return;
        if (state == 1) return;
        else if (state == 2) {
            addCash(-smallBlindAmount);
            cashInPot.addToValue(smallBlindAmount);
            bet = smallBlindAmount;
            highestBet.setValue(smallBlindAmount);
            return;
        } else if (state == 3) {
            addCash(-smallBlindAmount * 2);
            cashInPot.addToValue(smallBlindAmount * 2);
            bet = smallBlindAmount * 2;
            highestBet.setValue(smallBlindAmount * 2);
            return;
        } else {
            addCash(-highestBet.value());
            cashInPot.addToValue(highestBet.value() - bet);
            bet += highestBet.value() - bet;
        }
    }
}
