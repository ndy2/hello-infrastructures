CREATE KEYSPACE IF NOT EXISTS test
            WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};

CREATE TABLE IF NOT EXISTS test.members
(
    id   UUID PRIMARY KEY,
    name TEXT,
    age  INT
);
