package courses.equipments;

import courses.Equipment;
import teams.Jumping;
import teams.Teammate;

public class Wall implements Equipment {

  private int height;

  public Wall(int height) {
    this.height = height;
  }

  public boolean dolt(Teammate jumper) {

    if (jumper instanceof Jumping) {
      return ((Jumping) jumper).jump(this);
    }

    return false;
  }

  @Override
  public String getInfo() {
    return "прыжок на высоту " + height + " см.";
  }

  @Override
  public int getValue() {
    return height;
  }
}
