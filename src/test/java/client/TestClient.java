package client;

import org.s1.misc.Closure;
import org.s1.misc.ClosureException;
import org.s1.test.BasicTest;
import org.s1.test.LoadTestUtils;
import ru.systemate.morpholog.client.MorphologClient;

/**
 * morpholog-java
 * User: GPykhov
 * Date: 11.03.14
 * Time: 10:04
 */
public class TestClient extends BasicTest {

    public void testDeclinePhrase(){
        int p = 10;
        title("Decline phrase, parallel: "+p);
        assertEquals(p, LoadTestUtils.run("test",p,p,new Closure<Integer, Object>() {
            @Override
            public Object call(Integer input) throws ClosureException {
                assertEquals("бурый медведь",MorphologClient.getInstance().declinePhrase("бурый медведь", MorphologClient.Cases.imenit));
                assertEquals("бурого медведя",MorphologClient.getInstance().declinePhrase("бурый медведь", MorphologClient.Cases.rodit));
                assertEquals("бурому медведю",MorphologClient.getInstance().declinePhrase("бурый медведь", MorphologClient.Cases.dat));
                assertEquals("бурого медведя",MorphologClient.getInstance().declinePhrase("бурый медведь", MorphologClient.Cases.vinit));
                assertEquals("бурым медведем",MorphologClient.getInstance().declinePhrase("бурый медведь", MorphologClient.Cases.tvorit));
                assertEquals("буром медведе",MorphologClient.getInstance().declinePhrase("бурый медведь", MorphologClient.Cases.predl));
                return null;
            }
        }));
    }

    public void testGetGender(){
        int p = 10;
        title("Get gender, parallel: "+p);
        assertEquals(p, LoadTestUtils.run("test",p,p,new Closure<Integer, Object>() {
            @Override
            public Object call(Integer input) throws ClosureException {
                assertEquals(MorphologClient.Genders.male,MorphologClient.getInstance().getGender("бурый медведь"));
                assertEquals(MorphologClient.Genders.male,MorphologClient.getInstance().getGender("Петров Иван Васильевич"));
                assertEquals(MorphologClient.Genders.female,MorphologClient.getInstance().getGender("автомойка"));
                return null;
            }
        }));
    }

    public void testToNumeral(){
        int p = 10;
        title("To numeral, parallel: "+p);
        assertEquals(p, LoadTestUtils.run("test",p,p,new Closure<Integer, Object>() {
            @Override
            public Object call(Integer input) throws ClosureException {
                assertEquals("сто двадцать три",MorphologClient.getInstance().toNumeral(123, null, null, false).getNumeral());
                assertEquals(MorphologClient.Cases.rodit,MorphologClient.getInstance().toNumeral(123, null, null, false).getLabelCase());
                assertEquals(false,MorphologClient.getInstance().toNumeral(123, null, null, false).isPlural());
                return null;
            }
        }));
    }

    public void testFormatNumber(){
        int p = 10;
        title("format number, parallel: "+p);
        assertEquals(p, LoadTestUtils.run("test",p,p,new Closure<Integer, Object>() {
            @Override
            public Object call(Integer input) throws ClosureException {
                assertEquals("123,456.00",MorphologClient.getInstance().formatNumber(123456, ",###.00").getNumeral());
                assertEquals(MorphologClient.Cases.rodit,MorphologClient.getInstance().formatNumber(123456, ",###.00").getLabelCase());
                assertEquals(true,MorphologClient.getInstance().formatNumber(123456, ",###.00").isPlural());
                return null;
            }
        }));
    }

    public void testFormatTimeDiff(){
        int p = 10;
        title("format time diff, parallel: "+p);
        assertEquals(p, LoadTestUtils.run("test",p,p,new Closure<Integer, Object>() {
            @Override
            public Object call(Integer input) throws ClosureException {
                assertEquals("через 1 час 1 секунду 1 миллисекунду",MorphologClient.getInstance().formatTimeDiff(3601001L, false, false));
                return null;
            }
        }));
    }

    public void testDetectFWords(){
        int p = 10;
        title("detect fwords, parallel: "+p);
        assertEquals(p, LoadTestUtils.run("test",p,p,new Closure<Integer, Object>() {
            @Override
            public Object call(Integer input) throws ClosureException {
                assertEquals(1,MorphologClient.getInstance().detectFWords("тест бля тест").size());
                assertEquals(MorphologClient.FWordDictionaries.age_18,MorphologClient.getInstance().detectFWords("тест бля тест").get(0).getType());
                assertEquals("бля",MorphologClient.getInstance().detectFWords("тест бля тест").get(0).getWord());
                assertEquals(5L,MorphologClient.getInstance().detectFWords("тест бля тест").get(0).getStart());
                assertEquals(3,MorphologClient.getInstance().detectFWords("тест бля тест").get(0).getLength());
                return null;
            }
        }));
    }
       
}
