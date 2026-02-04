package nhom02.nguyenquoctrung.controllers;

import nhom02.nguyenquoctrung.entities.Book;
import nhom02.nguyenquoctrung.services.BookService;
import nhom02.nguyenquoctrung.services.CategoryService;
import nhom02.nguyenquoctrung.services.CartService;
import nhom02.nguyenquoctrung.daos.Item;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.*;
import java.io.IOException;
import java.util.UUID;

@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {
        private final BookService bookService;
        private final CategoryService categoryService;
        private final CartService cartService;

        @GetMapping
        public String showAllBooks(Model model,
                        @RequestParam(defaultValue = "0") Integer pageNo,
                        @RequestParam(defaultValue = "20") Integer pageSize,
                        @RequestParam(defaultValue = "id") String sortBy) {
                model.addAttribute("books", bookService.getAllBooks(pageNo,
                                pageSize, sortBy));
                model.addAttribute("currentPage", pageNo);
                model.addAttribute("totalPages",
                                bookService.getAllBooks(pageNo, pageSize, sortBy).size() / pageSize);
                model.addAttribute("categories",
                                categoryService.getAllCategories());
                return "book/list";
        }

        @GetMapping("/add")
        public String addBookForm(Model model) {
                model.addAttribute("book", new Book());
                model.addAttribute("categories",
                                categoryService.getAllCategories());
                return "book/add";
        }

        @PostMapping("/add")
        public String addBook(
                        @Valid @ModelAttribute("book") Book book,
                        BindingResult bindingResult,
                        @RequestParam("imageFile") MultipartFile imageFile,
                        Model model) {
                if (bindingResult.hasErrors()) {
                        var errors = bindingResult.getAllErrors()
                                        .stream()
                                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                                        .toArray(String[]::new);
                        model.addAttribute("errors", errors);
                        model.addAttribute("categories",
                                        categoryService.getAllCategories());
                        return "book/add";
                }
                if (!imageFile.isEmpty()) {
                        try {
                                String fileName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
                                Path path = Paths.get("src/main/resources/static/img");
                                Files.createDirectories(path);
                                Files.copy(imageFile.getInputStream(), path.resolve(fileName),
                                                StandardCopyOption.REPLACE_EXISTING);
                                book.setImage("img/" + fileName);
                        } catch (IOException e) {
                                e.printStackTrace();
                        }
                }
                bookService.addBook(book);
                return "redirect:/books";
        }

        @PostMapping("/add-to-cart")
        public String addToCart(HttpSession session,
                        @RequestParam long id,
                        @RequestParam String name,
                        @RequestParam double price,
                        @RequestParam(defaultValue = "1") int quantity) {
                var cart = cartService.getCart(session);
                cart.addItems(new Item(id, name, price, quantity));
                cartService.updateCart(session, cart);
                return "redirect:/books";
        }

        @GetMapping("/delete/{id}")
        public String deleteBook(@PathVariable long id) {
                bookService.getBookById(id)
                                .ifPresentOrElse(
                                                book -> bookService.deleteBookById(id),
                                                () -> {
                                                        throw new IllegalArgumentException("Book notfound");
                                                });
                return "redirect:/books";
        }

        @GetMapping("/edit/{id}")
        public String editBookForm(Model model, @PathVariable long id) {
                var book = bookService.getBookById(id);
                model.addAttribute("book", book.orElseThrow(() -> new IllegalArgumentException("Book not found")));
                model.addAttribute("categories",
                                categoryService.getAllCategories());
                return "book/edit";
        }

        @PostMapping("/edit")
        public String editBook(@Valid @ModelAttribute("book") Book book,
                        BindingResult bindingResult,
                        @RequestParam("imageFile") MultipartFile imageFile,
                        Model model) {
                if (bindingResult.hasErrors()) {
                        var errors = bindingResult.getAllErrors()
                                        .stream()
                                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                                        .toArray(String[]::new);
                        model.addAttribute("errors", errors);
                        model.addAttribute("categories",
                                        categoryService.getAllCategories());
                        return "book/edit";
                }
                if (!imageFile.isEmpty()) {
                        try {
                                String fileName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
                                Path path = Paths.get("src/main/resources/static/img");
                                Files.createDirectories(path);
                                Files.copy(imageFile.getInputStream(), path.resolve(fileName),
                                                StandardCopyOption.REPLACE_EXISTING);
                                book.setImage("img/" + fileName);
                        } catch (IOException e) {
                                e.printStackTrace();
                        }
                }
                bookService.updateBook(book);
                return "redirect:/books";
        }

        @GetMapping("/search")
        public String searchBook(
                        Model model,
                        @RequestParam String keyword,
                        @RequestParam(defaultValue = "0") Integer pageNo,
                        @RequestParam(defaultValue = "20") Integer pageSize,
                        @RequestParam(defaultValue = "id") String sortBy) {
                model.addAttribute("books", bookService.searchBook(keyword));
                model.addAttribute("currentPage", pageNo);
                model.addAttribute("totalPages",
                                bookService
                                                .getAllBooks(pageNo, pageSize, sortBy)
                                                .size() / pageSize);
                model.addAttribute("categories",
                                categoryService.getAllCategories());
                return "book/list";
        }
}