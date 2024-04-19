package hello.haha

import org.apache.zookeeper.CreateMode
import org.apache.zookeeper.ZooDefs
import org.apache.zookeeper.ZooKeeper
import org.apache.zookeeper.server.DumbWatcher

fun main() {
    val connectString = "localhost:2181"
    val path = "/services/hello"
    val data = "zookeeper".toByteArray()

    ZooKeeper(connectString, 3000, DumbWatcher()).use { zookeeper ->
        try {
            // `stat /services/hello`
            if (zookeeper.exists(path, false) == null) {

                // `create /services/hello "zookeeper" world:anyone:crwad`
                zookeeper.create(path, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT)
                println("Add data to $path")
            } else {
                println("Data already exists!")
            }
        } finally {
            // `delete -v -1 /services/hello`
            zookeeper.delete(path, -1)
        }
    }
}
