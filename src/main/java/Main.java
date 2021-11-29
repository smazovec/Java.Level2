/*
 * Sports competition system.
 *
 * Developed by Sergey Mazovets, Tomsk, november 2021
 */

import courses.Course;
import teams.Team;

public class Main {

  private static Course course;
  private static Team team;

  public static void main(String[] args) {
    course = new Course();
    team = new Team("Dream team");
    course.dolt(team);
    team.getAllTeamResult();
  }
}
