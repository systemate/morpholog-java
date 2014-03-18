# Morpholog-Java [![Build Status](https://travis-ci.org/systemate/morpholog-java.png?branch=master)](https://travis-ci.org/systemate/morpholog-java)

Mopholog-Java - клиент для сервиса Системэйт [Morpholog](http://systemate.ru/products/morpholog).

Сервис предназначен для работы со следующими правилами русского языка:
* Склонение (Ф.И.О., произвольных фраз, топонимов);
* Преобразование чисел в числительные;
* Определение пола (по Ф.И.О., по фразе);
* Определение нецензурной и ненормативной лексики;

Использование
-------------

Mopholog-Java доступен на Maven Central.

```xml
<dependency>
    <groupId>ru.systemate</groupId>
    <artifactId>morpholog-client</artifactId>
    <version>${morpholog.version}</version>
</dependency>
```

![Morpholog](http://systemate.ru/img/products/morpholog.png)