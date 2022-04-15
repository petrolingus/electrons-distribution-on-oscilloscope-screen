package me.petrolingus.edos;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.LineChart;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import me.petrolingus.edos.core.electrods.AcceleratingElectrode;
import me.petrolingus.edos.core.electrods.FocusingElectrode;
import me.petrolingus.edos.core.electrods.HorizontalElectrode;
import me.petrolingus.edos.core.electrods.VerticalElectrode;
import me.petrolingus.edos.core.electrongun.PointElectronGun;
import me.petrolingus.edos.core.phys.Electron;

public class Controller {

    public Canvas canvas;
    public Canvas canvasTest;

    public TextField speedZeroTextField;
    public TextField timeStepTextField;
    public Slider deflectionAngleSlider;

    public CheckBox accelerationActiveCheckBox;
    public Slider accelerationSignificandSlider;
    public Slider accelerationExponentSlider;

    public CheckBox focusActiveCheckBox;
    public Slider focusSizeSlider;

    // Horizontal main components
    public CheckBox horizontalActiveCheckBox;
    public ToggleGroup horizontalRadioGroup;
    public RadioButton horizontalConstantModeRadioButton;
    public RadioButton horizontalWaveModeRadioButton;
    // Horizontal constant mode components
    public Label horizontalSignificandLabel;
    public Slider horizontalSignificandSlider;
    public Label horizontalExponentLabel;
    public Slider horizontalExponentSlider;
    // Horizontal wave mode components
    public Label horizontalAmplitudeLabel;
    public TextField horizontalAmplitudeField;
    public Label horizontalFrequencyLabel;
    public TextField horizontalFrequencyField;
    public Label horizontalPhaseLabel;
    public TextField horizontalPhaseField;


    // Vertical main components
    public CheckBox verticalActiveCheckBox;
    public ToggleGroup verticalRadioGroup;
    public RadioButton verticalConstantModeRadioButton;
    public RadioButton verticalWaveModeRadioButton;
    // Vertical constant mode components
    public Label verticalSignificandLabel;
    public Slider verticalSignificandSlider;
    public Label verticalExponentLabel;
    public Slider verticalExponentSlider;
    // Vertical wave mode components
    public Label verticalAmplitudeLabel;
    public TextField verticalAmplitudeField;
    public Label verticalFrequencyLabel;
    public TextField verticalFrequencyField;
    public Label verticalPhaseLabel;
    public TextField verticalPhaseField;


    public LineChart<Number, Number> currentElectronsChart;

    private Service service;

