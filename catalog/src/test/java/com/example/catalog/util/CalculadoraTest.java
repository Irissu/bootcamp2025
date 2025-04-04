package com.example.catalog.util;
import static org.junit.jupiter.api.Assertions.*;

import com.example.catalog.ioc.Repository;
import org.junit.jupiter.api.*;

import static org.mockito.Mockito.*;

public class CalculadoraTest {
    Calculadora calc;

    @BeforeAll
    static void setUpBeforeClass() throws Exception {
    }

    @AfterAll
    static void tearDownAfterClass() throws Exception {
    }

    @BeforeEach
    void setUp() throws Exception {
        calc = new Calculadora();
    }

    @AfterEach
    void tearDown() throws Exception {
    }

    @Nested
    @DisplayName("Método: suma")
    class Suma {
        @Nested
        @DisplayName("Casos validos")
        class OK {
            @Test
            @DisplayName("Suma dos enteros")
            void testSuma() {
 		     var calc = new Calculadora();

                var actual = calc.suma(2, 3);

                assertEquals(5, actual);
            }

            @Test
            @DisplayName("Suma dos reales")
            void testSuma2() {
                var calc = new Calculadora();

                var actual = calc.suma(0.1, 0.2);

                assertEquals(0.3, actual);
            }

            @Test
            @DisplayName("Suma dos reales: resta")
            void testSuma3() {
                var calc = new Calculadora();

                assertEquals(0.1, calc.suma(1, -0.9));
            }
        }

        @Nested
        @DisplayName("Casos invalidos")
        class KO {
            @Test
            @DisplayName("Suma dos enteros grandes")
            @Disabled
            void testSumaInt() {
                var calc = new Calculadora();

                var actual = calc.suma(Integer.MAX_VALUE, 1);
                assertEquals(5, actual);

                assertTrue(1.0 / 0 > 0);
                assertEquals(5, 1.0 / 0);
//		assertTrue(actual > 0);
            }
        }
    }

    @Nested
    @DisplayName("Método: divide")
    class Divide {
        @Nested
        @DisplayName("Casos validos")
        class OK {
            @Test
            @DisplayName("Divide dos enteros")
            void testDivide() {
                var calc = new Calculadora();

                assertEquals(0.5, calc.divide(1.0, 2));
            }

            @Test
            @DisplayName("Divide por cero")
            void testDivide2() {
                var calc = new Calculadora();
                var ex = assertThrows(ArithmeticException.class, () -> calc.divide(1, 0));
                assertEquals("/ by zero", ex.getMessage());
            }

            @Test
            @DisplayName("Divide por cero con try")
            void testDivide3() {
                var calc = new Calculadora();
                try {
                    calc.divide(1, 0);
                    fail("No se ha lanzado excepcion");
                } catch (ArithmeticException ex) {
                    assertEquals("/ by zero", ex.getMessage());
                }
            }
        }
    }

    @Test
    void mockTheRepository() {
        var calc = mock(Calculadora.class);
        when(calc.suma(anyInt(), anyInt())).thenReturn(4);
        var repo = mock(Repository.class);
        doNothing().when(repo).guardar();

        var obj = new Factura(calc, repo);
        var actual = obj.calcularTotal(2, 2);
        obj.emitir();

        assertEquals(4, actual);
        verify(calc).suma(2, 2);
        verify(repo).guardar();



    }
}
