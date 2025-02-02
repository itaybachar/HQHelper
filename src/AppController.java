import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class AppController extends Thread {
    @FXML
    Label choice1, choice2, choice3, question;

    ScreenCapture screenCapture = new ScreenCapture();
    ImageToText imageToText = new ImageToText();
    GoogleAPI googleAPI = null;
    double[] percent = {
            0.0, 0.0, 0.0
    };

    public void updateLabels() {
        choice1.setText("A) " + imageToText.getAnswerChoices().get(0) + " " + percent[0] + " %");
        choice2.setText("B) " + imageToText.getAnswerChoices().get(1) + " " + percent[1] + " %");
        choice3.setText("C) " + imageToText.getAnswerChoices().get(2) + " " + percent[2] + " %");
    }

    public void takeScreenshot() throws Exception {

        if (screenCapture.takeScreenshot()) {
            imageToText.doOCR(screenCapture.getCapturedImage());

            question.setText(imageToText.getQuestion());
            updateLabels();
            GoogleAPI googleAPI = new GoogleAPI(imageToText.getAnswerChoices().get(0).toLowerCase(), imageToText.getAnswerChoices().get(1).toLowerCase(),
                    imageToText.getAnswerChoices().get(2).toLowerCase(), imageToText.getQuestion(), this);

            Task<Integer> task = new Task<>() {
                @Override
                protected Integer call() throws Exception {
                    while (!googleAPI.done)
                        updateLabels();

                    return 0;
                }
            };
            new Thread(task).start();
            googleAPI.doAPI();
        }
    }

    public void setScreenshot() throws Exception {
        screenCapture.setRectangle();
    }

    public void setPercentages(double A, double B, double C) {
        percent[0] = A;
        percent[1] = B;
        percent[2] = C;
    }

}