    public void initialize() {

        service = new Service(canvas);
        service.currentElectronsChart = currentElectronsChart;
        service.canvasTest = canvasTest;

        GraphicsContext graphicsContext2D = canvas.getGraphicsContext2D();
        graphicsContext2D.setFill(Color.BLACK);
        graphicsContext2D.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        GraphicsContext g = canvasTest.getGraphicsContext2D();
        g.setFill(Color.BLACK);
        g.fillRect(0, 0, canvasTest.getWidth(), canvasTest.getHeight());


        PointElectronGun.speed = Double.parseDouble(speedZeroTextField.getText());
        speedZeroTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                PointElectronGun.speed = Double.parseDouble(newValue);
            } catch (NumberFormatException e) {
                PointElectronGun.speed = 0;
            }
        });

        Electron.time = Double.parseDouble(timeStepTextField.getText());
        timeStepTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                Electron.time = Double.parseDouble(newValue);
            } catch (NumberFormatException e) {
                Electron.time = 0;
            }
        });

        bindDisabledPropertyBySelectedMode(
                horizontalWaveModeRadioButton, horizontalConstantModeRadioButton,
                horizontalExponentLabel, horizontalExponentSlider,
                horizontalSignificandLabel, horizontalSignificandSlider,
                horizontalAmplitudeLabel, horizontalAmplitudeField,
                horizontalFrequencyLabel, horizontalFrequencyField,
                horizontalPhaseLabel, horizontalPhaseField);

        bindDisabledPropertyBySelectedMode(
                verticalWaveModeRadioButton, verticalConstantModeRadioButton,
                verticalExponentLabel, verticalExponentSlider,
                verticalSignificandLabel, verticalSignificandSlider,
                verticalAmplitudeLabel, verticalAmplitudeField,
                verticalFrequencyLabel, verticalFrequencyField,
                verticalPhaseLabel, verticalPhaseField);

        makeSliderQuantized(accelerationExponentSlider);
        makeSliderQuantized(horizontalExponentSlider);
        makeSliderQuantized(verticalExponentSlider);

        PointElectronGun.theta = deflectionAngleSlider.getValue();
        deflectionAngleSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            PointElectronGun.theta = newValue.doubleValue();
        });


        AcceleratingElectrode.isActive = accelerationActiveCheckBox.isSelected();
        AcceleratingElectrode.significand = accelerationSignificandSlider.getValue();
        AcceleratingElectrode.exponent = accelerationExponentSlider.getValue();
        accelerationActiveCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            AcceleratingElectrode.isActive = newValue;
        });
        accelerationSignificandSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            AcceleratingElectrode.significand = newValue.doubleValue();
        });
        accelerationExponentSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            AcceleratingElectrode.exponent = newValue.doubleValue();
        });

        FocusingElectrode.isActive = focusActiveCheckBox.isSelected();
        FocusingElectrode.focusSize = focusSizeSlider.getValue();
        focusActiveCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            FocusingElectrode.isActive = newValue;
        });
        focusSizeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            FocusingElectrode.focusSize = newValue.doubleValue();
        });

        HorizontalElectrode.isActive = horizontalActiveCheckBox.isSelected();
        HorizontalElectrode.mode = HorizontalElectrode.Mode.WAVE;
        HorizontalElectrode.significand = horizontalSignificandSlider.getValue();
        HorizontalElectrode.exponent = horizontalExponentSlider.getValue();
        HorizontalElectrode.amplitude = Double.parseDouble(horizontalAmplitudeField.getText());
        HorizontalElectrode.frequency = Double.parseDouble(horizontalFrequencyField.getText());
        HorizontalElectrode.phase = Double.parseDouble(horizontalPhaseField.getText());
        horizontalActiveCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            HorizontalElectrode.isActive = newValue;
        });
        horizontalRadioGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (observable.getValue().equals(horizontalConstantModeRadioButton)) {
                HorizontalElectrode.mode = HorizontalElectrode.Mode.CONSTANT;
            } else {
                HorizontalElectrode.mode = HorizontalElectrode.Mode.WAVE;
            }
        });
        horizontalSignificandSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            HorizontalElectrode.significand = newValue.doubleValue();
        });
        horizontalExponentSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            HorizontalElectrode.exponent = newValue.doubleValue();
        });
        horizontalAmplitudeField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                HorizontalElectrode.amplitude = Double.parseDouble(newValue);
            } catch (NumberFormatException e) {
                HorizontalElectrode.amplitude = 0;
            }
        });
        horizontalFrequencyField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                HorizontalElectrode.frequency = Double.parseDouble(newValue);
            } catch (NumberFormatException e) {
                HorizontalElectrode.frequency = 0;
            }
        });
        horizontalPhaseField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                HorizontalElectrode.phase = Double.parseDouble(newValue);
            } catch (NumberFormatException e) {
                HorizontalElectrode.phase = 0;
            }
        });


        VerticalElectrode.isActive = verticalActiveCheckBox.isSelected();
        VerticalElectrode.mode = VerticalElectrode.Mode.WAVE;
        VerticalElectrode.significand = verticalSignificandSlider.getValue();
        VerticalElectrode.exponent = verticalExponentSlider.getValue();
        VerticalElectrode.amplitude = Double.parseDouble(verticalAmplitudeField.getText());
        VerticalElectrode.frequency = Double.parseDouble(verticalFrequencyField.getText());
        VerticalElectrode.phase = Double.parseDouble(verticalPhaseField.getText());
        verticalActiveCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            VerticalElectrode.isActive = newValue;
        });
        verticalRadioGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (observable.getValue().equals(verticalConstantModeRadioButton)) {
                VerticalElectrode.mode = VerticalElectrode.Mode.CONSTANT;
            } else {
                VerticalElectrode.mode = VerticalElectrode.Mode.WAVE;
            }
        });
        verticalSignificandSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            VerticalElectrode.significand = newValue.doubleValue();
        });
        verticalExponentSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            VerticalElectrode.exponent = newValue.doubleValue();
        });
        verticalAmplitudeField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                VerticalElectrode.amplitude = Double.parseDouble(newValue);
            } catch (NumberFormatException e) {
                VerticalElectrode.amplitude = 0;
            }
        });
        verticalFrequencyField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                VerticalElectrode.frequency = Double.parseDouble(newValue);
            } catch (NumberFormatException e) {
                VerticalElectrode.frequency = 0;
            }
        });
        verticalPhaseField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                VerticalElectrode.phase = Double.parseDouble(newValue);
            } catch (NumberFormatException e) {
                VerticalElectrode.phase = 0;
            }
        });

    }

    private void bindDisabledPropertyBySelectedMode(
            RadioButton verticalWaveModeRadioButton, RadioButton verticalConstantModeRadioButton,
            Label verticalExponentLabel, Slider verticalExponentSlider,
            Label verticalSignificandLabel, Slider verticalSignificandSlider,
            Label verticalAmplitudeLabel, TextField verticalAmplitudeField,
            Label verticalFrequencyLabel, TextField verticalFrequencyField,
            Label verticalPhaseLabel, TextField verticalPhaseField) {

        verticalSignificandLabel.disableProperty().bind(verticalWaveModeRadioButton.selectedProperty());
        verticalSignificandSlider.disableProperty().bind(verticalWaveModeRadioButton.selectedProperty());
        verticalExponentLabel.disableProperty().bind(verticalWaveModeRadioButton.selectedProperty());
        verticalExponentSlider.disableProperty().bind(verticalWaveModeRadioButton.selectedProperty());

        verticalAmplitudeLabel.disableProperty().bind(verticalConstantModeRadioButton.selectedProperty());
        verticalAmplitudeField.disableProperty().bind(verticalConstantModeRadioButton.selectedProperty());
        verticalFrequencyLabel.disableProperty().bind(verticalConstantModeRadioButton.selectedProperty());
        verticalFrequencyField.disableProperty().bind(verticalConstantModeRadioButton.selectedProperty());
        verticalPhaseLabel.disableProperty().bind(verticalConstantModeRadioButton.selectedProperty());
        verticalPhaseField.disableProperty().bind(verticalConstantModeRadioButton.selectedProperty());
    }

    // Making the slider quantized when moving with mouse
    private void makeSliderQuantized(Slider slider) {
        slider.valueProperty().addListener((observable, oldValue, newValue) -> slider.setValue(Math.round(newValue.doubleValue())));
    }

    public void onStart() {
        if (!service.isRunning()) {
            service.start();
        }
    }

    public void onStop() {
        if (service.isRunning()) {
            service.cancel();
            service.reset();
        }
    }

    public void onClearDistribution() {
        service.clearDistribution = true;
    }

}
