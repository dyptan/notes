package src.collections;

import java.util.HashMap;

/**
 * Created by diptan on 29.06.18.
 */
public class WordTranslator {
    private HashMap<String, String> dictionary = new HashMap<>();
    public void addNewWord (String en, String ua){
        dictionary.put(en, ua);
    }

    public String translate (String en){
        return dictionary.get(en);
    }
}
