package misc;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class HelloWorldTest extends BaseTest {
    @Test(timeout=SECOND)
    public void testConstructor() {
        HelloWorld hello = new HelloWorld();
    }

    @Test(timeout=SECOND)
    public void testFoo() {
        HelloWorld hello = new HelloWorld();

        assertEquals("bar", hello.foo());
    }

    @Test(timeout=SECOND)
    public void testEcho() {
        HelloWorld hello = new HelloWorld();

        for (int i = 0; i < 100; i++) {
            assertEquals(i, hello.echo(i));
        }
    }
}
