class Assignment4 {
  public static void main (String [ ] args) {
     display(new Cat().Baz());
  }
}

class Dog {
  int f;
  int g;
  int h;
    public int f() {
    	int[] a;
	a = new int[5];
	return a.length;
    }

  public int Foo (int i, int j, int k, int l, int m) {
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
	m = 100;
	return i + j + k + a + b + l - m + this.f();
  }
}

class Horse extends Dog {
	public int f() {
	  return 10; 
	}
	public int Foobar() {
          return 77;
	}
}

class Cat {
	Dog Dog;
	public int Baz() {
	    int[] a;
		Dog d;
		a = new int[10];
		display(a.length);
		a[5] = 1;
		display(a[5]);
		d = new Dog();
		Dog = new Horse();
		display(d.f());
		display(Dog.Foo(1, 2, 3, 4, 5));
		return 1; 
	}
}
