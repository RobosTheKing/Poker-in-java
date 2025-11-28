package players;

import java.util.Arrays;

import cardRelated.Card;
import panel.CardObj;

public class Dealer {
    private CardObj[] comunityCards = new CardObj[5];
    private int cardNum = 0;
    private int cardsShown = 0;


    public CardObj[] getComunityCards() {
        return Arrays.copyOf(comunityCards, comunityCards.length);
    }

    public void addCard(CardObj card) {
        this.comunityCards[cardNum] = card;
        cardNum++;
    }

    public CardObj getCard(int i) {
        return comunityCards[i];
    }

    public void resetComunutyCards() {
        for (int i = 0; i < comunityCards.length; i++) {
            comunityCards[i] = null;
        }
        cardsShown = 0;
        cardNum = 0;
    }

    public int getCardsShown() {
        return cardsShown;
    }

    public void setCardsShown(int cardsShown) {
        this.cardsShown = cardsShown;
    }
}
