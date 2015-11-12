package irl.util.reactiveio;

import rx.Observable;
import rx.Observer;
import rx.subjects.ReplaySubject;
import rx.subjects.Subject;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/2/15
 */
public class Pipe<T> {

    private final Subject<T, T> pipe;

    public Pipe() {
        pipe = ReplaySubject.<T>create().toSerialized();

        //make sure the pipe never completes
        mergeIn(Observable.<T>never());
    }

    public Observable<T> get() {
        return pipe;
    }

    @SafeVarargs
    public final void mergeIn(T... ts) {
        mergeIn(Observable.from(ts));
    }

    public void mergeIn(Observable<? extends T> observable) {
        observable.subscribe(new Observer<T>() {
            @Override
            public void onCompleted() {
                //do nothing... this pipe never completes
            }

            @Override
            public void onError(Throwable e) {
                pipe.onError(e);
            }

            @Override
            public void onNext(T t) {
                pipe.onNext(t);
            }
        });
    }

}
