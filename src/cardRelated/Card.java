package cardRelated;

public class Card {
    final private int card;
    private String cardName = "";
 
    public Card(int number, int suit) {
        this.card = suit * 13 + number;
        switch (number) {
            case 0:
                cardName = cardName + "two";
                break;
            case 1:
                cardName = cardName + "three";
                break;
            case 2:
                cardName = cardName + "four";
                break;
            case 3:
                cardName = cardName + "five";
                break;
            case 4:
                cardName = cardName + "six";
                break;
            case 5:
                cardName = cardName + "seven"; // SIIIXXXX SEEEEEVEEEN 6767676767
                break;
            case 6:
                cardName = cardName + "eight";
                break;
            case 7:
                cardName = cardName + "nine";
                break;
            case 8:
                cardName = cardName + "ten";
                break;
            case 9:
                cardName = cardName + "jack";
                break;
            case 10:
                cardName = cardName + "queen";
                break;
            case 11:
                cardName = cardName + "king";
                break;
            case 12:
                cardName = cardName + "ace";
                break;
        
            default:
                break;
        }
        switch (suit) {
            case 0:
                cardName = cardName + "_of_clubs";
                break;
            case 1:
                cardName = cardName + "_of_hearts";
                break;
            case 2:
                cardName = cardName + "_of_spades";
                break;
            case 3:
                cardName = cardName + "_of_diamonds";
                break;
        
            default:
                break;
        }
    }

    public int getSuit() {
        return (card - (card % 13)) / 13;
    }

    public int getNumber() {
        return card % 13;
    }

    public int getCard() {
        return card;
    }

    public String getCardName() {
        return cardName;
    }
}
