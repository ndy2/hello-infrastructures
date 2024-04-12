package hello.haha

import hello.haha.watchers.HealthHandler
import org.apache.zookeeper.AddWatchMode
import org.apache.zookeeper.CreateMode
import org.apache.zookeeper.ZooDefs
import org.apache.zookeeper.ZooKeeper

fun main() {
    val connectString = "localhost:2181"
    val path = "/services/hello"

    ZooKeeper(connectString, 3000, HealthHandler).use { zk ->
        if (zk.exists("/services/hello", false) == null) {
            zk.create("/services", null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.CONTAINER)
            zk.create("/services/hello", null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.CONTAINER)
        }
        zk.addWatch(path, HealthHandler, AddWatchMode.PERSISTENT_RECURSIVE)

        for (i in 1..100) {
            zk.create(
                "${path}/${CreateMode.EPHEMERAL_SEQUENTIAL}",
                "EPHEMERAL_SEQUENTIAL".toByteArray(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL_SEQUENTIAL
            )
        }

        val children = zk.getChildren(path, false)
        children.sort()
        println("children.size = ${children.size}")
        println("children = $children")
    }
}
