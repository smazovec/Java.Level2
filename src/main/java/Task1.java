/*
 * 1. Напишите метод, который возвращает индекс первого вхождения данного целого числа в списке.
 * Предположим, что индекс первого элемента в списке равен нулю.
 * Если числа не существует в списке, верните -1.
 * public int search(Integer n, Integer[] list)
 */

import inter.FirstEntry;
import java.util.Arrays;

public class Task1 {

  static final Integer[] list = new Integer[20];

  public static void run() {

    System.out.println("********** Task 1 **********");
    for (int i = 0; i < list.length; i++) {
      list[i] = i + 5;
    }
    System.out.println(Arrays.toString(list));
    FirstEntry firstEntry = (n, list) -> Arrays.asList(list).indexOf(n);
    System.out.println(firstEntry.findFirst(14, list));
  }
}
