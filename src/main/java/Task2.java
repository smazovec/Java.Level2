/* 2. Напишите метод, переворачивающий строку.
 * Например, «java interview» превращается в «weivretni avaj».
 * public String reverse(String s)
 */

import inter.Reverse;

public class Task2 {

  public static void run() {
    System.out.println();
    System.out.println("********** Task 2 **********");

    Reverse reverse = str -> new StringBuffer(str).reverse().toString();
    System.out.println(reverse.reverse("java interview"));
  }
}
