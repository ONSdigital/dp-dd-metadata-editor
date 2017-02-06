package uk.co.onsdigital.discovery.dao;

import org.junit.Test;
import uk.co.onsdigital.discovery.dao.parameters.NamedParam;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Verifies that {@link NamedParam.ListBuilder} behaves as expected.
 */
public class NamedParamTest {

    @Test
    public void shouldCreateExpectedNamedParamList() {
        UUID uuid = UUID.randomUUID();

        List<NamedParam> expected = new ArrayList<>();
        expected.add(new NamedParam("key1", "value1"));
        expected.add(new NamedParam("key2", uuid));

        List<NamedParam> actual = new NamedParam.ListBuilder()
                .addParam("key1", "value1")
                .addParam("key2", uuid)
                .toList();

        assertThat(expected, equalTo(actual));
    }
}
