// test doubles being assigned to variables

class cse401c_correct_10 {
	public static void main(String[] args) {
            System.out.println(new TestDoubleSimple().test(50.0, 500, 5000.0) + 1000000.0); //1000010.0
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
		int junk;
		d1 = 1000e-03;
        d2 = 2d;
        d3 = 3D;
        d4 = 4.;
	    System.out.println(d1); //1.0
	    System.out.println(d2); //2.0
	    System.out.println(d3); //3.0
	    System.out.println(d4); //4.0
		d1 = .5 + 4.5;
        d2 = 6E-0;
        d3 = 7E+0;
        d4 = 8E0;
	    System.out.println(d1); //5.0
	    System.out.println(d2); //6.0
	    System.out.println(d3); //7.0
	    System.out.println(d4); //8.0	
	    d1 = 3e+01;
        d2 = 0.003;
        d3 = .003D;
        d4 = 33e10;
	    System.out.println(d1); //30.0
	    System.out.println(d2); //0.003
	    System.out.println(d3); //0.003
	    System.out.println(d4); //3.3e11
		
		
		
	    if (d5 <= d6) {
			System.out.println(d6); // 50.0
	    } else {}
	    if (i >= i) {
			System.out.println(i); //500
	    } else {}
	    if (100.0 != 99.0) {
			System.out.println(d7); //5000.0
	    } else {}
		if (100.0 >= d1) {
			System.out.println(d1); //30.0
	    } else {}
		if (d1 + 0.00001 > d1) {
			System.out.println(d1 + 1.0); //31.0
	    } else {}
		if (d1 - 0.00001 < d1) {
			System.out.println(d1 + 2.0); //32.0
	    } else {}
		if (d1 + 0.0001 == d1 + 0.0001) {
			System.out.println(d1 + 3.0); //33.0
	    } else {}
		if (30.0 == d1 && d1 == 30.0) {
			System.out.println(d1 + 4.0); //34.0
	    } else {}
		d2 = 30.0;
		if (30.0 == d1 && d2 == 30.0 && d1 == d2) {
			System.out.println(d1 + 5.0); //35.0
	    } else {}
		
		if (d1 == 30.0 && i > 0) {
			System.out.println(d1 + 6.0); //36.0
	    } else {}
		if (i != 1 || d1 == 30.0) {
			System.out.println(d1 + 7.0); //37.0
	    } else {}
		
	    d1 = 1.0;
	    d2 = 2.0;
	    d3 = 3.0;
	    d4 = 4.0;
	    d5 = 5.0;
        junk = this.printd(d1 + d2); //3.0
        junk = this.printd(d3 - d2); //1.0
        junk = this.printd(d4 * d5); //20.0
	    junk = this.printd(d4 / d2); //2.0
	    junk = this.printi(100); //100
	    darr = new double[5];
	    darr[0] = d1;
	    iarr = new int[5];
	    iarr[0] = 1;
	    junk = this.printd(d1); // 1
		junk = this.printd(darr[0]); // 1
		junk = this.printd(0.0 + darr[0]); // 1
	    junk = this.printi(iarr[0] / iarr[0]); //1
	    junk = this.printd(darr[0] / darr[0]); //1.0
		junk = this.printd(darr[0] - darr[0]); //0.0
		junk = this.printd(darr[0] * darr[0]); //1.0
		junk = this.printd(darr[0] + darr[0]); //2.0
		junk = this.printd(darr[0]); // 1
		
	    //darr[1] = d2;
	    //darr[2] = d3;
	    //darr[3] = d4;
	    //darr[4] = d5;
	    junk = this.printd(d1); //1.0
	    junk = this.printd(d2); //2.0
	    junk = this.printd(d3); //3.0
	    junk = this.printd(d4); //4.0
	    junk = this.printd(d5); //5.0
	    junk = this.printd(1.0/0.0); //NaN (Infinity?)
		//darr[0] = 0.0;
		darr[1] = 2.0;
		darr[2] = 3.0;
		darr[3] = 4.0;
		darr[4] = 5.0;
		iarr[0] = 1;
		iarr[1] = 2;
		if (iarr[0] < iarr[1]){
			junk = this.printi(1); //1
	    } else {}
		if (iarr[1] > iarr[0]){
			junk = this.printi(2); //2
	    } else {}
		if (iarr[0] == iarr[0]){
			junk = this.printi(3); //3
	    } else {}
		if (iarr[1] != iarr[0]){
			junk = this.printi(4); //4
	    } else {}
		
		
		if (darr[1] > 0.0) {
			junk = this.printd(1.0); //1
	    } else {}
		if (0.0 < darr[1]) {
			junk = this.printd(2.0); //2
	    } else {}
		if (darr[1] > darr[0]) {
			junk = this.printd(3.0); //3
	    } else {}
		if (darr[1] / 2.0 == darr[0]) {
			junk = this.printd(4.0); //4
	    } else {}
		if (darr[2] / 3.0 == darr[0] && darr[3] < darr[4] && darr[1+1] == darr[2]) {
			junk = this.printd(5.0); //5
	    } else {}
		
		iarr[2] = iarr[0];
		junk = this.printi(iarr[2]); // 1
		d2 = 2.0;
		darr[2] = 2.0;
		junk = this.printd(darr[2]); // 2.0
		darr[2] = d2;
		junk = this.printd(darr[2]); // 2.0
		darr[2] = darr[1];
		junk = this.printd(darr[2]); // 2.0
		
        return (d1 + d2 + d3 + d4); //10.0
	}
	
	
	public int printi(int i) {
		System.out.println(i);
		return 0;
	}
	
	public int printd(double d) {
		System.out.println(d);
		return 0;
	}
}
