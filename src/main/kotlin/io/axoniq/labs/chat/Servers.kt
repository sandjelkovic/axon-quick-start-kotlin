package io.axoniq.labs.chat

import java.lang.reflect.Method

object Servers {  // Object instead of simple Main method just because of `classLoader.loadClass("org.jgroups.stack.GossipRouter")`
    @JvmStatic
    fun main(args: Array<String>) {
        val server: org.h2.tools.Server = org.h2.tools.Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9092")
        server.start()
        println("Database running on port 9092")
        var startMethod: Method? = null
        var gossipRouter: Any? = null
        var stopMethod: Method? = null
        try {
            val gossipRouterClass = Servers::class.java.classLoader.loadClass("org.jgroups.stack.GossipRouter")
            val constructor =
                gossipRouterClass.getDeclaredConstructor(String::class.java, Int::class.javaPrimitiveType)
            gossipRouter = constructor.newInstance("127.0.0.1", 12001)
            startMethod = gossipRouterClass.getMethod("start")
            stopMethod = gossipRouterClass.getMethod("stop")
        } catch (e: ClassNotFoundException) {
            // Gossip Router not on class path
        }
        if (startMethod != null) {
            startMethod.invoke(gossipRouter)
            println("Gossip Router started on port 12001")
        }
        println("Press any key to shut down")
        System.`in`.read()
        println("Stopping database.")
        server.stop()
        if (stopMethod != null) {
            println("Stopping Gossip Router")
            stopMethod.invoke(gossipRouter)
        }
    }
}
