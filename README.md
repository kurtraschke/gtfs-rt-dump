gtfs-rt-dump
============

Command-line dumper for GTFS-realtime feeds.

```
$ java -jar gtfs-rt-dump.jar -h
usage: gtfs-rt-dump [-h] (-u URL | -f FILE)

optional arguments:
  -h, --help             show this help message and exit
  -u URL, --url URL      URL containing GTFS-realtime feed to parse and display
  -f FILE, --file FILE   File containing GTFS-realtime feed to parse and display (default: -)
```

Sample usage:

```
$ java -jar gtfs-rt-dump.jar -u http://api.bart.gov/gtfsrt/tripupdate.aspx
header {
  gtfs_realtime_version: "1.0"
  incrementality: FULL_DATASET
  timestamp: 1418867234
}
entity {
  id: "46DC10"
  trip_update {
    trip {
      trip_id: "46DC10"
    }
```
