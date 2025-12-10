package com.example.hmrback.persistence.enums;

import lombok.Getter;

import java.util.List;

@Getter
public enum Unit {
    GRAM("g", "Gramme"),
    KILOGRAM("kg", "Kilogramme"),
    LITER("L", "Litre"),
    MILLILITER("mL", "Millilitre"),
    CUP("t.", "Tasse"),
    TABLESPOON("c.a.s.", "Cuillère à soupe"),
    TEASPOON("c.a.c.", "Cuillère à café"),
    PIECE("pc", "Pièce");

    private final String shortName;
    private final String fullName;

    Unit(String shortName, String fullName) {
        this.shortName = shortName;
        this.fullName = fullName;
    }

    public static List<Unit> valuesList() {
        return List.of(Unit.values());
    }

    public static Unit getByIndex(int index) {
        List<Unit> list = valuesList();
        if (index < 0 || index >= list.size()) {
            throw new IndexOutOfBoundsException("Index " + index + " out of bounds for enum " + list);
        }
        return list.get(index);
    }
}
