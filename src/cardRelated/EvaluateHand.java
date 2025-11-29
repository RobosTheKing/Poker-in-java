package cardRelated;

public class EvaluateHand {
    public static final long CLUB_MASK = (1L << 13) - 1;
    public static final long HEART_MASK = CLUB_MASK << 13;
    public static final long SPADE_MASK = CLUB_MASK << 26;
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
        long diamonds = cardMask & DIAMOND_MASK;
        long hearts   = cardMask & HEART_MASK;
        long spades   = cardMask & SPADE_MASK;

        long flushMask = 0;
        if (Long.bitCount(clubs) >= 5)    flushMask = clubs;
        else if (Long.bitCount(diamonds) >= 5) flushMask = diamonds;
        else if (Long.bitCount(hearts) >= 5)   flushMask = hearts;
        else if (Long.bitCount(spades) >= 5)   flushMask = spades;
        
        if (flushMask > 0) {
            int i = 0;

            while (Long.bitCount(flushMask) > 5) {
                i++;
                flushMask = flushMask >> 1;
            }

            flushMask = flushMask << i;
        }
        
        return -1;
    }
    
}
