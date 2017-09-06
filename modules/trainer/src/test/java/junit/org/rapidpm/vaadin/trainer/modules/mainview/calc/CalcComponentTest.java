package junit.org.rapidpm.vaadin.trainer.modules.mainview.calc;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.rapidpm.vaadin.server.Main;

/**
 *
 */
//@VaadinComponentTest
public class CalcComponentTest {


  @BeforeEach
  void setUp() {
    Main.start();
  }

  @AfterEach
  void tearDown() {
    Main.shutdown();
  }

  @DisplayName("5 + 5 = 10")
  @Test
  void test001() {

  }
}
