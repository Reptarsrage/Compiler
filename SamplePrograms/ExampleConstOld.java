class ExampleConst {
  public static void main(String[] a) {
	// display(6);
	// display(1 + 5);
	// display(7 - 1);
	// display(2 * 3);
	// display(18 / 3);
	// display(27 % 7);
	// display(23 > 8);
	// display(8 < 23);
	// display(8 <= 8);
	// display(9 >= 9);
	// display(10 == 10);
	// display(11 != 10);
	// display(!(12 == 11));
	// display((12 == 12) && (5 > 2));
	// display((5 != 4) || (6 != 6));
	// display((5 <= 4) || (6 == 6));
	display(Methods.test());
	display(Methods.test1(2));
	display(Methods.test2(4,5));
	display(Methods.test3(7,8,9));
	display(Methods.test4(11,12,13,14));
	display(Methods.test5(16,17,18,19,20));
	display(Methods.test6(22,23,24,25,26,27));
	// display(Methods.test7(2,3,4,5,6,7,8));
  }
}

 class Methods {
  public int test(){
	return 1;
  }
  
  public int test1(int a){
	display(a);
	return 3;
  }
  
    public int test2(int a, int b){
	display(a);
	display(b);
	return 6;
  }
  
    public int test3(int a, int b, int c){
	display(a);
	display(b);
	display(c);
	return 10;
  }
  
    public int test4(int a, int b, int c, int d){
	display(a);
	display(b);
	display(c);
	display(d);
	return 15;
  }
  
    public int test5(int a, int b, int c, int d, int e){
	display(a);
	display(b);
	display(c);
	display(d);
	display(e);
	return 21;
  }
  
    public int test6(int a, int b, int c, int d, int e, int f){
	display(a);
	display(b);
	display(c);
	display(d);
	display(e);
	display(f);
	return 28;
  }
  
    // public int test7(int a, int b, int c, int d, int e, int f, int g){
	// display(a);
	// display(b);
	// display(c);
	// display(d);
	// display(e);
	// display(f);
	// display(g);
	// return 1;
  // }
}