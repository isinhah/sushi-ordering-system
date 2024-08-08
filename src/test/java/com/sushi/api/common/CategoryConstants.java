package com.sushi.api.common;

import com.sushi.api.model.Category;

import java.util.List;
import java.util.Set;

public class CategoryConstants {
    public static final Category CATEGORY = new Category(1L, "Sushi Tradicional", "Uma seleção dos sushis tradicionais mais populares, incluindo nigiri, maki e sashimi, preparados com ingredientes frescos e autênticos.");
    public static final Category CATEGORY2 = new Category(2L, "Comida chinesa", "Uma seleção dos pratos chineses tradicionais mais populares, incluindo Pato de Pequim, Frango Kung Pao e Carne com Brócolis, preparados com receitas autênticas.");
    public static final Category CATEGORY3 = new Category(3L, "Sushi Fusion", "Inovadores e deliciosos sushis de fusão, combinando sabores tradicionais japoneses com ingredientes modernos e internacionais para uma experiência única.");
    public static final List<Category> CATEGORIES = List.of(CATEGORY, CATEGORY2, CATEGORY3);
    public static final Set<Category> CATEGORIES_FOR_PRODUCTS = Set.of(CATEGORY, CATEGORY2);
}