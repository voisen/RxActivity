package com.voisen.rxactivity;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public class RxObserve<T> {
    private static final String TAG = "RxActivity";
    protected List<Func> mapFuncList = new ArrayList<>();
    private ObserveResult observeResult = null;
    private boolean observeCompleted = false;
    private boolean isFinalObserve = false;
    protected RxListener<T> customer;

    private interface ObserveResult{
        void execResult();
    }

    public interface Func<T,T1>{
        T1 apply(T value);
    }

    public static<T> RxObserve<T> create(){
        return new RxObserve<>();
    }

    public synchronized void error(Throwable e){
        if (observeCompleted){
            Log.e(TAG, "error: Observe has be completed !");
            return;
        }
        this.observeResult = ()->{
            if (this.customer != null) {
                this.customer.onError(e);
            }
        };
        this.observeResult.execResult();
        observeCompleted = true;
    }

    public void canceled(){
        if (this.observeCompleted){
            Log.e(TAG, "error: Observe has be completed !");
            return;
        }
        this.observeResult = () -> {
            if (this.customer != null) {
                this.customer.onCanceled();
            }
        };
        this.observeResult.execResult();
        this.observeCompleted = true;
    }

    @SuppressWarnings("unchecked")
    public void onResult(T data){
        if (this.observeCompleted){
            Log.e(TAG, "error: Observe has be completed !");
            return;
        }
        if (this.customer != null) {
            this.customer.onResult((T) getMapValue(data));
        }
    }

    public synchronized void then(RxListener<T> customer){
        this.customer = customer;
        this.isFinalObserve = true;
        if (this.observeResult != null) {
            this.observeResult.execResult();
        }
    }

    public static<T> RxObserve<T> returnError(Throwable throwable){
        RxObserve<T> observe = new RxObserve<>();
        observe.error(throwable);
        return observe;
    }

    @SuppressWarnings("unchecked")
    public<T1> RxObserve<T1> map(Func<T,T1> func){
        if (this.isFinalObserve){
            throw new RuntimeException("Observe has be final status !");
        }
        if (func != null) {
            mapFuncList.add(func);
        }
        return (RxObserve<T1>) this;
    }

    @SuppressWarnings("unchecked")
    public Object getMapValue(Object data){
        if (this.mapFuncList.size() == 0){
            return data;
        }
        Object dat = data;
        for (Func<Object, Object> func : this.mapFuncList) {
            dat = func.apply(dat);
        }
        return dat;
    }

}
