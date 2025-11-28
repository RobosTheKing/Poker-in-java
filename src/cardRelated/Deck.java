package cardRelated;

public class Deck {
    private Card[] cardDeck;
    private int drawnCards = 0;

    public Deck() {
        Card[] cards = new Card[52];
        for (int i = 0; i < 13; i++) {
            for (int j = 0; j < 4; j++) {
                cards[j * 13 + i] = new Card(i, j);
            }
        }
        cardDeck = cards;
    }

    public void shuffle() {
        for (int i = 0; i < cardDeck.length; i++) {
            int index = (int)(Math.random() * cardDeck.length - i) + i;
            swap(index, i);
        }
        drawnCards = 0;
    }

    public Card draw() {
        return cardDeck[drawnCards++];
    }

    private void swap(int i, int j) {
        Card card = cardDeck[i];
        cardDeck[i] = cardDeck[j];
        cardDeck[j] = card;
    }
}
