package com.example

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.html.*
import io.ktor.jackson.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.*
import kotlinx.html.*
import org.jetbrains.exposed.sql.Database
import java.sql.DriverManager

val URL = "jdbc:postgresql://192.168.65.52:5000/event_db"
val DRIVER = "org.postgresql.Driver"
val USER = "postgres"
val PASS = "example"

fun Application.main() {
    install(DefaultHeaders)
    install(CallLogging)
    install(ContentNegotiation) {
        jackson {}
    }
    val dao = EventDAO(
        Database.connect(URL, driver = DRIVER, user = USER, password = PASS)
    )
    val conn = DriverManager.getConnection(URL, USER, PASS)

    println(dao.getAllEvents())

    routing {
        get("/") {
            call.respondHtml {
                head {
                    title { +"Ktor: Docker" }
                }
                body {
                    val runtime = Runtime.getRuntime()
                    h3 { +"Ktor Netty engine: Hello, ${System.getProperty("user.name")}!" }
                    p { +"CPUs: ${runtime.availableProcessors()}. Memory free/total/max: ${runtime.freeMemory()} / ${runtime.totalMemory()} / ${runtime.maxMemory()}." }
                    hr {}
                    h3 { +"REST API" }
                    p { a(href = "/events") { +"All events" } }
                }
            }
        }
        route("/events") {
            get() {
                call.respond(mapOf("events" to dao.getAllEvents()))
            }

            get("/joined") {
                call.respond(mapOf("events_joined" to dao.getAllEventsJoined()))
            }

            get("/raw") {
                val stmt = conn.createStatement()
                val res = stmt.executeQuery("SELECT * FROM bmn_event")
                val lstEvents = ArrayList<Event>()
                while (res.next()) {
                    lstEvents.add(
                        Event(
                            res.getInt("file_guid"),
                            res.getInt("event_number"),
                            res.getShort("software_id"),
                            res.getShort("period_number"),
                            res.getShort("run_number"),
                            res.getInt("track_number")
                        )
                    )
                }
                call.respond(mapOf("events_raw" to lstEvents))
            }

            // Example URL -- http://127.0.0.1:8080/events/4/10
            get("/{file_ptr}/{event_num}") {
                val file_ptr = call.parameters["file_ptr"]?.toInt()
                val event_num = call.parameters["event_num"]?.toInt()
                if (file_ptr != null && event_num != null) {
                    val resp = dao.getEvent(file_ptr = file_ptr, event_num = event_num)
                    if (resp != null) {
                        call.respond(resp)
                    } else {
                        call.respond("No such event found!")
                    }
                }
            }

            // Example URL -- http://127.0.0.1:8080/events/getevent?file_ptr=4&event_num=10
            get("/getevent") {
                val file_ptr = call.parameters["file_ptr"]?.toInt()
                val event_num = call.parameters["event_num"]?.toInt()
                if (file_ptr != null && event_num != null) {
                    val resp = dao.getEvent(file_ptr = file_ptr, event_num = event_num)
                    if (resp != null) {
                        call.respond(resp)
                    } else {
                        call.respond("No such event found!")
                    }
                } else {
                    call.respond("file_ptr and event_num are required here")
                }
            }

            // Example URL -- http://127.0.0.1:8080/events/search?period=7&run=5000
            get("/search") {
                val period = call.parameters["period"]?.toInt()
                val run = call.parameters["run"]?.toInt()
                if (period != null && run != null) {
                    call.respond(mapOf("events" to dao.searchEvents(period, run)))
                }
            }

            // **NB**: Content-Type is mandatory
            // POST http://127.0.0.1:8080/events/create
            // Content-Type: application/json
            //
            // { "file_ptr": 10, "event_num": 22, "period": 7, "run": 5000, "software_id": 1, "all_tracks": 55 }
            post("/create") {
                val event = call.receive<Event>()
                dao.createEvent(
                    event.file_guid,
                    event.event_number,
                    event.period_number,
                    event.run_number.toInt(),
                    event.software_id.toInt(),
                    event.track_number
                )
                call.respond("Event created!")
            }

            /*
             { "events": [
               { "file_ptr": 10, "event_num": 31, "period": 7, "run": 5000, "software_idr": 1, "all_tracks": 55 },
               { "file_ptr": 10, "event_num": 32, "period": 7, "run": 5000, "software_id": 1, "all_tracks": 55 }
             ] }
            */
            post("/create-multiple") {
                val events = call.receive<EventList>()
                events.events.forEach { event ->
                    dao.createEvent(
                        event.file_guid,
                        event.event_number,
                        event.period_number,
                        event.run_number.toInt(),
                        event.software_id.toInt(),
                        event.track_number
                    )
                }
                call.respond("${events.events.size} events created!")
            }

        }
    }
}

fun main(args: Array<String>) = io.ktor.server.netty.EngineMain.main(args)
