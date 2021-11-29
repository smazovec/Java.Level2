package courses.equipments;

import courses.Equipment;
import teams.Running;
import teams.Teammate;

public class Treadmill implements Equipment {

  private int length;

  public Treadmill(int length) {
    this.length = length;
  }

  public boolean dolt(Teammate runner) {

    if (runner instanceof Running) {
      return ((Running) runner).run(this);
    }

    return false;
  }

  @Override
  public String getInfo() {
    return "бег на дистанцию " + length + " м.";
  }

  @Override
  public int getValue() {
    return length;
  }
}
