import com.mapr.db.MapRDB;
import com.mapr.db.Table;
import org.ojai.Document;
import org.ojai.DocumentStream;
import org.ojai.store.QueryCondition;
import org.apache.commons.lang3.time.StopWatch;
public class query {
    
    public static void main(String[] args) {
        Table table =  MapRDB.getTable("/user/mapr/92499");
        QueryCondition condition = MapRDB.newCondition().is("ts1", QueryCondition.Op.LESS_OR_EQUAL, "1200000000").build();
        int counter = 0;
        StopWatch stopWatch = StopWatch.createStarted();
        try (DocumentStream documentStream = table.find(condition)) {
            for (Document doc : documentStream) {
                counter++;
            }
        }
        long end = stopWatch.getTime();
        System.out.println("\n\nTotal documents : " + counter + " for " + condition + " Exectuted in:"+end+ " ms");
    }
}