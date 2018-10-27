# todolist-api

ReactチュートリアルのためのAPIです。

- https://github.com/team-cerezo/react-tutorial
- https://github.com/team-cerezo/react-sample

## 動かし方

動かし方はいくつかあります。

### Mavenで動かす

```
mvn spring-boot:run
```

### javaコマンドで動かす

`java`コマンドで動かす場合はまずJARファイルを作る必要があります。

```
mvn package -DskipTests
```

`target`以下に`todolist-api.jar`が生成されるので、`java`コマンドで実行します。

```
java -jar target/todolist-api.jar
```

### docker-composeで動かす

WIP

