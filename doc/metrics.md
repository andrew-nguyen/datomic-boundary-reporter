| Metric | Units | Statistic | Description |
|--------|-------|-----------|-------------|
| Alarm | count | sum | a problem has occurred (see alarms table below) |
| AvailableMB | MB | minimum | unused RAM on transactor |
| ClusterCreateFS | msec | maximum | time to create a "file system" in the storage |
| CreateEntireIndexMsec | msec | maximum | time to create index |
| CreateFulltextIndexMsec | msec | maximum | time to create fulltext portion of index |
| Datoms | count | maximum | number of unique datoms in the index |
| HeartbeatMsec | count | sum | number of heartbeats written by active transactor |
| HeartMonitorMsec | count | sum | number of heartbeats observed by standby transactor |
| IndexDatoms | count | maximum | number of datoms stored by the index, all sorts |
| IndexSegments | count | maximum | total number of segments in the index |
| IndexWrites | count | sum | number of segments written by current indexing job |
| LogIngestBytes | bytes | maximum | in-memory size of log when a database size |
| LogIngestMsec | msec | maximum | time to play log back when a database start |
| Memcache | count | average | memcache hit ratio, from 0 (no hits) to 1 (all hits) |
| Memcache | count | sum | total number of requests to memcache |
| MemoryIndexMB | MB | maximum | MB of RAM consumed by memory index |
| ObjectCache | count | average | object cache hit ratio, from 0 (not hits) to 1 (all hits) |
| ObjectCache | count | sum | total number of requests to object cache |
| RemotePeers | count | maximum | number of remote peers connected |
| StorageBackoff | msec | maximum | worst case time spent in backoff/retry around calls to storage |
| StorageBackoff | msec | samples | number of times a storage operation was retried |
| Storage{Get,Put}Bytes | bytes | sum | throughput of storage operations |
| Storage{Get,Put}Bytes | bytes | samples | count of storage operations |
| Storage{Get,Put}Msec | msec | min, avg, max | distribution of storage latencies |
| TransactionBytes | bytes | sum | volume of transaction data to log, peers |
| TransactionBytes | bytes | samples | number of transactions |
| TransactionBytes | bytes | min, avg, max | distribution of transaction sizes |
| TransactionMsec | msec | min, avg, max | distribution of transaction latencies |
