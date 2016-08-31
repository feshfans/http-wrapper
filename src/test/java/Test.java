/**
 * Created by Administrator on 2016/8/11.
 */
public class Test {

    private static final  String test ="aa";

    private static void setStr(){
        System.out.println("test");
    }
    private static class InnerTest{
        static String str;
        static {
            setStr();
            str=new String("testing");
        }
    }
}
