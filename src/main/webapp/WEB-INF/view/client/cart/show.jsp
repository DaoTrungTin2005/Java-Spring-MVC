<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 
        <!DOCTYPE html>
        <html lang="en">

        <head>
            <meta charset="utf-8">
            <title>${product.name} - Laptopshop</title>
            <meta content="width=device-width, initial-scale=1.0" name="viewport">
            <meta content="" name="keywords">
            <meta content="" name="description">

            <!-- Google Web Fonts -->
            <link rel="preconnect" href="https://fonts.googleapis.com">
            <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
            <link
                href="https://fonts.googleapis.com/css2?family=Open+Sans:wght@400;600&family=Raleway:wght@600;800&display=swap"
                rel="stylesheet">

            <!-- Icon Font Stylesheet -->
            <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.15.4/css/all.css" />
            <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.4.1/font/bootstrap-icons.css" rel="stylesheet">

            <!-- Libraries Stylesheet -->
            <link href="/client/lib/lightbox/css/lightbox.min.css" rel="stylesheet">
            <link href="/client/lib/owlcarousel/assets/owl.carousel.min.css" rel="stylesheet">


            <!-- Customized Bootstrap Stylesheet -->
            <link href="/client/css/bootstrap.min.css" rel="stylesheet">

            <!-- Template Stylesheet -->
            <link href="/client/css/style.css" rel="stylesheet">
        </head>

        <body>

            <!-- Spinner Start -->
            <div id="spinner"
                class="show w-100 vh-100 bg-white position-fixed translate-middle top-50 start-50  d-flex align-items-center justify-content-center">
                <div class="spinner-grow text-primary" role="status"></div>
            </div>
            <!-- Spinner End -->

            <jsp:include page="../layout/header.jsp" />

            <!-- Single Product Start -->
                  <div class="container-fluid py-5 mt-5">
            <div class="container py-5">
                <div class="mb-3">
                        <div>
                            <nav aria-label="breadcumb">
                                <ol class="breadcumb">
                                    <li class="breadcumb-item"><a href="/">Home</a></li>
                                    <li class="breadcumb-item active" aria-current="page">Chi tiết giỏ hàng</li>
                                </ol>
                            </nav>
                        </div>


                <div class="table-responsive">
                    <table class="table">
                        <thead>
                        <tr>
                            <th scope="col">Products</th>
                            <th scope="col">Name</th>
                            <th scope="col">Price</th>
                            <th scope="col">Quantity</th>
                            <th scope="col">Total</th>
                            <th scope="col">Handle</th>
                        </tr>
                        </thead>
                        <tbody>
                            <%-- Nên Nhớ Lúc bên Controller truyền vô là cartDetails --%>

                            <%-- Giải thích vì sao phải ghi là cartDetail.product.image chứ không ghi product.image--%>

                            <%-- 1. Scope của biến trong vòng lặp
                            Trong vòng lặp này, biến bạn có là cartDetail, không phải product.
                            cartDetail là từng dòng chi tiết giỏ hàng, mỗi dòng chứa thông tin sản phẩm, số lượng, giá...

                            2. Lấy thông tin sản phẩm qua cartDetail
                            Để lấy hình ảnh sản phẩm, bạn phải đi qua đối tượng cartDetail:

                            3. Không có biến product trong scope
                            Nếu bạn ghi ${product.image} trong vòng lặp này, JSP sẽ không biết lấy product ở đâu (vì không có biến product trong scope hiện tại), dẫn đến lỗi hoặc không hiển thị gì. --%>
                            <%-- Lúc bên Controller truyền vô là cartDetails mà --%>

                            <%-- Bạn phải truyền cartDetails (danh sách CartDetail) vào view, không thể chỉ truyền product, vì:

                            1. Giỏ hàng cần nhiều thông tin hơn chỉ mỗi sản phẩm
                            CartDetail chứa:
                            Sản phẩm nào (product)
                            Số lượng sản phẩm đó trong giỏ (quantity)
                            Giá tại thời điểm thêm vào giỏ (price)
                            Nếu chỉ truyền product, bạn không biết số lượng từng sản phẩm trong giỏ, cũng không biết giá lúc thêm vào (nếu giá có thể thay đổi).

                            2. View giỏ hàng cần hiển thị gì?
                            Tên, ảnh, giá sản phẩm (cartDetail.product)
                            Số lượng sản phẩm trong giỏ (cartDetail.quantity)
                            Tổng tiền từng dòng (cartDetail.price * cartDetail.quantity)
                            Nếu chỉ truyền product, bạn không thể biết số lượng và không thể tính tổng tiền từng dòng. --%>
                            <c:forEach var = "cartDetail" items="${cartDetails}">
                            <tr>

                                <th scope="row">
                                    <div class="d-flex align-items-center">
                                        <img src="/images/product/${cartDetail.product.image}" 
                                        class="img-fluid me-5 rounded-circle" 
                                        style="width: 80px; height: 80px;" alt="">
                                    </div>
                                </th>

                                <td>
                                    <p class="mb-0 mt-4">
                                        <a href="/product/${cartDetail.product.id}" target="blank">
                                            ${cartDetail.product.name}
                                        </a>
                                    </p>
                                </td>

                                <td>
                                    <p class="mb-0 mt-4">
                                    <fmt:formatNumber type="number"
                                    value ="${cartDetail.product.price}"
                                    /> đ

                                    </p>
                                </td>

                                <td>
                                    <div class="input-group quantity mt-4" style="width: 100px;">\

                                        <div class="input-group-btn">
                                            <button class="btn btn-sm btn-minus rounded-circle bg-light border" >
                                            <i class="fa fa-minus"></i>
                                            </button>
                                        </div>

                                        <input type="text" class="form-control form-control-sm text-center border-0"
                                        value="${cartDetail.quantity}"

                                        <%-- Nói chung cái chức năng tăng giảm số lượng rồi giá tiền tăng theo thì nó bên javascript --%>
                                        <%--data-cart-detail-id , data-cart-detail-price , data-cart-total-price à HTML data attributes (thuộc tính dữ liệu tùy chỉnh) dùng để truyền dữ liệu từ server sang client (JavaScript). 
                                        Tóm lại: Các thuộc tính này giúp JavaScript dễ dàng lấy đúng dữ liệu để xử lý tăng/giảm số lượng, cập nhật giá tiền từng dòng và tổng tiền giỏ hàng ngay trên giao diện mà không cần reload trang. --%>

                                        <%-- Cần có id để định danh nó là ai --%>
                                        data-cart-detail-id="${cartDetail.id}"

                                        <%-- Cần có price để khi bấm nút tăng giảm giá tiền cũng tăng giảm theo (lấy số lượng nhân giá cả) --%>
                                        data-cart-detail-price="${cartDetail.price}"
                                        >

                                        <div class="input-group-btn">
                                            <button class="btn btn-sm btn-plus rounded-circle bg-light border">
                                                <i class="fa fa-plus"></i>
                                            </button>
                                        </div>

                                    </div>
                                </td>

                                <td>
                                    <%-- Thêm vô ${cartDetail.id} để biết đích danh ô input nào (đích danh phần tử HTML nào) cần phải cập nhật --%>
                                    <p class="mb-0 mt-4" data-cart-detail-id="${cartDetail.id}">
                                        <fmt:formatNumber type="number"
                                        value ="${cartDetail.price * cartDetail.quantity}" /> đ
                                    </p>
                                </td>

                                <%-- Xóa sản phẩm trong giỏ hàng --%>
                                <td>
                                <form method="post" action="/delete-cart-product/${cartDetail.id}">
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                    <button class="btn btn-md rounded-circle bg-light border mt-4" >
                                        <i class="fa fa-times text-danger"></i>
                                    </button
                                </form>
                                </td>

                            </tr>
                            </c:forEach>

                        </tbody>
                    </table>
                </div>

                <div class="mt-5 row g-4 justify-content-start">
                    <div class="col-12 col-md-8"></div>
                        <div class="bg-light rounded">
                            <div class="p-4">
                                <h1 class="display-6 mb-4">Cart <span class="fw-normal">Total</span></h1>
                                <div class="d-flex justify-content-between mb-4">
                                    <h5 class="mb-0 me-4">Subtotal:</h5>


                                    <p class="mb-0" data-cart-total-price="${totalPrice}">
                                        <fmt:formatNumber type="number"
                                        value ="${totalPrice}" /> đ
                                    </p>
                                </div>
                                <div class="d-flex justify-content-between">
                                    <h5 class="mb-0 me-4">Shipping</h5>
                                    <div class="">
                                        <p class="mb-0">0đ</p>
                                    </div>
                                </div>
                                <p class="mb-0 text-end">Shipping to Ukraine.</p>
                            </div>

                            <div class="py-4 mb-4 border-top border-bottom d-flex justify-content-between">
                                <h5 class="mb-0 ps-4 me-4">Total</h5>


                                <p class="mb-0 pe-4" data-cart-total-price="${totalPrice}">
                                    <fmt:formatNumber type="number"
                                    value ="${totalPrice}" /> đ
                                </p>
                            </div>

                            <button class="btn border-secondary rounded-pill px-4 py-3 text-primary text-uppercase mb-4 ms-4" type="button">Proceed Checkout</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
            <!-- Single Product End -->

            <jsp:include page="../layout/footer.jsp" />


            <!-- Back to Top -->
            <a href="#" class="btn btn-primary border-3 border-primary rounded-circle back-to-top"><i
                    class="fa fa-arrow-up"></i></a>


            <!-- JavaScript Libraries -->
            <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.4/jquery.min.js"></script>
            <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0/dist/js/bootstrap.bundle.min.js"></script>
            <script src="/client/lib/easing/easing.min.js"></script>
            <script src="/client/lib/waypoints/waypoints.min.js"></script>
            <script src="/client/lib/lightbox/js/lightbox.min.js"></script>
            <script src="/client/lib/owlcarousel/owl.carousel.min.js"></script>

            <!-- Template Javascript -->
            <script src="/client/js/main.js"></script>
        </body>

        </html>