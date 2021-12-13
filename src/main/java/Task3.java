/*
 * 3. Напишите метод, который возвращает наибольшее целое число в списке.
 * public Integer maximum(Integer[] list)
 */

import inter.BigNumber;
import java.util.Arrays;
import java.util.Collections;

public class Task3 {

  static final Integer[] list = {1, 10, 200, -45, 33, 250, 2300, 40, 6};

  public static void run() {
    System.out.println();
    System.out.println("********** Task 3 **********");

    System.out.println(Arrays.toString(list));

    BigNumber bigNumber = list -> Collections.max(Arrays.asList(list));
    System.out.println(bigNumber.maximum(list));

  }


}
