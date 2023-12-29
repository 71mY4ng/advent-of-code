package c2023;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Day7 {

    @Test
    void test() throws IOException {

        var input = """
                32T3K 765
                T55J5 684
                KK677 28
                KTJJT 220
                QQQJA 483
                """;

        Path testResource = Paths.get("src", "test", "resources");
        List<String> lines = Files.readAllLines(testResource.resolve("adventofcode_2023_day7.input"));
//        List<String> lines = Arrays.stream(input.split("\n")).toList();
        Map<HandType, List<CamelCard>> rank = new LinkedHashMap<>();
        for (String line : lines) {
            String[] split = line.split(" ");
            CamelCard hold = CamelCard.of(split[0].trim(), split[1].trim());
            HandType type = HandType.match(hold);

            rank.compute(type, (k, v) -> {
                if (v == null) {
                    v = new ArrayList<>();
                }
                v.add(hold);
                return v;
            });
        }

        List<CamelCard> list = rank.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .map(e -> HandType.FIVE_OF_A_KIND == e.getKey() ? rankFiveOfAKind(e.getValue()) : rankOtherKind(e.getValue()))
                .flatMap(List::stream)
                .toList();

        int ans = 0;
        for (int i = 0; i < list.size(); i++) {
            System.out.println("rank " + (list.size() - i) + ", hold: " + list.get(i));
            ans += ((list.size() - i) * list.get(i).bid);
        }

        System.out.println(ans);
    }

    public record CamelCard(String hand, int bid) {
        public static CamelCard of(String hand, String bid) {
            return new CamelCard(hand, Integer.parseInt(bid));
        }

        public Map<Character, Integer> histogram() {
            Map<Character, Integer> h = new HashMap<>(5);
            for (char c : hand.toCharArray()) {
                h.compute(c, (k, v) -> {
                    if (v == null) {
                        v = 0;
                    }
                    return v + 1;
                });
            }
            return h;
        }

        public char[] sortCards() {
            char[] cards = hand.toCharArray();
            Arrays.sort(cards);
            return cards;
        }
    }

    public List<CamelCard> rankFiveOfAKind(List<CamelCard> hands) {
        return hands.stream().sorted((c, s) -> {
            var compareDistinct = Card.parse(c.hand.toCharArray()[0]);
            var sDistinct = Card.parse(s.hand.toCharArray()[0]);
            return compareDistinct.compareTo(sDistinct);
        }).collect(Collectors.toList());
    }

    public List<CamelCard> rankOtherKind(List<CamelCard> hands) {
        return hands.stream().sorted((c, s) -> {
            char[] cCards = c.hand.toCharArray();
            char[] sCards = s.hand.toCharArray();
            for (int i = 0; i < 5; i++) {
                Card cCard = Card.parse(cCards[i]);
                Card sCard = Card.parse(sCards[i]);
                if (cCard.compareTo(sCard) == 0) {
                    continue;
                }
                return cCard.compareTo(sCard);
            }
            return 0;
        }).collect(Collectors.toList());
    }

    record Card(int order, char kind) implements Comparable<Card> {
        public static final Map<Character, Integer> ORDERS = Map.of(
                'A', 14,
                'K', 13,
                'Q', 12,
                'J', 11,
                'T', 10
        );
        public static Card parse(char kind) {
            if (kind >= 48 && kind <= 57) {
                return new Card(Integer.parseInt(String.valueOf(kind)), kind);
            }
            return new Card(ORDERS.get(kind), kind);
        }

        @Override
        public int compareTo(Card o) {
            return Integer.compare(o.order(), order());
        }
    }

    enum HandType {
        FIVE_OF_A_KIND(7),
        FOUR_OF_A_KIND(6),
        FULL_HOUSE(5),
        THREE_OF_A_KIND(4),
        TWO_PAIR(3),
        ONE_PAIR(2),
        HIGH_CARD(1),
        ;
        private int rank;
        HandType(int rank) {
            this.rank = rank;
        }

        public static HandType match(CamelCard camelCard) {
            Map<Character, Integer> h = camelCard.histogram();
            int hSize = h.size();
            if (hSize == 1) {
                return FIVE_OF_A_KIND;
            } else if (hSize == 2) {
                if (h.containsValue(4)) {
                    return FOUR_OF_A_KIND;
                }
                return FULL_HOUSE;
            } else if (hSize == 3) {
                if (h.containsValue(3)) {
                    return THREE_OF_A_KIND;
                }
                return TWO_PAIR;
            } else if (hSize == 4) {
                return ONE_PAIR;
            } else if (hSize == 5) {
                return HIGH_CARD;
            }

            return HIGH_CARD;
        }

        public int getRank() {
            return rank;
        }
    }


    @Test
    void testCamelCard() {

//        CamelCard hand = new CamelCard("K2K2K", 10);
        CamelCard hand = new CamelCard("AKQJT98765432", 10);
        Map<Character, Integer> h = hand.histogram();
        System.out.println(h.size());
        System.out.println(hand.sortCards());
        System.out.println(h);
    }
}
