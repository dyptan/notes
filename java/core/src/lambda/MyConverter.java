package src.lambda;

public interface MyConverter {
    String convertStr(String s);
    static Boolean isNull (Object o){
        if (o == null) {
            return true;
        } else return false;
    };
}
