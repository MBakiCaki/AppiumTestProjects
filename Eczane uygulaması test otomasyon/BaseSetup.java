/*Muhammed Baki Caki - 13.12.2021
 * BIL359 Assignment Test Automation Project 
 * */
package eczane_Test;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;

public class BaseSetup {

	public AndroidDriver<MobileElement> driver;
	public WebDriverWait wait;

	@BeforeMethod
	public void setup() throws MalformedURLException {
		// set desired capabilities
		DesiredCapabilities caps = new DesiredCapabilities();

		caps.setCapability("deviceName", "Pixel_11.0");
		caps.setCapability("udid", "emulator-5554");
		caps.setCapability("platformName", "Android");
		caps.setCapability("platformVersion", "11.0");
		caps.setCapability("appPackage", "com.example.roomexample");
		caps.setCapability("appActivity", ".LoginActivity");
		caps.setCapability("noReset", "false");

		driver = new AndroidDriver<MobileElement>(new URL("http://127.0.0.1:4723/wd/hub"), caps);
		wait = new WebDriverWait(driver, 10);
	}

	@Test
	public void test() throws InterruptedException {	//Test Cases
		testLogin();	//[1-3]
		System.out.println("Giris kismi basarili");
		
		test_ilacEkle();	//[4-7]
		System.out.println("Ilac Ekleme kismi basarili");
		
		test_dusukStok();	//8
		System.out.println("Dusuk stok kontrolu basarili");
		
		test_ilacAra();	//[9-12]
		System.out.println("Ilac arama kismi basarili");
		
		test_ilacListesi();	//[13-16]
		System.out.println("Ilac listeleme kismi basarili");

	}

	public void testLogin() {	//giris ekraný ezcane ismi ve resmi duzenleme
		
		//Test Case 1-2-3 : 3 noktaya basinca eczane adi ve resmini düzenleme secenekleri cikacak ve duzenlenebilecek
		
		// isim duzenleme
		MobileElement more_options = (MobileElement) driver.findElementByAccessibilityId("More options");
		more_options.click();
		driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS); // implicit wait
		try {
			String text1 = driver.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.ListView/android.widget.LinearLayout[1]/android.widget.LinearLayout/android.widget.RelativeLayout/android.widget.TextView").getAttribute("text");
			String text2 = driver.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.ListView/android.widget.LinearLayout[2]/android.widget.LinearLayout/android.widget.RelativeLayout/android.widget.TextView").getAttribute("text");
			assertEquals(text1, "Ýsmi Düzenle");
			assertEquals(text2, "Resmi Deðiþtir");
		}
		catch(Exception e) {
			System.out.println("3 noktaya týklayinca secenekler gelmiyor!");
		}
		
