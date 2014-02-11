//
// Test basics of code generation
//
class Assignment5 {

  public static void main (String [ ] args) {

      System.out.println (256);
      System.out.println ((0-16)*(0-16));
      System.out.println (999);

      System.out.println (20);
      System.out.println ((100/(10/2)));
      System.out.println (999);

      System.out.println (32);
      System.out.println (92%60);
      System.out.println (999);

      System.out.println (0 - 2);
      System.out.println (2 / (0 - 1));
      System.out.println (999);

      System.out.println (1);
      System.out.println (4 > 3);  // eventually won't type check
      System.out.println (999);

      System.out.println (1);
      if (true) {
        System.out.println (1);
      } else {
        System.out.println (0);
      }
      System.out.println (999);

      System.out.println (1);
      if (4 >= 3) {
        System.out.println (1);
      } else {
        System.out.println (0);
      }
      System.out.println (999);

      System.out.println (1);
      if (4 < 3 || 1 < 2) {
        System.out.println (1);
      } else {
        System.out.println (0);
      }
      System.out.println (999);

      System.out.println (1);
      if (4 > 3 && 1 < 2 && 17 != 16) {
        System.out.println (1);
      } else {
        System.out.println (0);
      }
      System.out.println (999);

      System.out.println (9);
      if (4 < 3 && (1 / 0) == 1) {
        System.out.println (0);
      } else {
        System.out.println (9);
      }
      System.out.println (999);

      System.out.println (8);
      if (4 > 3 || (1 % 0) == 1) {
        System.out.println (8);
      } else {
        System.out.println (0);
      }
      System.out.println (999);

      System.out.println (3);
      // Ignore allocation semantics of new, call three(1, 2, 3);
      System.out.println (new Dog().three(1, 2, 3));
      System.out.println (999);

      System.out.println (12);
      System.out.println (new Dog().three(3, 4, 5) + new Dog().three(10, 11, 12) * new Dog().three(10, 11, 12));
      System.out.println (999);

    }

}

class Dog {
  public int three (int a, int b, int c) {
    return 3;
  }
}
