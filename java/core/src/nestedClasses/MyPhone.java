package src.nestedClasses;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by diptan on 15.06.18.
 */

//MyPhoneBook staticObj = new MyPhoneBook();

public class MyPhone{
    MyPhoneBook phoneBook;

    public MyPhone() {
        phoneBook = new MyPhoneBook();
    }

    public void switchOn (){
        System.out.println("Loading PhoneBook records…");
        phoneBook.addPhoneNumber("Ivan", "063324465");
        phoneBook.addPhoneNumber("Pavlo", "098235981");
        phoneBook.addPhoneNumber("Anton", "099235981");
        //phoneBook.sortByPhoneNumber();
        System.out.println("OK");
    }

    public void call(int callee){
        System.out.println(String.format("calling...%s %s",phoneBook.phoneNumbers.get(callee).getName(), phoneBook.phoneNumbers.get(callee).getPhone()));
    }

    public class MyPhoneBook {
        private ArrayList<PhoneNumber> phoneNumbers = new ArrayList<>();

        public class PhoneNumber {
            private String name;
            private String phone;

            public PhoneNumber(String inname, String inphone) {
                name = inname;
                phone = inphone;
            }

            public String getName() {
                return name;
            }

            public String getPhone() {
                return phone;
            }

        }

        public void addPhoneNumber(String name, String phone) {
            phoneNumbers.add(new MyPhoneBook.PhoneNumber(name, phone));
        }

        public void printPhoneBook() {
            for (PhoneNumber element : phoneNumbers) {
                System.out.println(element.getName() + " " + element.getPhone());
            }
        }

        public void sortByName() {
            phoneNumbers.sort(new Comparator<PhoneNumber>() {
                @Override
                public int compare(PhoneNumber o1, PhoneNumber o2) {
                    return o1.name.charAt(0) - o2.name.charAt(0);
                }
            });
        }

        public void sortByPhoneNumber() {
            phoneNumbers.sort(new Comparator<PhoneNumber>() {
                @Override
                public int compare(PhoneNumber o1, PhoneNumber o2) {
                    return Integer.parseInt(o1.getPhone()) - Integer.parseInt(o2.getPhone());
                }
            });

        }
}
}
