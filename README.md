gtfs-rt-dump
============

Command-line dumper for GTFS-realtime feeds.

```
$ java -jar gtfs-rt-dump.jar -h
Usage: gtfs-rt-dump [-hV] [--disable-tls-validation] [-H=<String=String>]...
                    [-X=<enabledExtensions>]... [-f=<protobufPath> |
                    -u=<protobufUrl>] [-U=<username> -P=<password>] COMMAND
Parse and display the contents of a GTFS-realtime feed in a human-readable
format.
      --disable-tls-validation
                            Disable all TLS server certificate validation.
  -h, --help                Show this help message and exit.
  -H, --header=<String=String>
                            Add specified HTTP header to request.
  -V, --version             Print version information and exit.
  -X, --enable-extension=<enabledExtensions>
                            Enable specified GTFS-rt extensions. Valid values:
                              OBA, NYCT, LIRR, MNR, MTARR, MERCURY
Input can be read from a file or URL. If neither are specified, standard input
will be used.
  -f, --file=<protobufPath> Read GTFS-rt from specified file.
  -u, --url=<protobufUrl>   Read GTFS-rt from specified URL.
HTTP Basic authentication credentials can be specified if input is read from a
URL.
  -P, --password=<password> Password for HTTP Basic authentication
  -U, --username=<username> Username for HTTP Basic authentication
Commands:
  help    Displays help information about the specified command
  pbtext  Protocol Buffer text format output
  table   Formatted table output
  json    JSON output
  csv     CSV output

```

If pbtext or formatted table output is selected, the timestamp format can be specified:

```
$ java -jar gtfs-rt-dump.jar help table
Usage: gtfs-rt-dump table [--timestamp-format=<timestampFormatter>]
Formatted table output
      --timestamp-format=<timestampFormatter>
         Valid values: POSIX, ISO_8601_LOCAL, ISO_8601_UTC
```

```
Usage: gtfs-rt-dump pbtext [--timestamp-format[=<timestampFormatter>]]
Protocol Buffer text format output
      --timestamp-format[=<timestampFormatter>]
         Valid values: POSIX, ISO_8601_LOCAL, ISO_8601_UTC
```

To produce formatted timestamps in a timezone other than the OS timezone, set the `user.timezone` Java system property:

```
java -Duser.timezone="US/Pacific" -jar gtfs-rt-dump.jar -u http://api.bart.gov/gtfsrt/tripupdate.aspx pbtext --timestamp-format
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
$ java -jar gtfs-rt-dump.jar -u http://api.bart.gov/gtfsrt/tripupdate.aspx json | jq '[.entity[].tripUpdate | .trip.tripId as $tripId | .stopTimeUpdate[] | {tripId: $tripId, stopId: .stopId, arrival: .arrival.time|todate}] | map(select(.stopId == "EMBR")) | sort_by(.arrival) | .[0:3]'
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
