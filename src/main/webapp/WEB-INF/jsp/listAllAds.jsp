<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html>
<html lang="en">

<head>
    <jsp:include page="includes/resources.jsp"/>
</head>

<body>

<!-- Navigation -->

<jsp:include page="includes/navbar.jsp"/>

<div class="top-bar">
    <div class="container">
        <h2>Advert Listing</h2>
    </div>
</div>

<div class="content-area">
    <!-- Page Content -->
    <div class="container">

        <c:if test="${fn:length(adItemList) gt 0}">
            <div class="row">
                <c:forEach begin="0" items="${adItemList}" var="adItem" varStatus="rowCounter">

                    <div class="col-md-3">
                        <div class="thumbnail">
                            <div class="product-img">
                                <img src="${adItem.posterUrl}">
                            </div>
                            <div class="caption">
                                <h3>${adItem.adTopic}</h3>
                                <h5>Product Description Here</h5>
                                <div class="row">
                                    <div class="col-md-6">
                                    </div>
                                    <div class="col-md-6">
                                        <p class="product-price">${adItem.itemPrice}</p>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                </c:forEach>
            </div>
        </c:if>

    </div>

    <jsp:include page="includes/footer.jsp"></jsp:include>
    
</div>




</body>

</html>
