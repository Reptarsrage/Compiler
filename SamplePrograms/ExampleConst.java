// test doubles being assigned to variables

class cse401c_correct_10 {
	public static void main(String[] args) {
            System.out.println(new TestDoubleSimple().test());
	}
}

class TestDoubleSimple {
	public double test() {
            double d1;
            double d2;
            double d3;
            double d4;
	    double d5;
	    d1 = 1.0;
	    d2 = 2.0;
	    d3 = 3.0;
	    d4 = 4.0;
	    d5 = 5.0;
	    //d1 = 2e+01;
            //d2 = 0.003;
            //d3 = .004D;
            //d4 = 34e10;
            System.out.println(d1 + d2);
            System.out.println(d3 - d2);
            System.out.println(d4 * d5);
	    System.out.println(d4 / d2);
	    System.out.println(100);
            //System.out.println(d1 / d2);
            return (d1 + d2 + d3 + d4);
	}
}
