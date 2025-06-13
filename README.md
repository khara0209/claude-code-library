# 図書館管理システム

Kotlin + Spring Boot 3 + H2 Database + MyBatis + Thymeleaf + Bootstrap を使用した図書館の書籍貸し出し管理アプリケーションです。

## 機能

- 📚 **書籍管理**: 書籍の登録・編集・削除・一覧表示
- 👥 **ユーザー管理**: 利用者の登録・編集・削除・一覧表示
- 🔄 **貸し出し管理**: 書籍の貸し出し・返却処理
- 📊 **ダッシュボード**: 統計情報の表示
- 📱 **レスポンシブデザイン**: Bootstrap 5を使用したモバイル対応

## 技術スタック

- **言語**: Kotlin 1.9.20
- **フレームワーク**: Spring Boot 3.2.0
- **データベース**: H2 Database (インメモリ)
- **ORM**: MyBatis 3.0.3
- **テンプレートエンジン**: Thymeleaf
- **フロントエンド**: Bootstrap 5.3.0
- **ビルドツール**: Gradle 8.5

## 前提条件

- Java 17以上
- Gradle 8.5以上（または Maven 3.6以上）

## セットアップ

### 1. リポジトリのクローン

```bash
git clone <repository-url>
cd claude-code-library
```

### 2. アプリケーションの起動

#### Gradleを使用する場合
```bash
./gradlew bootRun
```

#### Mavenを使用する場合
```bash
mvn spring-boot:run
```

### 3. アクセス

- **メインページ**: http://localhost:8080
- **H2データベースコンソール**: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:librarydb`
  - Username: `sa`
  - Password: (空欄)

## 使用方法

### 初期データ

アプリケーション起動時に以下のサンプルデータが登録されます：

**書籍**
- Kotlin in Action
- Spring Boot in Action
- Clean Code
- Design Patterns
- Effective Java

**ユーザー**
- 田中太郎 (tanaka@example.com)
- 佐藤花子 (sato@example.com)
- 鈴木一郎 (suzuki@example.com)

### 基本操作

1. **書籍の追加**: [書籍管理] → [新規追加]
2. **ユーザーの追加**: [ユーザー管理] → [新規追加]
3. **書籍の貸し出し**: [貸し出し管理] → [新規貸し出し]
4. **書籍の返却**: [貸し出し管理] → 該当記録の[返却]ボタン

## プロジェクト構造

```
src/
├── main/
│   ├── kotlin/com/example/library/
│   │   ├── controller/          # Webコントローラー
│   │   ├── entity/              # エンティティクラス
│   │   ├── mapper/              # MyBatisマッパー
│   │   ├── service/             # サービス層
│   │   ├── config/              # 設定クラス
│   │   └── LibraryApplication.kt # メインクラス
│   └── resources/
│       ├── templates/           # Thymeleafテンプレート
│       ├── static/              # 静的リソース
│       ├── schema.sql           # データベーススキーマ
│       ├── data.sql             # 初期データ
│       └── application.yml      # アプリケーション設定
└── test/                        # テストコード
```

## ログ

アプリケーションのログは以下に出力されます：
- ファイル: `logs/library-app.log`
- コンソール: 標準出力

ログレベルの設定により、詳細なデバッグ情報も確認できます。

## データベーススキーマ

### books テーブル
| カラム名 | 型 | 説明 |
|---------|-----|------|
| id | BIGINT | 主キー |
| title | VARCHAR(255) | 書籍タイトル |
| author | VARCHAR(255) | 著者 |
| isbn | VARCHAR(20) | ISBN |
| publisher | VARCHAR(255) | 出版社 |
| published_year | INT | 出版年 |
| category | VARCHAR(100) | カテゴリ |
| is_available | BOOLEAN | 貸出可能フラグ |
| created_at | TIMESTAMP | 作成日時 |
| updated_at | TIMESTAMP | 更新日時 |

### users テーブル
| カラム名 | 型 | 説明 |
|---------|-----|------|
| id | BIGINT | 主キー |
| name | VARCHAR(255) | ユーザー名 |
| email | VARCHAR(255) | メールアドレス |
| phone | VARCHAR(20) | 電話番号 |
| created_at | TIMESTAMP | 作成日時 |
| updated_at | TIMESTAMP | 更新日時 |

### loans テーブル
| カラム名 | 型 | 説明 |
|---------|-----|------|
| id | BIGINT | 主キー |
| book_id | BIGINT | 書籍ID (外部キー) |
| user_id | BIGINT | ユーザーID (外部キー) |
| loan_date | DATE | 貸出日 |
| due_date | DATE | 返却予定日 |
| return_date | DATE | 返却日 |
| status | VARCHAR(20) | 状態 (ACTIVE/RETURNED) |
| created_at | TIMESTAMP | 作成日時 |
| updated_at | TIMESTAMP | 更新日時 |

## 開発

### コードスタイル
- Kotlinの標準的なコーディング規約に従います
- データクラスを活用したシンプルな設計
- Spring Bootのベストプラクティスを採用

### テスト実行
```bash
./gradlew test
```

### ビルド
```bash
./gradlew build
```

## ライセンス

このプロジェクトはMITライセンスの下で公開されています。

## 貢献

バグ報告や機能追加の提案は、GitHubのIssueまでお願いします。

## 作者

Claude Code Library Project