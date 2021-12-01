public class Lesson2 {

  public static void main(String[] args) {
    final String[][] convertibleArray = {
        {"1", "2", "3", "4"},
        {"1", "2", "3", "4"},
        {"1", "F", "3", "4"},
        {"1", "2", "3", "4"}};
    final ArrayService arrayService = new ArrayService();

    try {
      arrayService.sumSquareArray(convertibleArray);
    } catch (MyArraySizeException e) {
      System.out.println("Возникла ошибка размерности массива:");
      System.out.println(e.getMessage());
    } catch (MyArrayDataException e) {
      System.out.println("Возникла ошибка преобразования значения массива:");
      System.out.println(e.getMessage());
    }
  }

}
