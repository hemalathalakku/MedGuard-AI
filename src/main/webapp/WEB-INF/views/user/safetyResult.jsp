<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Safety Result - MedGuard AI</title>
    <%@ include file="/WEB-INF/views/fragments/head.jspf" %>
</head>
<body class="d-flex flex-column min-vh-100 mg-body">

<%@ include file="/WEB-INF/views/fragments/userNav.jspf" %>

<div class="container py-4" style="max-width:760px">

    <h4 class="mb-3">
        <i class="bi bi-shield-check text-primary"></i>
        Safety check: ${m.medicineName}
    </h4>

    <c:choose>

        <%-- NOT VERIFIED --%>
        <c:when test="${result.level == 'NOT_VERIFIED'}">
            <div class="card border-secondary p-4 text-center mb-4">
                <i class="bi bi-question-circle display-3 text-secondary"></i>
                <h3 class="text-secondary mt-2">NOT VERIFIED</h3>
                <div class="text-muted">
                    There is insufficient information to verify this medicine's safety.
                </div>
            </div>
        </c:when>

        <%-- SAFE --%>
        <c:when test="${result.level == 'SAFE'}">
            <div class="card border-success mg-result-safe p-4 text-center mb-4">
                <i class="bi bi-check-circle display-3 text-success"></i>
                <h3 class="text-success mt-2">SAFE</h3>
                <div class="text-muted">
                    No conflicts were identified in the completed checks.
                </div>
            </div>
        </c:when>

        <%-- CAUTION --%>
        <c:when test="${result.level == 'CAUTION'}">
            <div class="card border-warning mg-result-caution p-4 text-center mb-4">
                <i class="bi bi-exclamation-triangle display-3 text-warning"></i>
                <h3 class="text-warning mt-2">USE WITH CAUTION</h3>
                <div class="text-muted">
                    Possible issues found. Read the reasons below.
                </div>
            </div>
        </c:when>

        <%-- DANGEROUS --%>
        <c:otherwise>
            <div class="card border-danger mg-result-danger p-4 text-center mb-4">
                <i class="bi bi-x-octagon display-3 text-danger"></i>
                <h3 class="text-danger mt-2">DANGEROUS</h3>
                <div class="text-muted">
                    A serious conflict was identified in the available information.
                </div>
            </div>
        </c:otherwise>

    </c:choose>

    <div class="card mg-glass p-3 mb-4">
        <h6>Why this verdict?</h6>
        <ul class="mb-0">
            <c:forEach var="r" items="${result.reasons}">
                <li class="mb-1">${r}</li>
            </c:forEach>
        </ul>
    </div>

    <div class="alert alert-secondary small">
        <i class="bi bi-info-circle"></i>
        MedGuard AI provides informational safety checks only and is not
        a substitute for professional medical advice. Always consult a
        doctor or pharmacist before taking any medicine.
    </div>

    <div class="d-flex gap-2">
        <a href="${pageContext.request.contextPath}/user/medicines"
           class="btn btn-outline-primary">
            Check another medicine
        </a>

        <a href="${pageContext.request.contextPath}/user/history"
           class="btn btn-outline-secondary">
            View history
        </a>
    </div>

</div>

<%@ include file="/WEB-INF/views/fragments/footer.jspf" %>

</body>
</html>
