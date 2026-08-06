# テーブル定義書

- **データベースシステム:** PostgreSQL
- **接続情報:** リポジトリルートの README.md「データベースへのアクセス」を参照

## テーブル一覧

| 論理テーブル名 | 物理テーブル名        | 備考             |
| -------------- | --------------------- | ---------------- |
| ユーザ情報     | T_USER                | 認証に使用する。 |
| 加入者情報     | T_MEMBER              | 加入者を管理する。 |
| 料金情報       | T_CHARGE              | 料金を管理する。 |
| 請求データ     | T_BILLING_DATA        | 請求情報を保管する。     |
| 請求明細データ | T_BILLING_DETAIL_DATA | 請求の詳細情報を保管する。 |
| 請求データ状況 | T_BILLING_STATUS      | 請求情報の確定状況を管理する。 |

## ユーザ情報◆T_USER

論理テーブル名: ユーザ情報

物理テーブル名: T_USER

凡例：P=主キー、N=NULL許可、U=ユニーク

| No. | 論理列名     | 物理列名 | データ型     | 制約 | デフォルト値 | 備考                                       |
| --- | ------------ | -------- | ------------ | ---- | ------------ | ------------------------------------------ |
| 1   | ユーザ名     | username | VARCHAR(255) | P    |              | ログインID                                 |
| 2   | パスワード   | password | VARCHAR(255) |      |              | Argon2idでハッシュ化した文字列             |
| 3   | 有効フラグ   | enabled  | BOOLEAN      |      |              | TRUE: ログイン可<br>FALSE: ログイン不可    |

**補足**

- ログイン画面で入力されたユーザ名でこのテーブルを検索し、パスワードのハッシュ値を照合して認証する。
- パスワードのハッシュ化にはSpring Securityの`Argon2PasswordEncoder`（`defaultsForSpringSecurity_v5_8`）を使用する。平文のパスワードは格納しない。
- 他のテーブルと異なり、レコード作成日・レコード更新日の列は持たない。

## 加入者情報◆T_MEMBER

論理テーブル名: 加入者情報

物理テーブル名: T_MEMBER

凡例：P=主キー、N=NULL許可、U=ユニーク

| No. | 論理列名       | 物理列名       | データ型     | 制約 | デフォルト値      | 備考                          |
| --- | -------------- | -------------- | ------------ | ---- | ----------------- | ----------------------------- |
| 1   | 加入者ID       | member_id      | BIGINT       | P    |                   | シーケンス`t_member_seq`で採番 |
| 2   | メールアドレス | mail           | VARCHAR(255) | U    |                   |                               |
| 3   | 氏名           | name           | VARCHAR(31)  |      |                   |                               |
| 4   | 住所           | address        | VARCHAR(127) |      |                   |                               |
| 5   | 加入日         | start_date     | DATE         |      |                   |                               |
| 6   | 解約日         | end_date       | DATE         | N    |                   |                               |
| 7   | 決済方法       | payment_method | INT          |      |                   | 1: クレジット<br>2: 銀行振込 |
| 8   | レコード作成日 | created_at     | TIMESTAMP    |      | CURRENT_TIMESTAMP |                               |
| 9   | レコード更新日 | modified_at    | TIMESTAMP    |      | CURRENT_TIMESTAMP |                               |

**補足**

- `member_id`は自動採番列(SERIAL/IDENTITY)ではない。登録時にシーケンス`t_member_seq`から`nextval`で採番した値を明示的に設定する。

## 料金情報◆T_CHARGE

論理テーブル名: 料金情報

物理テーブル名: T_CHARGE

凡例：P=主キー、N=NULL許可、U=ユニーク

| No. | 論理列名       | 物理列名    | データ型      | 制約 | デフォルト値      | 備考 |
| --- | -------------- | ----------- | ------------- | ---- | ----------------- | ---- |
| 1   | 料金ID         | charge_id   | BIGINT        | P    |                   | シーケンス`t_charge_seq`で採番 |
| 2   | 料金名         | name        | VARCHAR(127)  |      |                   |      |
| 3   | 月額料金       | amount      | NUMERIC(9,0)  |      |                   |      |
| 4   | 適用開始日     | start_date  | DATE          |      |                   |      |
| 5   | 適用終了日     | end_date    | DATE          | N    |                   |      |
| 6   | レコード作成日 | created_at  | TIMESTAMP     |      | CURRENT_TIMESTAMP |      |
| 7   | レコード更新日 | modified_at | TIMESTAMP     |      | CURRENT_TIMESTAMP |      |

