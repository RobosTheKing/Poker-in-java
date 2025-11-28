package cardRelated;

public class Hands {
    public boolean isRoyalFlush(CardHand cardHand) {
        cardHand.sortByValue(); 
        return isStraightFlush(cardHand) &&
               cardHand.getCard(0).getNumber() == 12 ||
               cardHand.getCard(4).getNumber() != 0;
    }

    public boolean isFlush(CardHand cardHand) {
        return cardHand.getCard(0).getSuit() == cardHand.getCard(1).getSuit() &&
               cardHand.getCard(1).getSuit() == cardHand.getCard(2).getSuit() &&
               cardHand.getCard(2).getSuit() == cardHand.getCard(3).getSuit() &&
               cardHand.getCard(3).getSuit() == cardHand.getCard(4).getSuit();
    }

    public boolean isStraight(CardHand cardHand) {
        return (cardHand.getCard(0).getNumber() == cardHand.getCard(1).getNumber() + 1 &&
                cardHand.getCard(1).getNumber() == cardHand.getCard(2).getNumber() + 1 &&
                cardHand.getCard(2).getNumber() == cardHand.getCard(3).getNumber() + 1 &&
                cardHand.getCard(3).getNumber() == cardHand.getCard(4).getNumber() + 1) ||
               (cardHand.getCard(0).getNumber() == 12 &&
                cardHand.getCard(1).getNumber() == 3 &&
                cardHand.getCard(2).getNumber() == 2 &&
                cardHand.getCard(3).getNumber() == 1 &&
                cardHand.getCard(4).getNumber() == 0);
    }

    public boolean isStraightFlush(CardHand cardHand) {
        return isFlush(cardHand) && isStraight(cardHand);
    }

    public boolean isFour(CardHand cardHand) {
        return (cardHand.getCard(0).getNumber() == cardHand.getCard(1).getNumber() &&
                cardHand.getCard(1).getNumber() == cardHand.getCard(2).getNumber() &&
                cardHand.getCard(2).getNumber() == cardHand.getCard(3).getNumber()) ||
               (cardHand.getCard(1).getNumber() == cardHand.getCard(2).getNumber() &&
                cardHand.getCard(2).getNumber() == cardHand.getCard(3).getNumber() &&
                cardHand.getCard(3).getNumber() == cardHand.getCard(4).getNumber());
    }

    public boolean isThree(CardHand cardHand) {
        return (cardHand.getCard(0).getNumber() == cardHand.getCard(1).getNumber() &&
                cardHand.getCard(1).getNumber() == cardHand.getCard(2).getNumber()) ||
               (cardHand.getCard(1).getNumber() == cardHand.getCard(2).getNumber() &&
                cardHand.getCard(2).getNumber() == cardHand.getCard(3).getNumber()) ||
               (cardHand.getCard(2).getNumber() == cardHand.getCard(3).getNumber() &&
                cardHand.getCard(3).getNumber() == cardHand.getCard(4).getNumber());
    }

    public boolean isTwoPair(CardHand cardHand) {
        return (cardHand.getCard(0).getNumber() == cardHand.getCard(1).getNumber() &&
                cardHand.getCard(2).getNumber() == cardHand.getCard(3).getNumber()) ||
               (cardHand.getCard(0).getNumber() == cardHand.getCard(1).getNumber() &&
                cardHand.getCard(3).getNumber() == cardHand.getCard(4).getNumber()) ||
               (cardHand.getCard(1).getNumber() == cardHand.getCard(2).getNumber() &&
                cardHand.getCard(3).getNumber() == cardHand.getCard(4).getNumber());
    }

    public boolean isPair(CardHand cardHand) {
        return cardHand.getCard(0).getNumber() == cardHand.getCard(1).getNumber() ||
               cardHand.getCard(1).getNumber() == cardHand.getCard(2).getNumber() ||
               cardHand.getCard(2).getNumber() == cardHand.getCard(3).getNumber() ||
               cardHand.getCard(3).getNumber() == cardHand.getCard(4).getNumber();
    }

