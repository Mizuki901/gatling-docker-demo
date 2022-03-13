# gatling-docker-demo

GatlingをDockerイメージで動作させるためのデモ用リポジトリ。

## ディレクトリ構成

```
.
├── docker-compose.yml # Gatlingやその他のコンテナを起動する
├── gatling/ # Gatlingの環境構築、シナリオ、設定などをコードで管理する
│   ├── Dockerfile
│   ├── conf/ # 設定ファイルを管理するディレクトリ
│   ├── user-files/ # 負荷テストのシナリオをコードで管理するディレクトリ
│   ├── results/ # テスト結果のレポートが格納されるディレクトリ
│   └── ...
├── nginx/ # Gatlingの実行結果をブラウザで確認できるようにnginxを利用している
├── mock/ # 負荷テストを実行する対象のモックサーバ
:
```

## 環境構築

dockerコマンドが使用できる環境で以下を実行する。

### イメージのビルド

```
$ docker-compose build
```

### コンテナの作成

```
$ docker-compose up -d
```

### コンテナにログイン

```
$ docker-compose exec gatling /bin/bash --login
```

ログイン後は、ホームディレクトリ `/opt/gatling` に自動的に遷移される。

## Gatlingの実行方法

`/opt/gatling` ディレクトリで以下を実行する。

```
/opt/gatling# bin/gatling.sh

Choose a simulation number:
     [0] XxxSimulation01
     [1] XxxSimulation02
     ...
0 # 流したいシナリオの番号を入力
Select run description (optional)
[Enter] # 追加でしたいしたいオプションがなければそのままEnterを入力
```

その後、選択したシナリオが実行される。

## 実行結果の確認

http://{サーバーのIP or localhost}:3000 にアクセスして、各種レポートを確認できる。

* `{シナリオ名}-{実行日時}/` ：HTMLレポート
* `logs/` ：リクエスト・レスポンスの詳細なログ
