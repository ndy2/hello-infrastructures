# hello-zookeeper

![](zookeeper.png)

- https://zookeeper.apache.org/

## Requirements

- install zookeeper on localhost:2181
- see https://github.com/ndy2/helm-infrastructures/tree/main/helm-zookeeper#helm-install

## structure

```
├── README.md
├── build.gradle.kts
└── src
    └── main
        ├── kotlin
        │   ├── ZookeeperExample.kt             - zookeeper create/delete
        │   ├── ZookeeperNodeTypeExample1.kt    - zookeeper PERSISTENT/EPHEMERAL   node
        │   ├── ZookeeperNodeTypeExample2.kt    - zookeeper PERSISTENT_WITH_TTL    node
        │   ├── ZookeeperNodeTypeExample3.kt    - zookeeper (EPHEMERAL_)SEQUENTIAL node
        │   └── watchers
        │       └── HealthHandler.kt            - zookeeper Watcher
        └── resources
```
