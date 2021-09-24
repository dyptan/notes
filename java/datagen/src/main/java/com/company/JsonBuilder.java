package com.company;

import net.andreinc.mockneat.MockNeat;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Iterator;
import java.util.Random;



public class JsonBuilder {
    public static void main(String[] args) throws IOException {
        FileWriter fileWriter = new FileWriter(args[1]);

        int iter = Integer.valueOf(args[0]);
        Iterator<Integer> random = new Random().ints(0, iter).iterator();
        Iterator<Integer> years = new Random().ints(2000, 2010).iterator();
        MockNeat mockNeat = MockNeat.threadLocal();
        for (int i = 0; i < iter ; i++) {
            String  man = "{\"firstName\":\"" + mockNeat.names().first().valStr() +
                    "\",\"lastName\":\"" + mockNeat.names().last().valStr() +
                    "\",\"id\":\""+ random.next() +
                    "\",\"ts1\":\""+ mockNeat.ints().range(1000000000, 1500000000).valStr() + "\"," +
                    "\"ts2\":"+ mockNeat.ints().range(1000000000, 1500000000).valStr() + "," +
                    "\"zoneddate\":{" +
                    "\"$date\":\""+ OffsetDateTime.of(LocalDateTime.of(years.next(),1, 1,0,0,0), ZoneOffset.UTC).toString() + "\"}," +
                    "\"localdate\":\""+ mockNeat.localDates().between(LocalDate.of(2000,1, 1),LocalDate.of(2015,1, 1)).valStr() +
                    "\"}\n";
//            System.out.println(man);
            fileWriter.write(man);
        }

        fileWriter.flush();
        fileWriter.close();
    }
}
