package cardRelated;

import panel.CardObj;

public class CardHand7 {
    private Card[] cardHand = new Card[7];
    private int handValue = 0;
    private int cardNum = 0;

    public CardHand7(CardObj[] cards) {
        for (int i = 0; i < cards.length; i++) {
            this.cardHand[i] = cards[i].getRepresentingCard();
        }
    }

    public void addCard(Card card) {
        cardHand[cardNum] = card;
        cardNum++;
    }

    public void resetCards() {
        for (int i = 0; i < cardHand.length; i++) {
            cardHand[i] = null;
        }
        cardNum = 0;
    }

    public int getCardNum() {
        return cardNum;
    }

    public int getHandValue() {
        return handValue;
    }

    public void setHandValue(int handValue) {
        this.handValue = handValue;
    }

    public void sortByValue() {
        for (int i = 0; i < cardHand.length; i++) {
            for (int j = 0; j < cardHand.length; j++) {
                if (cardHand[i].getNumber() > cardHand[j].getNumber()) {
                    cardHand = swap(cardHand, i, j);
                }
            }
        }
    }

    private Card[] swap(Card[] cards, int i, int j) {
        Card card = cards[i];
        cards[i] = cards[j];
        cards[j] = card;
        return cards;
    }

    public Card getCard(int index) {
        return cardHand[index];
    }
}
