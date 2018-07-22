import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class AppController extends Thread{
    @FXML
    Label choice1, choice2, choice3, question;

    ScreenCapture screenCapture = new ScreenCapture();
    ImageToText imageToText = new ImageToText();
    GoogleAPI googleAPI = null;

    public void updateLabels(double percentA,double percentB,double percentC){
        choice1.setText("A) " + imageToText.getAnswerChoices().get(0) + " " + percentA + "%");
        choice2.setText("B) " + imageToText.getAnswerChoices().get(1) + " " + percentB + "%");
        choice3.setText("C) " + imageToText.getAnswerChoices().get(2) + " " + percentC + "%");
    }

    public void takeScreenshot() throws Exception {

        if (screenCapture.takeScreenshot()) {
            imageToText.doOCR(screenCapture.getCapturedImage());

            question.setText(imageToText.getQuestion());
            updateLabels(0.0,0.0,0.0);
            GoogleAPI googleAPI = new GoogleAPI(imageToText.getAnswerChoices().get(0).toLowerCase(), imageToText.getAnswerChoices().get(1).toLowerCase(),
                    imageToText.getAnswerChoices().get(2).toLowerCase(), imageToText.getQuestion(),this);
            googleAPI.doAPI();
        }
    }

    public void setScreenshot() throws Exception{
       screenCapture.setRectangle();
    }

}
