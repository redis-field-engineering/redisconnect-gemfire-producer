package com.redis.connect.gemfire.producer;

import java.util.List;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.client.ClientRegionFactory;
import org.apache.geode.cache.query.FunctionDomainException;
import org.apache.geode.cache.query.NameResolutionException;
import org.apache.geode.cache.query.QueryInvocationTargetException;
import org.apache.geode.cache.query.TypeMismatchException;
import org.apache.geode.cache.query.SelectResults;
import static org.apache.geode.cache.client.ClientRegionShortcut.PROXY;

public class GemfireLoader {
    protected static ClientCacheFactory clientCacheFactory;
    protected static ClientCache cache;
    protected static ClientRegionFactory<String, String> regionFactory;
    protected static Region<String, String> region;
    public static int recordCount;
    public static List<String> records;

    public GemfireLoader(String host, int port, String regionName) {
        recordCount = GemfireLoader.getRecordCount(host, port, regionName);
        records = GemfireLoader.getRecords(host, port, regionName);
    }

    public static void main(String...args) {
        if (args[3].equalsIgnoreCase("count")) {
            GemfireLoader.getRecordCount(args[0], Integer.parseInt(args[1]), args[2]);
            System.out.println(GemfireLoader.recordCount);
        } else if (args[3].equalsIgnoreCase("select")) {
            GemfireLoader.getRecords(args[0], Integer.parseInt(args[1]), args[2]);
            System.out.println(GemfireLoader.records);
        } else if (args[4].equalsIgnoreCase("insert")) {
            GemfireLoader.insertRecord(args[0], Integer.parseInt(args[1]), args[2], Integer.parseInt(args[3]));
        } else if (args[4].equalsIgnoreCase("update")) {
            GemfireLoader.updateRecord(args[0], Integer.parseInt(args[1]), args[2], Integer.parseInt(args[3]));
        } else if (args[4].equalsIgnoreCase("delete")) {
            GemfireLoader.deleteRecord(args[0], Integer.parseInt(args[1]), args[2], Integer.parseInt(args[3]));
        }
    }

    private static Region<String, String> createRegion(String host, int port, String regionName) {
        clientCacheFactory = new ClientCacheFactory().addPoolLocator(host, port);
        cache = clientCacheFactory.create();
        regionFactory = cache.createClientRegionFactory(PROXY);
        try {
            if (region == null) {
                region = regionFactory.create(regionName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return region;
    }

    public static List<String> getRecords(String host, int port, String regionName) {
        Region<String, String> region = createRegion(host, port, regionName);
        try {
            SelectResults<String> results = region.query("SELECT * FROM /" + regionName);
            if (results != null)
                records = results.asList();
        } catch (FunctionDomainException | TypeMismatchException | NameResolutionException |
                 QueryInvocationTargetException e) {
            throw new RuntimeException(e);
        }
        return records;
    }

    public static int getRecordCount(String host, int port, String regionName) {
        Region<String, String> region = createRegion(host, port, regionName);
        try {
            SelectResults<Integer> results = region.query("SELECT COUNT(*) FROM /" + regionName + ".entries");
            if (results != null)
                recordCount = results.asList().get(0);
        } catch (FunctionDomainException | TypeMismatchException | NameResolutionException |
                 QueryInvocationTargetException e) {
            throw new RuntimeException(e);
        }
        return recordCount;
    }

    public static void insertRecord(String host, int port, String regionName, int rowCount) {
        Region<String, String> region = createRegion(host, port, regionName);
        for (int i = 1; i <= rowCount; i++) {
            region.put("Key" + i, "Value" + i);
        }
    }

    public static void updateRecord(String host, int port, String regionName, int rowCount) {
        Region<String, String> region = createRegion(host, port, regionName);
        for (int i = 1; i <= rowCount; i++) {
            region.put("Key" + i, "UpdatedValue" + i);
        }
    }

    public static void deleteRecord(String host, int port, String regionName, int rowCount) {
        Region<String, String> region = createRegion(host, port, regionName);
        for (int i = 1; i <= rowCount; i++) {
            region.remove("Key" + i);
        }
    }

}