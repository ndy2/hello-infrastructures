package hello.haha

import hello.haha.watchers.HealthHandler
import org.apache.zookeeper.AddWatchMode
import org.apache.zookeeper.CreateMode
import org.apache.zookeeper.ZooDefs
import org.apache.zookeeper.ZooKeeper
import org.apache.zookeeper.data.Stat
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

        if (zk.exists("${path}/${CreateMode.PERSISTENT_WITH_TTL}", false) != null) {
            zk.delete("${path}/${CreateMode.PERSISTENT_WITH_TTL}", -1)
        }
        zk.create(
            "${path}/${CreateMode.PERSISTENT_WITH_TTL}",
            "PERSISTENT_WITH_TTL".toByteArray(),
            ZooDefs.Ids.OPEN_ACL_UNSAFE,
            CreateMode.PERSISTENT_WITH_TTL,
            Stat(),
            10000
        )
    }

    ZooKeeper(connectString, 3000, DumbWatcher()).use { zk ->
        print(zk.exists("${path}/${CreateMode.PERSISTENT_WITH_TTL}", false))
    }
    Thread.sleep(10000)

    println("TTL 을 주었다고 바로 지워지지 않는다.")
    ZooKeeper(connectString, 3000, DumbWatcher()).use { zk ->
        println("노드가 존재함")
        print(zk.exists("${path}/${CreateMode.PERSISTENT_WITH_TTL}", false))
    }
    println()

    ZooKeeper(connectString, 3000, DumbWatcher()).use { zk ->
        var count = 0
        while (zk.exists("${path}/${CreateMode.PERSISTENT_WITH_TTL}", false) != null) {
            println("PERSISTENT_WITH_TTL node exists after ${10 + count * 5}s ...")
            Thread.sleep(5000)
            count += 1
        }
    }
    println("이제서야 지워졌다.")
    println()
}
