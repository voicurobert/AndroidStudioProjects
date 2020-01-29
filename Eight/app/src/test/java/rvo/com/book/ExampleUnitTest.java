package rvo.com.book;

import org.junit.Test;

import rvo.com.book.common.Validator;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void phoneNumberIsCorrect() {
        assertEquals(true, Validator.isPhoneNumberCorrect("760318696"));
    }

    @Test
    public void testCalculateEndTime() {
        //assertEquals( "10:0", Employee.calculateBookEndTime( "8:45", 90 ) );
    }
}