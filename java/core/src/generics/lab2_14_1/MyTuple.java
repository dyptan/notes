package src.generics.lab2_14_1;

/**
 * Created by diptan on 15.06.18.
 */
public class MyTuple<A,B,C> {
    A avar;
    B bvar;
    C cvar;

    public MyTuple(){}

    public MyTuple(A avar, B bvar, C cvar) {
        this.avar = avar;
        this.bvar = bvar;
        this.cvar = cvar;
    }

    public A getAvar() {
        return avar;
    }

    public B getBvar() {
        return bvar;
    }

    public C getCvar() {
        return cvar;
    }

}

