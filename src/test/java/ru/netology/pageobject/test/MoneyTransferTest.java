package ru.netology.pageobject.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.pageobject.page.DashboardPage;
import ru.netology.pageobject.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.pageobject.data.DataHelper.*;

public class MoneyTransferTest {
  DashboardPage dashboardPage;
  CardInfo firstCardInfo;
  CardInfo secondCardInfo;

  @BeforeEach
  void setup() {
    var loginPage = open("http://localhost:9999", LoginPage.class);
    var authInfo = getAuthInfo();
    var verificationPage = loginPage.validLogin(authInfo);
    var verificationCode = getVerificationCode();
    dashboardPage = verificationPage.validVerify(verificationCode);
    firstCardInfo = getFirstCardInfo();
    secondCardInfo = getSecondCardInfo();
  }

    @Test
    void shouldTransferMoneyFromFirstToSecond() {
      var firstCardBalance = dashboardPage.getCardBalance(firstCardInfo);
      var secondCardBalance = dashboardPage.getCardBalance(secondCardInfo);
      var amount = generateValidAmount(firstCardBalance);
      var expectedBalanceFirstCard = firstCardBalance - amount;
      var expectedBalanceSecondCard = secondCardBalance + amount;
      var transferPage = dashboardPage.selectCardToTransfer(secondCardInfo);
      dashboardPage = transferPage.makeValidTransfer(String.valueOf(amount), firstCardInfo);
      var actualBalanceFirstCard = dashboardPage.getCardBalance(firstCardInfo);
      var actualBalanceSecondCard = dashboardPage.getCardBalance(secondCardInfo);
      assertEquals(expectedBalanceFirstCard, actualBalanceFirstCard);
      assertEquals(expectedBalanceSecondCard, actualBalanceSecondCard);
    }

}

