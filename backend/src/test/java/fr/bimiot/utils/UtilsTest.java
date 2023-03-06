package fr.bimiot.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UtilsTest {
    @Test
    void constructor_shouldThrowIllegalStateException() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Constructor<Utils> constructor = Utils.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        Exception exception = assertThrows(InvocationTargetException.class, constructor::newInstance);
        assertNotNull(exception);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"  ", "\t", "\n"})
    void isNotValidProjectName_shouldReturnTrue(String input) {
        assertTrue(Utils.isNotValidProjectName(input));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Project X", "Project_X", "Project-X"})
    void isNotValidProjectName_shouldReturnFalse(String input) {
        assertFalse(Utils.isNotValidProjectName(input));
    }
}