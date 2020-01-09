import java.time.Duration;
import java.time.LocalDateTime;

public class Test_class {

    public static void main(String[] args) {
        LocalDateTime now = LocalDateTime.now();
        System.out.println(now);
        System.out.println(now.plusDays(1));

        Duration d = Duration.between(now, now.plusDays(1));
        System.out.println(d.toDays());
    }
}