    public boolean isFullHouse(CardHand cardHand) {
        return (cardHand.getCard(0).getNumber() == cardHand.getCard(1).getNumber() &&
                (cardHand.getCard(2).getNumber() == cardHand.getCard(3).getNumber() &&
                 cardHand.getCard(3).getNumber() == cardHand.getCard(4).getNumber())) ||
               (cardHand.getCard(3).getNumber() == cardHand.getCard(4).getNumber() &&
                (cardHand.getCard(0).getNumber() == cardHand.getCard(1).getNumber() &&
                 cardHand.getCard(1).getNumber() == cardHand.getCard(2).getNumber()));
    }

    public int evaluateHand(CardHand cardHand) {
        cardHand.sortByValue();
        if (isRoyalFlush(cardHand)) return 10000000;
        if (isStraightFlush(cardHand)) return 9999999;
        if (isFour(cardHand)) return 9999998;
        if (isFullHouse(cardHand)) {
            if (cardHand.getCard(1).getNumber() == cardHand.getCard(2).getNumber()) {
                return 9000000 + cardHand.getCard(0).getNumber() * 100 + cardHand.getCard(4).getNumber();
            } else {
                return 9000000 + cardHand.getCard(4).getNumber() * 100 + cardHand.getCard(0).getNumber();
            }
        }
        if (isFlush(cardHand)) {
            return 8000000 + cardHand.getCard(0).getNumber();
        }
        if (isStraight(cardHand)) {
            return 7000000 + cardHand.getCard(0).getNumber();
        }

        if (isThree(cardHand)) {
            if (cardHand.getCard(0).getNumber() == cardHand.getCard(2).getNumber()) {
                return 6000000 + cardHand.getCard(0).getNumber() * 100 + cardHand.getCard(3).getNumber();
            } else if (cardHand.getCard(1).getNumber() == cardHand.getCard(3).getNumber()) {
                return 6000000 + cardHand.getCard(1).getNumber() * 100 + cardHand.getCard(0).getNumber();
            } else {
                return 6000000 + cardHand.getCard(2).getNumber() * 100 + cardHand.getCard(0).getNumber();
            }
        }
        if (isTwoPair(cardHand)) {
            if (cardHand.getCard(0).getNumber() == cardHand.getCard(1).getNumber() && cardHand.getCard(2).getNumber() == cardHand.getCard(3).getNumber()) {
               return 5000000 + cardHand.getCard(0).getNumber() * 10000 + cardHand.getCard(2).getNumber() * 100 + cardHand.getCard(5).getNumber();
            } else if (cardHand.getCard(0).getNumber() == cardHand.getCard(1).getNumber() && cardHand.getCard(3).getNumber() == cardHand.getCard(4).getNumber()) {
                return 5000000 + cardHand.getCard(0).getNumber() * 10000 + cardHand.getCard(3).getNumber() * 100 + cardHand.getCard(2).getNumber();
            } else {
                return 5000000 + cardHand.getCard(1).getNumber() * 10000 + cardHand.getCard(3).getNumber() * 100 + cardHand.getCard(0).getNumber();
            }
        }
        if (isPair(cardHand)) {
            if (cardHand.getCard(0).getNumber() == cardHand.getCard(1).getNumber()) {
                return 4000000 + cardHand.getCard(0).getNumber() * 10000 + cardHand.getCard(2).getNumber() * 100 + cardHand.getCard(3).getNumber();
            } else if (cardHand.getCard(1).getNumber() == cardHand.getCard(2).getNumber()) {
                return 4000000 + cardHand.getCard(1).getNumber() * 10000 + cardHand.getCard(0).getNumber() * 100 + cardHand.getCard(3).getNumber();
            } else if (cardHand.getCard(2).getNumber() == cardHand.getCard(3).getNumber()) {
                return 4000000 + cardHand.getCard(2).getNumber() * 10000 + cardHand.getCard(0).getNumber() * 100 + cardHand.getCard(1).getNumber();
            } else {
                return 4000000 + cardHand.getCard(3).getNumber() * 10000 + cardHand.getCard(0).getNumber() * 100 + cardHand.getCard(1).getNumber();
            }
        }
        return cardHand.getCard(0).getNumber();
    }
}
