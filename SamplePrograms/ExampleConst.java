// test doubles being assigned to variables

class cse401c_correct_10 {
	public static void main(String[] args) {
            System.out.println(new TestDoubleSimple().test(50.0, 500, 5000.0) + 1000000.0);
	}
}

class TestDoubleSimple {
    public double test(double d6, int i, double d7) {
            double d1;
            double d2;
            double d3;
            double d4;
	    double d5;
	    double[] darr;
	    int[] iarr;
	    d1 = 2e+01;
            d2 = 0.003;
            d3 = .004D;
            d4 = 34e10;
	    System.out.println(d1); //20.0
	    System.out.println(d2); //0.003
	    System.out.println(d3); //0.004
	    System.out.println(d4); //3.4e11
	    if (d5 <= d6) {
		System.out.println(d6); // 50.0
	    } else {}
	    if (i >= i) {
		System.out.println(i); //500
	    } else {}
	    if (100.0 != 99.0) {
		System.out.println(d7); //5000.0
	    } else {}
	    d1 = 1.0;
	    d2 = 2.0;
	    d3 = 3.0;
	    d4 = 4.0;
	    d5 = 5.0;
            System.out.println(d1 + d2); //3.0
            System.out.println(d3 - d2); //1.0
            System.out.println(d4 * d5); //20.0
	    System.out.println(d4 / d2); //2.0
	    System.out.println(100); //100
	    darr = new double[5];
	    darr[0] = d1;
	    iarr = new int[5];
	    iarr[0] = 1;
	    System.out.println(iarr[0] / iarr[0]); //1
	    System.out.println(darr[0] / darr[0]); //1.0
	    //darr[1] = d2;
	    //darr[2] = d3;
	    //darr[3] = d4;
	    //darr[4] = d5;
	    System.out.println(d1); //1.0
	    System.out.println(d2); //2.0
	    System.out.println(d3); //3.0
	    System.out.println(d4); //4.0
	    System.out.println(d5); //5.0
	    System.out.println(1.0/0.0); //NaN (Infinity?)
            return (d1 + d2 + d3 + d4); //10.0
	}
}
