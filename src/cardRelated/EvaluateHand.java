package cardRelated;

public class EvaluateHand {
    static private boolean isRoyalFlush(CardHand cardHand) {
        return isStraightFlush(cardHand) &&
               cardHand.getCard(0).getNumber() == 12 &&
               cardHand.getCard(4).getNumber() != 0;
    }

    static private boolean isFlush(CardHand cardHand) {
        return cardHand.getCard(0).getSuit() == cardHand.getCard(1).getSuit() &&
               cardHand.getCard(1).getSuit() == cardHand.getCard(2).getSuit() &&
               cardHand.getCard(2).getSuit() == cardHand.getCard(3).getSuit() &&
               cardHand.getCard(3).getSuit() == cardHand.getCard(4).getSuit();
    }

    static private boolean isStraight(CardHand cardHand) {
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

    static private boolean isStraightFlush(CardHand cardHand) {
        return isFlush(cardHand) && isStraight(cardHand);
    }

    static private boolean isFour(CardHand cardHand) {
        return (cardHand.getCard(0).getNumber() == cardHand.getCard(1).getNumber() &&
                cardHand.getCard(1).getNumber() == cardHand.getCard(2).getNumber() &&
                cardHand.getCard(2).getNumber() == cardHand.getCard(3).getNumber()) ||
               (cardHand.getCard(1).getNumber() == cardHand.getCard(2).getNumber() &&
                cardHand.getCard(2).getNumber() == cardHand.getCard(3).getNumber() &&
                cardHand.getCard(3).getNumber() == cardHand.getCard(4).getNumber());
    }

    static private boolean isThree(CardHand cardHand) {
        return (cardHand.getCard(0).getNumber() == cardHand.getCard(1).getNumber() &&
                cardHand.getCard(1).getNumber() == cardHand.getCard(2).getNumber()) ||
               (cardHand.getCard(1).getNumber() == cardHand.getCard(2).getNumber() &&
                cardHand.getCard(2).getNumber() == cardHand.getCard(3).getNumber()) ||
               (cardHand.getCard(2).getNumber() == cardHand.getCard(3).getNumber() &&
                cardHand.getCard(3).getNumber() == cardHand.getCard(4).getNumber());
    }

    static private boolean isTwoPair(CardHand cardHand) {
        return (cardHand.getCard(0).getNumber() == cardHand.getCard(1).getNumber() &&
                cardHand.getCard(2).getNumber() == cardHand.getCard(3).getNumber()) ||
               (cardHand.getCard(0).getNumber() == cardHand.getCard(1).getNumber() &&
                cardHand.getCard(3).getNumber() == cardHand.getCard(4).getNumber()) ||
               (cardHand.getCard(1).getNumber() == cardHand.getCard(2).getNumber() &&
                cardHand.getCard(3).getNumber() == cardHand.getCard(4).getNumber());
    }

    static private boolean isPair(CardHand cardHand) {
        return cardHand.getCard(0).getNumber() == cardHand.getCard(1).getNumber() ||
               cardHand.getCard(1).getNumber() == cardHand.getCard(2).getNumber() ||
               cardHand.getCard(2).getNumber() == cardHand.getCard(3).getNumber() ||
               cardHand.getCard(3).getNumber() == cardHand.getCard(4).getNumber();
    }

    static private boolean isFullHouse(CardHand cardHand) {
        return (cardHand.getCard(0).getNumber() == cardHand.getCard(1).getNumber() &&
                (cardHand.getCard(2).getNumber() == cardHand.getCard(3).getNumber() &&
                 cardHand.getCard(3).getNumber() == cardHand.getCard(4).getNumber())) ||
               (cardHand.getCard(3).getNumber() == cardHand.getCard(4).getNumber() &&
                (cardHand.getCard(0).getNumber() == cardHand.getCard(1).getNumber() &&
                 cardHand.getCard(1).getNumber() == cardHand.getCard(2).getNumber()));
    }

    static public long evaluateHand(CardHand cardHand) {
        cardHand.sortByValue();
        if (isRoyalFlush(cardHand)) return 100000000000L;
        if (isStraightFlush(cardHand)) return 99999999999L;
        if (isFour(cardHand)) return 99999999998L;
        if (isFullHouse(cardHand)) {
            if (cardHand.getCard(1).getNumber() == cardHand.getCard(2).getNumber()) {
                return 90000000000L + cardHand.getCard(0).getNumber() * 100 + cardHand.getCard(4).getNumber();
            } else {
                return 90000000000L + cardHand.getCard(4).getNumber() * 100 + cardHand.getCard(0).getNumber();
            }
        }
        if (isFlush(cardHand)) {
            return 80000000000L + cardHand.getCard(0).getNumber();
        }
        if (isStraight(cardHand)) {
            return 70000000000L + cardHand.getCard(0).getNumber();
        }

        if (isThree(cardHand)) {
            if (cardHand.getCard(0).getNumber() == cardHand.getCard(2).getNumber()) {
                return 60000000000L + cardHand.getCard(0).getNumber() * 10000 + cardHand.getCard(3).getNumber() * 100 + cardHand.getCard(4).getNumber();
            } else if (cardHand.getCard(1).getNumber() == cardHand.getCard(3).getNumber()) {
                return 60000000000L + cardHand.getCard(1).getNumber() * 10000 + cardHand.getCard(0).getNumber() * 100 + cardHand.getCard(4).getNumber();
            } else {
                return 60000000000L + cardHand.getCard(2).getNumber() * 10000 + cardHand.getCard(0).getNumber() * 100 + cardHand.getCard(1).getNumber();
            }
        }
        if (isTwoPair(cardHand)) {
            if (cardHand.getCard(0).getNumber() == cardHand.getCard(1).getNumber() && cardHand.getCard(2).getNumber() == cardHand.getCard(3).getNumber()) {
               return 50000000000L + cardHand.getCard(0).getNumber() * 10000 + cardHand.getCard(2).getNumber() * 100 + cardHand.getCard(4).getNumber();
            } else if (cardHand.getCard(0).getNumber() == cardHand.getCard(1).getNumber() && cardHand.getCard(3).getNumber() == cardHand.getCard(4).getNumber()) {
                return 50000000000L + cardHand.getCard(0).getNumber() * 10000 + cardHand.getCard(3).getNumber() * 100 + cardHand.getCard(2).getNumber();
            } else {
                return 50000000000L + cardHand.getCard(1).getNumber() * 10000 + cardHand.getCard(3).getNumber() * 100 + cardHand.getCard(0).getNumber();
            }
        }
        if (isPair(cardHand)) {
            if (cardHand.getCard(0).getNumber() == cardHand.getCard(1).getNumber()) {
                return 40000000000L + cardHand.getCard(0).getNumber() * 1000000 + cardHand.getCard(2).getNumber() * 10000 + cardHand.getCard(3).getNumber() * 100 + cardHand.getCard(4).getNumber();
            } else if (cardHand.getCard(1).getNumber() == cardHand.getCard(2).getNumber()) {
                return 40000000000L + cardHand.getCard(1).getNumber() * 1000000 + cardHand.getCard(0).getNumber() * 10000 + cardHand.getCard(3).getNumber() * 100 + cardHand.getCard(4).getNumber();
            } else if (cardHand.getCard(2).getNumber() == cardHand.getCard(3).getNumber()) {
                return 40000000000L + cardHand.getCard(2).getNumber() * 1000000 + cardHand.getCard(0).getNumber() * 10000 + cardHand.getCard(1).getNumber() * 100 + cardHand.getCard(4).getNumber();
            } else {
                return 40000000000L + cardHand.getCard(3).getNumber() * 1000000 + cardHand.getCard(0).getNumber() * 10000 + cardHand.getCard(1).getNumber() * 100 + cardHand.getCard(2).getNumber();
            }
        }
        return cardHand.getCard(0).getNumber() * 100000000 + cardHand.getCard(1).getNumber() * 1000000 + cardHand.getCard(2).getNumber() * 10000 + cardHand.getCard(3).getNumber() * 100 + cardHand.getCard(4).getNumber();
    }

    public static void main(String[] args) {
        Card card1 = new Card(12, 0);
        Card card2 = new Card(10, 1);
        Card card3 = new Card(10, 0);
        Card card4 = new Card(9, 0);
        Card card5 = new Card(8, 0);

        CardHand hand = new CardHand();
        hand.addCard(card1);
        hand.addCard(card2);
        hand.addCard(card3);
        hand.addCard(card4);
        hand.addCard(card5);


        System.out.println(evaluateHand(hand));
    }
}
