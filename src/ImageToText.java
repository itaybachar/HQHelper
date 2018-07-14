import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.awt.image.BufferedImage;
import java.util.LinkedList;

public class ImageToText {

    private String question;
    private LinkedList<String> answerChoices = new LinkedList<>();

    public void doOCR(BufferedImage image) throws TesseractException {
        ITesseract inst = new Tesseract();
        inst.setDatapath("C:\\Users\\itayb\\Tess4J-3.4.8-src\\Tess4J");
        String res = inst.doOCR(image);
        parseImageString(res);
    }

    private void parseImageString(String res){
        answerChoices.clear();
        System.out.println(res);
        //Parse question out
        String tempQ = res.substring(0,res.indexOf("?") + 1);
        res = res.replace(tempQ,"");

        tempQ = tempQ.replace("\n"," ");
        question = tempQ.trim();

        //Remove New Lines
        res.trim();
        while(true){
            if(res.charAt(0) !='\n'){
                break;
            }
            res = res.replaceFirst("\n","");
        }



        //Get Answer Choices
        for(int i = 0; i<3; i++){
            if(i < 2) {
                String temp = res.substring(0, res.indexOf("\n"));
                res = res.replace(temp + "\n", "");
                temp = temp.trim();
                res = res.trim();
                answerChoices.add(temp);
            } else answerChoices.add(res);

        }
    }

    public LinkedList<String> getAnswerChoices() {
        return answerChoices;
    }

    public String getQuestion() {
        return question;
    }
}
