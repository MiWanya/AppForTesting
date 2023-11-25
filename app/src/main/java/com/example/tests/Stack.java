package com.example.tests;

import android.util.Log;

import java.util.ArrayList;

public class Stack {
    private ArrayList<String> items;

    public Stack() {
        this.items = new ArrayList<>();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public void push(String item) {
        items.add(item);
    }

    public String pop() {
        if (!isEmpty()) {
            String poppedItem = items.remove(items.size() - 1);
            return poppedItem;
        } else {
            Log.d("Стек", "Стек пуст. Невозможно выполнить операцию извлечения (pop).");
            return null; // Возвращаем какой-то специальный код для обозначения ошибки или пустого стека
        }
    }

    public String peek() {
        if (!isEmpty()) {
            return items.get(items.size() - 1);
        } else {
            Log.d("Стек", "Стек пуст. Невозможно выполнить операцию просмотра (pick).");
            return null; // Возвращаем какой-то специальный код для обозначения ошибки или пустого стека
        }
    }

    public int size() {
        return items.size();
    }

}
