gtfs-rt-dump
============

Command-line dumper for GTFS-realtime feeds.

```
$ java -jar gtfs-rt-dump.jar -h
Usage: gtfs-rt-dump [-f=<protobufPath> | -u=<protobufUrl>] [-hV]
                    [-H=<String=String>]... COMMAND
Parse and display the contents of a GTFS-realtime feed in a human-readable
format.
  -h, --help                Show this help message and exit.
  -H, --header=<String=String>
                            Add specified HTTP header to request.
  -V, --version             Print version information and exit.
Input can be read from a file or URL. If neither are specified, standard input
will be used.
  -f, --file=<protobufPath> Read GTFS-rt from specified file.
  -u, --url=<protobufUrl>   Read GTFS-rt from specified URL.
Commands:
  pbtext  Protocol Buffer text format output
  table   Formatted table output
  json    JSON output
  csv     CSV output
```

If formatted table output is selected, the timestamp format can be specified:

```
$ java -jar gtfs-rt-dump.jar help table
Usage: gtfs-rt-dump table [--timestamp-format=<timestampFormatter>]
Formatted table output
      --timestamp-format=<timestampFormatter>
         Valid values: POSIX, ISO_8601_LOCAL, ISO_8601_UTC
```

For CSV output, a single feed component must be specified for extraction:

```
% java -jar gtfs-rt-dump.jar help csv  
Usage: gtfs-rt-dump csv <selectedFeedPart>
CSV output
      <selectedFeedPart>   Feed part to extract to CSV. Valid values:
                             FEED_HEADER, TRIP_UPDATES, VEHICLE_POSITIONS,
                             ALERTS

```

Sample usage:

```
$ java -jar gtfs-rt-dump.jar -u http://api.bart.gov/gtfsrt/tripupdate.aspx pbtext
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

The JSON output mode can be combined with [jq](https://stedolan.github.io/jq/) to slice and filter the resulting output:

```
$ java -jar target/gtfs-rt-dump.jar -u http://api.bart.gov/gtfsrt/tripupdate.aspx json | jq '[.entity[].tripUpdate | .trip.tripId as $tripId | .stopTimeUpdate[] | {tripId: $tripId, stopId: .stopId, arrival: .arrival.time|todate}] | map(select(.stopId == "EMBR")) | sort_by(.arrival) | .[0:3]'
[
  {
    "tripId": "4531853SAT",
    "stopId": "EMBR",
    "arrival": "2020-01-19T03:12:04Z"
  },
  {
    "tripId": "1071858SAT",
    "stopId": "EMBR",
    "arrival": "2020-01-19T03:16:42Z"
  },
  {
    "tripId": "3611847SAT",
    "stopId": "EMBR",
    "arrival": "2020-01-19T03:19:57Z"
  }
]
```