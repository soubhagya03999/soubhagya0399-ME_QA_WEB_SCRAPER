package demo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class TestCases {
    static WebDriver driver;
    
    @BeforeSuite
    public static void openBrowser(){
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
    }

    public static void openURL(String url){
        driver.get(url);
    }

    public static void click_on_HTFSP(){
        WrapperMethods.clickOperation(driver, By.linkText("Hockey Teams: Forms, Searching and Pagination"));
    }

    public static void click_on_OWFAJ(){
        try {
            WrapperMethods.clickOperation(driver, By.linkText("Oscar Winning Films: AJAX and Javascript"));
            Thread.sleep(9000);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public static void pages_on_HTFSP(int pageNo){
        try {
            WebElement page = driver.findElement(By.xpath("//ul[@class='pagination']/li/a[normalize-space()='"+String.valueOf(pageNo)+"']"));
            page.click();
            Thread.sleep(7000);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public static ArrayList<HashMap<String,String>> get_TheMovieDetails_on_OWFAJ(){
        try {
            Boolean isWinner=true;
            ArrayList<HashMap<String,String>> listOfHashMaps = new ArrayList<>();
            List<WebElement> page_of_year = driver.findElements(By.xpath("//a[contains(@class,'year-link')]"));
            for (WebElement webElement : page_of_year) {
                webElement.click();
                Thread.sleep(5000);
                int count=0;
                for(int i=2;i<=6;i++){
                    List<WebElement> title = driver.findElements(By.xpath("//tbody[@id='table-body']/tr/td[1]"));
                    HashMap<String,String> map= new HashMap<>();
                    int titlecount=0;
                    for (WebElement webElement2 : title) {
                        titlecount++;
                        count++;
                        if (count==1 || count==11 ) {
                                List<WebElement> bestpicture = driver.findElements(By.xpath("//tbody[@id='table-body']/tr/td[4]/i"));
                                for (WebElement webElement5 : bestpicture) {
                                    if (webElement5.isDisplayed()) {
                                        isWinner=true;
                                        map.put("isWinner", String.valueOf(isWinner));
                                    }
                                }
                        }else if(count==3|| count==6 || count==15 || count==13){
                            List<WebElement> bestpicture = driver.findElements(By.xpath("//tbody[@id='table-body']/tr/td[4]/i"));
                            for (WebElement webElement6 : bestpicture) {
                                    if (webElement6.isDisplayed())
                                    map.put("isWinner", "");
                            }
                        }


                        if (i==titlecount) {
                            break;
                        }              
                        map.put("Epoch Time of Scrape", String.valueOf(System.currentTimeMillis()));
                        map.put("Year", webElement.getText());
                        map.put("Title", webElement2.getText());
                        int nominationcount=1;
                        List<WebElement> nominations = driver.findElements(By.xpath("//tbody[@id='table-body']/tr/td[2]"));
                        for (WebElement webElement3 : nominations) {
                            if (nominationcount==titlecount) {
                                map.put("Nominations", webElement3.getText());
                            }
                            nominationcount++;
                        }
                        int awardcount=1;
                        List<WebElement> award = driver.findElements(By.xpath("//tbody[@id='table-body']/tr/td[3]"));
                        for (WebElement webElement4 : award) {
                            if (awardcount==titlecount) {
                                map.put("Award", webElement4.getText());
                            }
                            awardcount++;
                        }
                    }

                    listOfHashMaps.add(map);
                }
            }
            // for (HashMap<String,String> a : listOfHashMaps) {
            //     System.out.println(a);
            // }
            //System.out.println(listOfHashMaps);
            return listOfHashMaps;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<HashMap<String,String>> get_TeamName_Year_Win_where_win_less_than_40_percentage(){
        try {
            ArrayList<HashMap<String,String>> listOfHashMaps = new ArrayList<>();
            for(int i=1;i<=4;i++){
                pages_on_HTFSP(i);
                List<WebElement> win_percentage = driver.findElements(By.xpath("//table[@class='table']/tbody/tr/th[normalize-space()='Win %']/following::tr/td[6]"));
                List<WebElement> teamName = driver.findElements(By.xpath("//table[@class='table']/tbody/tr/th[normalize-space()='Win %']/following::tr/td[1]"));
                List<WebElement> year = driver.findElements(By.xpath("//table[@class='table']/tbody/tr/th[normalize-space()='Win %']/following::tr/td[2]"));
                int count=1;
                for (WebElement webElement : win_percentage) {
                    HashMap<String,String> map1= new HashMap<>();
                    if (Double.parseDouble(webElement.getText())<0.40) {
                        map1.put("Epoch Time of Scrape", String.valueOf(System.currentTimeMillis()));
                        map1.put("Win Percentage", webElement.getText());
                        int teamcount=1;
                        for (WebElement webElement1 : teamName) {
                            if (teamcount==count) {
                                map1.put("Team Name", webElement1.getText());
                                break;
                            }
                            teamcount++;
                        }
                        int yearcount=1;
                        for (WebElement webElement2 : year) {
                            if (yearcount==count) {
                                map1.put("Year",webElement2.getText());
                                break;
                            }
                            yearcount++;
                        }
                        listOfHashMaps.add(map1);
                    }
                    }
                    count++;
                }
                // for (HashMap<String,String> hashMap : listOfHashMaps) {
                //     System.out.println(hashMap);
                // }
                return listOfHashMaps;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return null;
        }
    }

    public static void writeResultsToJson(List<HashMap<String, String>> results, String outputDirectory, String fileName) {
    try {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        Path outputPath = Paths.get(outputDirectory, fileName);
        Files.createDirectories(outputPath.getParent());
        mapper.writeValue(outputPath.toFile(), results);
    } catch (IOException e) {
        e.printStackTrace();
    }
}

    @Test
    public static void TestCase01(){
        try {
            openURL("https://www.scrapethissite.com/pages/");
            click_on_HTFSP();
            String outputDirectory = "output";
            String fileName = "hockey-team-data.json";
            writeResultsToJson(get_TeamName_Year_Win_where_win_less_than_40_percentage(), outputDirectory, fileName);
            File outputFile = new File(outputDirectory, fileName);
            Assert.assertTrue(outputFile.exists(), "JSON file does not exist in the output folder");
            Assert.assertTrue(outputFile.length() > 0, "JSON file is empty");
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    @Test
    public static void TestCase02(){
        try {
            openURL("https://www.scrapethissite.com/pages/");
            click_on_OWFAJ();
            String outputDirectory = "output";
            String fileName = "oscar-winning-films-data.json";
            writeResultsToJson(get_TheMovieDetails_on_OWFAJ(), outputDirectory, fileName);
            File outputFile = new File(outputDirectory, fileName);
            Assert.assertTrue(outputFile.exists(), "JSON file does not exist in the output folder");
            Assert.assertTrue(outputFile.length() > 0, "JSON file is empty");
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    @AfterSuite
    public static void closeBrowser(){
        driver.quit();
    }
}
