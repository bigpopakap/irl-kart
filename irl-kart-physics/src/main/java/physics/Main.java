package physics;

import rx.Observable;

/**
 * Created by bigpopakap on 10/29/15.
 */
public class Main {

    public static void main(String[] args) {
        Observable.from(new String[] { "K", "A", "P", "I", "L"}).forEach((s) -> {
            System.out.println(s);
        });
    }

}