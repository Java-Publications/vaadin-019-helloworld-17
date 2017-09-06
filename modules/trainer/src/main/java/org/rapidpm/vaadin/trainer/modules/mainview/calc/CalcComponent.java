package org.rapidpm.vaadin.trainer.modules.mainview.calc;

import static com.vaadin.icons.VaadinIcons.CHECK_CIRCLE;
import static com.vaadin.icons.VaadinIcons.CLOSE_CIRCLE;
import static com.vaadin.icons.VaadinIcons.PLUS;
import static com.vaadin.shared.ui.ContentMode.HTML;

import java.util.Random;

import org.rapidpm.vaadin.trainer.modules.AbstractBaseCustomComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;

/**
 *
 */
public class CalcComponent extends AbstractBaseCustomComponent {


  @Override
  protected Component createComponent() {
    return createMathGrid();
  }


  private Button btn(String caption) {
    Button button = new Button(caption);
    return button;
  }

  public static final int COLUMNS = 7;
  public static final int ROWS = 2;

  private GridLayout grid;

  private TextField valueOne;
  private TextField valueTwo;
  private TextField humanResult;
  private TextField machineResult;
  private Label resultLabel;

  private Button buttonNext;
  private Button buttonCalculate;

  private Random random;

  private GridLayout createMathGrid() {

    initFields();
    createStructure();
    customizeFields();

    buttonCalculate.addClickListener((Button.ClickListener) event -> {
      String valueOneValue = valueOne.getValue();
      String valueTwoValue = valueTwo.getValue();

      Integer a = Integer.valueOf(valueOneValue);
      Integer b = Integer.valueOf(valueTwoValue);

      Integer x = a + b;

      machineResult.setValue(String.valueOf(x));

      //Compare Result
      Integer wasRight = Integer.valueOf(humanResult.getValue()) - x;
      if (wasRight == 0) {
        resultLabel.setCaption(CHECK_CIRCLE.getHtml());
        resultLabel.setCaptionAsHtml(true);

      } else {
        resultLabel.setCaption(CLOSE_CIRCLE.getHtml());
        resultLabel.setCaptionAsHtml(true);
      }
      resultLabel.setCaptionAsHtml(true);
      buttonCalculate.setEnabled(false);
      buttonNext.setEnabled(true);
    });

    buttonNext.addClickListener((Button.ClickListener) event -> {
      //clear
      valueOne.setValue("");
      valueTwo.setValue("");

      humanResult.setValue("");
      machineResult.setValue("");

      resultLabel.setCaption("");

      //toggle
      buttonCalculate.setEnabled(true);
      buttonNext.setEnabled(false);

      //init next
      valueOne.setValue(String.valueOf(random.nextInt(10)));
      valueTwo.setValue(String.valueOf(random.nextInt(10)));
    });


    return grid;
  }

  private void customizeFields() {
    machineResult.setReadOnly(true);
    machineResult.setReadOnly(true);
    buttonNext.setEnabled(false);

    //clear
    valueOne.setValue("");
    valueTwo.setValue("");

    humanResult.setValue("");
    machineResult.setValue("");

    //toggle
    buttonCalculate.setEnabled(true);
    buttonNext.setEnabled(false);

    //init next
    valueOne.setValue(String.valueOf(random.nextInt(10)));
    valueTwo.setValue(String.valueOf(random.nextInt(10)));

  }

  private void createStructure() {
    grid.addComponent(valueOne, 0, 0);
    grid.addComponent(new Label(PLUS.getHtml(), HTML), 1, 0);
    grid.addComponent(valueTwo, 2, 0);
    grid.addComponent(new Label("="), 3, 0);
    grid.addComponent(humanResult, 4, 0);
    grid.addComponent(buttonCalculate, 5, 0);
    grid.addComponent(buttonNext, 6, 0);

    grid.addComponent(machineResult, 4, 1);
    grid.addComponent(resultLabel, 5, 1);
  }

  private void initFields() {
    random = new Random(System.nanoTime());

    valueOne = new TextField();
    valueTwo = new TextField();
    humanResult = new TextField();
    machineResult = new TextField();
    resultLabel = new Label();

    buttonNext = btn("Next");
    buttonCalculate = btn("OK");

    grid = new GridLayout(COLUMNS, ROWS);
  }

}
