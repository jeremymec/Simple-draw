package com.jerem.simpledraw;


import java.util.ArrayList;
import java.util.Iterator;

public class DrawingObject implements Iterable<String[]> {
    private ArrayList<String> coords;
    private ArrayList<String> code;

    public DrawingObject(){
        coords = new ArrayList<>();
        code = new ArrayList<>();
    }

    public void add(String code, String coords){
        this.coords.add(coords);
        this.code.add(code);
    }


    public Iterator<String[]> iterator(){
        return new Iterator<String[]>() {
            int cursor = 0;

            @Override
            public boolean hasNext() {
                if (coords.size() == cursor){
                    return false;
                }
                return true;
            }

            @Override
            public String[] next() {
                String[] s = new String[] {code.get(cursor), coords.get(cursor)};
                cursor ++;
                return s;

            }

        };
    }

}