**補足**

- `charge_id`は自動採番列(SERIAL/IDENTITY)ではない。登録時にシーケンス`t_charge_seq`から`nextval`で採番した値を明示的に設定する。

## 請求データ◆T_BILLING_DATA

論理テーブル名: 請求データ

物理テーブル名: T_BILLING_DATA

凡例：P=主キー、N=NULL許可、U=ユニーク

| No. | 論理列名       | 物理列名       | データ型      | 制約 | デフォルト値      | 備考                                          |
| --- | -------------- | -------------- | ------------- | ---- | ----------------- | --------------------------------------------- |
| 1   | 請求年月       | billing_ym     | DATE          | P    |                   | 日は01固定<br>例: 2023-08-01  |
| 2   | 加入者ID       | member_id      | BIGINT        | P    |                   |                            |
| 3   | メールアドレス | mail           | VARCHAR(255)  |      |                   |                            |
| 4   | 氏名           | name           | VARCHAR(31)   |      |                   |                            |
| 5   | 住所           | address        | VARCHAR(127)  |      |                   |                            |
| 6   | 加入日         | start_date     | DATE          |      |                   |                            |
| 7   | 解約日         | end_date       | DATE          | N    |                   |                            |
| 8   | 決済方法       | payment_method | INT           |      |                   |                            |
| 9   | 請求金額       | amount         | NUMERIC(10,0) |      |                   | 税抜き金額                 |
| 10  | 消費税率       | tax_ratio      | NUMERIC(5,2)  |      |                   |                            |
| 11  | 請求総額       | total          | NUMERIC(10,0) |      |                   | 請求金額＋消費税額         |
| 12  | レコード作成日 | created_at     | TIMESTAMP     |      | CURRENT_TIMESTAMP |                            |
| 13  | レコード更新日 | modified_at    | TIMESTAMP     |      | CURRENT_TIMESTAMP |                            |

**外部キー制約**

- `billing_ym` → T_BILLING_STATUS(`billing_ym`)

## 請求明細データ◆T_BILLING_DETAIL_DATA

論理テーブル名: 請求明細データ

物理テーブル名: T_BILLING_DETAIL_DATA

凡例：P=主キー、N=NULL許可、U=ユニーク

| No. | 論理列名       | 物理列名    | データ型      | 制約 | デフォルト値      | 備考                                      |
| --- | -------------- | ----------- | ------------- | ---- | ----------------- | ----------------------------------------- |
| 1   | 請求年月       | billing_ym  | DATE          | P    |                   | 日は01固定<br>例: 2023-08-01 |
| 2   | 加入者ID       | member_id   | BIGINT        | P    |                   |                           |
| 3   | 料金ID         | charge_id   | BIGINT        | P    |                   |                           |
| 4   | 料金名         | name        | VARCHAR(127)  |      |                   |                           |
| 5   | 月額料金       | amount      | NUMERIC(9,0)  |      |                   |                           |
| 6   | 適用開始日     | start_date  | DATE          |      |                   |                           |
| 7   | 適用終了日     | end_date    | DATE          | N    |                   |                           |
| 8   | レコード作成日 | created_at  | TIMESTAMP     |      | CURRENT_TIMESTAMP |                           |
| 9   | レコード更新日 | modified_at | TIMESTAMP     |      | CURRENT_TIMESTAMP |                           |

**外部キー制約**

- `(billing_ym, member_id)` → T_BILLING_DATA(`billing_ym`, `member_id`)

## 請求データ状況◆T_BILLING_STATUS

論理テーブル名: 請求データ状況

物理テーブル名: T_BILLING_STATUS

凡例：P=主キー、N=NULL許可、U=ユニーク

| No. | 論理列名       | 物理列名    | データ型  | 制約 | デフォルト値      | 備考                      |
| --- | -------------- | ----------- | --------- | ---- | ----------------- | ------------------------- |
| 1   | 請求年月       | billing_ym  | DATE      | P    |                   | 日は01固定<br>例: 2023-08-01 |
| 2   | 確定           | is_commit   | BOOLEAN   |      | FALSE             |                           |
| 3   | レコード作成日 | created_at  | TIMESTAMP |      | CURRENT_TIMESTAMP |                           |
| 4   | レコード更新日 | modified_at | TIMESTAMP |      | CURRENT_TIMESTAMP |                           |
