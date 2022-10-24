package com.redis.connect.gemfire.producer;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.client.ClientRegionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import static org.apache.geode.cache.client.ClientRegionShortcut.PROXY;

@Command(name = "com.redis.connect.gemfire.producer.GemfireProducer", usageHelpAutoWidth = true, description = "Gemfire producer load generator.")
public class GemfireProducer implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(GemfireProducer.class.getName());
    @Option(names = {"-h", "--host"}, description = "Gemfire locator host (default: ${DEFAULT-VALUE})", defaultValue = "127.0.0.1")
    private static String host = null;
    @Option(names = {"-p", "--port"}, description = "Gemfire locator port (default: ${DEFAULT-VALUE})", defaultValue = "10334")
    private static int port;
    @Option(names = {"-i", "--iter"}, description = "Iterations to run (default: ${DEFAULT-VALUE})", defaultValue = "1")
    private static int iter;
    @Option(names = {"-r", "--region"}, description = "Name of the region (default: ${DEFAULT-VALUE})", defaultValue = "session")
    private static String regionName;
    @Option(names = {"-o", "--operation"}, description = "Name of the operation i.e. I (put), U (put) and D (remove) (default: ${DEFAULT-VALUE})", defaultValue = "I")
    private static char opType;
    @Option(names = "--help", usageHelp = true, description = "Show this help message and exit")
    private static boolean usageHelpRequested;

    protected ClientCacheFactory clientCacheFactory;
    protected ClientCache cache;
    protected ClientRegionFactory<String, String> regionFactory;
    protected Region<String, String> region;

    public GemfireProducer(String host, int port, int iter, String regionName, char opType, boolean usageHelpRequested) {
        GemfireProducer.host = host;
        GemfireProducer.port = port;
        GemfireProducer.iter = iter;
        GemfireProducer.regionName = regionName;
        GemfireProducer.opType = opType;
        GemfireProducer.usageHelpRequested = usageHelpRequested;
    }

    public static void main(String[] args) {
        new CommandLine(new GemfireProducer(host, port, iter, regionName, opType, usageHelpRequested)).execute(args);
    }

    public void run() {
        clientCacheFactory = new ClientCacheFactory().addPoolLocator(host, port);
        cache = clientCacheFactory.create();
        regionFactory = this.cache.createClientRegionFactory(PROXY);
        try {
            if (region == null) {
                region = regionFactory.create(regionName);
            }
        } catch (Exception e) {
            logger.error("Exception in creating region..{}", regionName, e);
        }

        for (int i = 1; i <= iter; i++) {
            try {
                switch (opType) {
                    case 'I':
                        region.put("Key" + i, "Value" + i);
                        logger.info("Adding Key: {} and Value: {} in region: {}", "Key" + i, "Value" + i, region);
                        break;
                    case 'U':
                        region.put("Key" + i, "UpdatedValue" + i);
                        logger.info("Updating Key: {} to Value: {} in region: {}", "Key" + i, "Value" + i, region);
                        break;
                    case 'D':
                        region.remove("Key" + i);
                        logger.info("Deleting Key: {} from region: {}", "Key" + i, region);
                        break;
                    default:
                        logger.info("Unknown operation type {}.", opType);
                }
                logger.info(String.valueOf(region.query("select * from /" + regionName)));
              //logger.info(String.valueOf(region.query("select * from /" + regionName + " alias where alias.Key=" + "Key"+i)));
            } catch (Exception e) {
                logger.error("Exception occurred: ", e);
            }
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            logger.error("InterruptedException..", e);
        }
    }
}