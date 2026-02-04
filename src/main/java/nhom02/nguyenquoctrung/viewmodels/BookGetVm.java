package nhom02.nguyenquoctrung.viewmodels;

import nhom02.nguyenquoctrung.entities.Book;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record BookGetVm(Long id, String title, String author, Double price, String category, String image) {
    public static BookGetVm from(@NotNull Book book) {
        return BookGetVm.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .price(book.getPrice())
                .category(book.getCategory().getName())
                .image(book.getImage())
                .build();
    }
}