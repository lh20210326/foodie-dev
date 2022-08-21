public class Demo {


    public Demo(){
        System.out.println("构造方法");
    }
    static {
        System.out.println("静态代码块");
    }
    {
        System.out.println("普通代码块");
    }
    public static void main(String[] args) {
        new Demo();
    }
}
