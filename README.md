# simple_UserService_Spring_Java_IoC/Di
Реализация проекта для демонтсрации возможностей

## UserService README
Проект "UserService" демонстрирует пример организации сервиса для управления пользователями с использованием Spring Framework. Проект включает в себя интерфейсы и реализации для работы с данными, а также конфигурационные классы для внедрения зависимостей и настройки.

## Структура проекта
Проект состоит из следующих компонентов:

- **CrudRepository**: Интерфейс, определяющий базовые методы для операций **CRUD** (Create, Read, Update, Delete) над сущностями.
- **UserRepository**: Интерфейс, расширяющий CrudRepository и добавляющий метод findByEmail для поиска пользователя по email.
- **UserRepositoryJdbcTemplate**: Реализация UserRepository с использованием JdbcTemplate и зависимостью от DataSource.
- **UserService**: Интерфейс, определяющий метод signUp для регистрации пользователей.
- **UserServiceImpl**: Реализация UserService, использующая UserRepositoryJdbcTemplate для выполнения операций с пользователями.
- **PasswordGenerateConfig**: Конфигурационный класс для настройки параметров сложности создания паролей.
## Тестирование
Проект **"UserService"** включает в себя тестовый класс **UserServiceImplTest**, который использует JUnit или другие тестовые фреймворки для проверки функциональности **UserServiceImpl**.
- **Тестирование включает**:

Позитивные и негативные сценарии регистрации пользователей с помощью UserService.
Проверку корректности работы методов UserRepository и взаимодействия с базой данных.

## Заключение
Проект **"UserService"** демонстрирует минимальную реализацию управления пользователями с использованием **Spring Framework**, включая тестирование. Вы можете доработать и расширить этот проект в соответствии с вашими потребностями и добавить дополнительные тесты для проверки функциональности

<pre>

public class Main {

  public static void main(String[] args) {
//    DataSource dataSource = HikDataSource.getDs();
//    UsersRepositoryJdbcImpl usersRepositoryJdbc = new UsersRepositoryJdbcImpl(dataSource);
//    usersRepositoryJdbc.createSchema();
    ApplicationContext context = 
			new AnnotationConfigApplicationContext(ApplicationConfig.class);
    UserServiceImpl userServiceImpl = 
			context.getBean("userServiceImpl", UserServiceImpl.class);
    userServiceImpl.signUp("iznullaaskarov@gmail.com");
  }
}

</pre>


<img width="480" alt="ServiceStructure" src="https://github.com/iznulla/simple_UserService_Spring_Java/assets/89657012/2c43c164-e219-4bbf-86ed-5a9fc019340f">
