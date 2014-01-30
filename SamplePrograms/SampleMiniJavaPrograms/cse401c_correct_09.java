// test the arithmatic of doubles

class cse401c_correct_09 {
	public static void main(String[] args) {
            System.out.println(new TestDoubleArithmetic().test());
	}
}

class TestDoubleArithmetic {
	public double test() {
		System.out.println(0.5 + 5e-1);
		System.out.println(2e+4 - 19999D);
		System.out.println(4.00d * .25);
		System.out.println(4D / 4e00);
		System.out.println(5.D % 4E00);
                return (1.0 / 2.0);
	}
}
