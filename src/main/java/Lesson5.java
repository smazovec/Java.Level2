import java.util.Arrays;

public class Lesson5 {

  static final int SIZE = 10000000;
  static final int HALF_SIZE = SIZE / 2;

  public static void main(String[] args) throws InterruptedException {

    float[] arr1 = mode1();
    float[] arr2 = mode2();

    System.out.println("Массивы идентичны: " + Arrays.equals(arr1, arr2));

  }

  static float[] mode1() {

    // 1) Создаем одномерный длинный массив
    float[] arr = new float[SIZE];

    // 2) Заполняем этот массив единицами;
    Arrays.fill(arr, 1);

    // 3) Засекаем время начала выполнения
    long time1 = System.currentTimeMillis();

    // 4) Проходим по всему массиву и для каждой ячейки считают новое значение по формуле
    function(arr, 0);

    // 5) Выводим время работы метода
    System.out.println(System.currentTimeMillis() - time1);

    return arr;

  }

  static float[] mode2() throws InterruptedException {

    // 1) Создаем одномерный длинный массив
    float[] arr = new float[SIZE];

    // 2) Заполняем этот массив единицами;
    Arrays.fill(arr, 1);

    // 3) Засекаем время начала выполнения
    long time1 = System.currentTimeMillis();

    // 4) Разбиваем массив на два массива
    float[] a1 = new float[HALF_SIZE];
    float[] a2 = new float[HALF_SIZE];
    System.arraycopy(arr, 0, a1, 0, HALF_SIZE);
    System.arraycopy(arr, HALF_SIZE, a2, 0, HALF_SIZE);

    // 5) Проходим по половинке массива в двух потоках и для каждой ячейки считают новое значение по формуле
    Thread thread1 = new Thread(() -> function(a1, 0));
    Thread thread2 = new Thread(() -> function(a2, HALF_SIZE));

    thread1.start();
    thread2.start();
    thread1.join();
    thread2.join();

    // 6) Склеиваем массив
    System.arraycopy(a1, 0, arr, 0, HALF_SIZE);
    System.arraycopy(a2, 0, arr, HALF_SIZE, HALF_SIZE);

    // 7) Выводим время работы метода
    System.out.println(System.currentTimeMillis() - time1);

    return arr;

  }

  // Для того чтобы на выходе массивы были идентичными добавлен параметр смещения shift
  static void function(float[] arr, int shift) {

    for (int i = 0; i < arr.length; i++) {
      arr[i] = (float) (arr[i] * Math.sin(0.2f + (i + shift) / 5)
          * Math.cos(0.2f + (i + shift) / 5) * Math.cos(0.4f + (i + shift) / 2));
    }
  }

}
