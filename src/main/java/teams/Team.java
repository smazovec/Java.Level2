package teams;

import courses.Equipment;
import teams.teammates.Cat;
import teams.teammates.Man;
import teams.teammates.Robot;

public class Team {

  private final String name;
  private final Teammate[] teammates = new Teammate[4]; // Полный состав команды
  private final TeamResultRecord[] teamResult = new TeamResultRecord[4 * 2]; // Резултаты команды
  private Teammate[] nextBattling; // Успешно прошедшие испытание, проходят следующее
  private int currentBattlingIndex = -1;

  public Team(String name) {
    this.name = name;

    teammates[0] = new Cat("Барсик", 90, 500);
    teammates[1] = new Man("Петя", 300, 5000);
    teammates[2] = new Man("Коля", 220, 15000);
    teammates[3] = new Robot("R2D2", 10, 10000);

    nextBattling = teammates.clone();
  }

  public String getName() {
    return this.name;
  }

  public void setNextReady(Equipment equipment) {
    currentBattlingIndex = 0;
    Teammate[] tmp = nextBattling.clone();
    nextBattling = new Teammate[getCountTeamResult(equipment)];
    for (int i = 0; i < tmp.length; i++) {
      if (getTeamResult(equipment, tmp[i]).getResult()) {
        nextBattling[currentBattlingIndex] = tmp[i];
        currentBattlingIndex++;
      }
    }
    currentBattlingIndex = -1;
  }

  public boolean nextBattling() {
    currentBattlingIndex++;
    return currentBattlingIndex < nextBattling.length;
  }

  public Teammate getBattlingTeammate() {
    return nextBattling[currentBattlingIndex];
  }

  public void setResult(Teammate teammate, Equipment equipment, boolean result) {
    teamResult[TeamResultRecord.getNextResultIndex()] = new TeamResultRecord(teammate, equipment,
        result);
  }

  public void getAllTeamResult() {
    System.out.println();
    System.out.println("Результаты команды " + getName() + ":");
    for (int i = 0; i < teamResult.length; i++) {
      if (teamResult[i] != null) {
        System.out.println("  Участник: " + teamResult[i].getTeammate().getName()
            + ", дисциплина: " + teamResult[i].getEquipment().getInfo()
            + ", результат: " + teamResult[i].getResult());
      }
    }
  }

  public int getCountTeamResult(Equipment equipment) {
    int countTeamResult = 0;
    for (int i = 0; i < teamResult.length; i++) {
      if (teamResult[i] != null
          && teamResult[i].getEquipment() == equipment && teamResult[i].getResult()) {
        countTeamResult++;
      }
    }
    return countTeamResult;
  }

  public TeamResultRecord getTeamResult(Equipment equipment, Teammate teammate) {
    for (int i = 0; i < teamResult.length; i++) {
      if (teamResult[i].getEquipment() == equipment
          && teamResult[i].getTeammate() == teammate) {
        return teamResult[i];
      }
    }
    return new TeamResultRecord(teammate, equipment, false);
  }
}

class TeamResultRecord {

  private static int resultIndex = 0;

  private final Teammate teammate;
  private final Equipment equipment;
  private final boolean result;

  public TeamResultRecord(Teammate teammate, Equipment equipment, boolean result) {
    this.teammate = teammate;
    this.equipment = equipment;
    this.result = result;

    resultIndex++;
  }

  public Teammate getTeammate() {
    return teammate;
  }

  public Equipment getEquipment() {
    return equipment;
  }

  public boolean getResult() {
    return result;
  }

  static int getNextResultIndex() {
    return resultIndex;
  }

}
