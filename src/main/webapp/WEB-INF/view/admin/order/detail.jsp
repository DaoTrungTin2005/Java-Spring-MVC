<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
    <meta name="description" content="Hỏi Dân IT - Dự án laptopshop" />
    <meta name="author" content="Hỏi Dân IT" />
    <title>Order Detail - Hỏi Dân IT</title>

    <link href="/css/styles.css" rel="stylesheet" />
    <script src="https://use.fontawesome.com/releases/v6.3.0/js/all.js" crossorigin="anonymous"></script>
</head>

<body class="sb-nav-fixed">
    <jsp:include page="../layout/header.jsp" />
    <div id="layoutSidenav">
        <jsp:include page="../layout/sidebar.jsp" />
        <div id="layoutSidenav_content">
            <main>
                <div class="container-fluid px-4">
                    <h1 class="mt-4">Orders</h1>
                    <ol class="breadcrumb mb-4">
                        <li class="breadcrumb-item"><a href="/admin">Dashboard</a></li>
                        <li class="breadcrumb-item"><a href="/admin/order">Orders</a></li>
                        <li class="breadcrumb-item active">View Detail</li>
                    </ol>

                    <div class="container mt-4">
                        <div class="row">
                            <div class="col-12 mx-auto">
                                <h4 class="text-primary mb-3">Order detail with id = ${id}</h4>
                                <table class="table table-bordered">
                                    <thead>
                                        <tr>
                                            <th>Sản phẩm</th>
                                            <th>Tên</th>
                                            <th>Giá cả</th>
                                            <th>Số lượng</th>
                                            <th>Thành tiền</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="od" items="${orderDetails}">
                                            <tr>
                                                <td>
                                                    <img src="/images/product/${od.product.image}" alt="product-image"
                                                        style="width: 60px; height: 60px; object-fit: contain;" />
                                                </td>
                                                <td>
                                                    <a href="#">${od.product.name}</a>
                                                </td>
                                                <td>
                                                    <fmt:formatNumber value="${od.price}" type="number" groupingUsed="true"/> đ
                                                </td>
                                                <td>${od.quantity}</td>
                                                <td>
                                                    <fmt:formatNumber value="${od.price * od.quantity}" type="number" groupingUsed="true"/> đ
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>

                                <a href="/admin/order" class="btn btn-success">Back</a>
                            </div>
                        </div>
                    </div>
                </div>
            </main>
            <jsp:include page="../layout/footer.jsp" />
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"
        crossorigin="anonymous"></script>
    <script src="/js/scripts.js"></script>
</body>

</html>
