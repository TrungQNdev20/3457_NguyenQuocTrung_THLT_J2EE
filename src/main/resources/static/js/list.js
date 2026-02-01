$(document).ready(function () {
    // Check if the user has admin role from data attribute
    var isAdmin = $('#book-table').data('is-admin');

    // Call API to get list of books
    $.ajax({
        url: '/api/v1/books',
        type: 'GET',
        dataType: 'json',
        success: function (data) {
            let trHTML = '';
            $.each(data, function (i, item) {
                trHTML += '<tr id="book-' + item.id + '">' +
                    '<td>' + item.id + '</td>' +
                    '<td>' + item.title + '</td>' +
                    '<td>' + item.author + '</td>' +
                    '<td>' + item.price + '</td>' +
                    '<td>' + item.category + '</td>' +
                    '<td>';

                // Admin buttons
                if (isAdmin) {
                    trHTML += '<a href="/books/edit/' + item.id + '" class="btn btn-success btn-sm">Edit</a> ' +
                        '<a href="javascript:void(0)" class="btn btn-danger btn-sm" onclick="apiDeleteBook(' + item.id + '); return false;">Delete</a> ';
                }

                // Add to Cart button (visible to all)
                trHTML += '<form action="/books/add-to-cart" method="post" class="d-inline">';

                if (typeof csrfToken !== 'undefined' && typeof csrfParam !== 'undefined') {
                    trHTML += '<input type="hidden" name="' + csrfParam + '" value="' + csrfToken + '">';
                }

                trHTML += '<input type="hidden" name="id" value="' + item.id + '">' +
                    '<input type="hidden" name="name" value="' + item.title + '">' +
                    '<input type="hidden" name="price" value="' + item.price + '">' +
                    '<button type="submit" class="btn btn-primary btn-sm" onclick="return confirm(\'Are you sure you want to add this book to cart?\')">Add to Cart</button>' +
                    '</form>' +
                    '</td>' +
                    '</tr>';
            });
            $('#book-table-body').append(trHTML);
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
                alert('Book deleted successfully!');
                $('#book-' + id).remove();
            },
            error: function (xhr, status, error) {
                alert('Error deleting book: ' + xhr.status);
            }
        });
    }
}
