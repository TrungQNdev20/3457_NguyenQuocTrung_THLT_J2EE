$(document).ready(function () {
    // Check if the user has admin role from data attribute
    var isAdmin = $('#book-grid').data('is-admin');

    // Call API to get list of books
    $.ajax({
        url: '/api/v1/books',
        type: 'GET',
        dataType: 'json',
        success: function (data) {
            let cardsHTML = '';
            $.each(data, function (i, item) {
                // Determine a placeholder letter or icon
                let placeholderChar = item.title ? item.title.charAt(0).toUpperCase() : 'B';

                cardsHTML += '<div class="col-md-4 col-lg-3 mb-4" id="book-' + item.id + '">';
                cardsHTML += '<div class="book-card">';

                // Image or Placeholder
                if (item.image) {
                    cardsHTML += '<div class="book-image-container" style="height: 200px; overflow: hidden; border-radius: 8px; margin-bottom: 1rem; background: #f3f4f6;">';
                    cardsHTML += '<img src="/' + item.image + '" alt="' + item.title + '" style="width: 100%; height: 100%; object-fit: cover;">';
                    cardsHTML += '</div>';
                } else {
                    cardsHTML += '<div class="book-image-placeholder">' + placeholderChar + '</div>';
                }

                // Content
                cardsHTML += '<div class="book-title" title="' + item.title + '">' + truncate(item.title, 40) + '</div>';
                cardsHTML += '<div class="book-author">' + item.author + '</div>';

                // Price - Formatting it nicely
                cardsHTML += '<div class="book-price">$' + item.price + '</div>';

                // Actions Container
                cardsHTML += '<div class="book-actions flex-column">';

                // Add to Cart button (visible to all) - Form
                cardsHTML += '<form action="/books/add-to-cart" method="post" class="w-100 mb-2">';
                if (typeof csrfToken !== 'undefined' && typeof csrfParam !== 'undefined') {
                    cardsHTML += '<input type="hidden" name="' + csrfParam + '" value="' + csrfToken + '">';
                }
                cardsHTML += '<input type="hidden" name="id" value="' + item.id + '">';
                cardsHTML += '<input type="hidden" name="name" value="' + item.title + '">';
                cardsHTML += '<input type="hidden" name="price" value="' + item.price + '">';
                cardsHTML += '<button type="submit" class="btn btn-primary w-100" onclick="return confirm(\'Add to Cart?\')">Add to Cart</button>';
                cardsHTML += '</form>';

                // Admin buttons
                if (isAdmin) {
                    cardsHTML += '<div class="d-flex gap-2 w-100">';
                    cardsHTML += '<a href="/books/edit/' + item.id + '" class="btn btn-outline-primary flex-grow-1 btn-sm">Edit</a>';
                    cardsHTML += '<a href="javascript:void(0)" class="btn btn-outline-danger flex-grow-1 btn-sm" onclick="apiDeleteBook(' + item.id + '); return false;">Delete</a>';
                    cardsHTML += '</div>';
                }

                cardsHTML += '</div>'; // End actions
                cardsHTML += '</div>'; // End card
                cardsHTML += '</div>'; // End col
            });
            $('#book-grid').append(cardsHTML);
        },
        error: function (xhr, status, error) {
            console.error("Error fetching books:", error);
            $('#book-grid').html('<div class="col-12 text-center text-danger">Failed to load books.</div>');
        }
    });
});

function apiDeleteBook(id) {
    if (confirm('Are you sure you want to delete this book?')) {
        $.ajax({
            url: '/api/v1/books/' + id,
            type: 'DELETE',
            beforeSend: function (xhr) {
                if (typeof csrfHeader !== 'undefined' && typeof csrfToken !== 'undefined') {
                    xhr.setRequestHeader(csrfHeader, csrfToken);
                }
            },
            success: function () {
                // Remove the card with animation
                $('#book-' + id).fadeOut(300, function () { $(this).remove(); });
            },
            error: function (xhr, status, error) {
                alert('Error deleting book: ' + xhr.status);
            }
        });
    }
}

function truncate(str, n) {
    return (str.length > n) ? str.substr(0, n - 1) + '&hellip;' : str;
}
