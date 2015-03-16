package zeroalloc;

import org.junit.Assert;
import org.junit.Test;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Class to demonstrate zero garbage creation.
 * Run with -verbose:gc -Xmx4G
 */
public class CreateOnHeapTest {
    private static final int ITERATIONS = 10_000_000;

    @Test
    public void testOnHeapMap() {
        System.out.println("----- HASHMAP ------------------------");
        Map<Integer, BondVOImpl> map = new ConcurrentHashMap<>(ITERATIONS);
        long actualQuantity = 0;
        long expectedQuantity = 0;
        long time = System.currentTimeMillis();

        System.out.println("*** Entering critical section ***");

        for (int i = 0; i < ITERATIONS; i++) {
            BondVOImpl bondVo = new BondVOImpl();
            bondVo.setQuantity(i);
            map.put(Integer.valueOf(i), bondVo);
            expectedQuantity += i;
        }


        long putTime = System.currentTimeMillis() - time;
        time = System.currentTimeMillis();
        System.out.println("************* STARTING GET *********************");
        for (int i = 0; i < map.size(); i++) {
            actualQuantity += map.get(i).getQuantity();
        }

        System.out.println("*** Exiting critical section ***");

        System.out.println("Time for putting " + putTime);
        System.out.println("Time for getting " + (System.currentTimeMillis() - time));

        Assert.assertEquals(expectedQuantity, actualQuantity);

        printMemUsage();
    }

    public static void printMemUsage() {
        System.gc();
        System.gc();
        System.out.println("Memory(heap) used " + humanReadableByteCount(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory(), true));
    }

    public static String humanReadableByteCount(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

}
