<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<head>
    <jsp:include page="includes/resources.jsp"/>
</head>

<body>

<!-- Navigation -->

<jsp:include page="includes/navbar.jsp"/>

<div class="top-bar">
    <div class="container">
        <h2>Create Advert</h2>
    </div>
</div>

<div class="content-area">
    <div class="container">
        <br>
        <div class="span6">
            <c:if test="${not empty successMessage}">
                <div class="alert fade in alert-success" style="text-align: center;">
                    <button type="button" class="close" data-dismiss="alert">&times;</button>
                    <strong>${successMessage} </strong>
                </div>
            </c:if>
        </div>
        <div class="row">
            <c:if test="${not empty errorMessage}">
                <div class="alert fade in alert-danger" style="text-align: center;">
                    <button type="button" class="close" data-dismiss="alert">&times;</button>
                    <strong>${errorMessage}</strong>
                </div>
            </c:if>
        </div>

        <div class="row">
            <div class="col-md-8 col-md-offset-3">
                <br>
                <form:form class="form-horizontal" role="form" method="post" id="adItemForm" commandName="adItemForm" action="createAd.htm">

                    <div class="form-group">
                        <label for="adTopic" class="col-sm-2 control-label"> Ad Topic</label>
                        <div class="col-sm-8">
                            <form:input cssStyle="padding-left: 10px"  class="form-control" name="adTopic" id="adTopic" path="adTopic"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="adDescription" class="col-sm-2 control-label">Description</label>
                        <div class="col-sm-8">
                            <form:input cssStyle="padding-left: 10px"  class="form-control" name="adDescription" id="adDescription" path="adDescription"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="adCategory" class="col-sm-2 control-label">Ad Category</label>
                        <div class="col-sm-8">
                            <form:select class="form-control" path="advertisementType" id="adCategory">
                                <form:options/>

                            </form:select>
                            <div class="help-block with-errors"></div>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="itemPrice" class="col-sm-2 control-label">Price</label>
                        <div class="col-sm-8">
                            <form:input cssStyle="padding-left: 10px"  class="form-control" name="itemPrice" id="itemPrice" path="itemPrice"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="mobileNumber" class="col-sm-2 control-label">Mobile Number</label>
                        <div class="col-sm-8">
                            <form:errors path="mobileNumber" element="div" cssClass="error_message_above_field"/>
                            <form:input cssStyle="padding-left: 10px"  class="form-control" name="mobileNumber" id="mobileNumber" path="mobileNumber"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="AdPoster" class="col-sm-2 control-label">Ad Poster</label>
                        <div class="col-sm-8">
                            <div id="choosefile">
                                <div class="fileinputs">
                                    <input type="file" name="file_1" id="control" title="you can import the files"/>
                                </div>
                            </div>
                            <br>
                            <input type="button" value="Upload Files" class="btn btn-default btn-small" onclick="addNewFile()"
                                   title="Upload to the server"/>
                            <div id="addFileResult"></div>
                        </div>
                    </div>

                    <br>
                    <div class="form-group">
                        <div class="col-sm-offset-2 col-sm-8">
                            <button class="btn btn-primary" name="submit" >Submit</button>
                        </div>
                    </div>
                </form:form>

            </div>
        </div>

        <jsp:include page="includes/footer.jsp"></jsp:include>

    </div>
</div>
</body>
