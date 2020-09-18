package idv.codievilky.august.common.Season;

/**
 * @auther Codievilky August
 * @since 2020/9/18
 */
public enum Season {
  Q1(1, 3, 31),
  Q2(2, 6, 30),
  Q3(3, 9, 30),
  Q4(4, 12, 31);


  Season(int id, int month, int day) {
    this.id = id;
    this.month = month;
    this.day = day;
    this.date = String.format("%02d-%02d", month, day);
  }

  public final int id;
  public final int month;
  public final int day;
  public final String date;

  public static Season of(int id) {
    return valueOf("Q" + id);
  }

  public static Season of(String name) {
    return valueOf(name);
  }
}
