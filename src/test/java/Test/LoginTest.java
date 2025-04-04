package Test;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.time.Duration;

public class LoginTest {
    WebDriver driver;

    @BeforeTest
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @Test
    public void testLogin() throws InterruptedException {
        driver.get("https://diva-pos-dev.web.app/u/customer"); // Thay URL nếu cần

        // Sử dụng WebDriverWait để đợi phần tử xuất hiện
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        // Tìm phần tử bằng XPath thay vì ID
        WebElement username = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='Nhập tài khoản']")));
        WebElement password = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='Nhập mật khẩu']")));
        WebElement loginButton = driver.findElement(By.xpath("//span[text()='Đăng nhập']"));
//        WebElement username1 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='Nhập tài khoản']")));
//        WebElement PIN = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='Nhập mã pin']")));
//        WebElement loginButton1 = driver.findElement(By.xpath("//span[text()='ĐĂNG NHẬP']"));


        // Nhập thông tin đăng nhập
        username.sendKeys("dva.pos");
        password.sendKeys("123@Abc");
        loginButton.click();
        Thread.sleep(6000);
//        WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(
//                By.xpath("//input[@placeholder='Nhập tài khoản']")
//        ));
//        usernameField.sendKeys("dva.pos");
        WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofSeconds(10));
        // Nhập mã PIN
        WebElement pinField = wait1.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@placeholder='Nhập mã pin']")
        ));
        pinField.sendKeys("123456");

        // Click nút đăng nhập (tuỳ vào giao diện)
        WebElement loginButton1 = driver.findElement(By.xpath("//span[text()='ĐĂNG NHẬP']"));

        loginButton1.click();
//        username1.sendKeys("dva.pos");
//        Thread.sleep(3000);
//        PIN.sendKeys("123456");
//        loginButton1.click();

        // Kiểm tra đăng nhập thành công bằng XPath

        JavascriptExecutor js = (JavascriptExecutor) driver;
        WebElement homeBreadcrumb = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//span[contains(text(),'Trang chủ')]")));
        Assert.assertTrue(homeBreadcrumb.isDisplayed(), "Login failed - Không tìm thấy Trang chủ!");


        // 2️⃣ **Chọn module "Quản lý Khách hàng"**
        // Chờ module "Khách hàng" xuất hiện và có thể click
        WebElement module = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[contains(text(),'Khách Hàng')]")));
        module.click();

        // Kiểm tra đã vào đúng module
//        WebElement module = driver.findElement(By.xpath("//div[contains(text(),'Khách Hàng')]"));
//
//        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", module);


        // 3️⃣ **Tìm kiếm khách hàng**
        WebElement searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='Tìm kiếm tên, số điện thoại khách hàng hoặc liên hệ']")));
        searchBox.sendKeys("0876077177");
        searchBox.submit(); // Nhấn Enter để tìm kiếm

        // Kiểm tra khách hàng hiển thị
        WebElement customerResult = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//td[contains(text(),'0876077177')]")));
        Assert.assertTrue(customerResult.isDisplayed(), "Không tìm thấy khách hàng!");

        // 4️⃣ **Tạo đơn hàng MP**
        WebElement createOrderButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'Tạo đơn hàng MP')]")));
        createOrderButton.click();

        // Kiểm tra hệ thống điều hướng đúng trang
        WebElement orderPageTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[contains(text(),'Tạo mới đơn hàng')]")));
        Assert.assertTrue(orderPageTitle.isDisplayed(), "Không vào được màn hình tạo đơn hàng!");

        // 5️⃣ **Chọn người nhận hoa hồng**
        WebElement commissionField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='Người nhận hoa hồng']")));
        commissionField.sendKeys("Nguyễn Văn A"); // Nhập tên người nhận

        // 6️⃣ **Chọn sản phẩm**
        WebElement selectProductButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'Chọn sản phẩm')]")));
        selectProductButton.click();

        // Chờ danh sách sản phẩm xuất hiện
        WebElement productCheckbox = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@type='checkbox' and @value='ProductID']")));
        productCheckbox.click(); // Tick chọn sản phẩm

        // 7️⃣ **Nhập số tiền & chọn phương thức thanh toán**
        WebElement amountField = driver.findElement(By.xpath("//input[@placeholder='Số tiền']"));
        amountField.sendKeys("500000");

        WebElement paymentMethod = driver.findElement(By.xpath("//select[@id='payment-method']"));
        paymentMethod.sendKeys("Chuyển khoản");

        // 8️⃣ **Hoàn tất tạo đơn hàng**
        WebElement confirmOrderButton = driver.findElement(By.xpath("//button[contains(text(),'Tạo mới đơn hàng')]"));
        confirmOrderButton.click();

        // Kiểm tra hệ thống hiển thị thông báo thành công
        WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(),'Tạo đơn hàng thành công')]")));
        Assert.assertTrue(successMessage.isDisplayed(), "Tạo đơn hàng thất bại!");
    }

    @AfterTest
    public void teardown() {
        driver.quit();
    }
}
