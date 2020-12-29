# kt-blockchain
kotlin(springboot) でブロックチェーンによる簡易送金システムを実装してみる

* [参考 : 現役シリコンバレーエンジニアが教えるPythonで始めるスクラッチからのブロックチェーン開発入門](https://www.udemy.com/course/python-blockchain/learn/lecture/15381470#overview)
## 起動

```aidl
# 起動
$ docker-compose up

# 停止
$ docker-compose down
```

* http://localhost:8080 でウォレット画面へ。  

* アクセス毎にキーペアとブロックチェーンアドレスが割り当てられる。  

* 複数タブ開いて「Send Money」のAddressに送信先のブロックチェーンアドレスとAmountに金額を入力し、  
Sendボタンを押下して送金する。

* http://localhost:5000/chain でマイニングに成功したブロックのアドレスと金額の取引情報が確認できる。こちらのアドレスと、コンソールに出力されたキーペアの情報をウォレット画面に入力し、Reload Walletを押下すると使用できる金額が表示される。

## 機能

### ウォレットサービス

ユーザーが使用する送金(画面)サービス。  
主に送金画面からJavaScriptで呼び出される。

#### 画面

| 画面 | 説明 | URL | ポート |
|:-----------|:------------|:------------|:------------|
| ウォレット画面 | 送金処理や残高の読み込みを行う | / | 8080 |

#### API 

| API |  メソッド | 説明 | URL | ポート |
|:-----------|:------------|:------------|:------------|:------------|
| ウォレット作成 | POST | ウォレットを作成する | /wallet| 8080 |
| トランザクション(送金)作成 | POST | 送金する | /transaction| 8080 |
| 所持金額計算 | GET | 特定のアドレスの所持金額を計算する | /wallet/amount| 8080 |

### ブロックチェーンサービス

送金(画面)サービスのバックエンド処理。  
APIは主に送金サービス・ブロックチェーンの他のノードから呼び出される。

#### API

| API |  メソッド | 説明 | URL | ポート |
|:-----------|:------------|:------------|:------------|:------------|
| ブロックチェーン一覧取得 | GET | ブロックチェーン情報の一覧を取得する | /chain| 500X |
| トランザクション一覧取得 | GET | トランザクションの一覧を取得する | /transactions| 500X |
| トランザクション新規作成(送金) | POST | トランザクションを新規作成する(送金) | /transactions| 500X |
| トランザクション新規作成(送金) | PUT | 他のノードで新規作成されたトランザクションの伝播用。 | /transactions| 500X |
| トランザクション削除 | DELETE | トランザクションを削除する。 | /transactions| 500X |
| マイニング | GET | PoWによるマイニングを行う | /mine| 500X |
| コンセンサス | GET | コンセンサスを行う。他の隣接ノードとブロックチェーンを比較し、最も長いブロックチェーンを採用する。 | /consensus| 500X |
| 所持金合計 | GET | パラメータに付与したブロックチェーンアドレスが保持する金額の合計を算出するl。 | /amount| 500X |
