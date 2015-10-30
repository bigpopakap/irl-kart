package irl.fw.physics;

import rx.Observable;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 10/29/15
 */
public class Main {

    public static void main(String[] args) {
        Observable.from(new String[] { "K", "A", "P", "I", "L"}).forEach((s) -> {
            System.out.println(s);
        });
    }

}