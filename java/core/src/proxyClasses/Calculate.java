package src.proxyClasses;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

public interface Calculate {
    int multiplication(int a, int b);
    int division(int a, int b);
}

class CalculateImpl implements Calculate{

    @Override
    public int multiplication(int a, int b) {
        return a*b;
    }

    @Override
    public int division(int a, int b) {
        return a/b;
    }
}

class CalculateProxy implements InvocationHandler {
    Object objCalc;

    public CalculateProxy(Object objCalc) {
        this.objCalc = objCalc;
    }

    public Object invoke(Object proxy, Method m, Object[] args){
        Object result = null;

        try {
            System.out.println("Method called: " + m.getName());
            result = m.invoke(objCalc, args);
            System.out.println("Arguments: "+Arrays.toString(args));
            System.out.println("Result: "+result);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException("unexpected invocation exception: " + e.getMessage());
        }

        return result;
    }

    public static Object newInstance(Object target){
        return Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                new CalculateProxy(target)
        );
    }
}

class Main{
    public static void main(String[] args) {
        Calculate myvar = (Calculate)CalculateProxy.newInstance(new CalculateImpl());
        myvar.multiplication(2,3);
        myvar.division(6,3);
    }
}