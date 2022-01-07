# redisconnect-gemfire-producer
Gemfire Producer for Redis Connect testing

## Download

Download the [latest release](https://github.com/redis-field-engineering/redisconnect-gemfire-producer/releases) and un-tar (e.g. tar -xvf redisconnect-gemfire-producer-1.0-SNAPSHOT.tar.gz) the redisconnect-gemfire-producer-1.0-SNAPSHOT.tar.gz archive.

## Usage

```bash
java -jar redisconnect-gemfire-producer-1.0-SNAPSHOT.jar --help
Usage: com.redis.connect.gemfire.producer.GemfireProducer [--help] [-h=<host>] [-i=<iter>] [-o=<opType>] [-p=<port>] [-r=<regionName>]
Gemfire producer load generator.
  -h, --host=<host>          Gemfire locator host (default: 127.0.0.1)
      --help                 Show this help message and exit
  -i, --iter=<iter>          Iterations to run (default: 1)
  -o, --operation=<opType>   Name of the operation i.e. I (put), U (put) and D (remove) (default: I)
  -p, --port=<port>          Gemfire locator port (default: 10334)
  -r, --region=<regionName>  Name of the region (default: session)
```