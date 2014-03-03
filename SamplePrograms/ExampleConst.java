class Assignment4 {
  public static void main (String [ ] args) {
     display(new Cat().Baz());
  }
}

class Dog {
  int f;
  int g;
  int h;
  public int Foo () {
	int a;
	int b;
	int c;
	int d;
	a = 1;
	b = 2;
	c = 3;
	d = 4;
	f = 5;
	g = 6;
	h = 7;
    return a + b + c + d+ f + g+ h;
  }
}

class Cat {
	Dog m;
	public int Baz() {
		Dog d;
		d = new Dog();
		m = new Dog();
		display(d.Foo());
		display(m.Foo());
		return 1; 
	}
}