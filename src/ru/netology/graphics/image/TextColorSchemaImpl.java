package ru.netology.graphics.image;

public class TextColorSchemaImpl implements TextColorSchema {
    private char[] list = {'#', '$', '@', '%', '*', '+', '-', '\''};

    public TextColorSchemaImpl(char[] list) {
        this.list = list;
    }

    public TextColorSchemaImpl() {
    }

    @Override
    public char convert(int color) {
        for (int i = 0; i < list.length; i++) {
            if (color <= 255 / (list.length - i)) {
                return list[i];
            }
        }
        return 0;
    }
}
