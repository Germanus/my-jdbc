package my.ilya;

import java.text.MessageFormat;

public class Test {
    public static void main(String[] args){
        String s = "fef {0}";
        System.out.println(String.format(s, new Object[]{"5"}));
        System.out.println(MessageFormat.format(s, "5"));

    }
}
