package hello.haha

import hello.haha.watchers.HealthHandler
import org.apache.zookeeper.AddWatchMode
import org.apache.zookeeper.CreateMode
import org.apache.zookeeper.ZooDefs
import org.apache.zookeeper.ZooKeeper
import org.apache.zookeeper.server.DumbWatcher

fun main() {
    val connectString = "localhost:2181"
    val path = "/services/hello"

    ZooKeeper(connectString, 3000, HealthHandler).use { zk ->
        if (zk.exists("/services/hello", false) == null) {
            zk.create("/services", null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.CONTAINER)
            zk.create("/services/hello", null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.CONTAINER)
        }
        zk.addWatch(path, HealthHandler, AddWatchMode.PERSISTENT_RECURSIVE)

        if (zk.exists("${path}/${CreateMode.PERSISTENT}", false) != null) {
            zk.delete("${path}/${CreateMode.PERSISTENT}", -1)
        }
        zk.create(
            "${path}/${CreateMode.PERSISTENT}",
            "PERSISTENT".toByteArray(),
            ZooDefs.Ids.OPEN_ACL_UNSAFE,
            CreateMode.PERSISTENT
        )
        zk.create(
            "${path}/${CreateMode.EPHEMERAL}",
            "EPHEMERAL".toByteArray(),
            ZooDefs.Ids.OPEN_ACL_UNSAFE,
            CreateMode.EPHEMERAL
        )
    }

    ZooKeeper(connectString, 3000, DumbWatcher()).use { zk ->
        print(zk.exists("${path}/${CreateMode.PERSISTENT}", false))
        print(zk.exists("${path}/${CreateMode.EPHEMERAL}", false))
    }
}
