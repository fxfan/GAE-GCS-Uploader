package jp.co.ayuta.gs_sample.model;

import org.slim3.tester.AppEngineTestCase;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class ImageFileTest extends AppEngineTestCase {

    private ImageFile model = new ImageFile();

    @Test
    public void test() throws Exception {
        assertThat(model, is(notNullValue()));
    }
}
