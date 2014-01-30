// test inheritance
class cse401c_correct_05 {
    public static void main(String[] args) {
        System.out.println(new TestInheritanceSimple().test());
    }
}

class TestInheritanceSimple {
    public int test() {
        Person p;
        boolean b;
        int i;
        p  = new Male();
        b = p.setAge(21);
        b = p.setIsMale(true);
        i = p.getAge();
        b = p.getIsMale();
        return 21;
    }
}

class Person {
    int age;
    boolean isMale;

    public boolean setAge(int a) {
        age = a;
        return true;
    }
    
    public boolean setIsMale(boolean iM) {
        isMale = iM;
        return true;
    }

    public int getAge() {
        return age;
    }

    public boolean getIsMale() {
        return isMale;
    }
}

class Male extends Person {
    //    public Male(int a) {
    //  age = a;
    //  isMale = true;
    //}
}