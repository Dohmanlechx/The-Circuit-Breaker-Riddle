import java.util.*

fun main() {
    print("Minimum trips: ${TheCircuitBreakerRiddle(amountOfLamps = 100).solve()}")
}

data class Socket(
    val id: String,
    var label: String = "0",
    var isOn: Boolean = false
)

class TheCircuitBreakerRiddle(val amountOfLamps: Int) {
    private val lamps = mutableListOf<Socket>()
    private val breakers = mutableListOf<Socket>()

    init {
        generateBreakersAndLamps()
    }

    fun solve(): Int {
        var trips = 0

        while (lamps.map(Socket::label).toSet().size < amountOfLamps) {
            for (group in breakers.groupBy(Socket::label)) {
                val breakersTurnedOn = group.value.filter(Socket::isOn)
                val breakersTurnedOff = group.value.filterNot(Socket::isOn)

                for (breaker in breakersTurnedOn.take(breakersTurnedOn.size / 2)) {
                    breaker.toggle(toOn = false)
                }

                for (breaker in breakersTurnedOff.take(breakersTurnedOff.size / 2)) {
                    breaker.toggle(toOn = true)
                }

                markAllBreakers()
            }
            markAllLamps()
            trips++
        }

        return trips
    }

    private fun generateBreakersAndLamps() {
        for (i in 0 until amountOfLamps) {
            UUID.randomUUID().toString().let { id ->
                lamps.add(Socket(id))
                breakers.add(Socket(id))
            }
        }
    }

    private fun markAllBreakers() {
        for (breaker in breakers) breaker.mark()
    }

    private fun markAllLamps() {
        for (lamp in lamps) lamp.mark()
    }

    private fun Socket.toggle(toOn: Boolean) {
        this.isOn = toOn
        lamps.find { it.id == this.id }!!.isOn = toOn
    }

    private fun Socket.mark() {
        label += if (isOn) "1" else "0"
    }
}