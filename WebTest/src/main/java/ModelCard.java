import com.sun.tools.internal.ws.wsdl.document.jaxws.Exception;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import javax.jws.WebParam;
import java.util.*;

/**
 * Created by nadya-bu on 31/05/2017.
 */
public class ModelCard {
    private static By rowClass = By.className("n-compare-row");
    private static By rowClassHidden = By.className("n-compare-row n-compare-row_hidden_yes");

    private static By rowName = By.className("n-compare-row-name");
    private static By cellClass = By.className("n-compare-cell");
    private static By alwaysShowClass = By.className("n-compare-group n-compare-group_state_opened n-compare-group_without-title_yes");
    private static By alwaysShowPath = By.xpath("/html/body/div/div[4]/div[2]/div[16]");

    static public class Feature {
        private String value;
        private boolean isHidden;
        private boolean isAlwaysShow = false;

        public Feature(String value, boolean isHidden) {
            this.value = value;
            this.isHidden = isHidden;
        }

        public String getValue() {
            return value;
        }

        public boolean isHidden() {
            return isHidden;
        }

        public void setAlwaysShow() {
            isAlwaysShow = true;
        }

        public boolean isAlwaysShow() {
            return isAlwaysShow;
        }
    }

    private HashMap<String, Feature> features = new HashMap<String, Feature>();

    static public List<ModelCard> getModelCards(WebElement webElement) {
        List<WebElement> rows = webElement.findElements(rowClass);
        List<WebElement> hiddenRows = webElement.findElements(rowClassHidden);
        WebElement alwaysShowGroup = webElement.findElement(alwaysShowPath);
        List<WebElement> alwaysShow = alwaysShowGroup.findElements(rowClass);

        List<ModelCard> models = null;

        for (int i = 0; i < rows.size(); i++) {
            WebElement row = rows.get(i);
            String nameRow;
            try {
                nameRow = row.findElement(rowName).getText();
            } catch (RuntimeException e) {
                nameRow = "feature_" + Integer.toString(i);
            }
            List<WebElement> values = row.findElements(cellClass);
            if (models == null) {
                models = new ArrayList<ModelCard>(values.size());
                for (int j = 0; j < values.size(); j++) {
                    models.add(new ModelCard());
                }
            }
            for (int j = 0; j < models.size(); j++) {
                models.get(j).addFeature(nameRow, values.get(j).getText(), hiddenRows.contains(row));
                if (alwaysShow.contains(row)) {
                    models.get(j).getFeature(nameRow).setAlwaysShow();
                }
            }
        }

        return models;
    }

    public void addFeature(String rowName, String value, boolean isHidden) {
        Feature feature = new Feature(value, isHidden);
        features.put(rowName, feature);
    }

    public Set<String> getNameFeatures() {
        return features.keySet();
    }

    public Feature getFeature(String featureName) {
        return features.get(featureName);
    }
}
