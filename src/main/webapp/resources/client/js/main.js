(function ($) {
    "use strict";

    // Spinner
    var spinner = function () {
        setTimeout(function () {
            if ($('#spinner').length > 0) {
                $('#spinner').removeClass('show');
            }
        }, 1);
    };
    spinner(0);


    // Fixed Navbar
    $(window).scroll(function () {
        if ($(window).width() < 992) {
            if ($(this).scrollTop() > 55) {
                $('.fixed-top').addClass('shadow');
            } else {
                $('.fixed-top').removeClass('shadow');
            }
        } else {
            if ($(this).scrollTop() > 55) {
                $('.fixed-top').addClass('shadow').css('top', 0);
            } else {
                $('.fixed-top').removeClass('shadow').css('top', 0);
            }
        } 
    });
    
    
   // Back to top button
   $(window).scroll(function () {
    if ($(this).scrollTop() > 300) {
        $('.back-to-top').fadeIn('slow');
    } else {
        $('.back-to-top').fadeOut('slow');
    }
    });
    $('.back-to-top').click(function () {
        $('html, body').animate({scrollTop: 0}, 1500, 'easeInOutExpo');
        return false;
    });


    // Testimonial carousel
    $(".testimonial-carousel").owlCarousel({
        autoplay: true,
        smartSpeed: 2000,
        center: false,
        dots: true,
        loop: true,
        margin: 25,
        nav : true,
        navText : [
            '<i class="bi bi-arrow-left"></i>',
            '<i class="bi bi-arrow-right"></i>'
        ],
        responsiveClass: true,
        responsive: {
            0:{
                items:1
            },
            576:{
                items:1
            },
            768:{
                items:1
            },
            992:{
                items:2
            },
            1200:{
                items:2
            }
        }
    });


    // vegetable carousel
    $(".vegetable-carousel").owlCarousel({
        autoplay: true,
        smartSpeed: 1500,
        center: false,
        dots: true,
        loop: true,
        margin: 25,
        nav : true,
        navText : [
            '<i class="bi bi-arrow-left"></i>',
            '<i class="bi bi-arrow-right"></i>'
        ],
        responsiveClass: true,
        responsive: {
            0:{
                items:1
            },
            576:{
                items:1
            },
            768:{
                items:2
            },
            992:{
                items:3
            },
            1200:{
                items:4
            }
        }
    });


    // Modal Video
    $(document).ready(function () {
        var $videoSrc;
        $('.btn-play').click(function () {
            $videoSrc = $(this).data("src");
        });
        console.log($videoSrc);

        $('#videoModal').on('shown.bs.modal', function (e) {
            $("#video").attr('src', $videoSrc + "?autoplay=1&amp;modestbranding=1&amp;showinfo=0");
        })

        $('#videoModal').on('hide.bs.modal', function (e) {
            $("#video").attr('src', $videoSrc);
        })
    });



    // Product Quantity
    // $('.quantity button').on('click', function () {
    //     var button = $(this);
    //     var oldValue = button.parent().parent().find('input').val();
    //     if (button.hasClass('btn-plus')) {
    //         var newVal = parseFloat(oldValue) + 1;
    //     } else {
    //         if (oldValue > 0) {
    //             var newVal = parseFloat(oldValue) - 1;
    //         } else {
    //             newVal = 0;
    //         }
    //     }
    //     button.parent().parent().find('input').val(newVal);
    // });


//     Bắt sự kiện click vào nút tăng/giảm
// Khi bạn bấm nút "+" hoặc "-" ở cột số lượng, hàm này sẽ chạy.
        $('.quantity button').on('click', function () {
    let change = 0;
    var button = $(this);

// Tính số lượng mới

// Nếu bấm "+", số lượng tăng lên 1.
// Nếu bấm "-", số lượng giảm đi 1 (nhưng không nhỏ hơn 1).
// Gán lại giá trị mới vào ô input.
    var oldValue = button.parent().parent().find('input').val();
    if (button.hasClass('btn-plus')) {
    var newVal = parseFloat(oldValue) + 1;
    change = 1;
    } else {
        if (oldValue > 1) {
        var newVal = parseFloat(oldValue) - 1;
        change = -1;
        } else {
        newVal = 1;
}
    }
    const input = button.parent().parent().find('input');
    input.val(newVal);

//. Cập nhật giá tiền từng dòng

//  Lấy giá sản phẩm và id của dòng hiện tại.
// Tính lại tổng tiền của dòng đó = giá * số lượng mới.
// Hiển thị lại giá tiền mới cho dòng đó.
    const price = input.attr("data-cart-detail-price");
    const id = input.attr("data-cart-detail-id");

    const priceElement = $(`p[data-cart-detail-id='${id}']`);
    if (priceElement) {
    const newPrice = +price* newVal;
    priceElement.text(formatCurrency (newPrice.toFixed(2)) + "d");
    }

//Cập nhật tổng tiền giỏ hàng

// Lấy tổng tiền hiện tại.
// Nếu tăng/giảm, cộng/trừ thêm giá của sản phẩm tương ứng.
// Cập nhật lại tổng tiền trên giao diện.
    const totalPriceElement = $(`p[data-cart-total-price]`);

    if (totalPriceElement && totalPriceElement.length) {
        const currentTotal = totalPriceElement.first().attr("data-cart-total-price");
        let newTotal = +currentTotal;
        if (change === 0) {
        newTotal = +currentTotal;
        } else {
        newTotal = change* (+price) + +currentTotal;
    }

    //reset change
    change = 0;

    //update
    totalPriceElement?.each(function (index, element) {
    //update text
    $(totalPriceElement[index]).text(formatCurrency (newTotal.toFixed(2)) + "đ" )

    //update data-attribute
    $(totalPriceElement [index]).attr("data-cart-total-price", newTotal);

    });
}

    });

//Định dạng số tiền theo kiểu Việt Nam (dùng dấu phẩy).

    function formatCurrency (value) {
    // Use the 'vi-VN' locale to format the number according to Vietnamese currency
    // and 'VND' as the currency type for Vietnamese đồng
    const formatter = new Intl.NumberFormat('vi-VN', {
            style: 'decimal',
            minimumFractionDigits: 0, // No decimal part for whole numbers
    });

    let formatted = formatter.format(value);
    // Replace dots with commas for thousands separator
    formatted = formatted.replace(/\./g,',');
    return formatted;

//     Tóm lại :
// Khi bạn bấm nút tăng/giảm số lượng:
// Số lượng sản phẩm sẽ thay đổi.
// Giá tiền của dòng đó sẽ cập nhật lại.
// Tổng tiền giỏ hàng cũng cập nhật lại ngay lập tức trên giao diện.
// Tất cả đều xử lý bằng JavaScript, không cần reload trang.
// Nếu bạn muốn cập nhật số lượng thực tế trong database, bạn cần gọi thêm Ajax về server khi bấm nút.
// Đoạn này chỉ xử lý giao diện (frontend).

    }



})(jQuery);

