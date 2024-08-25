# Дипломный проект по автоматизации API-тестов платформы для обучения по UI и API автоматизации [Demoqa](https://demoqa.com/) 
<img width="30%" title="IntelliJ IDEA" src="media/logo/QA-Tools.jpg">

## :scroll: Содержание:

- [Используемый стек](#computer-используемый-стек)
- [Описание автотестов](#pushpin-описание-тестов)
- [Запуск автотестов](#arrow_forward-запуск-автотестов)
- [Сборка в Jenkins](#-сборка-в-jenkins)
- [Пример Allure-отчета](#-пример-allure-отчета)
- [Графики и диаграммы](#-графики-и-диаграммы)
- [Тесты кейсы в Allure TestOps](#-тесты-кейсы-в-allure-testops)
- [Уведомления в Telegram](#-уведомления-в-telegram)
- [Видео примера запуска тестов в Selenoid](#-видео-примера-запуска-тестов-в-selenoid)

##  :computer: Используемый стек

<p align="center">
<a href="https://www.jetbrains.com/idea/"><img width="6%" title="IntelliJ IDEA" src="media/logo/Intelij_IDEA.svg"></a>
<a href="https://www.java.com/"><img width="6%" title="Java" src="media/logo/Java.svg"></a>
<a href="https://selenide.org/"><img width="6%" title="Selenide" src="media/logo/Selenide.svg"></a>
<a href="https://aerokube.com/selenoid/"><img width="6%" title="Selenoid" src="media/logo/Selenoid.svg"></a>
<a href="https://allurereport.org/"><img width="6%" title="Allure Report" src="media/logo/Allure_Report.svg"></a>
<a href="https://qameta.io/"><img width="6%" title="Allure TestOps" src="media/logo/AllureTestOps.svg"></a>
<a href="https://gradle.org/"><img width="6%" title="Gradle" src="media/logo/Gradle.svg"></a>
<a href="https://junit.org/junit5/"><img width="6%" title="JUnit5" src="media/logo/JUnit5.svg"></a>
<a href="https://github.com/"><img width="6%" title="GitHub" src="media/logo/GitHub.svg"></a>
<a href="https://www.jenkins.io/"><img width="6%" title="Jenkins" src="media/logo/Jenkins.svg"></a>
<a href="https://telegram.org/"><img width="6%" title="Telegram" src="media/logo/Telegram.svg"></a>
</p>

Тесты в данном проекте написаны на языке <code>Java</code> с использованием фреймворка для тестирования [Selenide](https://selenide.org/), сборщик - <code>Gradle</code>. <code>JUnit 5</code> задействован в качестве фреймворка модульного тестирования.
При прогоне тестов для запуска браузеров используется [Selenoid](https://aerokube.com/selenoid/).
Для удаленного запуска реализована джоба в <code>Jenkins</code> с формированием Allure-отчета и отправкой результатов в <code>Telegram</code> при помощи бота.

## :pushpin: Описание тестов:
- ✓ *userAuthorizationTest() - Тест для корректной авторизации*
- ✓ *invalidUserAuthorizationTest() - Тест для некорректной авторизации*
- ✓ *addBookToCustomerProfileBooksListTest() - Тест для проверки добавления случайно выбранной книги в профиль пользователя* 
- ✓ *addBookToCustomerProfileWithInvalidTokenTest() - Тест для проверки добавления случайно выбранной книги в профиль пользователя*
- ✓ *deleteBookFromProfileBooksListTest() - Тест для проверки удаления случайно выбранной книги из профиль пользователя*
- ✓ *deleteNonCorrectBookFromProfileTest()  - Тест для проверки удаления случайно выбранной книги из профиль пользователя*
 
 
Содержание Allure-отчета:
* Шаги теста;
* Скриншот страницы на последнем шаге;
* Page Source;
* Логи браузерной консоли;
* Видео выполнения автотеста.

## :arrow_forward: Запуск автотестов

### Запуск тестов из терминала
```
gradle clean test API 
```
При выполнении данной команды в терминале IDE тесты запустятся удаленно в <code>Selenoid</code>. 


## <img width="4%" style="vertical-align:middle" title="Jenkins" src="media/logo/Jenkins.svg"> Сборка в Jenkins

Для запуска сборки необходимо перейти в раздел <code>Build now</code> и нажать кнопку <code>Build</code>.

После выполнения сборки, в блоке <code>Build history</code> напротив номера сборки появятся значки <code>Allure Report</code> и <code>Allure TestOps</code>, при клике на которые откроется страница с сформированным html-отчетом и тестовой документацией соответственно.

<p align="center">
<img title="Jenkins Build" src="media/screen/build.png">
</p>

## <img width="4%" style="vertical-align:middle" title="Allure Report" src="media/logo/Allure_Report.svg"> Пример Allure-отчета

<p align="center">
<img title="Allure" src="media/screen/allure.png">
</p>

## <img width="4%" style="vertical-align:middle" title="Allure Report" src="media/logo/Allure_Report.svg"> Графики и диаграммы

<p align="center">
<img title="graphs" src="media/screen/graphs.png">
</p>


## <img width="4%" style="vertical-align:middle" title="Allure TestOps" src="media/logo/AllureTestOps.svg"> Тесты кейсы в Allure TestOps

<p align="center">
<img title="graphs" src="media/screen/allure-test-ops.png">
</p>

### <img width="4%" style="vertical-align:middle" title="Telegram" src="media/logo/Telegram.svg"> Уведомления в Telegram

После завершения сборки специальный бот, созданный в <code>Telegram</code>, автоматически обрабатывает и отправляет сообщение с отчетом о прогоне тестов.

<p align="center">
<img width="70%" title="Telegram Notifications" src="media/screen/telegram.png">
</p>


