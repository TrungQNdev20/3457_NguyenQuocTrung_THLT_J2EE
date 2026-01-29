package nhom02.nguyenquoctrung.controllers;

import nhom02.nguyenquoctrung.entities.Book;
import nhom02.nguyenquoctrung.services.BookService;
import nhom02.nguyenquoctrung.services.CategoryService;
import nhom02.nguyenquoctrung.services.CartService;
import nhom02.nguyenquoctrung.daos.Item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {
        private final BookService bookService;
        private final CategoryService categoryService;
        private final CartService cartService;

        @GetMapping
        public String showAllBooks(
                        Model model,
                        @RequestParam(defaultValue = "0") Integer pageNo,
                        @RequestParam(defaultValue = "20") Integer pageSize,
                        @RequestParam(defaultValue = "id") String sortBy) {
                model.addAttribute("books", bookService.getAllBooks(pageNo,
                                pageSize, sortBy));
                model.addAttribute("currentPage", pageNo);
                model.addAttribute("categories", categoryService.getAllCategories());
                model.addAttribute("totalPages",
                                bookService.getAllBooks(pageNo, pageSize, sortBy).size() / pageSize);
                return "book/list";
        }

        @GetMapping("/add")
        public String addBookForm(Model model) {
                model.addAttribute("book", new Book());
                model.addAttribute("categories", categoryService.getAllCategories());
                return "book/add";
        }

        @PostMapping("/add")
        public String addBook(@ModelAttribute("book") Book book, Model model) {
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
}