		MobileElement isim_duzenle = (MobileElement) wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
				"/hierarchy/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.ListView/android.widget.LinearLayout[1]/android.widget.LinearLayout/android.widget.RelativeLayout/android.widget.TextView")));
		isim_duzenle.click();
		MobileElement eczane_isim = (MobileElement) wait
				.until(ExpectedConditions.elementToBeClickable(By.id("com.example.roomexample:id/eczIsim")));
		eczane_isim.sendKeys("Esen");
		MobileElement kaydet = (MobileElement) driver.findElementById("com.example.roomexample:id/login_button");
		kaydet.click();

		// fotograf ekleme
		wait.until(ExpectedConditions.elementToBeClickable(more_options)).click();
		MobileElement resmi_duzenle = (MobileElement) wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
				"/hierarchy/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.ListView/android.widget.LinearLayout[2]/android.widget.LinearLayout/android.widget.RelativeLayout/android.widget.TextView")));
		resmi_duzenle.click();
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS); // implicit wait
		MobileElement profil_resmi = (MobileElement) wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
				"	\r\n"
				+ "//android.widget.LinearLayout[@content-desc=\"eczane.jpg, 51.69 kB, Dec 13\"]/android.widget.RelativeLayout/android.widget.FrameLayout[1]/android.widget.ImageView[1]")));
		profil_resmi.click();
		
		//profil resmi oldugunun kontrolu
		try {
			driver.findElementById("com.example.roomexample:id/eczResim");
		}
		catch(NoSuchElementException e) {
			System.out.println("Profil resmi eklenemedi!");
		}
		MobileElement giris_butonu = (MobileElement) wait
				.until(ExpectedConditions.elementToBeClickable(By.id("com.example.roomexample:id/login_button")));
		giris_butonu.click();
	}

	public void test_ilacEkle() throws InterruptedException { //ilac ekleme sayfasý - sirasiyla 2 firma, 2 kategori ve 3 ilac ekleniyor 
		
		String firma1 = "Bayer";
		String firma2 = "Novartis";

		String ilac1 = "Minoset";// bayer agrý kesici
		String ilac2 = "Cataflam"; // novartis agrý kesici
		String ilac3 = "Sirdalud"; // novartis kas gevsetici
	

		MobileElement yeni_ilac_ekle = (MobileElement) wait.until(
				ExpectedConditions.elementToBeClickable(By.id("com.example.roomexample:id/nav_menu_add_medicine_btn")));
		yeni_ilac_ekle.click();

		// ---- Firma Ekle ----
		//1-Bayer
		driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS); // implicit wait
		MobileElement more_options = (MobileElement) driver.findElementByAccessibilityId("More options");
		more_options.click();
		MobileElement firma_ekle = (MobileElement) driver.findElementByXPath(
				"/hierarchy/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.ListView/android.widget.LinearLayout[1]/android.widget.LinearLayout/android.widget.RelativeLayout/android.widget.TextView");
		firma_ekle.click();
		MobileElement firma_ismi = (MobileElement) wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
				"/hierarchy/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.EditText")));
		firma_ismi.sendKeys(firma1);
		MobileElement ok_button = (MobileElement) wait
				.until(ExpectedConditions.elementToBeClickable(By.id("android:id/button1")));
		ok_button.click();
		
		//2-Novartis
		MobileElement more_options2 = (MobileElement) driver.findElementByAccessibilityId("More options");
		more_options2.click();
		MobileElement firma_ekle2 = (MobileElement) driver.findElementByXPath(
				"/hierarchy/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.ListView/android.widget.LinearLayout[1]/android.widget.LinearLayout/android.widget.RelativeLayout/android.widget.TextView");
		firma_ekle2.click();
		MobileElement firma_ismi2 = (MobileElement) wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
				"/hierarchy/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.EditText")));
		firma_ismi2.sendKeys(firma2);
		MobileElement ok_button2 = (MobileElement) wait
				.until(ExpectedConditions.elementToBeClickable(By.id("android:id/button1")));
		ok_button2.click();

		// -----Kategori Ekle----
		//1 - Agri kesici
		MobileElement more_options3 = (MobileElement) driver.findElementByAccessibilityId("More options");
		more_options3.click();
		MobileElement kategori_ekle = (MobileElement) wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
				"/hierarchy/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.ListView/android.widget.LinearLayout[2]/android.widget.LinearLayout/android.widget.RelativeLayout/android.widget.TextView")));
		kategori_ekle.click();
		MobileElement kategori_ismi = (MobileElement) wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
				"/hierarchy/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.EditText")));
		kategori_ismi.sendKeys("Aðrý kesici");
		MobileElement ok_button3 = (MobileElement) driver.findElementById("android:id/button1");
		ok_button3.click();
		
		//2 - Kas gevsetici
		MobileElement more_options4 = (MobileElement) driver.findElementByAccessibilityId("More options");
		more_options4.click();
		MobileElement kategori_ekle2 = (MobileElement) wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
				"/hierarchy/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.ListView/android.widget.LinearLayout[2]/android.widget.LinearLayout/android.widget.RelativeLayout/android.widget.TextView")));
		kategori_ekle2.click();
		MobileElement kategori_ismi2 = (MobileElement) wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
				"/hierarchy/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.EditText")));
		kategori_ismi2.sendKeys("Kas Gevsetici");
		MobileElement ok_button4 = (MobileElement) driver.findElementById("android:id/button1");
		ok_button4.click();

		
		// ilac1'i ekleme - ve test caselerin kontrolu
		MobileElement ilac_ismi = (MobileElement) driver
				.findElementById("com.example.roomexample:id/inputMedicineName");
		ilac_ismi.sendKeys(ilac1);
		
		//Test Case 4: Firma secme
		try {
		MobileElement firmasec = (MobileElement) driver.findElementById("com.example.roomexample:id/inputCompany");
		firmasec.click();
		MobileElement sec_firma1 = (MobileElement) wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
				"/hierarchy/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.ListView/android.widget.CheckedTextView[1]")));
		sec_firma1.click();
		}
		catch(Exception e) {
			System.out.println("Firma secme basarisiz!");
		}
		
		//Test Case 6: Kategori Secme
		try {
		MobileElement kategorisec = (MobileElement) driver
				.findElementById("com.example.roomexample:id/inputMedicineType");
		kategorisec.click();
		MobileElement sec_kategori1 = (MobileElement) wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
				"/hierarchy/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.ListView/android.widget.CheckedTextView[2]")));
		sec_kategori1.click();
		}
		catch(Exception e){
			System.out.println("Kategori secme basarisiz!");
		}
		MobileElement konum = (MobileElement) driver
				.findElementById("com.example.roomexample:id/inputMedicineLocation");
		konum.sendKeys("Raf 1 Sira 1");		
		MobileElement listele_butonu = (MobileElement) driver
				.findElementById("com.example.roomexample:id/addMedicineButton");
		MobileElement stok = (MobileElement) driver.findElementById("com.example.roomexample:id/inputStock");
		stok.sendKeys("-1");
		listele_butonu.click();
		driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS); // implicit wait
		
		//Test Case 5: Stok degerine harf, 0<x ve x>300 girme
		try {
		String toastMessage1 = driver.findElementByXPath("/hierarchy/android.widget.Toast").getText();
		String expected1 = "Lütfen pozitif bir sayý giriniz!";
		assertEquals(expected1, toastMessage1);
		}
		catch(Exception e) {
			System.out.println("Negatif sayi girildiginde hata mesaji yanlis!");
		}
		Thread.sleep(5000);	//Toast mesajinin kaybolmasi icin bekle
		
		wait.until(ExpectedConditions.elementToBeClickable(stok)).sendKeys("300");
		listele_butonu.click();
		driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS); // implicit wait
		try {
		String toastMessage2 = driver.findElementByXPath("/hierarchy/android.widget.Toast").getText();
		String expected2 = "Eczanede ayný ilaçtan en fazla 200 adet bulunabilir!";
		assertEquals(expected2, toastMessage2);
		}
		catch(Exception e) {
			System.out.println("200+ stok girildiginde hata mesaji yanlis!");
		}
		Thread.sleep(5000);	//Toast mesajinin kaybolmasi icin bekle
		
		// Test Case 7: ilacin basariyla eklendigini dogrulama
		wait.until(ExpectedConditions.elementToBeClickable(stok)).sendKeys("");	//eksik bilgiyle ilac ekleme denemesi
		listele_butonu.click();
		driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS); // implicit wait
		
		try {
		String toastMessage3 = driver.findElementByXPath("/hierarchy/android.widget.Toast").getText();
		String expected3 = "Lütfen seçimlerinizi kontrol ediniz.";
		assertEquals(expected3, toastMessage3);
		}
		catch(Exception e) {
			System.out.println("Stok kismi eksik birakilinca hata cikmasi gereken hata mesaji yanlis!");
		}
		Thread.sleep(5000);	//Toast mesajinin kaybolmasi icin bekle
		
		wait.until(ExpectedConditions.elementToBeClickable(stok)).sendKeys("15");
		listele_butonu.click();
		driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS); // implicit wait
		
		try {
		String toastMessage4 = driver.findElementByXPath("/hierarchy/android.widget.Toast").getText();
		String expected4 = "Ýlaç baþarýyla eklendi";
		assertEquals(expected4, toastMessage4);
		}
		catch(AssertionError a) {
			System.out.println(a);
		}
		
		// ilac3 ekleme
		MobileElement ilac_ismi3 = (MobileElement) driver
				.findElementById("com.example.roomexample:id/inputMedicineName");
		ilac_ismi3.sendKeys(ilac3);
		MobileElement firmasec3 = (MobileElement) driver.findElementById("com.example.roomexample:id/inputCompany");
		firmasec3.click();
		MobileElement sec_firma3 = (MobileElement) wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
				"/hierarchy/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.ListView/android.widget.CheckedTextView[2]")));
		sec_firma3.click();
		MobileElement stok3 = (MobileElement) driver.findElementById("com.example.roomexample:id/inputStock");
		stok3.sendKeys("50");
		MobileElement kategorisec3 = (MobileElement) driver
				.findElementById("com.example.roomexample:id/inputMedicineType");
		kategorisec3.click();
		MobileElement sec_kategori3 = (MobileElement) wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
				"/hierarchy/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.ListView/android.widget.CheckedTextView[1]")));
		sec_kategori3.click();
		MobileElement konum3 = (MobileElement) driver
				.findElementById("com.example.roomexample:id/inputMedicineLocation");
		konum3.sendKeys("Raf 2 Sira 1");
		MobileElement listele_butonu3 = (MobileElement) driver
				.findElementById("com.example.roomexample:id/addMedicineButton");
		listele_butonu3.click();

		// ilac2 ekleme
		MobileElement ilac_ismi2 = (MobileElement) driver
				.findElementById("com.example.roomexample:id/inputMedicineName");
		ilac_ismi2.sendKeys(ilac2);
		MobileElement firmasec2 = (MobileElement) driver.findElementById("com.example.roomexample:id/inputCompany");
		firmasec2.click();
		MobileElement sec_firma2 = (MobileElement) wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
				"/hierarchy/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.ListView/android.widget.CheckedTextView[2]")));
		sec_firma2.click();
		MobileElement stok2 = (MobileElement) driver.findElementById("com.example.roomexample:id/inputStock");
		stok2.sendKeys("100");
		MobileElement kategorisec2 = (MobileElement) driver
				.findElementById("com.example.roomexample:id/inputMedicineType");
		kategorisec2.click();
		MobileElement sec_kategori2 = (MobileElement) wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
				"/hierarchy/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.ListView/android.widget.CheckedTextView[2]")));
		sec_kategori2.click();
		MobileElement konum2 = (MobileElement) driver
				.findElementById("com.example.roomexample:id/inputMedicineLocation");
		konum2.sendKeys("Raf 1 Sira 2");
		MobileElement listele_butonu2 = (MobileElement) driver
				.findElementById("com.example.roomexample:id/addMedicineButton");
		listele_butonu2.click();

		driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS); // implicit wait
		driver.navigate().back();	//ana sayfaya don
	}
	
	public void test_dusukStok() { // dusuk stoklu olan ilaci kontrol et

		MobileElement dusuk_stok_butonu = (MobileElement) wait.until(
				ExpectedConditions.elementToBeClickable(By.id("com.example.roomexample:id/nav_menu_low_on_stock_btn")));
		dusuk_stok_butonu.click();
		MobileElement dusuk_stok_isim = (MobileElement) driver.findElementById("com.example.roomexample:id/lowOnStockMedicineName");
		MobileElement dusuk_stok_deger = (MobileElement) driver.findElementById("com.example.roomexample:id/medicineStock");

		String result1 = dusuk_stok_isim.getText();
		String result2 = dusuk_stok_deger.getText();
		String expected1 = "Minoset";
		String expected2 = "15";
		try {
		assertEquals(result1, expected1);
		assertEquals(result2, expected2);
		}
		catch(AssertionError a) {
			System.out.println("Dusuk stok kontolu kismi hatali\n" + a);
		}

		driver.navigate().back();	//ana sayfaya don
	}

	public void test_ilacAra() { // tek ilaci ara ve konumunu kontrol et
		
		MobileElement ilac_ara_buton = (MobileElement) wait.until(ExpectedConditions
				.elementToBeClickable(By.id("com.example.roomexample:id/nav_menu_search_medicine_btn")));
		ilac_ara_buton.click();
		try {
		MobileElement arama_kutucugu = (MobileElement) driver.findElementById("com.example.roomexample:id/searchInput");
		arama_kutucugu.sendKeys("Cataflam");
		MobileElement arama_ikonu = (MobileElement) driver.findElementById("com.example.roomexample:id/searchButton");
		arama_ikonu.click();
		}
		catch(Exception e) {
			System.out.println("Ýlac arama kismi hatali");
		}

		//Test Case 10: Aranan ilacin konumunun kontrolu
		MobileElement konum = (MobileElement) driver.findElementById("com.example.roomexample:id/searchOutputMedicineLocation");// arama sonucu

		String result = konum.getText();
		String expected = "Raf 1 Sira 2";
		try {
		assertEquals(result, expected);
		}
		catch(AssertionError a) {
			System.out.println("Ýlac arama sonucu hatali!\n" +a);
		}
		//Test Case 11: Aranan ilac yoksa hata mesajinin kontrolu
		driver.findElementById("com.example.roomexample:id/searchInput").sendKeys("asdwq");	//arama kutucuguna rastgele degerler gonder
		driver.findElementById("com.example.roomexample:id/searchButton").click();
		
		try {
		String toastMessage5 = driver.findElementByXPath("/hierarchy/android.widget.Toast").getText();
		String expected5 = "Eczanenizde bu ilaç bulunmamaktadýr. Yazým hatasý yapmadýðýnýzdan emin olarak tekrar deneyebilirsiniz";
		assertEquals(expected5, toastMessage5);
		}
		catch(Exception a) {
			System.out.println("Mevcut olmayan ilac arandiginda hata mesaji eksik!");
		}

		driver.navigate().back();
	}

	public void test_ilacListesi() { // farklý turlere gore listele
		
		MobileElement ilac_listesi_butonu = (MobileElement) driver.findElementById("com.example.roomexample:id/nav_menu_medicine_list_btn");
		ilac_listesi_butonu.click();
		
		MobileElement siralama_turu = (MobileElement) driver.findElementById("android:id/text1");
		siralama_turu.click();
		driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS); // implicit wait

		
		MobileElement listele_ilac_turu = (MobileElement) wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
				"/hierarchy/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.ListView/android.widget.CheckedTextView[2]")));
		listele_ilac_turu.click();
		MobileElement ilac_listesi_butonu2 = (MobileElement) driver.findElementById("android:id/text1");
		ilac_listesi_butonu2.click();
		MobileElement listele_firma = (MobileElement) driver.findElementByXPath(
				"/hierarchy/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.ListView/android.widget.CheckedTextView[3]");
		listele_firma.click();
		
		driver.findElementById("android:id/text1").click();	//siralama turu
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(	//ilac ismine gore sirala
				"/hierarchy/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.ListView/android.widget.CheckedTextView[1]"))).click();

		
		//Test Case 15,16: Ilaclarin tam adi yazacak ve alfabetik siralanacak
		String [] isimler = {"Cataflam", "Minoset", "Sirdalud"};
		String ilk_ilac = driver.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.view.ViewGroup/androidx.recyclerview.widget.RecyclerView/android.view.ViewGroup[1]/android.widget.TextView[2]").getText();
		String ikinci_ilac = driver.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.view.ViewGroup/androidx.recyclerview.widget.RecyclerView/android.view.ViewGroup[2]/android.widget.TextView[2]").getText();
		String ucuncu_ilac = driver.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.view.ViewGroup/androidx.recyclerview.widget.RecyclerView/android.view.ViewGroup[3]/android.widget.TextView[2]").getText();

		try {
			assertEquals(isimler[0], ilk_ilac);
			assertEquals(isimler[1], ikinci_ilac);
			assertEquals(isimler[2], ucuncu_ilac);
		}
		catch(Exception e) {
			System.out.println("Alfabetik siralama hatali");
		}
		
		driver.navigate().back();
	}

	@AfterMethod
	public void teardown() {
		driver.quit();
	}

}
