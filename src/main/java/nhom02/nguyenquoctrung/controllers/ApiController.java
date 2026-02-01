package nhom02.nguyenquoctrung.controllers;

import nhom02.nguyenquoctrung.services.BookService;
import nhom02.nguyenquoctrung.services.CartService;
import nhom02.nguyenquoctrung.services.CategoryService;
import nhom02.nguyenquoctrung.viewmodels.BookGetVm;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import nhom02.nguyenquoctrung.entities.Book;
import nhom02.nguyenquoctrung.entities.Category;
import nhom02.nguyenquoctrung.viewmodels.BookPostVm;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ApiController {
    private final BookService bookService;
    private final CategoryService categoryService;
    private final CartService cartService;

    @GetMapping("/books")
    public ResponseEntity<List<BookGetVm>> getAllBooks(Integer pageNo, Integer pageSize, String sortBy) {
        return ResponseEntity.ok(bookService.getAllBooks(
                pageNo == null ? 0 : pageNo, pageSize == null ? 20 : pageSize,
                sortBy == null ? "id" : sortBy)
                .stream()
                .map(BookGetVm::from)
                .toList());
    }

    @GetMapping("/books/{id}")
    public ResponseEntity<BookGetVm> getBookById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBookById(id)
                .map(BookGetVm::from)
                .orElse(null));
    }

    @PostMapping("/books")
    public ResponseEntity<BookGetVm> addBook(@RequestBody BookPostVm bookPostVm) {
        Category category = categoryService.getCategoryById(bookPostVm.categoryId()).orElse(null);
        if (category == null) {
            return ResponseEntity.badRequest().build();
        }
        Book book = new Book();
        book.setTitle(bookPostVm.title());
        book.setAuthor(bookPostVm.author());
        book.setPrice(bookPostVm.price());
        book.setCategory(category);
        bookService.addBook(book);
        return ResponseEntity.ok(BookGetVm.from(book));
    }

    @PutMapping("/books/{id}")
    public ResponseEntity<BookGetVm> updateBook(@PathVariable Long id, @RequestBody BookPostVm bookPostVm) {
        if (bookService.getBookById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Category category = categoryService.getCategoryById(bookPostVm.categoryId()).orElse(null);
        if (category == null) {
            return ResponseEntity.badRequest().build();
        }
        Book book = new Book();
        book.setId(id);
        book.setTitle(bookPostVm.title());
        book.setAuthor(bookPostVm.author());
        book.setPrice(bookPostVm.price());
        book.setCategory(category);
        bookService.updateBook(book);
        return ResponseEntity.ok(BookGetVm.from(book));
    }

    @DeleteMapping("/books/{id}")
    public ResponseEntity<Void> deleteBookById(@PathVariable Long id) {
        if (bookService.getBookById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        bookService.deleteBookById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/books/search")
    public ResponseEntity<List<BookGetVm>> searchBooks(String keyword) {
        return ResponseEntity.ok(bookService.searchBook(keyword)
                .stream()
                .map(BookGetVm::from)
                .toList());
    }
}
