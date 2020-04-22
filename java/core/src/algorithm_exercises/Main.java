package algorithm_exercises;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        List<Number> mynumbers = new ArrayList<>(Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12,13,14));
        List<Product> allProds = getProducts(mynumbers);
        allProds.forEach(System.out::println);
    }


    public static List<Product> getProducts(List<Number> numbers) {

        List<Months> monthList = Arrays.asList(Months.values());

        List<Product> prodList = new ArrayList<>();




        for (int n = 0; n < numbers.size(); n++) {

            for (int i = 0; i < monthList.size(); i++) {
                if (n > monthList.size()) i = 0;
                prodList.add(new Product().setEntry(monthList.get(i), numbers.get(n)));
            }
        }

        return prodList;
    }

}

class Product {
    HashMap<Months, Number> prodMap = new HashMap<Months, Number>();

    public Product setEntry(Months month, Number number){
        this.prodMap.put(month, number);
        return this;
    }

    @Override
    public String toString() {
        return Arrays.toString(prodMap.entrySet().toArray());
    }
}


enum Months {
    JAN,
    FEB,
    MAR,
    APR,
    MAY,
    JUN,
    JUL,
    AUG,
    SEP,
    OCT,
    NOV,
    DEC
}






