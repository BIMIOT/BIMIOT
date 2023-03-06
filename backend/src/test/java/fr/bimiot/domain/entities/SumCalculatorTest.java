package fr.bimiot.domain.entities;

import fr.bimiot.fixtures.SensorFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SumCalculatorTest {

    @InjectMocks
    SumCalculator sumCalculator;

    @Test
    void normalCaseTest() {
        var sensor = SensorFixture.aSimpleSensor();
        sumCalculator.addValue(20f, sensor);
        assertEquals(sensor, sumCalculator.getFirstSensor());
    }

    @Test
    void getSum_shouldReturnOptionalEmpty() {
        assertTrue(sumCalculator.getSum().isEmpty());
    }

    @Test
    void getSum_shouldReturnValue() {
        sumCalculator.addValue(10f, SensorFixture.aSimpleSensor());
        assertTrue(sumCalculator.getSum().isPresent());
        assertEquals(10f, sumCalculator.getSum().get());
    }

    @Test
    void getFirstSensor_shouldThrowIllegalStateException() {
        var exception = assertThrows(IllegalStateException.class, () -> sumCalculator.getFirstSensor());
        assertEquals("Insert a value with a sensor first.", exception.getMessage());
    }
}