package com.ly.traffic.middleplatform.apt;

/**
 * @author liugw
 * @Package com.ly.traffic.middleplatform.infrastructure.apt
 * @Description: ${TODO}
 * @date 2020/9/1 15:45
 */
public class ProcessingException extends RuntimeException {
    public ProcessingException(Object classElement, String format, Object ... args) {
        new RuntimeException(String.format(format, args));
    }

    public static void main(String[] args) {
//        throw new ProcessingException(null, "hello %s", "ww");

        String ww = String.format("hello %s", "ww");
        System.out.println(ww);
    }

}
