import inter.ThreeLettersOnA;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Task5 {

  static List<String> list = new ArrayList<>();

  public static void run() {
    System.out.println();
    System.out.println("********** Task 5 **********");

    list.add("Аня");
    list.add("лодка");
    list.add("акт");
    list.add("астра");
    list.add("ага");
    list.add("быстро");
    list.add("ара");
    list.add("ай");

    System.out.println(list);

    ThreeLettersOnA threeLettersOnA = l ->
        new ArrayList<>(l.stream()
            .filter(s -> s.startsWith("а"))
            .filter(s -> s.length() == 3)
            .collect(Collectors.toList()));

    System.out.println(threeLettersOnA.search(list));

  }

}
