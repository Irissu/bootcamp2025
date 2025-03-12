package com.gildedrose;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.approvaltests.Approvals;


import java.util.List;


class GildedRoseTest {

//    @Test
//    void foo() {
//        Item[] items = new Item[] { new Item("foo", 0, 0) };
//        GildedRose app = new GildedRose(items);
//        app.updateQuality();
//        assertEquals("foo", app.items[0].name);
//    }

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

    @Test
//	@Disabled
    void snapshot() {
        Item[] items = new Item[] {
            new Item("+5 Dexterity Vest", 10, 20),
            new Item("Aged Brie", 2, 0),
            new Item("Elixir of the Mongoose", 5, 7),
            new Item("Sulfuras, Hand of Ragnaros", 0, 80),
            new Item("Sulfuras, Hand of Ragnaros", -1, 80),
            new Item("Backstage passes to a TAFKAL80ETC concert", 15, 20),
            new Item("Backstage passes to a TAFKAL80ETC concert", 10, 49),
            new Item("Backstage passes to a TAFKAL80ETC concert", 5, 49),
            new Item("Conjured Mana Cake", 3, 6)
        };

        GildedRose app = new GildedRose(items);
        var output = new StringBuilder();
        output.append("day,name, sellIn, quality\n");
        List.of(items).forEach(item -> output.append("0," + item + "\n"));
        for (int i = 1; i <= 9; i++) {
            app.updateQuality();
            for(Item item: items)
                output.append(i + "," + item + "\n");
        }
        Approvals.verify(output);
    }
}
