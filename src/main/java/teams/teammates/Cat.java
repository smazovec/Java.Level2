package teams.teammates;

import courses.Equipment;
import teams.Jumping;
import teams.Running;

public class Cat implements Running, Jumping {

  private String name;
  private int jumpLimit;
  private int runLimit;

  public Cat(String name, int jumpLimit, int runLimit) {
    this.name = name;
    this.jumpLimit = jumpLimit;
    this.runLimit = runLimit;
  }

  @Override
  public boolean run(Equipment equipment) {
    if (equipment.getValue() < runLimit) {
      System.out.println("    Кот " + name + " успешно пробежал дистанцию");
      return true;
    } else {
      System.out.println("    Кот " + name + " не смог пробежать дистанцию");
      return false;
    }
  }

  @Override
  public boolean jump(Equipment equipment) {
    if (equipment.getValue() < jumpLimit) {
      System.out.println("    Кот " + name + " успешно перепрыгнул препятствие");
      return true;
    } else {
      System.out.println("    Кот " + name + " не смог перепрыгнуть препятствие");
      return false;
    }
  }

  @Override
  public String getName() {
    return name;
  }
}
