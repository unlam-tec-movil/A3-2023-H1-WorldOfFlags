package ar.edu.unlam.mobile2.domain.movimiento
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DetectarMovimiento(context: Context) : SensorEventListener {

    private val sensorManager: SensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val gyroscope: Sensor =
        sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)

    private var lastUpdate: Long = 0
    private var initialRoll: Float = 0f
    private var isTiltedLeft: Boolean = false
    private var isTiltedRight: Boolean = false

    private val _tiltDirection: MutableStateFlow<TiltDirection> = MutableStateFlow(TiltDirection.NONE)
    val tiltDirection: StateFlow<TiltDirection> = _tiltDirection

    fun start() {
        sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL)
    }

    fun stop() {
        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onSensorChanged(event: SensorEvent) {
        val currentTime = System.currentTimeMillis()

        if ((currentTime - lastUpdate) > SHAKE_THRESHOLD) {
            val roll = event.values[2]

            if (lastUpdate == 0L) {
                initialRoll = roll
            }

            val rollDelta = roll - initialRoll

            if (rollDelta > TILT_THRESHOLD && !isTiltedRight) {
                isTiltedLeft = true
                _tiltDirection.value = TiltDirection.LEFT
            } else if (rollDelta < -TILT_THRESHOLD && !isTiltedLeft) {
                isTiltedRight = true
                _tiltDirection.value = TiltDirection.RIGHT
            }

            if (rollDelta < TILT_RESET_THRESHOLD && rollDelta > -TILT_RESET_THRESHOLD) {
                isTiltedLeft = false
                isTiltedRight = false
                _tiltDirection.value = TiltDirection.NONE
            }

            lastUpdate = currentTime
        }
    }

    companion object {
        private const val TILT_THRESHOLD = 1f
        private const val TILT_RESET_THRESHOLD =2f
        private const val SHAKE_THRESHOLD = 10
    }
}

enum class TiltDirection {
    NONE,
    LEFT,
    RIGHT
}
