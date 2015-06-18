#Test of the Multi-Version Concurrency Control (MVCC) on H2 database

Activated by default in 1.4.x

> The MVCC mode is enabled by default in version 1.4.x, with the default MVStore storage engine. MVCC is disabled by default when using the PageStore storage engine (which is the default in version 1.3.x). The following applies when using the PageStore storage engine: The MVCC feature is not fully tested yet. The limitations of the MVCC mode are: with the PageStore storage engine, it can not be used at the same time as MULTI_THREADED=TRUE; the complete undo log (the list of uncommitted changes) must fit in memory when using multi-version concurrency. The setting MAX_MEMORY_UNDO has no effect. Clustering / High Availability
> 
> -- [http://www.h2database.com/html/advanced.html]