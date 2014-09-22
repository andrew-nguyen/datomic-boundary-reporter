| Metric                  | Units | Statistic     | Description                                                    |
|-------------------------|-------|---------------|----------------------------------------------------------------|
| Alarm                   | count | sum           | a problem has occurred (see alarms table below)                |
| AvailableMB             | MB    | min           | unused RAM on transactor                                       |
| ClusterCreateFS         | msec  | max           | time to create a "file system" in the storage                  |
| CreateEntireIndexMsec   | msec  | max           | time to create index                                           |
| CreateFulltextIndexMsec | msec  | max           | time to create fulltext portion of index                       |
| Datoms                  | count | max           | number of unique datoms in the index                           |
| HeartbeatMsec           | count | sum           | number of heartbeats written by active transactor              |
| HeartMonitorMsec        | count | sum           | number of heartbeats observed by standby transactor            |
| IndexDatoms             | count | max           | number of datoms stored by the index, all sorts                |
| IndexSegments           | count | max           | total number of segments in the index                          |
| IndexWrites             | count | sum           | number of segments written by current indexing job             |
| LogIngestBytes          | bytes | max           | in-memory size of log when a database size                     |
| LogIngestMsec           | msec  | max           | time to play log back when a database start                    |
| Memcache                | count | avg           | memcache hit ratio, from 0 (no hits) to 1 (all hits)           |
| Memcache                | count | sum           | total number of requests to memcache                           |
| MemoryIndexMB           | MB    | max           | MB of RAM consumed by memory index                             |
| ObjectCache             | count | avg           | object cache hit ratio, from 0 (not hits) to 1 (all hits)      |
| ObjectCache             | count | sum           | total number of requests to object cache                       |
| RemotePeers             | count | max           | number of remote peers connected                               |
| StorageBackoff          | msec  | max           | worst case time spent in backoff/retry around calls to storage |
| StorageBackoff          | msec  | samples       | number of times a storage operation was retried                |
| StorageGetBytes         | bytes | sum           | throughput of storage operations                               |
| StoragePutBytes         | bytes | sum           | throughput of storage operations                               |
| StorageGetBytes         | bytes | samples       | count of storage operations                                    |
| StoragePutBytes         | bytes | samples       | count of storage operations                                    |
| StorageGetMsec          | msec  | min           | distribution of storage latencies                              |
| StorageGetMsec          | msec  | avg           | distribution of storage latencies                              |
| StorageGetMsec          | msec  | max           | distribution of storage latencies                              |
| StoragePutMsec          | msec  | min           | distribution of storage latencies                              |
| StoragePutMsec          | msec  | avg           | distribution of storage latencies                              |
| StoragePutMsec          | msec  | max           | distribution of storage latencies                              |
| TransactionBytes        | bytes | sum           | volume of transaction data to log, peers                       |
| TransactionBytes        | bytes | samples       | number of transactions                                         |
| TransactionBytes        | bytes | min           | distribution of transaction sizes                              |
| TransactionBytes        | bytes | avg           | distribution of transaction sizes                              |
| TransactionBytes        | bytes | max           | distribution of transaction sizes                              |
| TransactionMsec         | msec  | min           | distribution of transaction latencies                          |
| TransactionMsec         | msec  | avg           | distribution of transaction latencies                          |
| TransactionMsec         | msec  | max           | distribution of transaction latencies                          |
