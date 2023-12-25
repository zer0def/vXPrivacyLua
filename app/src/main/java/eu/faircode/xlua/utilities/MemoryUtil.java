package eu.faircode.xlua.utilities;

import android.util.Log;

import java.util.Random;

public class MemoryUtil {
    private static final String TAG = "XLua.MemoryUtil";

    private static final Random random = new Random();

    public static final String MEMINFO = "MemTotal:        %s kB\n" +
            "MemFree:          %s kB\n" +
            "MemAvailable:    %s kB\n" +
            "Buffers:            %s kB\n" +
            "Cached:          %s kB\n" +
            "SwapCached:         %s kB\n" +
            "Active:          %s kB\n" +
            "Inactive:         %s kB\n" +
            "Active(anon):    %s kB\n" +
            "Inactive(anon):    %s kB\n" +
            "Active(file):    %s kB\n" +
            "Inactive(file):   %s kB\n" +
            "Unevictable:      %s kB\n" +
            "Mlocked:          %s kB\n" +
            "SwapTotal:       %s kB\n" +
            "SwapFree:        %s kB\n" +
            "Dirty:            %s kB\n" +
            "Writeback:         %s kB\n" +
            "AnonPages:       %s kB\n" +
            "Mapped:           %s kB\n" +
            "Shmem:             %s kB\n" +
            "KReclaimable:     %s kB\n" +
            "Slab:             %s kB\n" +
            "SReclaimable:      %s kB\n" +
            "SUnreclaim:       %s kB\n" +
            "KernelStack:       %s kB\n" +
            "PageTables:       %s kB\n" +
            "NFS_Unstable:          %s kB\n" +
            "Bounce:                %s kB\n" +
            "WritebackTmp:          %s kB\n" +
            "CommitLimit:    %s kB\n" +
            "Committed_AS:   %s kB\n" +
            "VmallocTotal:   %s kB\n" +
            "VmallocUsed:       %s kB\n" +
            "VmallocChunk:          %s kB\n" +
            "CmaTotal:         %s kB\n" +
            "CmaFree:            %s kB\n";

    public static String generateFakeMeminfoContents(int totalGb, int availGb) {
        return String.format(MEMINFO,
                gigabytesToKilobytes(totalGb),                // MemTotal
                randomLong(10300, 500300),                    // MemFree
                gigabytesToKilobytes(availGb),                // MemAvailable
                randomLong(500, 3000),                        // Buffers
                randomLong(900000, 7000000),                  // Cached
                randomLong(5000, 15000),                      // SwapCached
                randomLong(2000000, 5000000),                 // Active
                randomLong(500000, 1000000),                  // Inactive
                randomLong(1000000, 3000000),                 // Active(anon)
                randomLong(0, 100),                           // Inactive(anon)
                randomLong(1000000, 3000000),                 // Active(file)
                randomLong(500000, 1000000),                  // Inactive(file)
                randomLong(200000, 400000),                   // Unevictable
                randomLong(200000, 400000),                   // Mlocked
                randomLong(4000000, 8000000),                 // SwapTotal
                randomLong(2000000, 6000000),                 // SwapFree
                randomLong(100000, 300000),                   // Dirty
                randomLong(0, 100),                           // Writeback
                randomLong(1000000, 3000000),                 // AnonPages
                randomLong(500000, 1500000),                  // Mapped
                randomLong(10000, 30000),                     // Shmem
                randomLong(200000, 500000),                   // KReclaimable
                randomLong(300000, 700000),                   // Slab
                randomLong(50000, 150000),                    // SReclaimable
                randomLong(300000, 500000),                   // SUnreclaim
                randomLong(40000, 100000),                    // KernelStack
                randomLong(80000, 150000),                    // PageTables
                randomLong(0, 100),                           // NFS_Unstable
                randomLong(0, 100),                           // Bounce
                randomLong(0, 100),                           // WritebackTmp
                randomLong(5000000, 15000000),                // CommitLimit
                randomLong(10000000, 20000000),               // Committed_AS
                randomLong(200000000, 300000000),             // VmallocTotal
                randomLong(50000, 100000),                    // VmallocUsed
                randomLong(0, 100),                           // VmallocChunk
                randomLong(300000, 400000),                   // CmaTotal
                randomLong(5000, 10000)                       // CmaFree
        );
    }

    public static long randomLong(long min, long max) {
        return min + (long) (random.nextDouble() * (max - min + 1));
    }

    public static long gigabytesToKilobytes(int gigabytes) {
        return gigabytes * 1024L * 1024L;
    }

}
