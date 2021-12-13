/*
 * 4. Напишите метод, который возвращает среднее значение из списка целых чисел.
 * public Double average(List<Integer> list)
 */

import inter.AverNumber;
import java.util.ArrayList;
import java.util.List;

public class Task4 {

  static final List<Integer> list = new ArrayList<>();

  public static void run() {
    System.out.println();
    System.out.println("********** Task 4 **********");

    list.add(1);
    list.add(13);
    list.add(14);
    list.add(5);
    list.add(16);

    System.out.println(list);

    AverNumber averNumber = l ->
        (double) l.stream().mapToInt(i -> i).sum() / (long) l.size();
    System.out.println(averNumber.average(list));
  }
}
