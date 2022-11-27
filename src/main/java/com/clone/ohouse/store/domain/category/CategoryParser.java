package com.clone.ohouse.store.domain.category;

import java.util.List;

public class CategoryParser {
    static public CategorySearch parseCategoryString(String categoriesString) {
        CategorySearch categorySearch = new CategorySearch(null, null, null, null);

        String[] s = categoriesString.split("_");

        if(s.length >= 1) categorySearch.setCode1(Long.parseLong(s[0]));
        if(s.length >= 2) categorySearch.setCode2(Long.parseLong(s[1]));
        if(s.length >= 3) categorySearch.setCode3(Long.parseLong(s[2]));
        if(s.length >= 4) categorySearch.setCode4(Long.parseLong(s[3]));

        return categorySearch;
    }
}
