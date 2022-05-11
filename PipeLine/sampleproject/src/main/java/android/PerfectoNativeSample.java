package android;

import java.awt.Desktop;
import java.net.URI;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openqa.selenium.By;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.perfecto.reportium.client.ReportiumClient;
import com.perfecto.reportium.client.ReportiumClientFactory;
import com.perfecto.reportium.model.Job;
import com.perfecto.reportium.model.PerfectoExecutionContext;
import com.perfecto.reportium.model.Project;
import com.perfecto.reportium.test.TestContext;
import com.perfecto.reportium.test.result.TestResultFactory;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;

import com.github.tomaslanger.chalk.Ansi;
import com.github.tomaslanger.cli.progress.ProgressBar;
import com.github.tomaslanger.cli.progress.StatusLoc;

public class PerfectoNativeSample {

	public static void main(String[] args) throws Exception {

		int exitCode = 0;
		ProgressBar progressBar = createProgressBar();
		DesiredCapabilities capabilities = new DesiredCapabilities("", "", Platform.ANY);

		// 1. Replace <<cloud name>> with your perfecto cloud name (for example, 'demo' is the cloudName of demo.perfectomobile.com).
		String cloudName = "trial";

		// 2. Replace <<security token>> with your Perfecto security token.
		String securityToken = "eyJhbGciOiJIUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICI2ZDM2NmJiNS01NDAyLTQ4MmMtYTVhOC1kODZhODk4MDYyZjIifQ.eyJpYXQiOjE2NTE2NTMxODMsImp0aSI6IjVjYWZlNGZkLWRiNGYtNGQ3MC1iOTczLWE5ZGNmODUzYzkzYSIsImlzcyI6Imh0dHBzOi8vYXV0aDMucGVyZmVjdG9tb2JpbGUuY29tL2F1dGgvcmVhbG1zL3RyaWFsLXBlcmZlY3RvbW9iaWxlLWNvbSIsImF1ZCI6Imh0dHBzOi8vYXV0aDMucGVyZmVjdG9tb2JpbGUuY29tL2F1dGgvcmVhbG1zL3RyaWFsLXBlcmZlY3RvbW9iaWxlLWNvbSIsInN1YiI6IjY1NGYyZTdiLTk3OGMtNGE1Yy05Njg3LWU4ODNiZmVlMGE5NiIsInR5cCI6Ik9mZmxpbmUiLCJhenAiOiJvZmZsaW5lLXRva2VuLWdlbmVyYXRvciIsIm5vbmNlIjoiZmNjYjMyYTctYmVlNS00ODhjLTgxYTMtMmRlM2ZkM2EzYWM2Iiwic2Vzc2lvbl9zdGF0ZSI6ImZkMmVhNzU2LTRjYmItNDRjNy04OGQwLWE2YzYwN2FmNmIxZCIsInNjb3BlIjoib3BlbmlkIG9mZmxpbmVfYWNjZXNzIHByb2ZpbGUgZW1haWwifQ.8_AKcqBAx2Ia2nj80BxBB6ljNHiuPcGR2pxWKm2pVSA";
		capabilities.setCapability("securityToken", securityToken);

		// 3. Set the device capabilities.
		//capabilities.setCapability("platformName", "Android");
		//capabilities.setCapability("platformVersion", "8.0.0");
		//capabilities.setCapability("manufacturer", "Samsung");
		//capabilities.setCapability("model", "Galaxy Tab S3 9.7");
		
		capabilities.setCapability("platformName", "Android");
		capabilities.setCapability("platformVersion", "9");
		//capabilities.setCapability("location", "NA-US-BOS");
		//capabilities.setCapability("resolution", "1440x3040");
		capabilities.setCapability("manufacturer", "Samsung");
		capabilities.setCapability("model", "Galaxy S10\\+");

		// 4. Set the Perfecto media repository path of the app under test.
		capabilities.setCapability("app", "PRIVATE:app-athleteSeries-uat.apk");

		// 5. Set the unique identifier of your app - this is highly recommended
		// capabilities.setCapability("appPackage", "YOUR.APP.APPPACKAGE");

		// 6. Set other capabilities.
		capabilities.setCapability("enableAppiumBehavior", true); // Enable the new Appium Architecture.
		capabilities.setCapability("autoLaunch", true); // Whether to install and launch the app automatically.
		capabilities.setCapability("autoInstrument", true); // To work with hybrid applications, install the iOS/Android application as instrumented.
		capabilities.setCapability("takesScreenshot", true);
		capabilities.setCapability("screenshotOnError", true);
		// capabilities.setCapability("fullReset", false); // Reset the app state by uninstalling the app.

		// 7. Initialize the AndroidDriver driver.
		progressBar.setProgress(1);
		Logger.getLogger("org.openqa.selenium.remote").setLevel(Level.OFF);
		AndroidDriver<AndroidElement> driver = new AndroidDriver<AndroidElement>(
				new URL("https://" + cloudName.replace(".perfectomobile.com", "")
						+ ".perfectomobile.com/nexperience/perfectomobile/wd/hub"),
				capabilities);

		// 8. Set an implicit wait.
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

		PerfectoExecutionContext perfectoExecutionContext;
		if (System.getenv("jobName") != null) {
			perfectoExecutionContext = new PerfectoExecutionContext.PerfectoExecutionContextBuilder()
					.withProject(new Project("Sample Project", "1.0"))
					.withJob(new Job(System.getenv("jobName"),
							Integer.parseInt(System.getenv("jobNumber"))))
					.withWebDriver(driver).build();
		} else {
			perfectoExecutionContext = new PerfectoExecutionContext.PerfectoExecutionContextBuilder()
					.withProject(new Project("Sample Project", "1.0"))
					.withWebDriver(driver)
					.build();
		}

		ReportiumClient reportiumClient = new ReportiumClientFactory()
				.createPerfectoReportiumClient(perfectoExecutionContext);

		reportiumClient.testStart("Native Java Android Sample", new TestContext("native", "android"));

		try {

		    /**
		    *****************************
		    *** Your test starts here. If you test a different app, you need to modify the test steps accordingly. ***
		    *****************************
		    */
			driver.findElement(By.id("com.statsports.apexconsumer:id/btn_login")).click();
			Thread.sleep(1000);
			
			driver.findElement(By.id("com.statsports.apexconsumer:id/login_et_email")).click();
			Thread.sleep(1000);
			
			driver.findElement(By.id("com.statsports.apexconsumer:id/login_et_email")).sendKeys("c.egan@statsports.com");
			Thread.sleep(1000);
			
			driver.findElement(By.id("com.statsports.apexconsumer:id/login_et_password")).click();
			Thread.sleep(1000);
			
			driver.findElement(By.id("com.statsports.apexconsumer:id/login_et_password")).sendKeys("Test12345");
			Thread.sleep(1000);
			
			driver.findElement(By.id("com.statsports.apexconsumer:id/btn_login")).click();
			Thread.sleep(10000);

		    /**
		    *****************************
		    *** Your test ends here. ***
		    *****************************
		    */

			progressBar.setProgress(9);
			reportiumClient.testStop(TestResultFactory.createSuccess());
		} catch (Exception e) {
			progressBar.setProgress(9);
			reportiumClient.testStop(TestResultFactory.createFailure(e));
			exitCode = 1;
		}

		// Obtains the Report URL
		String reportURL = reportiumClient.getReportUrl() + "&onboardingJourney=automated&onboardingDevice=nativeApp";

		// Quits the driver
		progressBar.setProgress(10);
		driver.quit();

		// Prints the Report URL
		progressBar.setProgress(11);
		System.out.println("\n\nOpen this link to continue with the guide: " + reportURL + "\n");

		// Launch browser with the Report URL
		try {
			Desktop.getDesktop().browse(new URI(reportURL));
		} catch (Exception e) {
			System.out.println("Unable to open Reporting URL in browser: " + e.getMessage());
		}

		System.exit(exitCode);
	}

	private static ProgressBar createProgressBar() {
		int TOTAL_STEPS = 11;
		int PROGRESS_BAR_CHAR_COUNT = 50;
		ProgressBar.Builder builder = new ProgressBar.Builder();
		builder.setMax(TOTAL_STEPS)
			.setCharCount(PROGRESS_BAR_CHAR_COUNT)
			.setBaseChar(' ')
			.setProgressChar('=')
			.setStatusLocation(StatusLoc.FIRST_LINE)
			.setKeepSingleColor(true)
			.setBeginString("[")
			.setEndString("]")
			.setFgColor(Ansi.Color.WHITE)
			.setBgColor(Ansi.BgColor.BLACK)
			.claimNoOuts();
		return builder.build();
	}
}
