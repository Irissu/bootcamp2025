package com.gildedrose;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GildedRoseTest {

    @Test
    void foo() {
        Item[] items = new Item[] { new Item("foo", 0, 0) };
        GildedRose app = new GildedRose(items);
        app.updateQuality();
        assertEquals("foo", app.items[0].name);
    }

    @Test
    void testProductNameIsConjured() {
        Item[] items = new Item[] { new Item("Conjured Space Cake", 0, 0) };
        GildedRose app = new GildedRose(items);
        app.updateQuality();
        assertTrue(items[0].name.contains("Conjured"));
    }

    @Test
    void testQualityDegradationOfConjuredItems() {
        Item[] items = new Item[] {
            new Item("Conjured Space Brownie", 3, 5),
            new Item("Conjured Space Cake", 1, 10),
            new Item("Conjured Split Banana", 0, 6)
        };
        GildedRose app = new GildedRose(items);
        app.updateQuality();

        assertEquals(3, items[0].quality);
        assertEquals(8, items[1].quality);
        assertEquals(2, items[2].quality);
    }

    @Test
    void testCheckPositiveQualityOfConjuredItems(){
        Item[] items = new Item[] { new Item("Conjured Space Cake", 0, 0) };

        GildedRose app = new GildedRose(items);
        app.updateQuality();

        assertTrue(items[0].quality >= 0);
    }
}
