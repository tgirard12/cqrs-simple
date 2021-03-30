package com.tgirard12.cqrssimple.spring

import com.tgirard12.cqrssimple.*
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.annotation.Bean
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.beans
import org.springframework.web.servlet.function.router


@SpringBootApplication
class CqrsApplication {

    @Bean
    fun router(cqrs: Cqrs) = router {
        GET("/bikes") { ok().body(cqrs.query(BikeListQuery("ASC"))) }
        PUT("/bike") { ok().body(cqrs.command(BikeCreateCommand("John"))) }
    }
}

val bean = beans {
    bean<CqrsImpl>()

    bean {
        CommandBusImpl().apply {
            register(commandHandler)
        }
    }
    bean {
        QueryBusImpl().apply {
            register(queryHandler)
        }
    }
    bean<MiddlewareBusImpl>()
    bean<EventBusImpl>()
}

class BeansInitializer : ApplicationContextInitializer<GenericApplicationContext> {
    override fun initialize(applicationContext: GenericApplicationContext) {
        bean.initialize(applicationContext)
    }
}

fun main(args: Array<String>) {
    runApplication<CqrsApplication>(*args)
}
