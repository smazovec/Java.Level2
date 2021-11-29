package courses;

import courses.equipments.Treadmill;
import courses.equipments.Wall;
import teams.Team;
import teams.Teammate;

public class Course {

  private final Equipment[] equipment = new Equipment[2];

  public Course() {
    equipment[0] = new Treadmill(500);
    equipment[1] = new Wall(240);
  }

  public void dolt(Team team) {
    System.out.println("Полосу проходит команда: " + team.getName());

    for (int eqIndex = 0; eqIndex < equipment.length; eqIndex++) {
      Equipment currentEquipment = equipment[eqIndex];
      System.out.println("  Дисциплина " + currentEquipment.getInfo());
      while (team.nextBattling()) {
        Teammate currentTeammate = team.getBattlingTeammate();
        team.setResult(currentTeammate, currentEquipment, currentEquipment.dolt(currentTeammate));
      }
      team.setNextReady(currentEquipment);
    }
  }
}
