import java.time.Duration;
import java.time.LocalDateTime;

public class Test_class {

    public static void main(String[] args) {
        LocalDateTime now = LocalDateTime.now();
        System.out.println();
        System.out.println("Current time:             " + now);
        System.out.println("Current time + 1 day:     " + now.plusDays(1));

        Duration d = Duration.between(now, now.plusDays(1));
        System.out.println("Time difference in days:  " + d.toDays());
        System.out.println("Time difference in hours: " + d.toHours());
        System.out.println("Time difference in mins:  " + d.toMinutes());
        System.out.println("Time difference in milli: " + d.toMillis());
    }
}
