//
// a comment
class Assignment4 {

  public static void main (String [ ] args) {
      //    {
    System.out.println(1e10);
    System.out.println(1e-10);
    System.out.println(1e+10);
    System.out.println(1.e+10);
    System.out.println(1.1e+10);
    System.out.println(1.1);
    System.out.println(.1);
    System.out.println(1.);
    System.out.println(00000001.);
    System.out.println(new Worker  ( ) . foo ( ));
    System.out.println(10///
      / 5);
    System.out.println(10/ // 5
      2);
    //    }
  }

}

class Dog {
  int A;
  public int Foo () {
    return 3;
  }
}

class Husky extends Dog {
}

// another comment

class Worker {
  int d1;
  int d2;

  int [] B;

  public int foo() {
    Worker aBar;

    bool x;
    int y;
    double d;
    int[] A;
    double[] B;

    A = new int [ 1// +17];
      ];
    B = new double [ 11 % 1 ];
    y = A.length;
    x = A.length != A.length;
    x = 1 < 2;
    x = 1 < 2 || 3 < 4;
    x = 1 < 2 && 3 < 4;
    if (x) {
      y = 0;
      // y = 9223372036854775807;
      y = 2147483647;
    } else {
      y = 1;
      // y = -9223372036854775808;
      // y = 0 - 2147483648;  // can't handle this boundary condition
    }
    while (1 > 2) {
      System.out.println ( 9999);
    }
    return 0;
  }

  public int a_ () {
    int x;
    return 4;
  }

  public int b_ () {
    return 4;
  }

  public bool True () {
    return true;
  }

  public bool False () {
    return false;
  }

  public int[] V () {
   int[] A;
   int i;
   int s;

   s = 27;
   A = new int[s];
   i = 0;
   while (i < s) {
     A[i] = i;
     i = i + 1;
   }
   return A;
  }

  public int args(int a, double d, bool b, int[] A, int[] V) {
    return A[0];
  }

  public int Precedence () {
    System.out.println((((e))));
    System.out.println((1+2)*3);
    System.out.println(1 + (2*3));
    return 1;
  }

}
// another comment
