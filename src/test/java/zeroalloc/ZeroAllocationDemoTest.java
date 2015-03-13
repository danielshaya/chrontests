package zeroalloc;

import net.openhft.chronicle.map.ChronicleMap;
import net.openhft.chronicle.map.ChronicleMapBuilder;
import net.openhft.lang.values.IntValue;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Class to demonstrate zero garbage creation.
 * Run with -verbose:gc
 * To run in JFR use these options for best results
 * -XX:+UnlockCommercialFeatures -XX:+FlightRecorder
 */
public class ZeroAllocationDemoTest {
    private static final int ITERATIONS = 10_000_000;

    @Test
    public void demoChronicleMap() throws IOException, InterruptedException {
        System.out.println("----- CHRONICLE MAP ------------------------");
        File file = new File("/tmp/chronicle-map-" + System.nanoTime() + ".map");
        file.deleteOnExit();

        ChronicleMapBuilder<IntValue, BondVOInterface> builder =
                ChronicleMapBuilder.of(IntValue.class, BondVOInterface.class)
                        .entries(ITERATIONS);

        try (ChronicleMap<IntValue, BondVOInterface> map =
                     builder.create()) {
            final BondVOInterface value = map.newValueInstance();
            final IntValue key = map.newKeyInstance();
            long actualQuantity = 0;
            long expectedQuantity = 0;

            long time = System.currentTimeMillis();

            System.out.println("*** Entering critical section ***");

            for (int i = 0; i < ITERATIONS; i++) {
                value.setQuantity(i);
                key.setValue(i);
                map.put(key, value);
                expectedQuantity += i;
            }

            long putTime = (System.currentTimeMillis()-time);
            time = System.currentTimeMillis();

            for (int i = 0; i < ITERATIONS; i++) {
                key.setValue(i);
                actualQuantity += map.getUsing(key, value).getQuantity();
            }

            System.out.println("*** Exiting critical section ***");

            System.out.println("Time for putting " + putTime);
            System.out.println("Time for getting " + (System.currentTimeMillis()-time));

            Assert.assertEquals(expectedQuantity, actualQuantity);
            printMemUsage();

        } finally {
            file.delete();
        }
    }

    @Test
    public void testOnHeapMap(){
        System.out.println("----- HASHMAP ------------------------");
        Map<Integer, BondVOImpl> map = new HashMap<>(ITERATIONS);
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


        long putTime = System.currentTimeMillis()-time;
        time = System.currentTimeMillis();

        for (int i = 0; i < map.size(); i++) {
            actualQuantity += map.get(i).getQuantity();
        }

        System.out.println("*** Exiting critical section ***");

        System.out.println("Time for putting " + putTime);
        System.out.println("Time for getting " + (System.currentTimeMillis()-time));

        Assert.assertEquals(expectedQuantity, actualQuantity);

        printMemUsage();
    }

    public static void printMemUsage(){
        System.gc();
        System.gc();
        System.out.println("Memory(heap) used " + humanReadableByteCount(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory(), true));
    }

    public static String humanReadableByteCount(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }


}
