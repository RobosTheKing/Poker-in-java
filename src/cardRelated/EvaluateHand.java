package cardRelated;

import java.util.Arrays;

public class EvaluateHand {
    public static final long CLUB_MASK    = (1L << 13) - 1;
    public static final long HEART_MASK   = CLUB_MASK << 13;
    public static final long SPADE_MASK   = CLUB_MASK << 26;
    public static final long DIAMOND_MASK = CLUB_MASK << 39;

    public static final int RANKS = 13;
    public static final int TABLE_SIZE = 1 << RANKS;

    public static final int[] STRAIGHT_TABLE = buildStraightTable();

    public static final int[] buildStraightTable() {
        int[] table = new int[TABLE_SIZE];

        for (int i = 0; i < table.length; i++) table[i] = -1;

        int[] patterns = new int[10];
        for (int start = 0; start < 9; start++) {
            int pat = 0;
            for (int k = 0; k < 5; k++) {
                pat |= (1 << (start + k));
            }
            patterns[start] = pat;
        }

        patterns[9] = (1 << 12) | (1 << 0) | (1 << 1) | (1 << 2) | (1 << 3);


        int[] highest = new int[10];
        for (int start = 0; start <= 8; start++) highest[start] = start + 4;
        highest[9] = 3;

        for (int mask = 0; mask < TABLE_SIZE; mask++) {
            for (int p = 0; p < patterns.length; p++) {
                int pat = patterns[p];
                if ((mask & pat) == pat) {
                    if (highest[p] > table[mask]) table[mask] = highest[p];
                }
            }
        }

        return table;
    }

    
    public static int evaluate7(long cardMask) {
        long clubs    = cardMask & CLUB_MASK;
        long hearts   = cardMask & HEART_MASK;
        long spades   = cardMask & SPADE_MASK;
        long diamonds = cardMask & DIAMOND_MASK;

        int flushType = 0;

        long flushMask = 0;
        if (Long.bitCount(clubs) >= 5)         flushMask = clubs;
        else if (Long.bitCount(hearts) >= 5)   {flushMask = hearts; flushType = 1;}
        else if (Long.bitCount(spades) >= 5)   {flushMask = spades; flushType = 2;}
        else if (Long.bitCount(diamonds) >= 5) {flushMask = diamonds; flushType = 3;}
        
        if (flushMask > 0) {
            int i = 0;

            while (Long.bitCount(flushMask) > 5) {
                i++;
                flushMask = flushMask >> 1;
            }

            flushMask = flushMask << i;
        }

        long rankMask = clubs | (hearts >> 13) | (spades >> 26) | (diamonds >> 39);

        int straghtValue = STRAIGHT_TABLE[(int) rankMask];

        int[] rankCount = new int[13];
        for (int i = 0; i < 52; i++) {
            if ((getBit(cardMask, i)) != 0) {
                rankCount[i % 13]++;
            }
        }

        int pairs = 0, triples = 0, fours = 0;
        for (int c : rankCount) {
            if (c == 2) pairs++;
            else if (c == 3) triples++;
            else if (c == 4) fours++;
        }

        
        
        if (flushMask != 0 && straghtValue > 0) {
            int score = 0b1000 << 20;
            int[] kickers = kickers(rankMask, 1, null);
            score += kickers[0];
            return score;
        }
        if (fours > 0) {
            int score = 0b111 << 20;
            score += lastIndexOf(rankCount, 3, -1) << 4;
            int[] kickers = kickers(rankMask, 1, new int[] {lastIndexOf(rankCount, 3, -1)});
            score += kickers[0];
            return score;
        }
        if (triples > 0 && pairs > 0) {
            int score = 0b110 << 20;
            score += lastIndexOf(rankCount, 3, -1) << 4;
            score += lastIndexOf(rankCount, 2, -1);
            return score;
        }
        if (flushMask != 0) {
            int score = 0b101 << 20;
            int[] kickers = kickers((flushMask >> 13 * flushType), 5, null);
            score += kickers[0] << 16;
            score += kickers[1] << 12;
            score += kickers[2] << 8;
            score += kickers[3] << 4;
            score += kickers[4];
            return score;
        }
        if (straghtValue > 0) {
            int score = 0b100 << 20;
            score += straghtValue;
            return score;
        }
        if (triples > 0) {
            int score = 0b11 << 20;
            score += lastIndexOf(rankCount, 3, -1) << 8;
            int[] kickers = kickers(rankMask, 2, new int[] {lastIndexOf(rankCount, 3, -1)});
            score += kickers[0] << 4;
            score += kickers[1];
            return score;
        }
        if (pairs >= 2) {
            int score = 0b10 << 20;
            int pair1 = lastIndexOf(rankCount, 2, -1);
            int pair2 = lastIndexOf(rankCount, 2, pair1);
            score += pair1 << 8;
            score += pair2 << 4;
            int[] kickers = kickers(rankMask, 1, new int[] {pair1, pair2});
            score += kickers[0];
            return score;
        }
        if (pairs == 1) {
            int score = 0b1 << 20;
            int pair = lastIndexOf(rankCount, 2, -1);
            score += pair << 12;
            int[] kickers = kickers(rankMask, 3, new int[] {pair});
            score += kickers[0] << 8;
            score += kickers[1] << 4;
            score += kickers[2];
            return score;
        }
        int score = 0;
        int[] kickers = kickers(rankMask, 5, null);
        score += kickers[0] << 16;
        score += kickers[1] << 12;
        score += kickers[2] << 8;
        score += kickers[3] << 4;
        score += kickers[4];
        return score;
    }

    private static int[] kickers(long rankMask, int amountOfKickers, int[] ignore) {
        int[] kicker = new int[amountOfKickers];
        int savedKickers = 0;

        for (int i = 12; i >= 0; i--) {
            if (getBit(rankMask, i) == 1 && indexOf(ignore, i) == -1) {
                kicker[savedKickers] = i;
                savedKickers++;
            }



            if (savedKickers == amountOfKickers) break;
        }
        if (savedKickers == 0) return new int[] {-1};
        return kicker;
    }

    private static int getBit(long value, int index) {
        return (int) (value >> index) & 1;
    }

    private static int indexOf(int[] array, int value) {
        if (array == null) return -1;
        for (int i = 0; i < array.length; i++) {
            if (array[i] == value) {
                return i;
            }
        }
        return -1;
    }

    private static int lastIndexOf(int[] array, int value, int ignore) {
        for (int i = array.length - 1; i >= 0 ; i--) {
            if (array[i] == value && i != ignore) {
                return i;
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        long mask = 0b100100010000001000001100100;
        System.out.println(evaluate7(mask));
    }
}
