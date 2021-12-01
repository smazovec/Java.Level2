public class ArrayService {

  private int sum;

  public void sumSquareArray(String[][] squareArray) throws IllegalArgumentException {

    // 1. Напишите метод, на вход которого подаётся двумерный строковый массив размером 4х4,
    // при подаче массива другого размера необходимо бросить исключение MyArraySizeException.
    if (squareArray.length != 4) {
      throw new MyArraySizeException("Размерность массива должна быть = 4*4");
    }

    for (int i = 0; i < squareArray.length; i++) {
      if (squareArray[i].length != 4) {
        throw new MyArraySizeException("Размерность массива должна быть = 4*4");
      }

      // 2. Далее метод должен пройтись по всем элементам массива, преобразовать в int, и просуммировать.
      // Если в каком-то элементе массива преобразование не удалось (например, в ячейке лежит символ или текст вместо числа),
      // должно быть брошено исключение MyArrayDataException, с детализацией в какой именно ячейке лежат неверные данные.

      for (int j = 0; j < squareArray[i].length; j++) {
        try {
          sum += Integer.valueOf(squareArray[i][j]);
        } catch (NumberFormatException e) {
          throw new MyArrayDataException("Ошибка преобразования в число значения \""
              + squareArray[i][j] + "\" в ячейке [" + i + "][" + j + "]");
        }
      }
    }
    System.out.println("Сумма массива = " + sum);
  }
}
