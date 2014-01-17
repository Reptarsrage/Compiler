// test inheritance
class cse401c_correct_05 {
    public static void main(String[] args) {
        System.out.println(new TestInheritanceSimple().test());
    }
}

class TestInheritanceSimple {
    public int test() {
        Person p = new Male(21);
        if (p.getIsMale()) {
            System.out.println(1);
        }
        return p.getAge();
    }
}

class Person {
    int age;
    boolean isMale;

    public Person() {
        age = 0;
        isMale = false;
    }

    public Person(int a) {
        age = a;
        isMale = false;
    }
    
    public Person(int a, boolean m) {
        age = a;
        isMale = m;
    }

    public int getAge() {
        return age;
    }

    public boolean getIsMale() {
        return isMale;
    }
}

class Male extends Person {
    public Male(int a) {
        age = a;
        isMale = true;
    }
}