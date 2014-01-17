// test doubles being assigned to variables

class cse401c_correct_10 {
	public static void main(String[] args) {
		double d1 = 2e+01;
		double d2 = 0.003;
		double d3 = .004D;
		double d4 = 34e10;
		TestDoubleSimple test = new TestDoubleSimple();
		test.test(d1, d4);
		System.out.println(d3 * d1);
		System.out.println(d1 / d1);
		System.out.println(3. * d2);
		System.out.println(d3 * 3 * d4 + d2 - d1 % d3);
	}
}

class TestDoubleSimple {
	public void test(double d1, double d2) {
		System.out.println(d1 / d2);
	}
}
