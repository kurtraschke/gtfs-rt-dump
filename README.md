gtfs-rt-dump
============

Command-line dumper for GTFS-realtime feeds.

Use as follows:

$ java -jar gtfs-rt-dump-1.0-SNAPSHOT-withAllDependencies.jar -u http://api.bart.gov/gtfsrt/tripupdate.aspx
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

