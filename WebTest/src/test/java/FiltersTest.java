/**
 * Created by nadya-bu on 31/05/2017.
 */

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.jws.WebParam;
import java.util.List;
import java.util.Set;

public class FiltersTest {
    private void prepareToTest(WebDriver driver, int cnt) {
        driver.get("https://market.yandex.ru");

        WebElement searchField = driver.findElement(By.id("header-search"));
        WebElement search = driver.findElement(By.className("search2__button"));

        searchField.sendKeys("laptop Lenovo");
        search.click();

        WebElement item1 = (new WebDriverWait(driver, 10)).
                until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[1]/div[4]/div[2]/div[1]/div[2]/div[1]/div[1]/div[3]/div/div[2]/div/div/div[2]/div/div/i[1]")));

        WebElement item2 = driver.findElement(By.xpath("/html/body/div[1]/div[4]/div[2]/div[1]/div[2]/div[1]/div[2]/div[3]/div/div[2]/div/div/div[2]/div/div/i[1]"));
        WebElement item3 = driver.findElement(By.xpath("/html/body/div[1]/div[4]/div[2]/div[1]/div[2]/div[1]/div[3]/div[3]/div/div[2]/div/div/div[2]/div/div/i[1]"));

        item1.click();
        item2.click();
        item3.click();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        WebElement webElement = driver.findElement(By.xpath("/html/body/div[1]/div[1]/noindex/div/div/div[2]/div/div[2]/div[1]/a[2]/span[1]/span[1]"));
        webElement.click();
    }

    @Test
    public void simpleTest() {
        int cntModels = 3;
        WebDriver driver = new SafariDriver();
        try {
            prepareToTest(driver, cntModels);

            ComparePage page = new ComparePage(driver);
            List<ModelCard> models = page.getDiff();
            Set<String> features = models.get(0).getNameFeatures();

            for (String featureName: features) {
                boolean same = true;
                for (int i = 0; i < models.size() - 1; i++) {
                    String curValue = models.get(i).getFeature(featureName).getValue();
                    String nextValue = models.get(i + 1).getFeature(featureName).getValue();
                    if (!curValue.equals(nextValue)) {
                        same = false;
                        break;
                    }
                }
                if (same) {
                    for (ModelCard model: models) {
                        ModelCard.Feature curFeature = model.getFeature(featureName);
                        assert (curFeature.isHidden() || (!curFeature.isHidden() && curFeature.isAlwaysShow()));
                    }
                } else {
                    for (ModelCard model: models) {
                        assert (!model.getFeature(featureName).isHidden());
                    }
                }
            }

            models = page.getAll();
            for (String featureName: features) {
                for (ModelCard model: models) {
                    assert (!model.getFeature(featureName).isHidden());
                }
            }
        } finally {
            driver.quit();
        }
    }
}
