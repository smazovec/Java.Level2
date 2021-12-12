package task1;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class ArrayOfWords {

  private final String[] arrayOfWords;

  public ArrayOfWords(String text) {
    this.arrayOfWords = text.split(" ");
  }

  public void findUnique() {
    Set<String> hashSetOfArray = new LinkedHashSet<>(Arrays.asList(arrayOfWords));
    System.out.println(hashSetOfArray);
  }

  public void countTheWords() {
    Map<String, Integer> hashMapOfArray = new LinkedHashMap<>();
    for (String word : arrayOfWords) {
      int count = hashMapOfArray.getOrDefault(word, 0);
      hashMapOfArray.put(word, count + 1);
    }
    System.out.println(hashMapOfArray);
  }
}
