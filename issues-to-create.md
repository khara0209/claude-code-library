# GitHubに追加すべきIssue一覧

以下のIssueをGitHubリポジトリ（https://github.com/khara0209/claude-code-library/issues）に追加してください。

## Issue 1: 書籍詳細ページとユーザー詳細ページの実装

**Title**: Implement book detail and user detail pages

**Body**:
```markdown
## 概要
現在、書籍とユーザーの詳細表示機能がコントローラーには実装されていますが、対応するThymeleafテンプレートが不足しています。

## 必要な作業
- [ ] `src/main/resources/templates/books/detail.html` の作成
- [ ] `src/main/resources/templates/users/detail.html` の作成
- [ ] 書籍の貸し出し履歴表示機能
- [ ] ユーザーの借用履歴表示機能

## 受け入れ基準
- 書籍詳細ページで基本情報と貸し出し状況が表示される
- ユーザー詳細ページで基本情報と借用履歴が表示される
- Bootstrapを使った統一されたデザイン
- 編集・削除ボタンからの適切な導線

## 技術仕様
- Thymeleaf `th:each` を使った履歴の一覧表示
- Bootstrap cardsを使ったレイアウト
- 日付フォーマット: `yyyy/MM/dd`

## 関連ファイル
- `src/main/kotlin/com/example/library/controller/BookController.kt:26` (detail method)
- `src/main/kotlin/com/example/library/controller/UserController.kt:26` (detail method)
```

**Labels**: `feature`, `frontend`, `good first issue`

---

## Issue 2: テストケースの追加

**Title**: Add comprehensive test cases for library management system

**Body**:
```markdown
## 概要
現在、テストコードが不足しており、アプリケーションの品質保証が不十分です。

## 必要なテスト

### Unit Tests
- [ ] Entity classes (Book, User, Loan)
- [ ] Service layer (BookService, UserService, LoanService)
- [ ] Mapper interfaces (MyBatis)

### Integration Tests
- [ ] Controller layer tests
- [ ] Database integration tests
- [ ] End-to-end workflow tests

### Test Scenarios
- [ ] 書籍の貸し出し・返却フロー
- [ ] 重複貸し出しの防止
- [ ] データ整合性の確認
- [ ] エラーハンドリングのテスト

## 技術要件
- Spring Boot Test (`@SpringBootTest`)
- MyBatis Test (`@MybatisTest`) 
- TestContainers (H2 database)
- MockMvc for controller testing
- Kotlin test framework integration

## 実装例
```kotlin
@SpringBootTest
class BookServiceTest {
    @Test
    fun `should loan book successfully when available`() {
        // テスト実装
    }
}
```

## 期待される成果
- テストカバレッジ80%以上
- CI/CDパイプラインでの自動テスト実行
- リグレッションの早期発見
```

**Labels**: `testing`, `quality`, `enhancement`

---

## Issue 3: エラーハンドリングとバリデーションの改善

**Title**: Improve error handling and validation across the application

**Body**:
```markdown
## 概要
現在のアプリケーションにはより堅牢なエラーハンドリングとバリデーション機能が必要です。

## 必要な改善

### バリデーション強化
- [ ] BookエンティティのBean Validation追加
- [ ] Userエンティティのメールアドレス重複チェック
- [ ] Loanエンティティの日付バリデーション
- [ ] フロントエンドでのリアルタイムバリデーション

### エラーハンドリング
- [ ] グローバル例外ハンドラーの実装
- [ ] カスタム例外クラスの作成
- [ ] ユーザーフレンドリーなエラーメッセージ
- [ ] エラーページのカスタマイズ

### セキュリティ向上
- [ ] SQLインジェクション対策の確認
- [ ] XSS対策の実装
- [ ] CSRF保護の有効化

## 技術実装
```kotlin
@ControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(BookNotFoundException::class)
    fun handleBookNotFound(ex: BookNotFoundException): ResponseEntity<ErrorResponse> {
        // エラーハンドリング実装
    }
}
```

## 受け入れ基準
- 全てのユーザー入力に適切なバリデーションが適用される
- エラー発生時に適切なメッセージが表示される
- セキュリティ脆弱性が解消される
```

**Labels**: `bug`, `security`, `enhancement`, `validation`

---

## Issue 4: パフォーマンス最適化とモニタリング

**Title**: Performance optimization and monitoring implementation

**Body**:
```markdown
## 概要
アプリケーションのパフォーマンス監視と最適化機能を追加します。

## パフォーマンス最適化
- [ ] データベースクエリの最適化
- [ ] N+1問題の解決（LoansWithDetails）
- [ ] ページネーション機能の追加
- [ ] キャッシュ機能の実装

### モニタリング機能
- [ ] Spring Boot Actuatorの設定
- [ ] メトリクス収集の実装
- [ ] ヘルスチェックエンドポイント
- [ ] アプリケーションログの構造化

### UI/UX改善
- [ ] 検索機能の追加
- [ ] ソート機能の実装
- [ ] レスポンシブデザインの向上
- [ ] ローディング状態の表示

## 技術実装
```kotlin
@Cacheable("books")
fun findAvailableBooks(): List<Book> {
    return bookMapper.findAvailableBooks()
}

@GetMapping("/books")
fun list(
    @RequestParam(defaultValue = "0") page: Int,
    @RequestParam(defaultValue = "10") size: Int,
    model: Model
): String {
    // ページネーション実装
}
```

## 期待される成果
- ページ読み込み時間の短縮
- 大量データでのスムーズな操作
- システム状態の可視化
```

**Labels**: `performance`, `monitoring`, `enhancement`, `ux`

---

## 追加手順

1. https://github.com/khara0209/claude-code-library/issues にアクセス
2. "New Issue" ボタンをクリック
3. 上記の各IssueのTitle、Body、Labelsをコピーして作成
4. Priority は以下の順序で設定:
   - Issue 1: High (機能完成度向上)
   - Issue 2: Medium (品質向上)
   - Issue 3: High (セキュリティ・安定性)
   - Issue 4: Low (最適化・監視)