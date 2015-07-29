package amalloy;

public class Dummy {
  public static class Holder {
    public String s;
    public Holder(String s) {
      this.s = s;
    }
  }

  public Holder h;

  public Dummy(Holder h) {
    this.h = h;
  }
}
