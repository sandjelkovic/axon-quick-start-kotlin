package io.axoniq.labs.chat

import org.h2.tools.Server
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@SpringBootTest
@ExtendWith(SpringExtension::class)
internal class ChatScalingOutApplicationTests {
    @Test
    fun contextLoads() {
    }

    companion object {
        private var server: Server? = null

        @BeforeAll
        fun beforeClass() {
            server = Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9092")
            server!!.start()
        }

        @AfterAll
        fun afterClass() {
            if (server != null) {
                server!!.stop()
            }
        }
    }
}
