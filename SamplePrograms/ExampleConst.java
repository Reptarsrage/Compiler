class Assignment4 {
  public static void main (String [ ] args) {
     display(new Dog().Foo());
     display(new Dog().Foo());
  }
}

class Dog {
  int i;
  public int Foo() {
    i = 0;
    while (i < 10) {
      i = i + 1;
    }
    return 999;
  }
}
