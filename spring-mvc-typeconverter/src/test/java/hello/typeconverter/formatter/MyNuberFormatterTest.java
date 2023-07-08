package hello.typeconverter.formatter;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.util.Locale;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class MyNuberFormatterTest {

    MyNuberFormatter myNuberFormatter = new MyNuberFormatter();

    @Test
    void parse() throws ParseException {
        Number result = myNuberFormatter.parse("1,000", Locale.KOREA);
        assertThat(result).isEqualTo(1000L);
    }

    @Test
    void print() {
        String print = myNuberFormatter.print(1000, Locale.KOREA);
        assertThat(print).isEqualTo("1,000");
    }
}
