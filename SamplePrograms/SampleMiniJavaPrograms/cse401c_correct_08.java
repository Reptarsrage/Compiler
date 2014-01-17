// test the basic funtionality of doubles

class cse401c_correct_08 {
	public static void main(String[] args) {
		TestDoubleSimple test = new TestDoubleSimple();
		test.test();
	}
}

class TestDoubleSimple {
	public void  test() {
		System.out.println(1.0);
		System.out.println(1e00);
		System.out.println(10e-1);
		System.out.println(1e+00);
		System.out.println(.1);
		System.out.println(1.);
		System.out.println(1d);
		System.out.println(1D);
		System.out.println(1.01d);
		System.out.println(1E000004);
	}
}
