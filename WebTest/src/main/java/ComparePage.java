import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.xml.bind.Element;
import java.util.List;

/**
 * Created by nadya-bu on 31/05/2017.
 */


public class ComparePage {
    private WebDriver webDriver;
    private By show_all = By.className("link n-compare-show-controls__all");
    private By show_diff = By.className("link n-compare-show-controls__diff n-compare-show-controls__diff_active_yes");
    private By clazz = By.className("link__inner");
    private By compareTableClass = By.className("n-compare-table");
    private List<ModelCard> models;

    public ComparePage(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public List<ModelCard> getDiff() {
        return get(show_diff);
    }

    public List<ModelCard> getAll() {
        return  get(show_all);
    }

    private List<ModelCard> get(By byClass) {
        WebElement el_parent = (new WebDriverWait(webDriver, 10)).
                until(ExpectedConditions.presenceOfElementLocated(byClass));
        WebElement el = el_parent.findElement(clazz);
        el.click();

        models = ModelCard.getModelCards((new WebDriverWait(webDriver, 10).
                until(ExpectedConditions.presenceOfElementLocated(compareTableClass))));

        return models;
    }
}
