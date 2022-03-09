# gatling-docker-demo

GatlingをDockerイメージで動作させるためのデモ用リポジトリ。

## ディレクトリ構成

```
.
├── Dockerfile # Gatlingを使うためのDockerfile
├── docker-compose.yml # Gatlingとnginxのコンテナを起動する
├── conf # Gatlingの設定ファイル
│   └── ...
├── user-files # Gatlingのシナリオをコードで管理するディレクトリ
│   └── simulations
│       └── ...
├── results # Gatlingの実行結果のHTMLレポート／リクエストログが格納されるディレクトリ
│   └── ...
├── nginx # Gatlingの実行結果をブラウザで確認できるようにする
│   └── default.conf
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
