package com.redis.connect.domain;

import java.util.*;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.client.ClientRegionShortcut;

public class EmployeeLoader {
    private final Region<EmployeeKey, EmployeeData> region;

    public EmployeeLoader(Region<EmployeeKey, EmployeeData> region) {
        this.region = region;
    }

    public static void main(String[] args) {
        // connect to the locator using default port 10334
        ClientCache cache = new ClientCacheFactory().addPoolLocator("redis-connect.demo-rlec.redislabs.com", 10334)
                .set("log-level", "DEBUG").create();

        // create a local region that matches the server region
        Region<EmployeeKey, EmployeeData> region =
                cache.<EmployeeKey, EmployeeData>createClientRegionFactory(ClientRegionShortcut.PROXY)
                        .create("employee-region");

        EmployeeLoader employee = new EmployeeLoader(region);

        employee.insertValues(new String[]{"Allen Terleto", "Virag Tripathi", "Kyle Banker", "John Burke",
                "Julien Ruaux", "Steve Lorello", "Prasanna Rajagopal", "Pat Pearson", "Ricky Reliable",
                "Taylor Tack", "Zelda Zankowski"});
        //employee.insertValues(new String[]{"Tim Cook"});
        employee.printValues(employee.getValues());

        cache.close();
    }

    Set<EmployeeKey> getValues() {
        return new HashSet<>(region.keySetOnServer());
    }

    void insertValues(String[] names) {
        Random r = new Random();
        Arrays.stream(names).forEach(name -> {
            EmployeeKey key = new EmployeeKey(name, 1 + r.nextInt(1000000));
            EmployeeData val = new EmployeeData(key, 50000 + r.nextInt(100000), 40);
            region.put(key, val);
        });
    }

    void printValues(Set<EmployeeKey> values) {
        values.forEach(key -> System.out.println(key.getName() + " -> " + region.get(key)));
    }
}