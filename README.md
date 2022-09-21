## BillBoard API

OpenAPI Swagger documentation: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

OpenAPI Yaml: [http://localhost:8080/api-docs](http://localhost:8080/api-docs)

### Original Technical Task
<details>
  <summary>Click me (in Russian)</summary>
**Задание:** разработать backend-сервер на Spring для функционирования доски объявлений.

#### Функционал, который требуется реализовать:
1. Регистрация и аутентификация пользователя в личном кабинете:
  * пользователь при регистрации должен указать роль, email и пароль;
  * аутентификацию реализовать через вход по email и паролю.
2. В личном кабинете пользователь может создать объявление и разместить его на доске объявлений в общем списке. Объявление содержит название, описание, контакты продавца и изображения.
3. Доска объявлений в данном случае - это список всех объявлений с многочисленными фильтрами (продумать максимально возможные варианты фильтров на своё усмотрение), который отображается на главной странице сервиса.
4. Пользователь может как размещать свои объявления, так и совершать сделки в рамках других объявлений.
5. Объявления имеют 2 статуса - активное и снятое с публикации.
6. Продумать и реализовать вариант коммуникации между покупателем и продавцом во время совершения сделки.
7. Для всех методов необходимо реализовать API-методы с документацией на Swagger.
8. Покрыть весь функционал тестами. Желательно использовать TDD при разработке.

#### Дополнительные требования:
1. Сделать обертку исходного кода в docker-образ (добавить в корневую директорию файл Dockerfile, docker-compose.yml при необходимости).
2. В readme файл разместить текст данного задания, а, также, инструкцию по развертыванию проекта и основные команды для запуска.
3. Исходный код выложить на github.com в публичный репозиторий.
4. При создании коммитов писать осмысленные названия.
5. Использовать инструмент тестового покрытия для отображения % покрытия исходного кода тестами.
6. Для проверки кода дополнительно подключить линтер на выбор.

</details>

<details>
  <summary>Click me (in English)</summary>
Assignment: develop a backend server in Spring to function as a bulletin board.

#### Functionality to be implemented:
1. Registration and authentication of the user in the personal account:
  * when registering, the user must specify the role, email and password;
  * Authentication to implement through login by email and password.
2. In the personal account, the user can create an announcement and place it on the bulletin board in the general list. The ad contains the title, description, seller's contacts and images.
3. The bulletin board in this case is a list of all ads with numerous filters (think about the maximum possible filter options at your discretion), which is displayed on the main page of the service.
4. The user can both place his ads and make transactions within the framework of other ads.
5. Ads have 2 statuses - active and unpublished.
6. Think over and implement a communication option between the buyer and the seller during the transaction.
7. For all methods, you need to implement API methods with Swagger documentation.
8. Cover all functionality with tests. It is desirable to use TDD in development.

#### Additional requirements:
1. Wrap the source code into a docker image (add a Dockerfile, docker-compose.yml file to the root directory if necessary).
2. In the readme file, place the text of this task, as well as instructions for deploying the project and the main commands for launching.
3. Put the source code on github.com in a public repository.
4. When creating commits, write meaningful names.
5. Use the test coverage tool to display % of source code coverage with tests.
6. To check the code, additionally connect the linter of your choice.

</details>

**Deployment on local machine:**
```
docker run --rm -d -p 5432:5432 -e POSTGRES_DB=billboard -e POSTGRES_PASSWORD=password postgres && mvn spring-boot:run
```

**Deployment in docker-compose:**
```
docker-compose up --build
```