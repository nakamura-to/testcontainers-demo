# Testcontainersを使ってテストを効率化しよう

このリポジトリはTestcontainersの利用例を示します。

この例の特徴は以下の通りです。

- プログラミング言語：Kotlin
- データベース：PostgreSQL
- データベースアクセスのフレームワーク：[Komapper](https://github.com/komapper/komapper)
- ビルドツール：Gradle

動作をさせるにはDockerが動く環境で以下のコマンドを実行してください。

```kotlin
./gradlew build
```