package classes;

import main.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

//    @Test
//    void  managersLoadObjectTest() {
//
//        Managers managers = new Managers();
//       // ClassLoader loader = Test.class.getClassLoader();
//        ClassLoader loader = ClassLoader.getSystemClassLoader();
//        managers.getDefault();
//
//        Assertions.assertNotNull(ClassLoader.getSystemClassLoader().findLoadedClass(TaskManager.class));
//    }



    /* Прошу прощения за несколько недоделанное задание, но есть ряд вопросов по тестам. Не все из них получилось сделать. Помоги, пожалуйста.

    1) Тест на инициализацию и загрузку класса классом Managers. Загуглил, что это можно проверить через
    ClassLoader.findLoadedClass(), но не смог пробиться через доступ метода (он protected). Подскажешь как это сделать?

    2) В задании есть требование написать тест, что эпик нельзя добавить в этот же эпик подзадачей, тест, что сабтаску
    нельзя сделать своим же эпиком, тест, что задачи равны друг другу при условии одинакового id, тест,
    что задачи с сгенерированным id и с заданным вручную не будут конфликтовать.

    Это все возможно сделать (и проверить, соответственно), только при условии, что есть возможность менять id вручную.
    А в этом коде такой возможности нет.

    Вопрос. Мне надо добавить метод для изменения id, или как-то изменить основные методы, чтобы это стало возможным?
    Или раз код не предусматриват варианта ошибки, то и тесты такого рода не нужны?

    3) Та же проблема с id и с тестами методов update. Я в комментарии конкретно к ним это описал, чтобы удобнее было и код смотреть, и коммент читать.
     */

}