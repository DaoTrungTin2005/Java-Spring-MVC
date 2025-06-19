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
    //     Bắt sự kiện click vào nút tăng/giảm
    // Khi bạn bấm nút "+" hoặc "-" ở cột số lượng, hàm này sẽ chạy.
    $('.quantity button').on('click', function () {
        let change = 0;
        var button = $(this);

        // Tính số lượng mới
        // Nếu bấm "+", số lượng tăng lên 1.
        // Nếu bấm "-", số lượng giảm đi 1 (nhưng không nhỏ hơn 1).
        // Gán lại giá trị mới vào ô input.
        // Lấy input hiển thị trong bảng giỏ hàng
        const displayInput = button.parent().parent().find('input'); // Đây là input type="text" trong bảng
        var oldValue = parseFloat(displayInput.val());

        if (button.hasClass('btn-plus')) {
            var newVal = oldValue + 1;
            change = 1;
        } else {
            if (oldValue > 1) {
                var newVal = oldValue - 1;
                change = -1;
            } else {
                newVal = 1;
            }
        }
        displayInput.val(newVal); // Cập nhật giá trị hiển thị

        //set form index
        const index = displayInput.attr("data-cart-detail-index");
        // **Cập nhật ID cho đúng với format của Spring MVC và ID bạn đã đặt:**
        // Tìm thẻ <form:input> ẩn (hoặc hiển thị) dùng để gửi dữ liệu

        // JavaScript cập nhật giá trị quantity trong form ẩn khi người dùng thay đổi số lượng
        // Sử dụng index để xác định đúng input cần cập nhật
        const hiddenFormInput = document.getElementById(`cartDetails${index}__quantity`);
        if (hiddenFormInput) {
            hiddenFormInput.value = newVal;
        }

        //. Cập nhật giá tiền từng dòng
        //  Lấy giá sản phẩm và id của dòng hiện tại.
        // Tính lại tổng tiền của dòng đó = giá * số lượng mới.
        // Hiển thị lại giá tiền mới cho dòng đó.
        const price = displayInput.attr("data-cart-detail-price");
        const id = displayInput.attr("data-cart-detail-id");

        const priceElement = $(`p[data-cart-detail-id='${id}']`);
        if (priceElement.length) { // Kiểm tra nếu phần tử tồn tại
            const newPrice = +price * newVal;
            priceElement.text(formatCurrency(newPrice.toFixed(2)) + "đ"); // Đã sửa "d" thành "đ" cho đúng tiền Việt
        }

        //Cập nhật tổng tiền giỏ hàng
        // Lấy tổng tiền hiện tại.
        // Nếu tăng/giảm, cộng/trừ thêm giá của sản phẩm tương ứng.
        // Cập nhật lại tổng tiền trên giao diện.
        const totalPriceElement = $(`p[data-cart-total-price]`);

        if (totalPriceElement && totalPriceElement.length) {
            // Lấy tổng tiền hiện tại từ thuộc tính data-cart-total-price của phần tử đầu tiên
            const currentTotal = parseFloat(totalPriceElement.first().attr("data-cart-total-price"));
            let newTotal = currentTotal + (change * (+price));

            // Đảm bảo tổng tiền không âm nếu giá trị ban đầu là 0 hoặc rất nhỏ và bị trừ đi
            if (newTotal < 0) {
                newTotal = 0;
            }
            
            //reset change
            change = 0; // reset change sau khi đã dùng để tính newTotal

            //update
            totalPriceElement.each(function (idx, element) {
                //update text
                $(element).text(formatCurrency(newTotal.toFixed(2)) + "đ"); // Đã sửa "d" thành "đ"
                //update data-attribute
                $(element).attr("data-cart-total-price", newTotal);
            });
        }
    });

    //Định dạng số tiền theo kiểu Việt Nam (dùng dấu phẩy).
    function formatCurrency(value) {
        const formatter = new Intl.NumberFormat('vi-VN', {
            style: 'decimal',
            minimumFractionDigits: 0, 
        });

        let formatted = formatter.format(value);
        return formatted;
    }

})(jQuery);