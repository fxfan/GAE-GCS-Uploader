package jp.co.ayuta.gs_sample.controller;

import org.slim3.tester.ControllerTestCase;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class ShowControllerTest extends ControllerTestCase {

    @Test
    public void run() throws Exception {
        tester.start("/show");
        ShowController controller = tester.getController();
        assertThat(controller, is(notNullValue()));
        assertThat(tester.isRedirect(), is(false));
        assertThat(tester.getDestinationPath(), is("/show.jsp"));
    }
}
