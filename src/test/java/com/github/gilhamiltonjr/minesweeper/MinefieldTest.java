package com.github.gilhamiltonjr.minesweeper;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MinefieldTest {
    private static Minefield minefieldFromString(String s) {
        var mines = new ArrayList<Minefield.Coordinate>();
        var assertions = new HashMap<Minefield.Coordinate, Integer>();

        int x = 0, y = 0;
        int width = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '*':
                    mines.add(new Minefield.Coordinate(x, y));
                    break;
                case '.':
                    break;
                case '\n':
                    if (width == 0) {
                        width = x;
                    } else if (width != x) {
                        throw new IllegalArgumentException("minefield must be rectangular");
                    }
                    x = 0;
                    y++;
                    continue;
                case '0', '1', '2', '3', '4', '5', '6', '7', '8':
                    assertions.put(new Minefield.Coordinate(x, y), c - '0');
                    break;
                default:
                    throw new IllegalArgumentException("invalid character " + c);
            }

            x++;
        }

        Minefield f = new Minefield(width, y, 0);

        for (Minefield.Coordinate mine : mines) {
            f.placeMine(mine);
        }

        for (Map.Entry<Minefield.Coordinate, Integer> assertion : assertions.entrySet()) {
            assertEquals(assertion.getValue(),
                    f.getNeighborsCount(assertion.getKey().x(), assertion.getKey().y()),
                    "mine count at " + assertion.getKey());
        }

        return f;
    }

    @Test
    public void testBasicCount() {
        Minefield f = minefieldFromString("""
                .2*2.
                .2*31
                .112*
                """);
        assertNotNull(f);
        assertEquals(5, f.getWidth());
        assertEquals(3, f.getHeight());
        assertEquals(3, f.getMineCount());
    }

    @Test
    public void test3BV() {
        // A minefield with a couple of islands
        Minefield f = minefieldFromString("""
                0000002*
                0000013*
                110113*3
                *101*3*2
                11011222
                0000001*
                00122222
                001**2*1
                """);

        List<Minefield.Coordinate> requiredClicks = f.getMinimumClicksToSolve();
        assertEquals(8, requiredClicks.size());
    }
}
