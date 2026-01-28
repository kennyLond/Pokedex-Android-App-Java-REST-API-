package com.example.pokedex.utils;

import android.graphics.Color;

public class TypeColors {

    public static int getTypeColor(String type) {
        if (type == null) {
            return Color.parseColor("#6D6D4E"); // normal oscuro
        }

        switch (type.toLowerCase()) {
            case "normal":
                return Color.parseColor("#6D6D4E");
            case "fire":
                return Color.parseColor("#C0501F"); // Naranja más oscuro
            case "water":
                return Color.parseColor("#4060C0"); // Azul más oscuro
            case "electric":
                return Color.parseColor("#C09000"); // Amarillo más oscuro
            case "grass":
                return Color.parseColor("#4F8030"); // Verde más oscuro
            case "ice":
                return Color.parseColor("#5090A8"); // Celeste más oscuro
            case "fighting":
                return Color.parseColor("#A02020"); // Rojo más oscuro
            case "poison":
                return Color.parseColor("#802080"); // Morado más oscuro
            case "ground":
                return Color.parseColor("#B08040"); // Café más oscuro
            case "flying":
                return Color.parseColor("#7060C0"); // Lila más oscuro
            case "psychic":
                return Color.parseColor("#C04060"); // Rosa más oscuro
            case "bug":
                return Color.parseColor("#708018"); // Verde oliva más oscuro
            case "rock":
                return Color.parseColor("#907028"); // Café más oscuro
            case "ghost":
                return Color.parseColor("#503860"); // Morado oscuro
            case "dragon":
                return Color.parseColor("#5028C0"); // Azul violeta oscuro
            case "dark":
                return Color.parseColor("#503830"); // Negro café
            case "steel":
                return Color.parseColor("#7070A0"); // Gris azulado oscuro
            case "fairy":
                return Color.parseColor("#C05890"); // Rosa oscuro
            default:
                return Color.parseColor("#6D6D4E");
        }
    }

    public static int getTypeLightColor(String type) {
        if (type == null) {
            return Color.parseColor("#C6C6A7");
        }

        switch (type.toLowerCase()) {
            case "normal":
                return Color.parseColor("#C6C6A7");
            case "fire":
                return Color.parseColor("#F5AC78");
            case "water":
                return Color.parseColor("#9DB7F5");
            case "electric":
                return Color.parseColor("#FAE078");
            case "grass":
                return Color.parseColor("#A7DB8D");
            case "ice":
                return Color.parseColor("#BCE6E6");
            case "fighting":
                return Color.parseColor("#D67873");
            case "poison":
                return Color.parseColor("#C183C1");
            case "ground":
                return Color.parseColor("#EBD69D");
            case "flying":
                return Color.parseColor("#C6B7F5");
            case "psychic":
                return Color.parseColor("#FA92B2");
            case "bug":
                return Color.parseColor("#C6D16E");
            case "rock":
                return Color.parseColor("#D1C17D");
            case "ghost":
                return Color.parseColor("#A292BC");
            case "dragon":
                return Color.parseColor("#A27DFA");
            case "dark":
                return Color.parseColor("#A29288");
            case "steel":
                return Color.parseColor("#D1D1E0");
            case "fairy":
                return Color.parseColor("#F4BDC9");
            default:
                return Color.parseColor("#C6C6A7");
        }
    }
}